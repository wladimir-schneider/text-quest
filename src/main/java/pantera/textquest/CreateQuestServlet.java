package pantera.textquest;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pantera.textquest.dao.InMemoryUserDao;
import pantera.textquest.model.Quest;
import pantera.textquest.model.User;
import pantera.textquest.model.QuestStep;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@WebServlet(name = "CreateQuestServlet", value = "/quests/create")
public class CreateQuestServlet extends HttpServlet {
    private InMemoryUserDao dao;
    private static final Logger log = Logger.getLogger(CreateQuestServlet.class.getName());

    @Override
    public void init() {
        dao = (InMemoryUserDao) getServletContext().getAttribute("userDao");
        if (dao == null) {
            log.severe("UserDao not found in ServletContext");
        } else {
            log.info("CreateQuestServlet initialized");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.fine("POST /quests/create received");
        req.getRequestDispatcher("/createQuest.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.fine("POST /quests/create received");
        Object o = req.getSession().getAttribute("currentUser");
        if (!(o instanceof User u)) {
            log.warning("Unauthorized quest creation attempt");
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        String title = req.getParameter("title");
        if (title == null || title.isBlank()) {
            log.warning("Quest creation failed: missing title (user=" + u.getUsername() + ")");
            req.setAttribute("error", "Title required");
            doGet(req, resp);
            return;
        }

        Quest q = new Quest(title, u.getUsername());
        
        String questStructure = req.getParameter("questStructure");
        Map<String, String> stepOrder = new HashMap<>();
        
        if (questStructure != null && !questStructure.isBlank()) {
            String[] stepLines = questStructure.split("\n");
            String startStepId = null;
            int stepNum = 0;
            
            for (String line : stepLines) {
                line = line.trim();
                if (line.isEmpty()) continue;
                
                String[] parts = line.split("\\|");
                if (parts.length < 2) continue;
                
                String stepId = parts[0].trim();
                String description = parts[1].trim();
                
                if (stepNum == 0) {
                    startStepId = stepId;
                }
                stepNum++;
                
                QuestStep step = new QuestStep(stepId, description);
                log.info("Creating quest: title=" + title + ", user=" + u.getUsername());

                for (int i = 2; i < parts.length; i++) {
                    String choiceStr = parts[i].trim();
                    if (choiceStr.contains("->")) {
                        String[] choiceParts = choiceStr.split("->");
                        if (choiceParts.length == 2) {
                            String choiceText = choiceParts[0].trim();
                            String nextStepId = choiceParts[1].trim();
                            step.addChoice(new QuestStep.QuestChoice(choiceText, nextStepId));
                        }
                    } else if (choiceStr.equals("END")) {
                        step.setEnding(true);
                        step.setEndingText(description);
                        q.addEnding(stepId);
                    }
                }
                
                q.addStep(step);
            }
            
            if (startStepId != null) {
                q.setStartStepId(startStepId);
            }
        }
        
        dao.addQuest(u.getUsername(), q);
        log.info("Quest created successfully: " + title);
        resp.sendRedirect(req.getContextPath() + "/quests");
    }
}
