package bexysuttx.jmemcached_server;

import bexysuttx.Jmemcached_common.jmemcashed.protocol.model.Request;
import bexysuttx.Jmemcached_common.jmemcashed.protocol.model.Response;

public interface CommandHandler {

	Response handle(Request request);
}
