package ProjectTeamModelling.domain;

public class Effort {
    private EffortType effortType;
    private int effort;

    public Effort(final EffortType effortType, int effort) {
        this.effortType = effortType;
        this.effort = effort;
    }
}
