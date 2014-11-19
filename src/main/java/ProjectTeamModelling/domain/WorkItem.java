package ProjectTeamModelling.domain;

import org.joda.time.Period;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import rx.subjects.PublishSubject;

// Dependencies between WorkItems:
// 1. Start - Finish Dependency
// 2. Finish - Finish together
// 3. Start - Start together
// 4. Partial Finish - Start

public class WorkItem {
    private final Logger logger = LoggerFactory.getLogger(WorkItem.class);
    private Period period;

    private String name;
    PublishSubject<WorkItemEvents> subject = PublishSubject.create();

    public WorkItem(final String name) {
        this.name = name;
    }

    public WorkItem(final String name, final Period period) {
        this(name);
        this.period = period;
    }

    public void start() {
        logger.debug(String.format("%s Start()", name));
        subject.onNext(WorkItemEvents.Start);


        finish();
    }

    private void finish() {
        logger.debug(String.format("%s Finish()", name));
        subject.onNext(WorkItemEvents.Finish);
    }

    public void Subscribe(WorkItem workItem1, WorkItemEvents event) {
        Observable<WorkItemEvents> o = workItem1.subject.filter(t -> {return t == event;});
        o.subscribe(workItemEvents -> start());
    }

    public void Subscribe(WorkItem[] workItems, WorkItemEvents event) {
        final Observable<WorkItemEvents> o1 = workItems[0].subject.filter(t -> {return t == event;});
        final Observable<WorkItemEvents> o2 = workItems[1].subject.filter(t -> {return t == event;});

        final Observable<WorkItemEvents> o = Observable.zip(o1, o2,
                (workItemEvents, workItemEvents2) -> null
        );

        o.subscribe(workItemEvents -> start());
    }

    public void setPeriod(final Period period) {
        this.period = period;
    }
}