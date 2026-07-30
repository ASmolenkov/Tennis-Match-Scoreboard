package io.github.asmolenkov.tennismatchscoreboard.listener;

import io.github.asmolenkov.tennismatchscoreboard.repository.ActiveMatchRepository;
import io.github.asmolenkov.tennismatchscoreboard.repository.FinishedMatchRepository;
import io.github.asmolenkov.tennismatchscoreboard.repository.HibernatePlayerRepository;
import io.github.asmolenkov.tennismatchscoreboard.service.*;
import io.github.asmolenkov.tennismatchscoreboard.utils.HibernateUtils;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.hibernate.SessionFactory;

@WebListener
public class AppContextListener implements ServletContextListener {
    public static final String PLAYER_SERVICE_KEY = "playerService";
    public static final String PLAYER_REPOSITORY_KEY = "playerRepository";
    public static final String MATH_REPOSITORY_KEY = "mathRepository";
    public static final String ONGOING_MATH_SERVICE_KEY = "mathRepository";
    public static final String MATCH_SCORE_CALCULATION_SERVICE_KEY = "matchScoreCalculation";
    public static final String FINISHED_MATCH_REPOSITORY_SERVICE_KEY = "finishedMatchRepository";
    public static final String FINISHED_MATCHES_PERSISTENCE_SERVICE_SERVICE_KEY = "finishedMatchesPersistenceService";



    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
        HibernatePlayerRepository hibernatePlayerRepository = new HibernatePlayerRepository(sessionFactory);
        PlayerService playerService = new PlayerService(hibernatePlayerRepository, new HibernateTransactionManager(sessionFactory));
        ActiveMatchRepository activeMatchRepository = new ActiveMatchRepository();
        OngoingMatchesService ongoingMatchesService = new OngoingMatchesService(activeMatchRepository);
        FinishedMatchRepository finishedMatchRepository = new FinishedMatchRepository();
        MatchScoreCalculationService matchScoreCalculationService = new MatchScoreCalculationService(activeMatchRepository);
        FinishedMatchesPersistenceService finishedMatchesPersistenceService = new FinishedMatchesPersistenceService(sessionFactory, finishedMatchRepository);
        context.setAttribute(PLAYER_SERVICE_KEY, playerService);
        context.setAttribute(PLAYER_REPOSITORY_KEY, hibernatePlayerRepository);
        context.setAttribute(MATH_REPOSITORY_KEY, activeMatchRepository);
        context.setAttribute(ONGOING_MATH_SERVICE_KEY, ongoingMatchesService);
        context.setAttribute(MATCH_SCORE_CALCULATION_SERVICE_KEY, matchScoreCalculationService);
        context.setAttribute(FINISHED_MATCH_REPOSITORY_SERVICE_KEY, finishedMatchRepository);
        context.setAttribute(FINISHED_MATCHES_PERSISTENCE_SERVICE_SERVICE_KEY, finishedMatchesPersistenceService);
    }
}
