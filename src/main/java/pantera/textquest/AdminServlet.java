package pantera.textquest;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pantera.textquest.dao.InMemoryUserDao;
import pantera.textquest.model.User;

import java.io.IOException;
import java.util.List;

import java.util.logging.Logger;

@WebServlet(name = "AdminServlet", value = "/admin")
public class AdminServlet extends HttpServlet {
    private static final Logger log = Logger.getLogger(AdminServlet.class.getName());
    private InMemoryUserDao dao;

    @Override
    public void init() {
        dao = (InMemoryUserDao) getServletContext().getAttribute("userDao");
    }

    private boolean isAdmin(HttpServletRequest req) {
        Object o = req.getSession().getAttribute("currentUser");
        if (o instanceof User u) {
            log.info("Admin panel opened");
            return u.getRole() != null && u.getRole().name().equals("ADMIN");
        }
        return false;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!isAdmin(req)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            log.warning("Unauthorized admin access attempt");
            return;
        }
        List<User> users = dao.listAll();
        req.setAttribute("users", users);
        req.getRequestDispatcher("/admin.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (!isAdmin(req)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            log.warning("Unauthorized admin access attempt");
            return;
        }
        String action = req.getParameter("action");
        String username = req.getParameter("username");
        if ("delete".equals(action) && username != null) {
            dao.delete(username);
        }
        resp.sendRedirect(req.getContextPath() + "/admin");
    }
}
