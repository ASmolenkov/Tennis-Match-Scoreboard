package io.github.asmolenok.entity;

import io.github.asmolenkov.tennismatchscoreboard.entity.Player;
import io.github.asmolenkov.tennismatchscoreboard.utils.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PlayerTest {

    @Test
    public void newPlayerIsCorrect() {
        try(Session session = HibernateUtils.getSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Player player = Player.builder()
                                      .name("Sasha")
                                      .build();
                session.persist(player);
                transaction.commit();
                try(Session sessionSecond = HibernateUtils.getSession()) {
                    sessionSecond.beginTransaction();
                    Player playerGet = sessionSecond.find(Player.class, player.getId());

                    Assertions.assertNotNull(playerGet);
                    Assertions.assertEquals(player.getId(), playerGet.getId());
                    Assertions.assertEquals(player.getName(), playerGet.getName());
                    sessionSecond.getTransaction().commit();
                }
            }catch (Exception e){
                transaction.rollback();
                throw e;
            }
        }


    }
}
