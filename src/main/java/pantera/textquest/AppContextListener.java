package pantera.textquest;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import pantera.textquest.dao.InMemoryUserDao;

import java.util.logging.Logger;

@WebListener
public class AppContextListener implements ServletContextListener {
    private static final Logger log = Logger.getLogger(AppContextListener.class.getName());
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.info("Application is started");
        InMemoryUserDao dao = new InMemoryUserDao();
        sce.getServletContext().setAttribute("userDao", dao);
    }
}
