package pantera.textquest.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a single step in a branching quest.
 * Each step has a description and optional choices leading to other steps.
 */
public class QuestStep {
    private String id;
    private String description;
    private List<QuestChoice> choices = new ArrayList<>();
    private boolean isEnding = false;
    private String endingText;

    public QuestStep() {}

    public QuestStep(String id, String description) {
        this.id = id;
        this.description = description;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<QuestChoice> getChoices() { return choices; }
    public void setChoices(List<QuestChoice> choices) { this.choices = choices; }

    public boolean isEnding() { return isEnding; }
    public void setEnding(boolean ending) { isEnding = ending; }

    public String getEndingText() { return endingText; }
    public void setEndingText(String endingText) { this.endingText = endingText; }

    public void addChoice(QuestChoice choice) {
        if (choices == null) choices = new ArrayList<>();
        choices.add(choice);
    }

    public static class QuestChoice {
        private String text;
        private String nextStepId;

        public QuestChoice() {}

        public QuestChoice(String text, String nextStepId) {
            this.text = text;
            this.nextStepId = nextStepId;
        }

        public String getText() { return text; }
        public void setText(String text) { this.text = text; }

        public String getNextStepId() { return nextStepId; }
        public void setNextStepId(String nextStepId) { this.nextStepId = nextStepId; }
    }
}
