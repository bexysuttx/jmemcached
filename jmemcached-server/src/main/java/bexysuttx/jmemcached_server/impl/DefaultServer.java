package bexysuttx.jmemcached_server.impl;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bexysuttx.Jmemcached_common.jmemcashed.exception.JMemCachedException;
import bexysuttx.jmemcached_server.Server;
import bexysuttx.jmemcached_server.ServerConfig;

class DefaultServer implements Server {
	public static final Logger LOGGER = LoggerFactory.getLogger(DefaultServer.class);
	private final ServerConfig serverConfig;
	private final ServerSocket serverSocket;
	private final ExecutorService executorService;
	private final Thread mainServerThread;
	private volatile boolean serverStopped;

	public DefaultServer(ServerConfig serverConfig) {
		this.serverConfig = serverConfig;
		this.serverSocket = createServerSocket();
		this.executorService = createExecutorService();
		this.mainServerThread = createMainServerThread(createServerRunnable());
	}

	protected Runnable createServerRunnable() {
		return new Runnable() {

			@Override
			public void run() {
				while (!mainServerThread.isInterrupted()) {
					try {
						Socket clientSocket = serverSocket.accept();
						try {
							executorService.submit(serverConfig.BuildNewClientSocketHandler(clientSocket));
							LOGGER.info("A new clien connection estabilished: "
									+ clientSocket.getRemoteSocketAddress().toString());
						} catch (RejectedExecutionException e) {
							LOGGER.error("All worker thread are busy. A new connection rejected: " + e.getMessage());
							clientSocket.close();
						}

					} catch (IOException e) {
						if (!serverSocket.isClosed()) {
							LOGGER.error("Can't accept client socket: " + e.getMessage(), e);

						}

						destroyJMemcachedServer();
						break;

					}
				}
			}

		};
	}

	protected Thread getShutdownHook() {
		return new Thread(new Runnable() {

			@Override
			public void run() {
				if (!serverStopped) {
					destroyJMemcachedServer();
				}
			}
		}, "ShutDownHook");
	}

	protected void destroyJMemcachedServer() {
		try {
			serverConfig.close();
		} catch (Exception e) {
			LOGGER.error("Close serverConfig failed: " + e.getMessage(), e);
		}
		executorService.shutdownNow();
		LOGGER.info("Server stopped");
		serverStopped = true;
	}

	protected Thread createMainServerThread(Runnable r) {
		Thread th = new Thread(r, "Main server Thread");
		th.setPriority(Thread.MAX_PRIORITY);
		th.setDaemon(false);
		return th;
	}

	protected ExecutorService createExecutorService() {
		ThreadFactory threadFactory = serverConfig.getWorkerThreadFactory();
		int threadInitThreadCount = serverConfig.getInitThreadCount();
		int threadMaxThreadCount = serverConfig.getMaxThreadCount();
		return new ThreadPoolExecutor(threadInitThreadCount, threadMaxThreadCount, 60L, TimeUnit.SECONDS,
				new SynchronousQueue<Runnable>(), threadFactory, new ThreadPoolExecutor.AbortPolicy());
	}

	protected ServerSocket createServerSocket() {
		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(serverConfig.getServerPort());
			serverSocket.setReuseAddress(true);
			return serverSocket;
		} catch (IOException e) {
			throw new JMemCachedException("Can't create server socket with port: " + serverConfig.getServerPort(), e);
		}
	}

	@Override
	public void start() {
		if (mainServerThread.getState() != Thread.State.NEW) {
			throw new JMemCachedException(
					"Current JMcached already started or stopped. Please create a new server instance.");
		}
		Runtime.getRuntime().addShutdownHook(getShutdownHook());
		mainServerThread.start();
		LOGGER.info("Server started!" + serverConfig);
	}

	@Override
	public void stop() {
		LOGGER.info("Detected stop command!");
		mainServerThread.interrupt();
		try {
			serverSocket.close();
		} catch (IOException e) {
			LOGGER.warn("Error during close server socket: " + e.getMessage());
		}
	}

}
