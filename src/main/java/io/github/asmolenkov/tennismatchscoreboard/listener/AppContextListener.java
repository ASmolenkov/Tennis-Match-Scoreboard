package io.github.asmolenkov.tennismatchscoreboard.listener;

import io.github.asmolenkov.tennismatchscoreboard.repository.ActiveMatchRepository;
import io.github.asmolenkov.tennismatchscoreboard.repository.FinishedMatchRepository;
import io.github.asmolenkov.tennismatchscoreboard.repository.HibernateFinishedMatchRepository;
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

    public static final String PLAYER_REPOSITORY_KEY = "playerRepository";
    public static final String MATH_REPOSITORY_KEY = "mathRepository";
    public static final String FINISHED_MATCH_REPOSITORY_SERVICE_KEY = "finishedMatchRepository";
    public static final String FINISHED_MATCHES_PERSISTENCE_SERVICE_SERVICE_KEY = "finishedMatchesPersistenceService";


    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
        TransactionManager transactionManager = new HibernateTransactionManager(sessionFactory);
        HibernatePlayerRepository hibernatePlayerRepository = new HibernatePlayerRepository(sessionFactory);
        PlayerInterface playerService = new PlayerService(hibernatePlayerRepository, transactionManager);
        ActiveMatchRepository activeMatchRepository = new ActiveMatchRepository();

        FinishedMatchRepository finishedMatchRepository = new HibernateFinishedMatchRepository(sessionFactory);
        FinishedMatchesPersistence finishedMatchesPersistence = new FinishedMatchesPersistenceService(
                transactionManager, finishedMatchRepository);
        OngoingMatches ongoingMatchesService = new OngoingMatchesService(activeMatchRepository,finishedMatchesPersistence ,playerService);
        context.setAttribute(PlayerService.class.getSimpleName(), playerService);
        context.setAttribute(PLAYER_REPOSITORY_KEY, hibernatePlayerRepository);
        context.setAttribute(MATH_REPOSITORY_KEY, activeMatchRepository);
        context.setAttribute(OngoingMatchesService.class.getSimpleName(), ongoingMatchesService);
        context.setAttribute(FINISHED_MATCH_REPOSITORY_SERVICE_KEY, finishedMatchRepository);
        context.setAttribute(FinishedMatchesPersistenceService.class.getSimpleName(), finishedMatchesPersistence);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();

        SessionFactory sessionFactory = (SessionFactory) context.getAttribute(SessionFactory.class.getSimpleName());
        if(sessionFactory != null && !sessionFactory.isClosed()){
            sessionFactory.close();
        }

        context.removeAttribute(SessionFactory.class.getSimpleName());
    }
}
