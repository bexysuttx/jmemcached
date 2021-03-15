package bexysuttx.jmemcached.client;



import java.io.IOException;
import java.util.concurrent.TimeUnit;

import bexysuttx.Jmemcached_common.jmemcashed.protocol.model.Status;


public interface Client extends AutoCloseable {

    Status put(String key, Object object) throws IOException;

    Status put(String key, Object object, Integer ttl, TimeUnit timeUnit) throws IOException;

    <T> T get(String key) throws IOException;

    Status remove(String key) throws IOException;
    
    

    Status clear() throws IOException;
}
