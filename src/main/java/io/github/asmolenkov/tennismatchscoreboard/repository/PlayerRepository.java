package io.github.asmolenkov.tennismatchscoreboard.repository;

import io.github.asmolenkov.tennismatchscoreboard.entity.Player;
import io.github.asmolenkov.tennismatchscoreboard.utils.HibernateUtils;
import org.hibernate.Session;

import java.util.Optional;

public class PlayerRepository {

    public void save(Player player, Session session) {
        session.persist(player);
    }

    public boolean existsByName(String name, Session session) {
        String jpql = "FROM Player p WHERE p.name = :name";
        long result = session.createQuery(jpql, Player.class)
                             .setParameter("name", name)
                             .getResultCount();

        return result > 0;
    }
}
