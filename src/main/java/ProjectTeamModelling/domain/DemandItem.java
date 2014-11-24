package ProjectTeamModelling.domain;

import org.joda.time.Period;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import rx.Observable;
import rx.Subscription;
import rx.subjects.PublishSubject;

// Dependencies between WorkItems:
// 1. Start - Finish Dependency
// 2. Finish - Finish together
// 3. Start - Start together
// 4. Partial Finish - Start

public class DemandItem extends Demand {
    private final Logger logger = LoggerFactory.getLogger(DemandItem.class);

    private PublishSubject<DemandEvents> subject = PublishSubject.create();
    private PublishSubject<Integer> percentComplete = PublishSubject.create();
    private Subscription subscription;
    private boolean running = false;

    public DemandItem(final String name, final Period period) {
        super(name, period);
    }

    public DemandItem(final String name, final Period period, final SkillEstimateTuple[] skills) {
        super(name, period, skills);
    }

    public DemandItem(final String name) {
        super(name);
    }

    public void start() {
        running = true;

        if (subscription != null)
            subscription.unsubscribe();

        logger.debug(String.format("%s Start()", name));
        subject.onNext(DemandEvents.Start);

        for (int i=0; i < 10; i++) {
            final int percent = (i+1) * 10;
            percentComplete.onNext(percent);
        }

        finish();
    }

    private void finish() {
        logger.debug(String.format("%s Finish()", name));
        subject.onNext(DemandEvents.Finish);
    }

    public void startWhen(final DemandItem demandItem, final DemandEvents event) {
        Assert.isNull(subscription, "Already subscribed");

        storeLinkage(demandItem);

        final Observable<DemandEvents> o = demandItem.subject.filter(t -> {return (!running && t == event);});
        subscription = o.subscribe(workItemEvents -> start());
    }

    public void startWhen(final DemandItem[] demandItems, final DemandEvents event) {
        Assert.isNull(subscription, "Already subscribed");

        storeLinkage(demandItems[0]);
        storeLinkage(demandItems[1]);

        final Observable<DemandEvents> o1 = demandItems[0].subject.filter(t -> {return !running && t == event;});
        final Observable<DemandEvents> o2 = demandItems[1].subject.filter(t -> {return !running && t == event;});

        final Observable<DemandEvents> o = Observable.zip(o1, o2,
                (workItemEvents, workItemEvents2) -> null
        );

        subscription = o.subscribe(workItemEvents -> start());
    }

    public void startWhen(final DemandItem demandItem, final int percentComplete) {
        Assert.isNull(subscription, "Already subscribed");

        storeLinkage(demandItem);

        final Observable<Integer> o = demandItem.percentComplete.filter(t -> {return (!running && t >= percentComplete);});
        subscription = o.subscribe(workItemEvents -> start());
    }

    public String walk(final WalkDirection direction) {
        switch (direction) {
            case FORWARDS:
                return walkForwards(0);
            case BACKWARDS:
                return reverseWalk();
        }

        return null;
    }
}