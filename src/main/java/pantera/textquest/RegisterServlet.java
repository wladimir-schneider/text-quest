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

@WebServlet(name = "RegisterServlet", value = "/register")
public class RegisterServlet extends HttpServlet {
    private static final Logger log = Logger.getLogger(RegisterServlet.class.getName());
    private InMemoryUserDao dao;


    @Override
    public void init() {
        dao = (InMemoryUserDao) getServletContext().getAttribute("userDao");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.fine("POST /register received");
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        if (username == null || password == null || username.isBlank()) {
            req.setAttribute("error", "Invalid input");
            doGet(req, resp);
            return;
        }
        log.info("Registering new user: " + username);
        User u = new User();
        u.setUsername(username.trim());
        u.setPassword(password);
        boolean ok = dao.add(u);
        if (!ok) {
            req.setAttribute("error", "User already exists");
            log.warning("Registration failed, username exists: " + username);
            doGet(req, resp);
            return;
        }
        req.getSession().setAttribute("currentUser", u);
        log.info("User registered: " + username);
        resp.sendRedirect(req.getContextPath() + "/game");
    }
}
