package org.obrienlabs.nbi.graph.service;

import java.io.File;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Named;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.helpers.collection.Pair;
import org.neo4j.server.NeoServer;
import org.neo4j.server.ServerBootstrapper;
import org.neo4j.server.enterprise.EnterpriseBootstrapper;

@Named("biometricGraphDatabase")
public class GraphDatabase implements IGraphDatabase {
	@Inject
	private GraphDatabaseService graphDb;

    private String path = null;
    
    private Thread shutdownHook;

    private ServerBootstrapper server = null;

    public GraphDatabase()
    {
        shutdownHook = new Thread()
        {
            @Override
            public void run()
            {
                GraphDatabase.this.stop();
                
            }
        };
    }
    
    public GraphDatabase(String aInPath)
    {
        path = aInPath;
        shutdownHook = new Thread()
        {
            @Override
            public void run()
            {
                GraphDatabase.this.stop();
            }
        };
    }

    @Override
    /**
     * For non-Spring (standalone) execution
     */
    public void start()
    {
        if (graphDb == null)
        {
            //graphDb = new GraphDatabaseFactory().
            //    newEmbeddedDatabaseBuilder(new File(path)).newGraphDatabase();
            //server = new EnterpriseBootstrapper();
            
            
            //ServerBootstrapper serverBootstrapper = new EnterpriseBootstrapper();//CommunityBootstrapper();
            //NeoServer directNeoServer = serverBootstrapper.getServer();
          /*  int i = server.start(new File("/ec2-user"), // will resolve to /ec2-user/data/databases/graph.db 
            		Optional.of(new File("/Users/michaelobrien/Documents/Neo4j/.neo4j.conf")), //  stub only
            				    	Pair.of("dbms.connector.http.address","0.0.0.0:7575"),
            				    	Pair.of("dbms.connector.http.enabled","true" ),
            				    	Pair.of("dbms.connector.http.type", "HTTP" ),
            				    	Pair.of("dbms.connector.http.tls_level", "DISABLED")
            				    	// BOLT not advised if we run an embedded Neo4j DB inside a NeoServer wrapper
            				    	// BOLT should be enabled if you run a standalone server's browser against this embedded bolt port
            				    	// WebSocket connection failure. Due to security constraints in your web browser, the reason for the failure is not available to this Neo4j Driver. Please use your browsers development console to determine the root cause of the failure. Common reasons include the database being unavailable, using the wrong connection URL or temporary network problems. If you have enabled encryption, ensure your browser is configured to trust the certificate Neo4j is configured to use. WebSocket `readyState` is: 3
             				    	,Pair.of("dbms.connector.bolt.address","0.0.0.0:7688"),
            				    	Pair.of("dbms.connector.bolt.enabled","true" ),
            				    	Pair.of("dbms.connector.bolt.type", "HTTP" ),
            				    	Pair.of("dbms.connector.bolt.tls_level", "DISABLED")       				    	
            				);
            NeoServer neoServer = server.getServer();
            graphDb = neoServer.getDatabase().getGraph();  
            Runtime.getRuntime().addShutdownHook(shutdownHook);*/
            
        }
        //init();
    }

    @Override
    /**
     * For non-Spring (standalone) execution
     */
    public void stop()
    {
        if (graphDb != null)
        {
            server.stop();
            graphDb.shutdown();
            Runtime.getRuntime().removeShutdownHook(shutdownHook);
            graphDb = null;
            server = null;
        }
    }

    @Override
    public GraphDatabaseService getDB()
    {
        return graphDb;
    }
	

}
