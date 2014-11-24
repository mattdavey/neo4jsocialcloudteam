package ProjectTeamModelling.domain;

import java.util.ArrayList;

public class Project extends DemandItem {
    private ArrayList<DemandItem> demandItems = new ArrayList<>();
    private Team team;

    public Project(final String name) {
        super(name);
    }

    public void addTeam(final Team team) {
        this.team = team;
    }

    public void addWorkItem(final DemandItem demandItem) {
        demandItems.add(demandItem);
    }
}
