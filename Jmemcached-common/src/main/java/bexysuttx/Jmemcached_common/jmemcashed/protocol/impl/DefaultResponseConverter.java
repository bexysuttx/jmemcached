package bexysuttx.Jmemcached_common.jmemcashed.protocol.impl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

import bexysuttx.Jmemcached_common.jmemcashed.protocol.ResponseConverter;
import bexysuttx.Jmemcached_common.jmemcashed.protocol.model.Response;
import bexysuttx.Jmemcached_common.jmemcashed.protocol.model.Status;

public class DefaultResponseConverter extends AbstractPackageConverter implements ResponseConverter {

	@Override
	public Response readResponse(InputStream in) throws IOException {
		DataInputStream dataInputStream = new DataInputStream(in);
		checkProtocolVersion(dataInputStream.readByte());
		byte status = dataInputStream.readByte();
		Response response = new Response(Status.valueof(status));
		byte data = dataInputStream.readByte();
		if (data == 1) {
			int dataLength = dataInputStream.readInt();
			response.setData(IOUtils.readFully(dataInputStream, dataLength));
			  
		}
		return response;
	}

	@Override
	public void writeResponse(OutputStream out, Response response) throws IOException {
		DataOutputStream dataOutputStream = new DataOutputStream(out);
		dataOutputStream.writeByte(getVersionByte());
		dataOutputStream.writeByte(response.getStatus().getByteCode());
		dataOutputStream.writeByte(response.hasData() ? 1 : 0);
		 if (response.hasData()) {
			 dataOutputStream.writeInt(response.getData().length);
			 dataOutputStream.write(response.getData());
		 }
		 dataOutputStream.flush();
	}

}
