package io.github.asmolenkov.tennismatchscoreboard.dto;

import java.util.List;
import java.util.UUID;

public record FinishedMatchDto(UUID matchUuid, String playerOneName, String playerTwoName, String winnerName,
                               List<SetScoreDto> sets) {
}
