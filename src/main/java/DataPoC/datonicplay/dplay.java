package DataPoC.datonicplay;

import datomic.*;

import java.io.FileReader;
import java.io.Reader;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;


public class dplay {
    public static void main(String[] args) {
        final dplay teamPlay = new dplay();
        teamPlay.run();
    }

    private void run() {
        try {
            System.out.println("Creating and connecting to database...");

            final String uri = "datomic:mem://teams";
            Peer.createDatabase(uri);
            final Connection conn = Peer.connect(uri);

            System.out.println("Parsing schema edn file and running transaction...");

            final Reader schema_rdr = new FileReader("src/main/java/data/teams-schema.edn");
            final List schema_tx = (List) Util.readAll(schema_rdr).get(0);
            Object txResult = conn.transact(schema_tx).get();
            System.out.println(txResult);

            System.out.println("Parsing seed data edn file and running transaction...");

            final Reader data_rdr = new FileReader("src/main/java/data/team-testdata.edn");
            final List data_tx = (List) Util.readAll(data_rdr).get(0);
            data_rdr.close();
            txResult = conn.transact(data_tx).get();

            System.out.println("Finding all teams, counting results...");

            Collection results = Peer.q("[:find ?match :where [?match :person/name]]", conn.db());
            System.out.println(String.format("Number of people: %d", results.size()));

            final Database db = conn.db();
            for (Object result : results) {
                final Entity entity = db.entity(((List) result).get(0));
                System.out.println(String.format("%s %s", entity.toString(), entity.get(":person/name")));
            }

            results = Peer.q("[:find ?match :where [?match :project/name \"Risk Project 1\"]]", conn.db());
            System.out.println();
            System.out.println(String.format("Number of teams in \"Risk Project 1\":%d", results.size()));

            for (Object result : results) {
                final Entity entity = db.entity(((List) result).get(0));
                System.out.println(String.format("%s %s %s", entity.toString(), entity.get(":team/name"), entity.get("team/people")));
            }

            results = Peer.q("[:find ?read :where [?read team/people] [?match :project/name \"Risk Project 1\"]]", conn.db());
            System.out.println();
            System.out.println(String.format("Number of people in \"Risk Project 1\" teams:%d",results.size()));

            for (Object result : results) {
                final Entity entity = db.entity(((List) result).get(0));
                System.out.println(String.format("%s %s", entity.toString(), entity.get(":team/people")));
            }

            results = Peer.q("[:find ?read :where [?read :team/name \"Risk Feature ProjectTeamModelling.services.domain.Team 1\"]]", conn.db());
            System.out.println();
            final Entity team = db.entity(((List) results.iterator().next()).get(0));


            results = Peer.q("[:find ?c_name ?r_name :where " +
                            "[?c :team/name ?c_name]" +
                            "[?c :project/name \"Risk Project 1\"]" +
                            "[?r :team/people ?r_name]]",
                    conn.db());
            for (Object result : results) System.out.println(result);

            Peer.shutdown(true);

        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

    }

    private static final Scanner scanner = new Scanner(System.in);

    private static void pause() {
        if (System.getProperty("NOPAUSE") == null) {
            System.out.println("\nPress enter to continue...");
            scanner.nextLine();
        }
    }
}
