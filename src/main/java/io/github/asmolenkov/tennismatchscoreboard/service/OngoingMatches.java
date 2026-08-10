package io.github.asmolenkov.tennismatchscoreboard.service;

import io.github.asmolenkov.tennismatchscoreboard.dto.MatchScoreDto;
import io.github.asmolenkov.tennismatchscoreboard.model.CurrentMatch;

import java.util.Optional;
import java.util.UUID;

public interface OngoingMatches {
    UUID createMatch(String playerOneName, String playerSecondName);

    Optional<CurrentMatch> findMatchByUuid(UUID uuid);

    void addPoint(UUID uuid, long playerId);
     MatchScoreDto getMatchScore(UUID matchUuid);
}
