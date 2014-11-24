package ProjectTeamModelling.domain;

import java.util.HashMap;

public class Team {
    private String name;
    private HashMap<String, Person> people = new HashMap<>();

    public Team(final String name) {
        setName(name);
    }

    public void setName(final String name) {

        this.name = name;
    }

    public void addPerson(final Person fred) {
        people.put(fred.getName(), fred);
    }

    public void addPersons(final Person[] persons) {
        for (final Person person : persons) {
            addPerson(person);
        }
    }
}
