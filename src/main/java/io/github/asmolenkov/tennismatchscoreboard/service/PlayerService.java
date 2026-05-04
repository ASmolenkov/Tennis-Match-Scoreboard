package io.github.asmolenkov.tennismatchscoreboard.service;

import io.github.asmolenkov.tennismatchscoreboard.entity.Player;
import io.github.asmolenkov.tennismatchscoreboard.exception.DuplicateNameException;
import io.github.asmolenkov.tennismatchscoreboard.repository.PlayerRepository;
import io.github.asmolenkov.tennismatchscoreboard.utils.HibernateUtils;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.Optional;
@Slf4j
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final SessionFactory sessionFactory;


    public PlayerService(PlayerRepository playerRepository, SessionFactory sessionFactory) {
        this.playerRepository = playerRepository;
        this.sessionFactory = sessionFactory;
    }

    public void createPlayer(String name){
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                if (playerRepository.existsByName(name, session)) {
                    transaction.rollback();
                    log.warn("Игрок {} уже существует в БД!", name);
                    return;
                    // throw new DuplicateNameException("Пользователь с именем '" + name + "' уже существует");
                }

                Player player = Player.builder()
                                      .name(name)
                                      .build();
                // TODO Не вижу отображения новых игроков в БД(написан тест для проверки метода, убедится в наличии игроков написав тестовый jsp)
                playerRepository.save(player, session);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
            }

        }

    }
}
