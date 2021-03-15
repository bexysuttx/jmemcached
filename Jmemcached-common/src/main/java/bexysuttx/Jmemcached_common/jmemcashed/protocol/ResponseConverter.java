package bexysuttx.Jmemcached_common.jmemcashed.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import bexysuttx.Jmemcached_common.jmemcashed.protocol.model.Response;

public interface ResponseConverter {

	Response readResponse(InputStream in) throws IOException;
	
	void writeResponse(OutputStream out, Response response) throws IOException;  
}
