package io.github.asmolenkov.tennismatchscoreboard.repository;

import io.github.asmolenkov.tennismatchscoreboard.dto.MatchDto;
import io.github.asmolenkov.tennismatchscoreboard.entity.Match;

import java.util.List;
import java.util.Optional;

public interface FinishedMatchRepository {
    void save(MatchDto matchDto);

    Optional<Match> find(long id);

    List<Match> findWithPagination(int offset, int limit);

    long countTotal();

    List<Match> findByNameWithPagination(String name, int offset, int limit);

    long countByName(String name);
}
