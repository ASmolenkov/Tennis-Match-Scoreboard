package io.github.asmolenkov.tennismatchscoreboard.dto;

public record SetScoreDto(int playerOneGames, int playerSecondGames, boolean isTieBreak,
                          String tieBreakScore, Integer tieBreakPlayerOnePoints, Integer tieBreakPlayerSecondPoints) {
}
