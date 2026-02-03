package pantera.textquest;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pantera.textquest.model.User;

import java.io.IOException;

@WebServlet(name = "GameServlet", value = "/game")
public class GameServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Object o = req.getSession().getAttribute("currentUser");
        if (!(o instanceof User)) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        User u = (User) o;
        req.setAttribute("progress", u.getProgress());
        req.getRequestDispatcher("/game.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Object o = req.getSession().getAttribute("currentUser");
        if (!(o instanceof User)) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        User u = (User) o;
        String action = req.getParameter("action");
        String progress = u.getProgress();
        if (action == null) {
            resp.sendRedirect(req.getContextPath() + "/game");
            return;
        }
        if ("start".equals(action)) {
            u.setProgress("zone");
        } else if ("explore_ruins".equals(action) && "zone".equals(progress)) {
            u.setProgress("ruins");
        } else if ("enter_anomaly".equals(action) && "zone".equals(progress)) {
            u.setProgress("anomaly");
        } else if ("search_artifacts".equals(action) && "ruins".equals(progress)) {
            u.setProgress("artifact_found");
        } else if ("danger_zone".equals(action) && "ruins".equals(progress)) {
            u.setProgress("mutant_encounter");
        } else if ("jump_anomaly".equals(action) && "anomaly".equals(progress)) {
            u.setProgress("teleported");
        } else if ("escape_anomaly".equals(action) && "anomaly".equals(progress)) {
            u.setProgress("escaped");
        } else if ("fight".equals(action) && "mutant_encounter".equals(progress)) {
            u.setProgress("fought_mutant");
        } else if ("flee".equals(action) && "mutant_encounter".equals(progress)) {
            u.setProgress("fled");
        } else if ("reset".equals(action)) {
            u.setProgress("start");
        }
        resp.sendRedirect(req.getContextPath() + "/game");
    }
}
