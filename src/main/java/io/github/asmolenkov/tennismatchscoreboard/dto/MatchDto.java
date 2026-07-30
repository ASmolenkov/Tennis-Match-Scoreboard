package io.github.asmolenkov.tennismatchscoreboard.dto;

public record MatchDto(PlayerDto playerOne, PlayerDto playerSecond, PlayerDto winner) {
}
