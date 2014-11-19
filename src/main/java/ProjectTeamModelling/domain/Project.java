package ProjectTeamModelling.domain;

import java.util.ArrayList;

public class Project {
    private ArrayList<WorkItem> workItems = new ArrayList<>();
    private String name;
    private Team team;

    public Project(final String name) {
        this.name = name;
    }

    public void addTeam(final Team team) {
        this.team = team;
    }

    public void addWorkItem(final WorkItem workItem) {
        workItems.add(workItem);
    }
}
