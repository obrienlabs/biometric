package org.obrienlabs.nbi.graph.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseBuilder;
import org.neo4j.graphdb.factory.GraphDatabaseFactoryState;
import org.neo4j.graphdb.factory.HighlyAvailableGraphDatabaseFactory;
import org.neo4j.helpers.collection.Pair;
import org.neo4j.kernel.ha.HighlyAvailableGraphDatabase;
import org.neo4j.kernel.ha.cluster.HighAvailabilityMemberStateMachine;
import org.neo4j.server.enterprise.EnterpriseBootstrapper;

// http://localhost:8080/biometric-nbi/FrontController?action=graph
//@Named("WrappedEnterpriseBoootstrapper")
public class ExtendedHighlyAvailableGraphDatabaseFactory extends HighlyAvailableGraphDatabaseFactory {
	private Log log = LogFactory.getLog(ExtendedHighlyAvailableGraphDatabaseFactory.class);
    private HaMonitor haMonitor;

    @Override
     protected GraphDatabaseBuilder.DatabaseCreator createDatabaseCreator(
                final File storeDir, final GraphDatabaseFactoryState state)  {
            return new GraphDatabaseBuilder.DatabaseCreator() {
                @Override
                public GraphDatabaseService newDatabase( final Map<String, String> config ) {
                    EnterpriseBootstrapper neoServer = new EnterpriseBootstrapper();
                    // convert all config (spring, conf, code) to vararg Pairs
                	List<Pair<String, String>> pairs = new ArrayList<>();
                	for(Entry<String, String> entry : config.entrySet()) {
                		pairs.add(Pair.of(entry.getKey(), entry.getValue()));
                	} 	
                    Pair<String, String> pairArray[] = new Pair[pairs.size()];
                    // will resolve to /dir/data/databases/graph.db 
                    neoServer.start(storeDir, Optional.empty(), pairs.toArray(pairArray));
                    GraphDatabaseService graph = neoServer.getServer().getDatabase().getGraph(); 
                    // set the paxos HA listener only when dbms.mode=HA
                    if(graph instanceof HighlyAvailableGraphDatabase) {
                    	haMonitor.setDb((HighlyAvailableGraphDatabase) graph);
                    	HighAvailabilityMemberStateMachine memberStateMachine = 
                    			((HighlyAvailableGraphDatabase)graph).getDependencyResolver()
                    				.resolveDependency(HighAvailabilityMemberStateMachine.class);
                    	if ( memberStateMachine != null ) {
                    		memberStateMachine.addHighAvailabilityMemberListener(haMonitor);
                    		log.info("register: " +  haMonitor);
                    	}
                    }
                    return graph;
                } };
    }
    
    public ExtendedHighlyAvailableGraphDatabaseFactory(HaMonitor aInHaMonitor)  {
        haMonitor = aInHaMonitor;
    }
    
    
    //private GraphDatabaseSettings.BoltConnector bolt = GraphDatabaseSettings.boltConnector( "0" );

   /* public GraphDatabaseService getEnterpriseGraphDatabase(final File _storeDir, final GraphDatabaseFactoryState _state,  
    		final Map<String, String> _config) {
    	// http://stackoverflow.com/questions/30074232/replacement-for-deprecated-wrappingneoserverbootstrapper
    	// Warning: internal/unsupported API - NeoServer wrappers are usually only run in  server mode - not embedded
        EnterpriseBootstrapper serverBootstrapper = new EnterpriseBootstrapper();//CommunityBootstrapper();
        serverBootstrapper.start(_storeDir, // will resolve to /ec2-user/data/databases/graph.db 
        		Optional.of(new File("/Users/Documents/Neo4j/.neo4j.conf")), //  stub only
        						configCopy(_config));
        NeoServer neoServer = serverBootstrapper.getServer();
        GraphDatabaseService graph = neoServer.getDatabase().getGraph(); 
        return graph;
    }
    
        private Pair<String, String>[] configCopy(final Map<String, String> _config) {
    	List<Pair> pairs = new ArrayList<>();
    	for(Entry<String, String> entry : _config.entrySet()) {
    		pairs.add(Pair.of(entry.getKey(), entry.getValue()));
    	} 	
        Pair pairArray[] = new Pair[pairs.size()];
        pairArray = pairs.toArray(pairArray);
    	return pairArray;
    }
    
    *
    */
   /* public GraphDatabaseService getGraphDatabase(final File _storeDir, final GraphDatabaseFactoryState _state,  
    		final Map<String, String> _config) {
    	// http://stackoverflow.com/questions/30074232/replacement-for-deprecated-wrappingneoserverbootstrapper
    	// Warning: internal/unsupported API - NeoServer wrappers are usually only run in  server mode - not embedded
        ServerBootstrapper bootstrapper = new EnterpriseBootstrapper();//CommunityBootstrapper();
        bootstrapper.start(new File("/ec2-user"), // will resolve to /ec2-user/data/databases/graph.db 
        		Optional.of(new File("/Users/Documents/Neo4j/.neo4j.conf")), //  stub only
								Pair.of( "unsupported.dbms.ephemeral", "false" ),
        				    	Pair.of("dbms.connector.http.address","0.0.0.0:7575"),
        				    	Pair.of("dbms.connector.http.enabled","true" ),
        				    	Pair.of("dbms.connector.http.type", "HTTP" ),
        				    	Pair.of("dbms.connector.http.tls_level", "DISABLED"),
        				    	// BOLT not advised if we run an embedded Neo4j DB inside a NeoServer wrapper
        				    	// BOLT should be enabled if you run a standalone server's browser against this embedded bolt port
        				    	// WebSocket connection failure. Due to security constraints in your web browser, the reason for the failure is not available to this Neo4j Driver. Please use your browsers development console to determine the root cause of the failure. Common reasons include the database being unavailable, using the wrong connection URL or temporary network problems. If you have enabled encryption, ensure your browser is configured to trust the certificate Neo4j is configured to use. WebSocket `readyState` is: 3
         				    	Pair.of("dbms.connector.bolt.address","0.0.0.0:7688"),
        				    	Pair.of("dbms.connector.bolt.enabled","true" ),
        				    	Pair.of("dbms.connector.bolt.type", "HTTP" ),
        				    	Pair.of("dbms.connector.bolt.tls_level", "DISABLED")       				    	
        				);
        NeoServer neoServer = bootstrapper.getServer();
        GraphDatabaseService graph = neoServer.getDatabase().getGraph(); 
        return graph;
    }*/
}
