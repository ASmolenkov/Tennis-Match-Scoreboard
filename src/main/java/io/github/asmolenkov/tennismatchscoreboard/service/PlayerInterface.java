package io.github.asmolenkov.tennismatchscoreboard.service;

import io.github.asmolenkov.tennismatchscoreboard.dto.PlayerDto;

public interface PlayerInterface {
     PlayerDto createPlayer(String name);
}
