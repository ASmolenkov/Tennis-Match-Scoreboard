package io.github.asmolenkov.tennismatchscoreboard.controller;

import io.github.asmolenkov.tennismatchscoreboard.service.OngoingMatchesService;
import io.github.asmolenkov.tennismatchscoreboard.utils.ValidateUtil;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@WebServlet("/new-match")
public class NewMatchServlet extends BaseServlet {

    private static final String PAGE_PATH = "/WEB-INF/views/NewMatch.jsp";
    private static final String REDIRECT_PATH_TEMPLATE = "/match-score?uuid=%s";
    private static final String PAGE_NAME = "NewMatch";

    private static final String PARAMETER_PLAYER_ONE_NAME = "playerOneName";
    private static final String PARAMETER_PLAYER_TWO_NAME = "playerTwoName";



    private OngoingMatchesService ongoingMatchesService;


    @Override
    public void init() {
        ServletContext context = getServletContext();
        this.ongoingMatchesService = (OngoingMatchesService) context.getAttribute(OngoingMatchesService.class.getSimpleName());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(PAGE_PATH)
           .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String nameOnePlayer = req.getParameter(PARAMETER_PLAYER_ONE_NAME);
        String nameSecondPlayer = req.getParameter(PARAMETER_PLAYER_TWO_NAME);

        String normalizedNameOne = nameOnePlayer.trim().toLowerCase();
        String normalizedNameSecond = nameSecondPlayer.trim().toLowerCase();

        ValidateUtil.validateNamePlayer(normalizedNameOne);
        ValidateUtil.validateNamePlayer(normalizedNameSecond);
        ValidateUtil.validateNamesAreUnique(normalizedNameOne, normalizedNameSecond);

        UUID matchUuid = ongoingMatchesService.createMatch(normalizedNameOne, normalizedNameSecond);


        resp.sendRedirect(REDIRECT_PATH_TEMPLATE.formatted(matchUuid));
    }

    @Override
    protected String getErrorPath() {
        return PAGE_NAME;
    }

}
