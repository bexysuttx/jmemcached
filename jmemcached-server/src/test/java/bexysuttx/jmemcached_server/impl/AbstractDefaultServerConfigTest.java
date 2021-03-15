package bexysuttx.jmemcached_server.impl;

import static org.mockito.Mockito.mock;

import java.util.Properties;

import bexysuttx.jmemcached_server.Storage;

public abstract class AbstractDefaultServerConfigTest {

	protected Storage storage;

	protected DefaultServerConfig createDefaultServerConfigMock(Properties overrideApplicationProperties) {
		storage = mock(Storage.class);
		return new DefaultServerConfig(overrideApplicationProperties) {
			@Override
			protected Storage createStorage() {
				return storage;
			}
		};
	}
}
