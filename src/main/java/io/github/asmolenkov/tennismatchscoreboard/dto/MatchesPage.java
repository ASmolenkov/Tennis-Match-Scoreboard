package io.github.asmolenkov.tennismatchscoreboard.dto;

import java.util.List;


public record MatchesPage(List<MatchDto> matches, PageInfo pageInfo) {
}
