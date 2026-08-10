package io.github.asmolenkov.tennismatchscoreboard.dto;

import java.util.List;
import java.util.UUID;

public record MatchScoreDto(UUID matchUuid, String playerOneName, String playerSecondName, long playerOneId,
                            long playerSecondId, List<SetScoreDto> completedSets, SetScoreDto currentSet,
                            String playerOneCurrentGameScore, String playerSecondCurrentGameScore, boolean isFinished,
                            String winnerName) {

    public static MatchScoreDto finishedPlaceholder(UUID matchUuid) {
        return new MatchScoreDto(
                matchUuid,
                null,  // Имена неизвестны — матч удалён из памяти
                null,
                0,
                0,
                List.of(),
                null,
                null,
                null,
                true,  // ← Ключевой флаг
                null   // Победитель тоже неизвестен
        );
    }

}
