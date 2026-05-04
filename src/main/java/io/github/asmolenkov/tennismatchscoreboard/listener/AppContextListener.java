package io.github.asmolenkov.tennismatchscoreboard.listener;

import io.github.asmolenkov.tennismatchscoreboard.repository.PlayerRepository;
import io.github.asmolenkov.tennismatchscoreboard.service.PlayerService;
import io.github.asmolenkov.tennismatchscoreboard.utils.HibernateUtils;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

@WebListener
public class AppContextListener implements ServletContextListener {
    public static final String PLAYER_SERVICE_KEY = "playerService";
    public static final String PLAYER_REPOSITORY_KEY = "playerRepository";



    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        PlayerRepository playerRepository = new PlayerRepository();
        SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
        PlayerService playerService = new PlayerService(playerRepository, sessionFactory);
        context.setAttribute("playerService", playerService);
        context.setAttribute("playerRepository", playerRepository);
    }
}
