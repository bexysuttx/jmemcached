package bexysuttx.jmemcached.client.impl;

import bexysuttx.Jmemcached_common.jmemcashed.protocol.ObjectSerializer;
import bexysuttx.Jmemcached_common.jmemcashed.protocol.RequestConverter;
import bexysuttx.Jmemcached_common.jmemcashed.protocol.ResponseConverter;
import bexysuttx.Jmemcached_common.jmemcashed.protocol.impl.DefaultObjectSerializer;
import bexysuttx.Jmemcached_common.jmemcashed.protocol.impl.DefaultRequestConverter;
import bexysuttx.Jmemcached_common.jmemcashed.protocol.impl.DefaultResponseConverter;
import bexysuttx.jmemcached.client.ClientConfig;

class DefaultClientConfig implements ClientConfig {
    private final String host;
    private final int port;
    private final RequestConverter requestConverter;
    private final ResponseConverter responseConverter;
    private final ObjectSerializer objectSerializer;

    DefaultClientConfig(String host, int port) {
        this.host = host;
        this.port = port;
        this.requestConverter = new DefaultRequestConverter();
        this.responseConverter = new DefaultResponseConverter();
        this.objectSerializer = new DefaultObjectSerializer();
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public RequestConverter getRequestConverter() {
        return requestConverter;
    }

    @Override
    public ResponseConverter getResponseConverter() {
        return responseConverter;
    }

    @Override
    public ObjectSerializer getObjectSerializer() {
        return objectSerializer;
    }
}
