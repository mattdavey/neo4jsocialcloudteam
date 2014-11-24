package ProjectTeamModelling.domain;

import org.joda.time.DateTime;
import org.joda.time.DurationFieldType;
import org.joda.time.Period;
import org.parboiled.common.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public abstract class Demand {
    private final Logger logger = LoggerFactory.getLogger(Demand.class);

    protected String name;
    protected DateTime start;
    protected DateTime end;
    protected Period period;
    protected Effort effort = new Effort(EffortType.DAYS, 0);
    protected DeliveryStatus deliveryStatu = DeliveryStatus.NotStarted;
    protected SkillEstimateTuple[] skills;

    private ArrayList<Demand> forwardsLinkage = new ArrayList<>();
    private ArrayList<Demand> reverseLinkage = new ArrayList<>();

    public Demand(final String name, final Period period) {
        this.name = name;
        this.period = period;
    }

    public Demand(final String name, final Period period, final SkillEstimateTuple[] skills) {
        this(name, period);
        this.skills = skills;
    }

    public Demand(final String name) {
        this.name = name;
    }

    public String getName() {return name;}

    public void setPeriod(final Period period) {
        this.period = period;
    }

    public DateTime getStart() {return start;}

    public void setStart(final DateTime start) {this.start = start;}

    public void setEnd(final DateTime end) {this.end = end;}

    public DateTime getEnd() {return end;}

    public int walkForwardDuration(final DurationFieldType durationFieldType) {
        int duration = period.get(durationFieldType);
        for (final Demand demand : forwardsLinkage) {
            duration += demand.walkForwardDuration(durationFieldType);
        }

        return duration;
    }

    protected void storeLinkage(final Demand demandItem) {
        demandItem.forwardsLinkage.add(this);
        this.reverseLinkage.add(demandItem);
    }

    protected String walkForwards(final int indent) {
        final String indentSpace = StringUtils.repeat('|', indent);

        String str = String.format("%s-%s (%d) \n", indentSpace, name, period.get(DurationFieldType.seconds()));
        for (final Demand demand : forwardsLinkage) {
            str += demand.walkForwards(indent + 1);
        }

        return str;
    }

    protected String reverseWalk() {
        final StringBuffer str = new StringBuffer(String.format("<-%s", name));
        for (final Demand demand : reverseLinkage) {
            str.append(demand.reverseWalk());
        }

        return str.toString();
    }
}
