package bexysuttx.jmemcached.client;

import bexysuttx.Jmemcached_common.jmemcashed.protocol.ObjectSerializer;
import bexysuttx.Jmemcached_common.jmemcashed.protocol.RequestConverter;
import bexysuttx.Jmemcached_common.jmemcashed.protocol.ResponseConverter;

public interface ClientConfig {

    String getHost();

    int getPort();

    RequestConverter getRequestConverter();

    ResponseConverter getResponseConverter();

    ObjectSerializer getObjectSerializer();
}
