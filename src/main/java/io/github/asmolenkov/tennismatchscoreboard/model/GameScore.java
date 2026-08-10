package io.github.asmolenkov.tennismatchscoreboard.model;

import io.github.asmolenkov.tennismatchscoreboard.exception.addPoint.AddPointGameException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
@Slf4j
@Getter
public class GameScore implements Game {

    private Point playerOnePoint = Point.ZERO;
    private Point playerSecondPoint = Point.ZERO;
    private PlayerSide winner;
    private static final String CANNOT_ADD_POINT = "Cannot add point: game is already finished";
    private static final String LOG_GAME_FINISHED = "Game ended";

    @Override
    public void addPoint(PlayerSide side) {
        if (isFinished()) {
            throw new AddPointGameException(CANNOT_ADD_POINT);
        }

        Point current = (side == PlayerSide.ONE) ? playerOnePoint : playerSecondPoint;
        Point opponent = (side == PlayerSide.ONE) ? playerSecondPoint : playerOnePoint;

        if (opponent == Point.ADVANTAGE) {
            resetOpponentAdvantage(side);
            return;
        }

        if (current == Point.ADVANTAGE) {
            log.info(LOG_GAME_FINISHED);
            this.winner = side;
            return;
        }

        /*if (isStandardGameWon()) {
            log.info(LOG_GAME_FINISHED);
            this.winner = calculateStandardWinner();
        }*/

        if (current == Point.FORTY && opponent != Point.FORTY) {
            this.winner = side;
            log.info(LOG_GAME_FINISHED);
            return; // Мы не инкрементируем счёт, мы просто фиксируем победу
        }

        incrementPoint(side);


    }
    @Override
    public Optional<PlayerSide> getWinner() {
        return Optional.ofNullable(winner);
    }
    @Override
    public boolean isFinished() {
        return winner != null;
    }

    private void resetOpponentAdvantage(PlayerSide side) {
        if (side == PlayerSide.ONE) {
            this.playerSecondPoint = Point.FORTY;
        } else {
            this.playerOnePoint = Point.FORTY;
        }
    }

    private void incrementPoint(PlayerSide side) {
        if (side == PlayerSide.ONE) {
            this.playerOnePoint = getNextPoint(playerOnePoint);
        } else {
            this.playerSecondPoint = getNextPoint(playerSecondPoint);
        }
    }

    private Point getNextPoint(Point current) {
        return switch (current) {
            case ZERO -> Point.FIFTEEN;
            case FIFTEEN -> Point.THIRTY;
            case THIRTY -> Point.FORTY;
            case FORTY -> Point.ADVANTAGE;
            case ADVANTAGE ->
                    throw new IllegalStateException("Нельзя добавить очко при преимуществе — гейм должен быть выигран");
        };
    }


}
