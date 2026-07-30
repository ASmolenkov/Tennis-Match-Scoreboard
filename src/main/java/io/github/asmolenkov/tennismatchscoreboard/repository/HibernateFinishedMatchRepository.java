package io.github.asmolenkov.tennismatchscoreboard.repository;

import io.github.asmolenkov.tennismatchscoreboard.dto.MatchDto;
import io.github.asmolenkov.tennismatchscoreboard.entity.Match;
import io.github.asmolenkov.tennismatchscoreboard.entity.Player;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
public class HibernateFinishedMatchRepository implements FinishedMatchRepository {

    private final SessionFactory sessionFactory;

    private static final String JPQL_FIND_MATCH = """
            FROM Match m WHERE m.id = :id
            """;
    private static final String JPQL_FIND_PAGINATION_MATCH = """
            SELECT m FROM Match m
            JOIN FETCH m.playerOne
            JOIN FETCH m.playerSecond
            LEFT JOIN FETCH m.winner
            ORDER BY m.id DESC  /* Новые матчи сверху */
            """;
    private static final String JPQL_COUNT_TOTAL = "SELECT COUNT(m) FROM Match m";
    private static final String JPQL_FIND_BY_NAME_PAGINATION_MATCH = """
            SELECT m FROM Match m
            JOIN FETCH m.playerOne
            JOIN FETCH m.playerSecond
            LEFT JOIN FETCH m.winner
            WHERE LOWER(m.playerOne.name) LIKE LOWER(:name)
               OR LOWER(m.playerSecond.name) LIKE LOWER(:name)
            ORDER BY m.id DESC
            """;
    private static final String JPQL_COUNT_BY_NAME_TOTAL = """
            SELECT COUNT(m) FROM Match m
            WHERE LOWER(m.playerOne.name) LIKE LOWER(:name)
               OR LOWER(m.playerSecond.name) LIKE LOWER(:name)
            """;

    private static final String PARAMETER_NAME = "name";


    public void save(MatchDto matchDto) {
        EntityManager entityManager = sessionFactory.createEntityManager().unwrap(EntityManager.class);

        Player playerOne = entityManager.getReference(Player.class, matchDto.playerOne().id());
        Player playerSecond = entityManager.getReference(Player.class, matchDto.playerSecond().id());
        Player winner = entityManager.getReference(Player.class, matchDto.winner().id());

        Match match = new Match(playerOne, playerSecond, winner);

       sessionFactory.getCurrentSession().persist(match);
    }

    public Optional<Match> find(long id) {

        List<Match> findMatches = sessionFactory.getCurrentSession().createQuery(JPQL_FIND_MATCH, Match.class)
                                         .setParameter("id", id)
                                         .getResultList();

        return findMatches.isEmpty() ? Optional.empty() : Optional.of(findMatches.getFirst());
    }


    public List<Match> findWithPagination(int offset, int limit) {

        return sessionFactory.getCurrentSession().createQuery(JPQL_FIND_PAGINATION_MATCH, Match.class)
                      .setFirstResult(offset)
                      .setMaxResults(limit)
                      .getResultList();
    }

    public long countTotal() {
        return sessionFactory.getCurrentSession().createQuery(JPQL_COUNT_TOTAL, Long.class)
                      .getSingleResult();
    }

    public List<Match> findByNameWithPagination(String name, int offset, int limit) {
        return sessionFactory.getCurrentSession().createQuery(JPQL_FIND_BY_NAME_PAGINATION_MATCH, Match.class)
                      .setParameter(PARAMETER_NAME, "%" + name + "%")
                      .setFirstResult(offset)
                      .setMaxResults(limit)
                      .getResultList();
    }

    public long countByName(String name) {
        return sessionFactory.getCurrentSession().createQuery(JPQL_COUNT_BY_NAME_TOTAL, Long.class)
                      .setParameter(PARAMETER_NAME, "%" + name + "%")
                      .getSingleResult();
    }
}
