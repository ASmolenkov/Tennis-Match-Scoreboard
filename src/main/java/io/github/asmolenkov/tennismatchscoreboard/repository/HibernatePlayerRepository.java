package io.github.asmolenkov.tennismatchscoreboard.repository;

import io.github.asmolenkov.tennismatchscoreboard.entity.Player;
import io.github.asmolenkov.tennismatchscoreboard.exception.PlayerCreationException;
import io.github.asmolenkov.tennismatchscoreboard.exception.PlayerFindException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;


import java.util.Optional;

@Slf4j
@AllArgsConstructor
public class HibernatePlayerRepository implements PlayerRepository {

    private final SessionFactory sessionFactory;

    private static final String JPQL_FIND_PLAYER = """
                                                     FROM Player p
                                                     WHERE p.name = :name
                                                   """;
    private static final String PARAMETER_NAME = "name";
    private static final String SAVE_PLAYER_FAILED_TEMPLATE = "Failed to save player %s";
    private static final String PLAYER_NOT_FOUND_TEMPLATE = "Player %s not found";

    @Override
    public void save(Player player) {
        try {
            sessionFactory.getCurrentSession().persist(player);
        } catch (Exception e) {
            throw new PlayerCreationException(SAVE_PLAYER_FAILED_TEMPLATE.formatted(player.getName()), e);
        }

    }

    @Override
    public Optional<Player> findByName(String name) {
        try {
            return sessionFactory.getCurrentSession()
                                 .createQuery(JPQL_FIND_PLAYER, Player.class)
                                 .setParameter(PARAMETER_NAME, name)
                                 .uniqueResultOptional();
        } catch (Exception e) {
            throw new PlayerFindException(PLAYER_NOT_FOUND_TEMPLATE.formatted(name), e);
        }
    }

}
