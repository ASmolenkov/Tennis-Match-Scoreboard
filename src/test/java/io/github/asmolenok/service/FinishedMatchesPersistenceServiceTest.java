package io.github.asmolenok.service;

import io.github.asmolenkov.tennismatchscoreboard.dto.PlayerDto;
import io.github.asmolenkov.tennismatchscoreboard.entity.Match;
import io.github.asmolenkov.tennismatchscoreboard.entity.Player;
import io.github.asmolenkov.tennismatchscoreboard.model.CurrentMatch;
import io.github.asmolenkov.tennismatchscoreboard.repository.HibernateFinishedMatchRepository;
import io.github.asmolenkov.tennismatchscoreboard.repository.HibernatePlayerRepository;
import io.github.asmolenkov.tennismatchscoreboard.service.FinishedMatchesPersistenceService;
import io.github.asmolenkov.tennismatchscoreboard.service.HibernateTransactionManager;
import io.github.asmolenkov.tennismatchscoreboard.service.PlayerService;
import io.github.asmolenkov.tennismatchscoreboard.service.TransactionManager;
import io.github.asmolenok.utils.TestUtils;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;

public class FinishedMatchesPersistenceServiceTest {
    private static SessionFactory testSf;
    private FinishedMatchesPersistenceService finishedMatches;
    private PlayerService playerService;
    private final TransactionManager transactionManager = new HibernateTransactionManager(testSf);

    @BeforeAll
    static void initDatabase() {
        testSf = new Configuration()
                .setProperty("hibernate.connection.driver_class", "org.h2.Driver")
                .setProperty("hibernate.connection.url", "jdbc:h2:mem:test_tennis_db;DB_CLOSE_DELAY=-1")
                .setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect")
                .setProperty("hibernate.hbm2ddl.auto", "create-drop")
                .setProperty("hibernate.show_sql", "true")
                .setProperty("hibernate.current_session_context_class","thread")
                .addAnnotatedClass(Match.class)
                .addAnnotatedClass(Player.class)
                .buildSessionFactory();
    }

    @BeforeEach
    void setUp() {
        HibernateFinishedMatchRepository hibernateFinishedMatchRepository = new HibernateFinishedMatchRepository(testSf);
        finishedMatches = new FinishedMatchesPersistenceService(transactionManager, hibernateFinishedMatchRepository);
        HibernatePlayerRepository hibernatePlayerRepository = new HibernatePlayerRepository(testSf);
        playerService = new PlayerService(hibernatePlayerRepository, new HibernateTransactionManager(testSf));
    }

    @Test
    @DisplayName("Матч сохранен")
    void saveMatch_success(){

        PlayerDto playerDto1 = playerService.createPlayer("Sasha");
        PlayerDto playerDto2 = playerService.createPlayer("Masha");
        CurrentMatch currentMatch = TestUtils.createCompletedMatch(playerDto1, playerDto2);

        finishedMatches.saveMatch(currentMatch);

        Match match = finishedMatches.findMathById(1L);

        Assertions.assertEquals(currentMatch.getPlayerOne().id(), match.getPlayerOne().getId());
        Assertions.assertEquals(currentMatch.getPlayerSecond().id(), match.getPlayerSecond().getId());

    }
}
