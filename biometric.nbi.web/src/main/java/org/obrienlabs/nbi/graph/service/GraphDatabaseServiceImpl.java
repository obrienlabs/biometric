package org.obrienlabs.nbi.graph.service;

import org.neo4j.graphdb.NotFoundException;

import javax.inject.Inject;
import javax.inject.Named;

import org.neo4j.graphdb.GraphDatabaseService; // neo4j-graphdb-api
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
//import org.neo4j.graphdb.factory.GraphDatabaseFactory;

@Named("graphDatabaseService")
public class GraphDatabaseServiceImpl implements IGraphDatabaseService {
	
	@Inject 
	private GraphDatabaseService databaseService;
	
	private static long identifier = 1L;

    private static enum RelTypes implements RelationshipType {
        KNOWS
    }
    
    // https://neo4j.com/docs/java-reference/current/#tutorials-java-embedded
    /*public void init() {
    	GraphDatabaseService service = new GraphDatabaseFactory().newEmbeddedDatabase( "/ec2-user/graph.db");
    	registerShutdownHook( service );
    }*/
    
	@Override
	public Node createNode() {
		Node node = null;
	       try ( Transaction tx = databaseService.beginTx() )  {      	
	            node =  databaseService.createNode();
	            node.setProperty( "message", " node-" + identifier++ );
	            tx.success();
	        }
		return node;
	}

	@Override
	public Node getNodeById(long id) {
		try {
			return databaseService.getNodeById(id);
		} catch (NotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

}
