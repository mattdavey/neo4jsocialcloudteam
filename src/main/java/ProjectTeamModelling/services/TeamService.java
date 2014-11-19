package ProjectTeamModelling.services;

import ProjectTeamModelling.domain.Team;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.File;

public class TeamService {
    private static final String DB_PATH = "target/neo4j-team-db";
    private GraphDatabaseService graphDb;

    private static enum RelTypes implements RelationshipType
    {
        TEAMS,
        KNOWS,
        MEMBER
    }

    public TeamService() {
        createDb();
    }

    public Team getRootTeam()
    {
//        final Node teamNode = graphDb.createNode();
//        final Team team = new TeamImpl( teamNode );
//        team.setName( "Teams" );
//        return team;
        return null;
    }


    private void createDb() {
        deleteFileOrDirectory(new File(DB_PATH));
        graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(DB_PATH);
        registerShutdownHook(graphDb);
    }

    public void shutDown()
    {
        System.out.println();
        System.out.println( "Shutting down database ..." );
        // START SNIPPET: shutdownServer
        graphDb.shutdown();
        // END SNIPPET: shutdownServer
    }

    // START SNIPPET: shutdownHook
    private static void registerShutdownHook( final GraphDatabaseService graphDb )
    {
        // Registers a shutdown hook for the Neo4j instance so that it
        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
        // running application).
        Runtime.getRuntime().addShutdownHook( new Thread()
        {
            @Override
            public void run()
            {
                graphDb.shutdown();
            }
        } );
    }
    // END SNIPPET: shutdownHook

    private static void deleteFileOrDirectory( File file )
    {
        if ( file.exists() )
        {
            if ( file.isDirectory() )
            {
                for ( File child : file.listFiles() )
                {
                    deleteFileOrDirectory( child );
                }
            }
            file.delete();
        }
    }
}
