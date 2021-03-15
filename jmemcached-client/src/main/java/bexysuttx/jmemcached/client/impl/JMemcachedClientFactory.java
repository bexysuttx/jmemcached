package bexysuttx.jmemcached.client.impl;



import java.io.IOException;

import bexysuttx.jmemcached.client.Client;


public class JMemcachedClientFactory {

    public static Client buildNewClient(String host, int port) throws IOException {
        return new DefaultClient(new DefaultClientConfig(host, port));
    }

    public static Client buildNewClient(String host) throws IOException {
        return buildNewClient(host, 9010);
    }

    public static Client buildNewClient() throws IOException {
        return buildNewClient("localhost");
    }
}
