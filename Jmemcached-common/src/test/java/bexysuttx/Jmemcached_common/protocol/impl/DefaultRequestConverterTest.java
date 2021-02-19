package bexysuttx.Jmemcached_common.protocol.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.lang3.StringUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import bexysuttx.Jmemcached_common.jmemcashed.exception.JMemCachedException;
import bexysuttx.Jmemcached_common.jmemcashed.protocol.impl.DefaultRequestConverter;
import bexysuttx.Jmemcached_common.jmemcashed.protocol.model.Command;
import bexysuttx.Jmemcached_common.jmemcashed.protocol.model.Request;

public class DefaultRequestConverterTest {

	private DefaultRequestConverter defaultRequestConverter = new DefaultRequestConverter();

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void testGetFlagsByteEmpty() {
		Request request = new Request(Command.CLEAR);
		byte flags = defaultRequestConverter.getFlagsByte(request);
		assertEquals(0, flags);
	}

	@Test
	public void testGetFlagsByteFull() {
		Request request = new Request(Command.PUT, "key", System.currentTimeMillis(), new byte[] { 1, 2, 3 });
		byte flags = defaultRequestConverter.getFlagsByte(request);
		assertEquals(7, flags);
	}

	@Test
	public void testWriteKeySuccess() throws IOException {
		DataOutputStream dataOutputStream = spy(new DataOutputStream(mock(OutputStream.class)));
		String key = "key";
		defaultRequestConverter.writeKey(dataOutputStream, new Request(Command.GET, key));

		verify(dataOutputStream).write(key.getBytes(StandardCharsets.US_ASCII));
		verify(dataOutputStream).writeByte(3);
	}

	@Test
	public void testWriteKeyFailed() throws IOException {
		String key = StringUtils.repeat('a', 128);
		thrown.expect(JMemCachedException.class);
		thrown.expectMessage(is("Length key should be 0 to 127 bytes. Key: " + key));

		DataOutputStream dataOutputStream = new DataOutputStream(null);
		defaultRequestConverter.writeKey(dataOutputStream, new Request(Command.CLEAR, key));
	}

	@Test
	public void testReadRequestEmpty() throws IOException {
		Request request = defaultRequestConverter.readRequest(new ByteArrayInputStream(new byte[] 
				//version, cmd, flags
				{16,0,0}));
		assertEquals(Command.CLEAR, request.getCommand());
		assertFalse(request.hasKey());
		assertFalse(request.hasTtl());
		assertFalse(request.hasData());	
	}
	
	@Test
	public void testReadRequestAll() throws IOException {
		Request request = defaultRequestConverter.readRequest(new ByteArrayInputStream(new byte[] 
				//version, cmd, flags,key.length, key, ttl, data.length, data
				{16,  1,  7,  3,  49,50,51,  0,0,0,0,0,0,0,5,  0,0,0,3,  1,2,3}));
		assertEquals(Command.PUT, request.getCommand());
		assertTrue(request.hasKey());
		assertEquals("123", request.getKey());
		assertTrue(request.hasTtl());
		assertEquals(Long.valueOf(5L), request.getTtl());
		assertTrue(request.hasData());
		assertArrayEquals(new byte[] {1,2,3}, request.getData());
	}
	
	@Test
	public void testWriteRequestEmpty() throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		defaultRequestConverter.writeRequest(out, new Request (Command.CLEAR));
		assertArrayEquals(new byte[] {16, 0, 0}, out.toByteArray());
	}
	
	@Test
	public void testWriteRequestAll() throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		defaultRequestConverter.writeRequest(out, new Request(Command.CLEAR, "123", 5L,new byte[] {1,2,3}));
		assertArrayEquals(new byte[]
				////version, cmd, flags,key.length, key, ttl, data.length, data
				{16,0,7 , 3,  49,50,51,  0,0,0,0,0,0,0,5,  0,0,0,3,  1,2,3}, out.toByteArray());
	}
}
