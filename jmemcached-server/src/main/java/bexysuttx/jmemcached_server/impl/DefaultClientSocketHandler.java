package bexysuttx.jmemcached_server.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bexysuttx.Jmemcached_common.jmemcashed.protocol.RequestConverter;
import bexysuttx.Jmemcached_common.jmemcashed.protocol.ResponseConverter;
import bexysuttx.Jmemcached_common.jmemcashed.protocol.model.Request;
import bexysuttx.Jmemcached_common.jmemcashed.protocol.model.Response;
import bexysuttx.jmemcached_server.ClienSocketHandler;
import bexysuttx.jmemcached_server.CommandHandler;
import bexysuttx.jmemcached_server.ServerConfig;

class DefaultClientSocketHandler implements ClienSocketHandler {
	private final Logger LOGGER = LoggerFactory.getLogger(DefaultClientSocketHandler.class);
	private final Socket socket;
	private final ServerConfig serverConfig;

	DefaultClientSocketHandler(Socket socket, ServerConfig serverConfig) {
		this.socket = socket;
		this.serverConfig = serverConfig;
	}

	protected boolean isStopRun() {
		return Thread.interrupted();
	}

	@Override
	public void run() {
		try {
			RequestConverter requestConverter = serverConfig.getRequestConverter();
			ResponseConverter responseConverter = serverConfig.getResponseConverter();
			CommandHandler commandHandler = serverConfig.getCommandHandler();
			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();
			while (!isStopRun()) {
				try {
					Request request = requestConverter.readRequest(in);
					Response response = commandHandler.handle(request);
					responseConverter.writeResponse(out, response);
					LOGGER.debug("Command {} -> {}", request, response);
				} catch (RuntimeException e) {
					LOGGER.error("Handle request failed:" + e.getMessage(), e);
				}
			}

		} catch (SocketException e) {
			LOGGER.info("Remote client connection closed: " + socket.getRemoteSocketAddress().toString() + ": "
					+ e.getMessage());
		} catch (IOException e) {

			if (!socket.isClosed()) {
				LOGGER.error("IO ERROR: " + e.getMessage(), e);
			}
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				LOGGER.error("Close socket failed: " + e.getMessage(), e);

			}
		}

	}
}
