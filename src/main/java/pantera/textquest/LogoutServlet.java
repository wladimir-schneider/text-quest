package pantera.textquest;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.Logger;

@WebServlet(name = "LogoutServlet", value = "/logout")
public class LogoutServlet extends HttpServlet {
    private static final Logger log = Logger.getLogger(LogoutServlet.class.getName());
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.getSession().invalidate();
        log.info("User logged out");
        resp.sendRedirect(req.getContextPath() + "/");
    }
}
