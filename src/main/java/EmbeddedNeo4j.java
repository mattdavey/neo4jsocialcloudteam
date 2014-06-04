import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.traversal.Evaluators;

public class EmbeddedNeo4j
{
    private static final String DB_PATH = "target/neo4j-hello-db";

    public String greeting;

    GraphDatabaseService graphDb;

    // START SNIPPET: createReltype
    private static enum RelTypes implements RelationshipType
    {
        KNOWS,
        MEMBER
    }
    // END SNIPPET: createReltype

    public static void main( final String[] args )
    {
        EmbeddedNeo4j hello = new EmbeddedNeo4j();
        hello.createDb();
        final Node team = hello.createTeam();
        System.out.println(hello.knowsLikesTraverser(team));
        System.out.println("Press any key to stop");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        hello.removeData();
        hello.shutDown();
    }

    String knowsLikesTraverser( Node node )
    {
        try ( Transaction tx = graphDb.beginTx() ) {
            String output = "";
            // START SNIPPET: knowslikestraverser
            for (org.neo4j.graphdb.Path position : graphDb.traversalDescription()
                    .depthFirst()
                    .relationships(RelTypes.MEMBER)
                    .evaluator(Evaluators.toDepth(5))
                    .traverse(node)) {
                output += position + "\n";
            }
            // END SNIPPET: knowslikestraverser
            return output;
        }
    }

    void createDb() {
        deleteFileOrDirectory(new File(DB_PATH));
        graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(DB_PATH);
        registerShutdownHook(graphDb);
    }

    Node createTeam() {
        try ( Transaction tx = graphDb.beginTx() )
        {
            final Node team = graphDb.createNode();
            team.setProperty("name", "Team");

            final List<Node> nodes = new ArrayList<>();
            final List<String> teamMembers = Arrays.asList("Bob", "Fred", "Elvis", "Nicola");
            teamMembers.forEach((name) -> {
                final Node member = graphDb.createNode();
                member.setProperty("name", name);
                nodes.add(member);
            });

            nodes.stream().forEach((node) -> {
                final Relationship relationshipMember = team.createRelationshipTo( node, RelTypes.MEMBER );

                nodes.stream().forEach((otherNodes) -> {
                    if (node != otherNodes) {
                        final Relationship relationship = node.createRelationshipTo( otherNodes, RelTypes.KNOWS );
                    }
                });
            });
            tx.success();
            return team;
        }
    }

    void stuff() {
        // START SNIPPET: transaction
        try ( Transaction tx = graphDb.beginTx() )
        {
            // Database operations go here
            Node firstNode = graphDb.createNode();
            firstNode.setProperty( "name", "Bob" );
            Node secondNode = graphDb.createNode();
            secondNode.setProperty( "name", "Fred" );

            Relationship relationship = firstNode.createRelationshipTo( secondNode, RelTypes.KNOWS );
            relationship.setProperty( "message", "brave Neo4j " );
            // END SNIPPET: addData

            // START SNIPPET: readData
            System.out.print( firstNode.getProperty( "message" ) );
            System.out.print( relationship.getProperty( "message" ) );
            System.out.print( secondNode.getProperty( "message" ) );
            // END SNIPPET: readData


            // START SNIPPET: transaction
            tx.success();
        }
        // END SNIPPET: transaction
    }

    void removeData()
    {
        try ( Transaction tx = graphDb.beginTx() )
        {
            // START SNIPPET: removingData
            // let's remove the data
//            firstNode.getSingleRelationship( RelTypes.KNOWS, Direction.OUTGOING ).delete();
//            firstNode.delete();
//            secondNode.delete();
            // END SNIPPET: removingData

            tx.success();
        }
    }

    void shutDown()
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