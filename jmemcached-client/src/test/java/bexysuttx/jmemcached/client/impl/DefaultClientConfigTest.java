package bexysuttx.jmemcached.client.impl;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import bexysuttx.Jmemcached_common.jmemcashed.protocol.impl.DefaultObjectSerializer;
import bexysuttx.Jmemcached_common.jmemcashed.protocol.impl.DefaultRequestConverter;
import bexysuttx.Jmemcached_common.jmemcashed.protocol.impl.DefaultResponseConverter;


public class DefaultClientConfigTest {
    private final DefaultClientConfig defaultClientConfig = new DefaultClientConfig("localhost", 9010);

    @Test
    public void getHost() {
        assertEquals("localhost", defaultClientConfig.getHost());
    }

    @Test
    public void getPort() {
        assertEquals(9010, defaultClientConfig.getPort());
    }

    @Test
    public void getRequestConverter() {
        assertEquals(DefaultRequestConverter.class, defaultClientConfig.getRequestConverter().getClass());
    }

    @Test
    public void getResponseConverter() {
        assertEquals(DefaultResponseConverter.class, defaultClientConfig.getResponseConverter().getClass());
    }

    @Test
    public void getObjectSerializer() {
        assertEquals(DefaultObjectSerializer.class, defaultClientConfig.getObjectSerializer().getClass());
    }
}