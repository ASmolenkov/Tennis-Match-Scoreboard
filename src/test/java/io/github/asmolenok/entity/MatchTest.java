package io.github.asmolenok.entity;

import io.github.asmolenkov.tennismatchscoreboard.entity.Match;
import io.github.asmolenkov.tennismatchscoreboard.entity.Player;
import io.github.asmolenkov.tennismatchscoreboard.utils.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MatchTest {
    @Test
    public void createMathIsCorrect() {
        try (Session session = HibernateUtils.getSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Player player = Player.builder()
                                      .name("Sasha")
                                      .build();
                Player player2 = Player.builder()
                                       .name("Masha")
                                       .build();
                Player winner = Player.builder()
                                      .name("Sasha")
                                      .build();

                session.persist(player);
                session.persist(player2);


                Match match = Match.builder()
                                   .playerOne(player)
                                   .playerSecond(player2)
                                   .winner(player)
                                   .build();

                session.persist(match);
                transaction.commit();
                try(Session session2 = HibernateUtils.getSession()) {
                    session2.beginTransaction();

                    Match mathGet = session2.find(Match.class, match.getId());

                    Assertions.assertEquals(match.getId(), mathGet.getId());
                    Assertions.assertEquals(match.getPlayerOne().getName(), mathGet.getPlayerOne().getName());
                    Assertions.assertEquals(match.getPlayerSecond().getName(), mathGet.getPlayerSecond().getName());
                    Assertions.assertEquals(match.getWinner().getName(), mathGet.getWinner().getName());
                    session2.getTransaction().commit();
                }
            } catch (Exception e) {
                transaction.rollback();
                throw e;
            }
        }
    }
}
