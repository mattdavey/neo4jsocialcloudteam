import ProjectTeamModelling.domain.*;
import ProjectTeamModelling.services.MetaDateService;
import ProjectTeamModelling.services.TeamService;
import org.joda.time.Period;
import org.springframework.util.Assert;

import java.io.IOException;

public class Program {
    public static void main( final String[] args ) {
        new Program().run();
    }

    public void run() {
        testMetaDataService();



        // Add meta data to system
        final MetaDateService metaDate = new MetaDateService();
        metaDate.add("//Skill//Programming//Language//Java");
        metaDate.add("//Skill//Programming//Language//C#");
        metaDate.add("//Skill//Programming//Language//C++");
        metaDate.add("//Department//Sales");
        metaDate.add("//Department//Engineering");
        metaDate.add("//JobTitle//Management");
        metaDate.add("//JobTitle//Developer");
        metaDate.add("//JobTitle//UX");

        // Create people
        final Person matt = new Person("Matt", "Engineering");
        matt.addSkills(new String[]{"Java", "#C", "C++"});

        final Person ross = new Person("Ross", "Sales");
        matt.addRelationship(ross, PersonRelationship.Colleague);

        final Person fred = new Person("Fred", "Management");
        final Person bob = new Person("Bob", "Engineering", "Developer");
        final Person jane = new Person("Jane", "Engineering", "UX");

        // Create a team
        final Team team = new Team("Energy Team");
        team.addRole(new Role("Lead Engineer", matt));
        team.addRole(new Role("Product Owner"));

        // Create a Project
        final Project energyProject = new Project("Dependency with power station online");

        // Build workitem dependency
        final WorkItem workItem1 = new WorkItem("DigHole", new Period(0, 0, 2, 0));
        final WorkItem workItem2 = new WorkItem("LayCableA");
        workItem2.setPeriod(new Period(0, 0, 2, 0));
        final WorkItem workItem4 = new WorkItem("Backfill");
        final WorkItem workItem3 = new WorkItem("LayCableB");

        workItem2.Subscribe(workItem1, WorkItemEvents.Finish);
        workItem3.Subscribe(workItem1, WorkItemEvents.Finish);
        workItem4.Subscribe(new WorkItem[]{workItem2, workItem3}, WorkItemEvents.Finish);

        // Add workitems to project
        energyProject.addWorkItem(workItem1);

        // Check linkage of workitems
        workItem1.start();



        final TeamService service = new TeamService();
        service.getRootTeam();

        try {
            System.out.println("Press Enter to quite");
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

        service.shutDown();
    }

    private void testMetaDataService() {
        // Test meta data system
        final MetaDateService metaDate = new MetaDateService();
        metaDate.add("//Test1//Test2//Test3");
        Assert.isTrue(metaDate.validateMetaData("//Test1//Test2//Test3"), "Metadata should exist");
        Assert.isTrue(metaDate.validateMetaData("//Test1//Test2"), "Metadata should exist");
        Assert.isTrue(!metaDate.validateMetaData("//Test1//Test2//Test2"), "Metadata shouldn't exist");
        Assert.isTrue(!metaDate.validateMetaData("//Test1//Test22"), "Metadata shouldn't exist");
    }

    private void testWorkItemLinkage() {
        // Build workitem dependency
        final WorkItem workItem1 = new WorkItem("DigHole", new Period(0, 0, 2, 0));
        final WorkItem workItem2 = new WorkItem("LayCableA");
        workItem2.setPeriod(new Period(0, 0, 2, 0));
        final WorkItem workItem4 = new WorkItem("Backfill");
        final WorkItem workItem3 = new WorkItem("LayCableB");

        workItem2.Subscribe(workItem1, WorkItemEvents.Finish);
        workItem3.Subscribe(workItem1, WorkItemEvents.Finish);
        workItem4.Subscribe(new WorkItem[]{workItem2, workItem3}, WorkItemEvents.Finish);

        // Check linkage of workitems
        workItem1.start();

    }
}
