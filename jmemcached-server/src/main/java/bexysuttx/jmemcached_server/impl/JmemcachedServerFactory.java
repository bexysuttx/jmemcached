package bexysuttx.jmemcached_server.impl;

import java.util.Properties;

import bexysuttx.jmemcached_server.Server;

public class JmemcachedServerFactory {

	public static Server buildNewServer(Properties overrideApplicationProperties) {
		return new DefaultServer(new DefaultServerConfig(overrideApplicationProperties));
	}
}
