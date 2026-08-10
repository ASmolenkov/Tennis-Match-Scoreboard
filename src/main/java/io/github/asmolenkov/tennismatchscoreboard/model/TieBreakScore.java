package io.github.asmolenkov.tennismatchscoreboard.model;

import io.github.asmolenkov.tennismatchscoreboard.exception.addPoint.AddPointTieBreakException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
@Slf4j
@Getter
public class TieBreakScore implements Game {
    private static final int START_SCORE = 0;
    private static final int MAX_NUMBER_POINT_TO_WIN = 7;
    private static final int MIN_DIFFERENCE_SCORE_TO_WIN = 2;
    private static final String CANNOT_ADD_POINT = "Cannot add point: tie-break is already finished";


    private int playerOnePoint;
    private int playerSecondPoint;

    public TieBreakScore() {
        this.playerOnePoint = START_SCORE;
        this.playerSecondPoint = START_SCORE;
    }

    @Override
    public void addPoint(PlayerSide playerSide) {
        if (isFinished()) {
            throw new AddPointTieBreakException(CANNOT_ADD_POINT);
        }
        switch (playerSide) {
            case ONE -> playerOnePoint++;
            case TWO -> playerSecondPoint++;
        }

    }

    @Override
    public boolean isFinished() {
        return getWinner().isPresent();
    }

    @Override
    public Optional<PlayerSide> getWinner() {
        int p1 = this.playerOnePoint;
        int p2 = this.playerSecondPoint;

        if (p1 >= MAX_NUMBER_POINT_TO_WIN && p1 - p2 >= MIN_DIFFERENCE_SCORE_TO_WIN) {
            return Optional.of(PlayerSide.ONE);
        }

        if (p2 >= MAX_NUMBER_POINT_TO_WIN && p2 - p1 >= MIN_DIFFERENCE_SCORE_TO_WIN) {
            return Optional.of(PlayerSide.TWO);
        }

        return Optional.empty();
    }


}
