package io.github.asmolenkov.tennismatchscoreboard.mapper;

import io.github.asmolenkov.tennismatchscoreboard.dto.MatchDto;
import io.github.asmolenkov.tennismatchscoreboard.dto.PlayerDto;
import io.github.asmolenkov.tennismatchscoreboard.entity.Match;
import io.github.asmolenkov.tennismatchscoreboard.model.CurrentMatch;



import java.util.ArrayList;
import java.util.List;

public class MatchMapper {

    public static MatchDto toDto(Match match) {
        if (match == null) {
            return null;
        }
        PlayerDto playerOne = new PlayerDto(match.getPlayerOne().getId(), match.getPlayerOne().getName());
        PlayerDto playerSecond = new PlayerDto(match.getPlayerSecond().getId(), match.getPlayerSecond().getName());
        PlayerDto winner = new PlayerDto(match.getWinner().getId(), match.getWinner().getName());

        return new MatchDto(playerOne, playerSecond, winner);
    }

    public static MatchDto toDto(CurrentMatch match) {
        if (match == null) {
            return null;
        }
        PlayerDto playerOne = new PlayerDto(match.getPlayerOne().id(), match.getPlayerOne().name());
        PlayerDto playerSecond = new PlayerDto(match.getPlayerSecond().id(), match.getPlayerSecond().name());
        PlayerDto winner = new PlayerDto(match.getWinner().id(), match.getWinner().name());


        return new MatchDto(playerOne, playerSecond, winner);
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
