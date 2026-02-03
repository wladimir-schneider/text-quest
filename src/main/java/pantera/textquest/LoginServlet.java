package pantera.textquest;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pantera.textquest.dao.InMemoryUserDao;
import pantera.textquest.model.User;

import java.io.IOException;
import java.util.logging.Logger;

@WebServlet(name = "LoginServlet", value = "/login")
public class LoginServlet extends HttpServlet {
    private static final Logger log = Logger.getLogger(LoginServlet.class.getName());
    private InMemoryUserDao dao;

    @Override
    public void init() {
        dao = (InMemoryUserDao) getServletContext().getAttribute("userDao");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.fine("POST /login received");
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        log.info("Login attempt for user: " + username);
        if (username == null || password == null) {
            req.setAttribute("error", "Invalid credentials");
            log.warning("Invalid credentials for username: " + username);
            doGet(req, resp);
            return;
        }
        java.util.Optional<User> u = dao.authenticate(username.trim(), password);
        if (u.isEmpty()) {
            req.setAttribute("error", "Invalid credentials");
            log.warning("Invalid credentials for username: " + username);
            doGet(req, resp);
            return;
        }
        log.info("Login successful for user: " + username);
        req.getSession().setAttribute("currentUser", u.get());
        resp.sendRedirect(req.getContextPath() + "/game");
    }
}
