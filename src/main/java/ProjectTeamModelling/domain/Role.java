package ProjectTeamModelling.domain;

public class Role {
    private String roleName;
    private Person person;

    public Role(final String roleName, final Person person) {
        this.roleName = roleName;
        this.person = person;
    }

    public Role(final String roleName) {
        this.roleName = roleName;
    }
}
