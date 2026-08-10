package io.github.asmolenkov.tennismatchscoreboard.service;

import io.github.asmolenkov.tennismatchscoreboard.dto.MatchesPage;
import io.github.asmolenkov.tennismatchscoreboard.model.CurrentMatch;

public interface FinishedMatchesPersistence {
    void saveMatch(CurrentMatch currentMatch);

    MatchesPage getMatchesPage(String playerName, int page, int size);
}
