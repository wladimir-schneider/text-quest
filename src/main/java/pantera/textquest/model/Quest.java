package pantera.textquest.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Quest {
    private final String id = UUID.randomUUID().toString();
    private String title;
    private String creator;
    private Map<String, QuestStep> steps = new HashMap<>();
    private String startStepId;
    private List<String> endingIds = new ArrayList<>();

    public Quest() {}

    public Quest(String title, String creator) {
        this.title = title;
        this.creator = creator;
    }

    // Backwards-compatible constructor: from linear list of step descriptions
    public Quest(String title, List<String> stepsList, String creator) {
        this.title = title;
        this.creator = creator;
        this.steps = new HashMap<>();
        if (stepsList != null) {
            int idx = 1;
            for (String desc : stepsList) {
                String id = "step" + idx++;
                QuestStep step = new QuestStep(id, desc == null ? "" : desc);
                this.steps.put(id, step);
            }
            if (!stepsList.isEmpty()) this.startStepId = "step1";
        }
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCreator() { return creator; }
    public void setCreator(String creator) { this.creator = creator; }

    public Map<String, QuestStep> getSteps() { return steps; }
    public void setSteps(Map<String, QuestStep> steps) { this.steps = steps; }

    // Backwards-compatible setter: accept a List of step descriptions
    public void setSteps(List<String> stepsList) {
        this.steps = new HashMap<>();
        if (stepsList != null) {
            int idx = 1;
            for (String desc : stepsList) {
                String id = "step" + idx++;
                QuestStep step = new QuestStep(id, desc == null ? "" : desc);
                this.steps.put(id, step);
            }
            if (!stepsList.isEmpty()) this.startStepId = "step1";
        }
    }

    public String getStartStepId() { return startStepId; }
    public void setStartStepId(String startStepId) { this.startStepId = startStepId; }

    public List<String> getEndingIds() { return endingIds; }
    public void setEndingIds(List<String> endingIds) { this.endingIds = endingIds; }

    public void addStep(QuestStep step) {
        if (steps == null) steps = new HashMap<>();
        steps.put(step.getId(), step);
    }

    public QuestStep getStep(String stepId) {
        return steps != null ? steps.get(stepId) : null;
    }

    public void addEnding(String endingId) {
        if (endingIds == null) endingIds = new ArrayList<>();
        endingIds.add(endingId);
    }
}
