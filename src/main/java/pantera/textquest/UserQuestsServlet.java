package pantera.textquest;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pantera.textquest.dao.InMemoryUserDao;
import pantera.textquest.model.User;

import java.io.IOException;

@WebServlet(name = "UserQuestsServlet", value = "/quests")
public class UserQuestsServlet extends HttpServlet {
    private InMemoryUserDao dao;

    @Override
    public void init() {
        dao = (InMemoryUserDao) getServletContext().getAttribute("userDao");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Object o = req.getSession().getAttribute("currentUser");
        if (!(o instanceof User u)) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        req.setAttribute("quests", dao.listQuests(u.getUsername()));
        req.getRequestDispatcher("/userQuests.jsp").forward(req, resp);
    }
}
