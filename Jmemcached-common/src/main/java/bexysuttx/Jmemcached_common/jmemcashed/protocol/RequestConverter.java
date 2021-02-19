package bexysuttx.Jmemcached_common.jmemcashed.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import bexysuttx.Jmemcached_common.jmemcashed.protocol.model.Request;

public interface RequestConverter {
	
	Request readRequest(InputStream in) throws IOException;
	
	void writeRequest (OutputStream out, Request request) throws IOException;

}
