package bexysuttx.Jmemcached_common.jmemcashed.protocol.impl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;

import bexysuttx.Jmemcached_common.jmemcashed.exception.JMemCachedException;
import bexysuttx.Jmemcached_common.jmemcashed.protocol.RequestConverter;
import bexysuttx.Jmemcached_common.jmemcashed.protocol.model.Command;
import bexysuttx.Jmemcached_common.jmemcashed.protocol.model.Request;

public class DefaultRequestConverter extends AbstractPackageConverter implements RequestConverter {

	@Override
	public Request readRequest(InputStream in) throws IOException {
		DataInputStream dataInputStream = new DataInputStream(in);
		checkProtocolVersion(dataInputStream.readByte());
		byte command = dataInputStream.readByte();
		byte flags = dataInputStream.readByte();
		boolean hasKey = (flags & 1) != 0;
		boolean hasTtl = (flags & 2) != 0;
		boolean hasData = (flags & 4) != 0;

		return readFlags(command, hasKey, hasTtl, hasData, dataInputStream);
	}

	private Request readFlags(byte command, boolean hasKey, boolean hasTtl, boolean hasData,
			DataInputStream dataInputStream) throws IOException {
		Request request = new Request(Command.valueOf(command));
		if (hasKey) {
			byte keyLength = dataInputStream.readByte();
			byte[] keyBytes = IOUtils.readFully(dataInputStream, keyLength);
			request.setKey(new String(keyBytes, StandardCharsets.US_ASCII));
		}
		if (hasTtl) {
			request.setTtl(dataInputStream.readLong());
		}
		if (hasData) {
			int dataLength = dataInputStream.readInt();
			request.setData(IOUtils.readFully(dataInputStream, dataLength));
		}
		return request;
	}

	@Override
	public void writeRequest(OutputStream out, Request request) throws IOException {
		DataOutputStream dataOutStream = new DataOutputStream(out);
		dataOutStream.writeByte(getVersionByte());
		dataOutStream.writeByte(request.getCommand().getByteCode());
		dataOutStream.writeByte(getFlagsByte(request));
		if (request.hasKey()) {
			writeKey(dataOutStream, request);
		}
		if (request.hasTtl()) {
			dataOutStream.writeLong(request.getTtl());
		}
		if (request.hasData()) {
			dataOutStream.writeInt(request.getData().length);
			dataOutStream.write(request.getData());
		}
		
		dataOutStream.flush();

	}

	public byte getFlagsByte(Request request) {
		byte flags = 0;
		if (request.hasKey()) {
			flags |= 1;
		}
		if (request.hasTtl()) {
			flags |= 2;
		}
		if (request.hasData()) {
			flags |= 4;
		}
		return flags;
	}

	public void writeKey(DataOutputStream dataOutputStream, Request request) throws IOException {
		byte[] key = request.getKey().getBytes(StandardCharsets.US_ASCII);
		if (key.length > 127) {
			throw new JMemCachedException("Length key should be 0 to 127 bytes. Key: " + request.getKey());
		}
		dataOutputStream.writeByte(key.length);
		dataOutputStream.write(key);
	}

}
