package ProjectTeamModelling.domain;

import org.joda.time.Period;

public class SkillEstimateTuple {
    private String skill;
    private Period estimate;

    public SkillEstimateTuple(final String skill, final Period estimate) {

        this.skill = skill;
        this.estimate = estimate;
    }
}
