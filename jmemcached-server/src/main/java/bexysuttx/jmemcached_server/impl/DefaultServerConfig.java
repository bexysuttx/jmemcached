package bexysuttx.jmemcached_server.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Properties;
import java.util.concurrent.ThreadFactory;

import bexysuttx.Jmemcached_common.jmemcashed.exception.JMemcachedConfigException;
import bexysuttx.Jmemcached_common.jmemcashed.protocol.RequestConverter;
import bexysuttx.Jmemcached_common.jmemcashed.protocol.ResponseConverter;
import bexysuttx.Jmemcached_common.jmemcashed.protocol.impl.DefaultRequestConverter;
import bexysuttx.Jmemcached_common.jmemcashed.protocol.impl.DefaultResponseConverter;
import bexysuttx.jmemcached_server.ClienSocketHandler;
import bexysuttx.jmemcached_server.CommandHandler;
import bexysuttx.jmemcached_server.ServerConfig;
import bexysuttx.jmemcached_server.Storage;

public class DefaultServerConfig implements ServerConfig {
	private final Properties applicationProperties = new Properties();
	private final RequestConverter requestConverter;
	private final ResponseConverter responseConverter;
	private final Storage storage;
	private final CommandHandler commandHandler;

	public DefaultServerConfig(Properties overrideProperties) {
		loadApplicationProperties("server.properties");
		if (overrideProperties != null) {
			applicationProperties.putAll(overrideProperties);
		}
		this.requestConverter = createRequestConverter();
		this.responseConverter = createResponseConverter();
		this.storage = createStorage();
		this.commandHandler = createCommandHandler();
	}

	protected InputStream getClassPathResourceInputStream(String serverProp) {
		return getClass().getClassLoader().getResourceAsStream(serverProp);
	}

	protected void loadApplicationProperties(String serverProp) {
		try (InputStream in = getClassPathResourceInputStream(serverProp)) {
			if (in == null) {
				throw new JMemcachedConfigException("Classpath resources not found: " + serverProp);
			} else {
				applicationProperties.load(in);
			}
		} catch (IOException e) {
			throw new JMemcachedConfigException("Can't load application properties from classpath: " + serverProp);
		}

	}

	protected CommandHandler createCommandHandler() {
		return new DefaultCommandHandler(this);
	}

	protected Storage createStorage() {
		return new DefaultStorage(this);
	}

	protected ResponseConverter createResponseConverter() {
		return new DefaultResponseConverter();
	}

	protected RequestConverter createRequestConverter() {
		return new DefaultRequestConverter();
	}

	@Override
	public void close() throws Exception {
		storage.close();
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
	public ThreadFactory getWorkerThreadFactory() {

		return new ThreadFactory() {
			private int threadCount = 0;

			@Override
			public Thread newThread(Runnable r) {
				Thread th = new Thread(r, "Worker - " + threadCount);
				threadCount++;
				th.setDaemon(true);
				return th;
			}
		};
	}

	@Override
	public Storage getStorage() {
		return storage;
	}

	@Override
	public CommandHandler getCommandHandler() {
		return commandHandler;
	}

	@Override
	public ClienSocketHandler BuildNewClientSocketHandler(Socket clientSocket) {
		return new DefaultClientSocketHandler(clientSocket, this);
	}

	@Override
	public int getServerPort() {
		String value = applicationProperties.getProperty("jmemcached.server.port");
		try {
			int port = Integer.parseInt(value);
			if (port < 0 || port > 65535) {
				throw new JMemcachedConfigException("Port should be between 0 and 65535");
			}
			return port;
		} catch (NumberFormatException e) {
			throw new JMemcachedConfigException("Port should be a number", e);
		}
	}

	@Override
	public int getInitThreadCount() {
		return getThreadCount("jmemcached.server.init.thread.count");
	}

	@Override
	public int getMaxThreadCount() {
		return getThreadCount("jmemcashed.server.max.thread.count");
	}

	protected int getThreadCount(String propertyName) {
		String value = applicationProperties.getProperty(propertyName);
		try {
			int threadCount = Integer.parseInt(value);
			if (threadCount < 1) {
				throw new JMemcachedConfigException(propertyName + " should be >=1");
			}
			return threadCount;
		} catch (NumberFormatException e) {
			throw new JMemcachedConfigException(propertyName + "should be a number", e);
		}
	}

	@Override
	public int getClearDataIntervalInMs() {
		String value = applicationProperties.getProperty("jmemcached.storage.clear.data.interval.ms");
		try {
			int clearDataIntervalInMs = Integer.parseInt(value);
			if (clearDataIntervalInMs < 1000) {
				throw new JMemcachedConfigException("clearDataIntervalInMs should be >1000 ms");
			}
			return clearDataIntervalInMs;
		} catch (NumberFormatException e) {
			throw new JMemcachedConfigException("clearDataIntervalInMs should be a number", e);
		}
	}

	@Override
	public String toString() {
		return String.format(
				"DefaultServerConfig: port=%s, initThreadCount=%s, maxThreadCount=%s, clearDataIntervalInMs=%sms",
				getServerPort(), getInitThreadCount(), getMaxThreadCount(), getClearDataIntervalInMs());
	}

}
