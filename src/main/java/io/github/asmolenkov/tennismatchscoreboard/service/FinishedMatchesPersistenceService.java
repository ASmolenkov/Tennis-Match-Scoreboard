package io.github.asmolenkov.tennismatchscoreboard.service;

import io.github.asmolenkov.tennismatchscoreboard.dto.MatchDto;
import io.github.asmolenkov.tennismatchscoreboard.dto.MatchesPage;
import io.github.asmolenkov.tennismatchscoreboard.dto.PageInfo;
import io.github.asmolenkov.tennismatchscoreboard.entity.Match;
import io.github.asmolenkov.tennismatchscoreboard.exception.FindMatchException;
import io.github.asmolenkov.tennismatchscoreboard.exception.SaveMatchException;
import io.github.asmolenkov.tennismatchscoreboard.mapper.MatchMapper;
import io.github.asmolenkov.tennismatchscoreboard.model.CurrentMatch;
import io.github.asmolenkov.tennismatchscoreboard.repository.FinishedMatchRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import java.util.List;


@Slf4j
@AllArgsConstructor
public class FinishedMatchesPersistenceService {
    private final TransactionManager transactionManager;
    private final FinishedMatchRepository finishedMatchRepository;


    public void saveMatch(CurrentMatch currentMatch) {
        try {
            transactionManager.executeInTransaction(() -> {
                MatchDto matchDto = MatchMapper.toDto(currentMatch);
                finishedMatchRepository.save(matchDto);
            });
        } catch (Exception e) {
            throw new SaveMatchException("Ошибка сохранения матча", e);
        }
    }

    public Match findMathById(long id) {
        return transactionManager.executeInTransaction(() -> finishedMatchRepository.find(id)
                                                                                    .orElseThrow(
                                                                                            () -> new FindMatchException(
                                                                                                    "Матч с ID - %s не найден".formatted(id))));
    }

    public MatchesPage getMatchesPage(String playerName, int page, int size) {
        try {
            return transactionManager.executeInTransaction(() -> {
                int safePage = Math.max(page, 1);
                int safeSize = Math.max(size, 3);
                int offset = (safePage - 1) * safeSize;

                String searchName = (playerName != null && !playerName.trim().isEmpty()) ? playerName.trim() : null;

                List<Match> matches;
                long totalItems;

                if (searchName != null) {
                    matches = finishedMatchRepository.findByNameWithPagination(searchName, offset, safeSize);
                    totalItems = finishedMatchRepository.countByName(searchName);
                } else {
                    matches = finishedMatchRepository.findWithPagination(offset, safeSize);
                    totalItems = finishedMatchRepository.countTotal();
                }

                int totalPages = (int) Math.ceil((double) totalItems / safeSize);

                PageInfo pageInfo = PageInfo.builder()
                                            .currentPage(safePage)
                                            .pageSize(safeSize)
                                            .totalItems(totalItems)
                                            .totalPages(totalPages)
                                            .build();
                return MatchesPage.builder().pageInfo(pageInfo).matches(MatchMapper.toDtoList(matches)).build();
            });
        } catch (Exception e) {

            log.error("Ошибка при загрузке страницы матчей", e);
            throw new RuntimeException("Ошибка загрузки матчей", e);

        }
    }
}
