package io.github.asmolenkov.tennismatchscoreboard.model;

import io.github.asmolenkov.tennismatchscoreboard.exception.addPoint.AddPointSetException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Slf4j
@Getter
public class SetScore {
    private static final int GAMES_TO_WIN = 6;
    private static final int POINT_DIFFERENCE_IN_SET = 2;
    private static final String CANNOT_ADD_POINT = "Cannot add point: set is already finished";
    private static final String LOG_MATCH_FINISHED = "Match ended";
    private static final String LOG_START_TIE_BREAK = "Tie-break begins";

    private final List<Game> games = new ArrayList<>();
    private Game currentGame;

    public SetScore() {
        this.currentGame = new GameScore();
        this.games.add(currentGame);
    }


    public void addPoint(PlayerSide playerSide) {
        if (isSetFinished()) {
            throw new AddPointSetException(CANNOT_ADD_POINT);
        }

        currentGame.addPoint(playerSide);

        if (currentGame.isFinished()) {
            log.info(LOG_MATCH_FINISHED);
            handleGameFinished();
        }
    }

    public boolean isSetFinished() {
        return getWinner().isPresent();
    }

    public int getPlayerOneGames (){
        return countWonGames(PlayerSide.ONE);
    }

    public int getPlayerSecondGames (){
        return countWonGames(PlayerSide.TWO);
    }

    protected Optional<PlayerSide> getWinner() {
        int playerOneGames = countWonGames(PlayerSide.ONE);
        int playerTwoGames = countWonGames(PlayerSide.TWO);


        if (playerOneGames >= GAMES_TO_WIN && playerOneGames - playerTwoGames >= POINT_DIFFERENCE_IN_SET) {
            return Optional.of(PlayerSide.ONE);
        }
        if (playerTwoGames >= GAMES_TO_WIN && playerTwoGames - playerOneGames >= POINT_DIFFERENCE_IN_SET) {
            return Optional.of(PlayerSide.TWO);
        }


        if (playerOneGames == 7 && playerTwoGames == 6) {
            return Optional.of(PlayerSide.ONE);
        }
        if (playerTwoGames == 7 && playerOneGames == 6) {
            return Optional.of(PlayerSide.TWO);
        }

        return Optional.empty();
    }

    private void handleGameFinished() {
        if (isSetFinished()) {
            return;
        }
        Game newGame;

        if (needsTieBreak()){
            log.info(LOG_START_TIE_BREAK);
            newGame = new TieBreakScore();
        }else {
            newGame = new GameScore();
        }
        games.add(newGame);
        currentGame = newGame;
    }


    private boolean needsTieBreak() {
        int playerOneGames = countWonGames(PlayerSide.ONE);
        int playerSecondGames = countWonGames(PlayerSide.TWO);

        return playerOneGames == GAMES_TO_WIN && playerSecondGames == GAMES_TO_WIN;
    }

    private int countWonGames(PlayerSide playerSide) {
        return (int) games.stream().filter(game -> game.getWinner().orElse(null) == playerSide).count();
    }
}
