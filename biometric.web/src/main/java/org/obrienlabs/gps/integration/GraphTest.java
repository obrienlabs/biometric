package org.obrienlabs.gps.integration;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

/*import org.neo4j.causalclustering.core.state.CoreBootstrapper;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.helpers.collection.Pair;
import org.neo4j.io.fs.FileUtils;
import org.neo4j.server.Bootstrapper;
import org.neo4j.server.CommunityBootstrapper;
import org.neo4j.server.NeoServer;
import org.neo4j.server.ServerBootstrapper;
import org.neo4j.server.enterprise.EnterpriseBootstrapper;*/
public class GraphTest {	
	  	private static final String DB_PATH = "neo4j-hello-db";

	    public String greeting;
/*	    private GraphDatabaseService graphDbService; // GraphDatabaseFacade instance
	    private EnterpriseBootstrapper bootstrapper;
	    private Node firstNode;
	    private Node secondNode;
	    private Relationship relationship;

	    private static enum RelTypes implements RelationshipType {
	        KNOWS
	    }


	    void createDb() throws IOException {
	        FileUtils.deleteRecursively( new File( DB_PATH ) );
	        
	        String configFile = "reference.conf";
	        graphDbService = new GraphDatabaseFactory().newEmbeddedDatabase(new File(DB_PATH) );
	        bootstrapper = new EnterpriseBootstrapper();
	        System.setProperty( "org.neo4j.server.properties", configFile );
	        int i = bootstrapper.start(bootstrapper, "--home-dir=" + DB_PATH);//(new File(DB_PATH), Optional.of(new File(configFile)));//, Pair.of("dbms.connector.http.address","0.0.0.0:7575"));	        //int i = bootstrapper.start(new File(DB_PATH), null, Pair.of("dbms.connector.http.address","0.0.0.0:7575"));
	        //NeoServer neoServer = bootstrapper.getServer(); // npe
	        //GraphDatabaseService graph = neoServer.getDatabase().getGraph();
	        //System.out.println(graph);

	        registerShutdownHook( graphDbService );

	        try ( Transaction tx = graphDbService.beginTx() )  {
	            firstNode = graphDbService.createNode();
	            firstNode.setProperty( "message", " node1 " );
	            secondNode = graphDbService.createNode();
	            secondNode.setProperty( "message", " node2 " );

	            relationship = firstNode.createRelationshipTo( secondNode, RelTypes.KNOWS );
	            relationship.setProperty( "message", "aRelationship " );

	            System.out.println( firstNode.getProperty( "message" ) );
	            System.out.println( relationship.getProperty( "message" ) );
	            System.out.println( secondNode.getProperty( "message" ) );

	            greeting = ( (String) firstNode.getProperty( "message" ) )
	                       + ( (String) relationship.getProperty( "message" ) )
	                       + ( (String) secondNode.getProperty( "message" ) );

	            tx.success();
	        }
	    }

	    void removeData()  {
	        try ( Transaction tx = graphDbService.beginTx() ) {
	            firstNode.getSingleRelationship( RelTypes.KNOWS, Direction.OUTGOING ).delete();
	            firstNode.delete();
	            secondNode.delete();
	            tx.success();
	        }
	    }

	    void shutDown()  {
	        System.out.println( "\nShutting down database ..." );
	        graphDbService.shutdown();
	        System.out.println( "Finished ..." );
	    }

	    private static void registerShutdownHook( final GraphDatabaseService graphDb ) {
	        Runtime.getRuntime().addShutdownHook( new Thread()  {
	            @Override
	            public void run()  {
	                graphDb.shutdown();
	            }});
	    }

	    
	public void run()  throws Exception {
	        createDb();
	        //removeData();
	        shutDown();
	}

	public static void main(String[] args) {
		GraphTest test = new GraphTest();
		try {
			test.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
}
