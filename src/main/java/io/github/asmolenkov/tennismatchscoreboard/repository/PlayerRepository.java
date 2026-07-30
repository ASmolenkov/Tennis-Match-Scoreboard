package io.github.asmolenkov.tennismatchscoreboard.repository;

import io.github.asmolenkov.tennismatchscoreboard.entity.Player;

import java.util.Optional;

public interface PlayerRepository {
    void save(Player player);
    Optional<Player> findByName(String name);
}
