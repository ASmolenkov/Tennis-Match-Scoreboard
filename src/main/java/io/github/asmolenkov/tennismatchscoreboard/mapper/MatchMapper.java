package io.github.asmolenkov.tennismatchscoreboard.mapper;

import io.github.asmolenkov.tennismatchscoreboard.dto.MatchDto;
import io.github.asmolenkov.tennismatchscoreboard.entity.Match;
import io.github.asmolenkov.tennismatchscoreboard.entity.Player;
import io.github.asmolenkov.tennismatchscoreboard.model.CurrentMatch;
import jakarta.persistence.EntityManager;


import java.util.ArrayList;
import java.util.List;

public class MatchMapper {

    public static Match toEntity(CurrentMatch model, EntityManager entityManager) {
        if (model == null) {
            return null;
        }
        Player playerOne = entityManager.getReference(Player.class, model.getPlayerOne().id());
        Player playerSecond = entityManager.getReference(Player.class, model.getPlayerSecond().id());
        Player winner = entityManager.getReference(Player.class, model.getWinner().id());
        return new Match(playerOne, playerSecond, winner);
    }

    public static MatchDto toDto(Match match) {
        if (match == null) {
            return null;
        }

        return new MatchDto(match.getPlayerOne()
                                 .getName(), match.getPlayerSecond()
                                                  .getName(), match.getWinner()
                                                                   .getName());
    }

    public static List<MatchDto> toDtoList(List<Match> entity) {
        if (entity == null) {
            return null;
        }

        List<MatchDto> matches = new ArrayList<>();
        for (Match match : entity) {
            matches.add(toDto(match));
        }

        return matches;
    }

}
