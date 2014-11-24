import ProjectTeamModelling.domain.*;
import ProjectTeamModelling.services.MetaDateService;
import org.joda.time.DurationFieldType;
import org.joda.time.Period;
import org.springframework.util.Assert;

import java.io.IOException;

public class Program {
    public static void main( final String[] args ) {
        new Program().run();
    }

    public void runTests() {
        testMetaDataService();
    }

    public void run() {
//        runTests();
        runFakeProject();
    }

    public void runFakeProject() {
        // Add meta data to system
        final MetaDateService metaDate = new MetaDateService();
        metaDate.add("//Skill//UseSpade");
        metaDate.add("//Skill//LayCable");

        // Create supply
        final Person fred = new Person("Fred", "Labourer");
        fred.addSkills(new String[]{"//Skill//UseSpade"});
        final Person bob = new Person("Bob", "Labourer");
        bob.addSkills(new String[]{"//Skill//LayCable"});
        final Person jane = new Person("Jane", "Installer");
        fred.addRelationship(bob, PersonRelationship.Colleague);

        // Create a supply team
        final Team team = new Team("Installer Team");
        team.addPersons(new Person[]{fred, bob, jane});

        // Create a Demand Project
        final Project energyProject = new Project("Dependency with power station online");

        // Build workitem dependency
        final DemandItem demandItem1 = new DemandItem("DigHole1", new Period(0, 0, 3, 0),
                new SkillEstimateTuple[]{new SkillEstimateTuple("//Skill//UseSpade", new Period(0, 0, 3, 0))});

        final DemandItem demandItem2 = new DemandItem("LayCableA2", new Period(0, 0, 1, 0));
        final DemandItem demandItem3 = new DemandItem("InstallSystem3", new Period(0, 0, 2, 0));
        final DemandItem demandItem4 = new DemandItem("InstallGenerator4", new Period(0, 0, 3, 0));
        final DemandItem demandItem5 = new DemandItem("InstallSubPanel5", new Period(0, 0, 4, 0));
        final DemandItem demandItem6 = new DemandItem("TestGenerator6", new Period(0, 0, 5, 0));
        final DemandItem demandItem7 = new DemandItem("SignContract7", new Period(0, 0, 6, 0));
        final DemandItem demandItem8 = new DemandItem("TestSystem8", new Period(0, 0, 7, 0));
        final DemandItem demandItem9 = new DemandItem("FillInHole9", new Period(0, 0, 8, 0),
                new SkillEstimateTuple[]{new SkillEstimateTuple("//Skill//UseSpade", new Period(0, 0, 7, 0))});

        // Assert only one startWhen call on a workItem, else assert
        demandItem2.startWhen(demandItem1, DemandEvents.Finish);
        demandItem3.startWhen(new DemandItem[]{demandItem2, demandItem5}, DemandEvents.Finish);
        demandItem4.startWhen(demandItem1, 50);
        demandItem5.startWhen(demandItem4, DemandEvents.Finish);
        demandItem6.startWhen(demandItem5, DemandEvents.Finish);
        demandItem8.startWhen(new DemandItem[]{demandItem3, demandItem6}, DemandEvents.Finish);
        demandItem9.startWhen(demandItem8, DemandEvents.Finish);

        // Add workitems to project
        energyProject.addWorkItem(demandItem1);

        System.out.println("--Walk Forwards--");
        System.out.println(demandItem1.walk(WalkDirection.FORWARDS));
        System.out.println("\n--Walk Backwards--");
        System.out.println(demandItem9.walk(WalkDirection.BACKWARDS));
        System.out.println("\n--Calculate duration--");
        System.out.println(String.format("Estimated time to complete %s %d", demandItem1.getName(), demandItem1.walkForwardDuration(DurationFieldType.seconds())));

        // Check linkage of workitems
//        demandItem1.start();

        try {
            System.out.println("Press Enter to quite");
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void testMetaDataService() {
        // Test meta data system
        final MetaDateService metaDate = new MetaDateService();
        metaDate.add("//Test1//Test2//Test3");
        Assert.isTrue(metaDate.validMetaData("//Test1//Test2//Test3"), "Metadata should exist");
        Assert.isTrue(metaDate.validMetaData("//Test1//Test2"), "Metadata should exist");
        Assert.isTrue(!metaDate.validMetaData("//Test1//Test2//Test2"), "Metadata shouldn't exist");
        Assert.isTrue(!metaDate.validMetaData("//Test1//Test22"), "Metadata shouldn't exist");
    }

    private void testWorkItemLinkage() {
        // Build workitem dependency
        final DemandItem demandItem1 = new DemandItem("DigHole", new Period(0, 0, 3, 0));
        final DemandItem demandItem2 = new DemandItem("LayCableA", new Period(0, 0, 3, 0));
        final DemandItem demandItem4 = new DemandItem("Backfill", new Period(0, 0, 3, 0));
        final DemandItem demandItem3 = new DemandItem("LayCableB", new Period(0, 0, 3, 0));

        demandItem2.startWhen(demandItem1, DemandEvents.Finish);
        demandItem3.startWhen(demandItem1, DemandEvents.Finish);
        demandItem4.startWhen(new DemandItem[]{demandItem2, demandItem3}, DemandEvents.Finish);

        // Check linkage of workitems
        demandItem1.start();
    }
}