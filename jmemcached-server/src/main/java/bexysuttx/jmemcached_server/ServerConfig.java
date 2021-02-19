package bexysuttx.jmemcached_server;

import java.net.Socket;
import java.util.concurrent.ThreadFactory;

import bexysuttx.Jmemcached_common.jmemcashed.protocol.RequestConverter;
import bexysuttx.Jmemcached_common.jmemcashed.protocol.ResponseConverter;

public interface ServerConfig extends AutoCloseable {

	RequestConverter getRequestConverter();

	ResponseConverter getResponseConverter();

	ThreadFactory getWorkerThreadFactory();

	Storage getStorage();

	CommandHandler getCommandHandler();

	ClienSocketHandler BuildNewClientSocketHandler(Socket clientSocket);

	int getServerPort(); 

	int getInitThreadCount();

	int getMaxThreadCount();

	int getClearDataIntervalInMs();

}
