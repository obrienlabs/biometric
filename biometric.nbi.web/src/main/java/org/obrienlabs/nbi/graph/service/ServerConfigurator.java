package org.obrienlabs.nbi.graph.service;

//import com.typesafe.config.Config;
//import com.typesafe.config.ConfigFactory;
//import com.typesafe.config.ConfigValue;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.MapConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerConfigurator //extends Configurator.Adapter 
implements Map<String,String>
{
    public static final String HA_TX_PUSH_FACTOR = "ha.tx_push_factor";
    public static final String HA_SERVER_ID = "ha.server_id";
    private static Logger logger = LoggerFactory.getLogger(ServerConfigurator.class);
    private static final org.slf4j.Logger serverConfiguratorLogger =
            org.slf4j.LoggerFactory.getLogger(ServerConfigurator.class.getName() + ".ServerConfigurator");

    private Configuration config ;
    //private List<ThirdPartyJaxRsPackage> jaxRsPackages = new LinkedList<>();

    private Map<String,String> properties = new HashMap<>();
    public ServerConfigurator( Map<String,Object>  aInMap)
    {
        config = new MapConfiguration(aInMap);
        for (Map.Entry<String, Object> lEntry : aInMap.entrySet())
        {
             properties.put(lEntry.getKey(),lEntry.getValue()+"");
        }

    }
    //@Override
    public Configuration configuration()
    {
        return config;
    }

    //@Override
    public Map<String, String> getDatabaseTuningProperties()
    {
        return properties;
    }

    /*//@Override
    public List<ThirdPartyJaxRsPackage> getThirdpartyJaxRsPackages()
    {
        return jaxRsPackages;
    }*/

    public void init() {
        logger.info("Starting service: " + this);

        // Load from installer location first
        File installerPath = new File(System.getProperty("nsp.environment.configuration.dir"), "neo4j-db.conf");
//        Config installerConfig = ConfigFactory.parseFileAnySyntax(installerPath);
        // Fallback to built-in reference.conf for default settings (localhost)
//        Config finalConfig = installerConfig.withFallback(ConfigFactory.defaultReference()).resolve();

//        Config neoConfig = finalConfig.getConfig("neo4j-configs");
/*        for (Map.Entry<String, ConfigValue> entry : neoConfig.entrySet()) {
            if (serverConfiguratorLogger.isInfoEnabled()) {
                serverConfiguratorLogger.info(entry.getKey() + "=" + entry.getValue().unwrapped());
            }
            config.setProperty(entry.getKey(), entry.getValue().unwrapped());
            properties.put(entry.getKey(), String.valueOf(entry.getValue().unwrapped()));
        }
 */   }

    @Override
    public int size()
    {
        return properties.size();
    }

    @Override
    public boolean isEmpty()
    {
        return properties.isEmpty();
    }

    @Override
    public boolean containsKey(Object key)
    {
        return properties.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value)
    {
        return properties.containsValue(value);
    }

    @Override
    public String get(Object key)
    {
        return properties.get(key);
    }

    @Override
    public String put(String key, String value)
    {
        config.setProperty(key,value);
        return properties.put(key,value);
    }

    @Override
    public String remove(Object key)
    {
        config.clearProperty(key.toString());
        return properties.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ? extends String> m)
    {
        for (Entry<? extends String, ? extends String> lEntry : m.entrySet())
        {
            config.setProperty(lEntry.getKey(),lEntry.getValue());
        }
        properties.putAll(m);
    }

    @Override
    public void clear()
    {
        config.clear();
        properties.clear();
    }

    @Override
    public Set<String> keySet()
    {
        return properties.keySet();
    }

    @Override
    public Collection<String> values()
    {
        return properties.values();
    }

    @Override
    public Set<Entry<String, String>> entrySet()
    {
        return properties.entrySet();
    }
}