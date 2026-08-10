package io.github.asmolenkov.tennismatchscoreboard.controller;


import io.github.asmolenkov.tennismatchscoreboard.dto.MatchScoreDto;
import io.github.asmolenkov.tennismatchscoreboard.exception.PlayerIdException;
import io.github.asmolenkov.tennismatchscoreboard.mapper.MatchMapper;
import io.github.asmolenkov.tennismatchscoreboard.model.CurrentMatch;
import io.github.asmolenkov.tennismatchscoreboard.service.*;
import io.github.asmolenkov.tennismatchscoreboard.utils.ValidateUtil;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@WebServlet("/match-score")
public class MatchScoreServlet extends BaseServlet {
    private static final String PARAMETER_UUID = "uuid";
    private static final String PARAMETER_PLAYER_ID = "playerId";

    private static final String ATTRIBUTE_FINISHED_MATCH = "finishedMatch";
    private static final String ATTRIBUTE_CURRENT_MATCH = "matchScore";

    private static final String PATH_FORWARD = "/WEB-INF/views/MatchScore.jsp";
    private static final String NAME_PAGE = "MatchScore";
    private static final String PATH_REDIRECT_TEMPLATE = "%s/match-score?uuid=%s";
    private static final String ID_PLAYER_EMPTY = "Player ID cannot be empty";
    private static final String ID_MUST_BE_NUMBER_TEMPLATE = "Player ID to be only a number, received %s";


    private OngoingMatches ongoingMatchesService;


    @Override
    public void init() {
        ServletContext context = getServletContext();
        this.ongoingMatchesService = (OngoingMatchesService) context.getAttribute(
                OngoingMatchesService.class.getSimpleName());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uuid = req.getParameter(PARAMETER_UUID);

        UUID uuidMath = ValidateUtil.parseUuidOrThrow(uuid);

        MatchScoreDto matchScore = ongoingMatchesService.getMatchScore(uuidMath);

        req.setAttribute(ATTRIBUTE_CURRENT_MATCH, matchScore);

        req.getRequestDispatcher(PATH_FORWARD).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String uuid = req.getParameter(PARAMETER_UUID);
        String playerId = req.getParameter(PARAMETER_PLAYER_ID);

        UUID uuidMap = ValidateUtil.parseUuidOrThrow(uuid);
        long id = parseLong(playerId);

        Optional<CurrentMatch> matchBefore = ongoingMatchesService.findMatchByUuid(uuidMap);

        ongoingMatchesService.addPoint(uuidMap, id);

        if (matchBefore.isPresent() && matchBefore.get().isMatchFinished()) {
            // Матч только что завершился, берём его из matchBefore
            MatchScoreDto finalScore = MatchMapper.toMatchScoreDto(matchBefore.get());
            req.setAttribute("matchScore", finalScore);
            req.getRequestDispatcher(PATH_FORWARD).forward(req, resp);
            return;
        }

        resp.sendRedirect(PATH_REDIRECT_TEMPLATE.formatted(req.getContextPath(), uuidMap));

    }

    @Override
    protected String getErrorPath() {
        return NAME_PAGE;
    }

    private long parseLong(String playerId) {
        if (playerId == null || playerId.trim().isEmpty()) {
            throw new PlayerIdException(ID_PLAYER_EMPTY);
        }
        try {
            return Long.parseLong(playerId);
        } catch (NumberFormatException e) {
            throw new PlayerIdException(ID_MUST_BE_NUMBER_TEMPLATE.formatted(playerId), e);
        }
    }

}
