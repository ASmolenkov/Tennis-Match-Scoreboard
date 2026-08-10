package io.github.asmolenkov.tennismatchscoreboard.service;

import io.github.asmolenkov.tennismatchscoreboard.dto.MatchDto;
import io.github.asmolenkov.tennismatchscoreboard.dto.MatchesPage;
import io.github.asmolenkov.tennismatchscoreboard.dto.PageInfo;
import io.github.asmolenkov.tennismatchscoreboard.entity.Match;
import io.github.asmolenkov.tennismatchscoreboard.exception.SaveMatchException;
import io.github.asmolenkov.tennismatchscoreboard.mapper.MatchMapper;
import io.github.asmolenkov.tennismatchscoreboard.model.CurrentMatch;
import io.github.asmolenkov.tennismatchscoreboard.repository.FinishedMatchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import java.util.List;



@Slf4j
@RequiredArgsConstructor
public class FinishedMatchesPersistenceService implements FinishedMatchesPersistence {
    private static final String ERROR_SAVING_MATCH = "Error saving match";
    private static final String ERROR_MATCHES_LOADING = "Error loading matches page";

    private final TransactionManager transactionManager;
    private final FinishedMatchRepository finishedMatchRepository;


    @Override
    public void saveMatch(CurrentMatch currentMatch) {
        try {
            transactionManager.executeInTransaction(() -> {
                MatchDto matchDto = MatchMapper.toDto(currentMatch);
                finishedMatchRepository.save(matchDto);
            });
        } catch (Exception e) {
            throw new SaveMatchException(ERROR_SAVING_MATCH, e);
        }
    }


    @Override
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

                PageInfo pageInfo = new PageInfo(safePage, safeSize,totalItems, totalPages);

                return new MatchesPage(MatchMapper.toDtoList(matches),pageInfo);

            });
        } catch (Exception e) {

            log.error(ERROR_MATCHES_LOADING, e);
            throw new RuntimeException(ERROR_MATCHES_LOADING, e);

        }
    }

}
