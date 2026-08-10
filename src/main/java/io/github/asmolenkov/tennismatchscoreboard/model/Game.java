package io.github.asmolenkov.tennismatchscoreboard.model;

import java.util.Optional;

public interface Game {
    void addPoint(PlayerSide playerSide);
    Optional<PlayerSide> getWinner();
    boolean isFinished();
}
