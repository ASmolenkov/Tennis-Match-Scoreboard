package io.github.asmolenkov.tennismatchscoreboard.mapper;

import io.github.asmolenkov.tennismatchscoreboard.dto.MatchDto;
import io.github.asmolenkov.tennismatchscoreboard.dto.MatchScoreDto;
import io.github.asmolenkov.tennismatchscoreboard.dto.PlayerDto;
import io.github.asmolenkov.tennismatchscoreboard.dto.SetScoreDto;
import io.github.asmolenkov.tennismatchscoreboard.entity.Match;
import io.github.asmolenkov.tennismatchscoreboard.model.*;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@UtilityClass
@Slf4j
public class MatchMapper {

    public static MatchDto toDto(Match match) {
        if (match == null) {
            return null;
        }
        PlayerDto playerOne = new PlayerDto(match.getPlayerOne().getId(), match.getPlayerOne().getName());
        PlayerDto playerSecond = new PlayerDto(match.getPlayerSecond().getId(), match.getPlayerSecond().getName());
        PlayerDto winner = new PlayerDto(match.getWinner().getId(), match.getWinner().getName());

        return new MatchDto(playerOne, playerSecond, winner);
    }

    public static MatchDto toDto(CurrentMatch match) {
        if (match == null) {
            return null;
        }
        PlayerDto playerOne = new PlayerDto(match.getPlayerOne().id(), match.getPlayerOne().name());
        PlayerDto playerSecond = new PlayerDto(match.getPlayerSecond().id(), match.getPlayerSecond().name());
        Optional<PlayerModel> playerModel = match.getWinner();
        PlayerDto winner;
        winner = playerModel.map(model -> new PlayerDto(model.id(), model.name())).orElse(null);

        return new MatchDto(playerOne, playerSecond, winner);
    }

    public static List<MatchDto> toDtoList(List<Match> entity) {
        if (entity == null) {
            return null;
        }

        List<MatchDto> matches = new ArrayList<>();
        for (Match match : entity) {
            matches.add(toDto(match));
        }

        return matches;
    }

    public static MatchScoreDto toMatchScoreDto(CurrentMatch match){
        List<SetScoreDto> completedSets = new ArrayList<>();
        List<SetScore> allSets = match.getMatchScore().getSets();
        for (int i = 0; i < allSets.size() - 1; i++) {
            completedSets.add((toSetScoreDto(allSets.get(i))));
        }
        SetScore currentSet = match.getMatchScore().getCurrentSet();
        SetScoreDto currentSetDto = toSetScoreDto(currentSet);

        String winnerName = match.getWinner().map(PlayerModel::name).orElse(null);
        Game currentGame = currentSet.getCurrentGame();
        String playerOneCurrentGameScore = getPlayerOneGameScore(currentGame);
        String playerSecondCurrentGameScore = getPlayerSecondGameScore(currentGame);


        log.info("Uuid в текущем матче = {}", match.getUuid());
        return new MatchScoreDto(
                match.getUuid(),
                match.getPlayerOne().name(),
                match.getPlayerSecond().name(),
                match.getPlayerOne().id(),
                match.getPlayerSecond().id(),
                completedSets,
                currentSetDto,
                playerOneCurrentGameScore,
                playerSecondCurrentGameScore,
                match.isMatchFinished(),
                winnerName
        );
    }

    private static String getPlayerOneGameScore(Game currentGame) {
        if(currentGame instanceof GameScore gameScore){
            return gameScore.getPlayerOnePoint().getDisplayValue();
        } else if (currentGame instanceof TieBreakScore tieBreakScore) {
            return String.valueOf(tieBreakScore.getPlayerOnePoint());
        }
        return "0";
    }

    private static String getPlayerSecondGameScore(Game currentGame) {
        if(currentGame instanceof GameScore gameScore){
            return gameScore.getPlayerSecondPoint().getDisplayValue();
        } else if (currentGame instanceof TieBreakScore tieBreakScore) {
            return String.valueOf(tieBreakScore.getPlayerSecondPoint());
        }
        return "0";
    }

    private static SetScoreDto toSetScoreDto (SetScore setScore){
        Integer tbPlayerOnePoints = null;
        Integer tbPlayerTwoPoints = null ;
        String tieBreakScore = null;
        boolean isTieBreak = false;

        for (Game game:setScore.getGames()){
            if(game instanceof TieBreakScore tieBreak){
                isTieBreak = true;
                tbPlayerOnePoints = tieBreak.getPlayerOnePoint();
                tbPlayerTwoPoints = tieBreak.getPlayerSecondPoint();
                tieBreakScore =  "%s-%s".formatted(tieBreak.getPlayerOnePoint(), tieBreak.getPlayerSecondPoint());
            }
        }
        return new SetScoreDto(setScore.getPlayerOneGames(), setScore.getPlayerSecondGames(), isTieBreak,
                               tieBreakScore, tbPlayerOnePoints, tbPlayerTwoPoints);
    }

}
