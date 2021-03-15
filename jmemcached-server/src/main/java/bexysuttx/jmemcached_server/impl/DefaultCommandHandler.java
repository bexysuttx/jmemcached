package bexysuttx.jmemcached_server.impl;

import bexysuttx.Jmemcached_common.jmemcashed.exception.JMemCachedException;
import bexysuttx.Jmemcached_common.jmemcashed.protocol.model.Command;
import bexysuttx.Jmemcached_common.jmemcashed.protocol.model.Request;
import bexysuttx.Jmemcached_common.jmemcashed.protocol.model.Response;
import bexysuttx.Jmemcached_common.jmemcashed.protocol.model.Status;
import bexysuttx.jmemcached_server.CommandHandler;
import bexysuttx.jmemcached_server.ServerConfig;
import bexysuttx.jmemcached_server.Storage;

class DefaultCommandHandler implements CommandHandler {
	private final Storage storage;

	DefaultCommandHandler(ServerConfig serverConfig) {
		this.storage = serverConfig.getStorage();
	}

	@Override
	public Response handle(Request request) {
		Status status;
		byte[] data = null;
		if (request.getCommand() == Command.CLEAR) {
			status = storage.clear();
		} else if (request.getCommand() == Command.PUT) {
			status = storage.put(request.getKey(), request.getTtl(), request.getData());
		} else if (request.getCommand() == Command.REMOVE) {
			status = storage.remove(request.getKey());
		} else if (request.getCommand() == Command.GET) {
			data = storage.get(request.getKey());
			status = data == null ? Status.NOT_FOUND : Status.GOTTEN;
		} else {
			throw new JMemCachedException("Unsupported command: " + request.getCommand());
		}
		return new Response(status, data);
	}

}
