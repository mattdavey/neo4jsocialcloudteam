package ProjectTeamModelling.domain;

public class Person extends SupplyResource {
    final private String name;
    final private String department;
    private String title;
    private String[] skills;

    public Person(final String name, final String department) {
        this.name = name;
        this.department = department;
    }

    public Person(final String name, final String department, final String title) {
        this(name, department);

        this.title = title;
    }

    public void addSkills(final String[] skills) {
        this.skills = skills;
    }

    public void addRelationship(final Person person, final PersonRelationship relationship) {

    }

    public String getName() {
        return name;
    }
}
