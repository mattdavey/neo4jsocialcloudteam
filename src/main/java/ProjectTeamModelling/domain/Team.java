package ProjectTeamModelling.domain;

public class Team {
    private String name;
    private String description;

    public Team(final String name) {
        setName(name);
    }

    public void setName(final String name) {

        this.name = name;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public void addRole(final Role role) {

    }
}
