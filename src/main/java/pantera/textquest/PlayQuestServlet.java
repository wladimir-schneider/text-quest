package pantera.textquest;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pantera.textquest.dao.InMemoryUserDao;
import pantera.textquest.model.Quest;
import pantera.textquest.model.QuestStep;
import pantera.textquest.model.User;

import java.io.IOException;

@WebServlet(name = "PlayQuestServlet", value = "/play-quest")
public class PlayQuestServlet extends HttpServlet {
    private InMemoryUserDao dao;

    @Override
    public void init() {
        dao = (InMemoryUserDao) getServletContext().getAttribute("userDao");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User currentUser = (User) req.getSession().getAttribute("currentUser");
        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String questId = req.getParameter("questId");
        String stepId = req.getParameter("stepId");

        if (questId == null) {
            resp.sendRedirect(req.getContextPath() + "/quests");
            return;
        }

        Quest quest = dao.getQuest(questId);
        if (quest == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Quest not found");
            return;
        }

        if (stepId == null) {
            stepId = quest.getStartStepId();
            if (stepId == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Quest has no start step");
                return;
            }
        }

        QuestStep currentStep = quest.getStep(stepId);
        if (currentStep == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid step");
            return;
        }

        req.setAttribute("quest", quest);
        req.setAttribute("currentStep", currentStep);
        req.setAttribute("stepId", stepId);
        req.setAttribute("isEnding", currentStep.isEnding());

        req.getRequestDispatcher("/playQuest.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User currentUser = (User) req.getSession().getAttribute("currentUser");
        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String questId = req.getParameter("questId");
        String choiceIndex = req.getParameter("choiceIndex");

        if (questId == null || choiceIndex == null) {
            resp.sendRedirect(req.getContextPath() + "/quests");
            return;
        }

        Quest quest = dao.getQuest(questId);
        if (quest == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Quest not found");
            return;
        }

        String currentStepId = req.getParameter("currentStepId");
        QuestStep currentStep = quest.getStep(currentStepId);
        if (currentStep == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid step");
            return;
        }

        try {
            int index = Integer.parseInt(choiceIndex);
            if (index < 0 || index >= currentStep.getChoices().size()) {
                resp.sendRedirect(req.getContextPath() + "/play-quest?questId=" + questId + "&stepId=" + currentStepId);
                return;
            }

            String nextStepId = currentStep.getChoices().get(index).getNextStepId();
            resp.sendRedirect(req.getContextPath() + "/play-quest?questId=" + questId + "&stepId=" + nextStepId);
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/quests");
        }
    }
}
