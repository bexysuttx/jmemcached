package bexysuttx.Jmemcached_common.protocol.impl;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Test;

import bexysuttx.Jmemcached_common.jmemcashed.protocol.impl.DefaultResponseConverter;
import bexysuttx.Jmemcached_common.jmemcashed.protocol.model.Response;
import bexysuttx.Jmemcached_common.jmemcashed.protocol.model.Status;

public class DefaultResponseConverterTest {
	private final DefaultResponseConverter defaultResponseConverter = new DefaultResponseConverter();
	
	
	@Test
	public void testReadResponseWithoutData() throws IOException {
		Response response = defaultResponseConverter.readResponse(new ByteArrayInputStream(new byte[] {
				16,0,0
		}));
		assertEquals(Status.ADDED, response.getStatus());
		assertFalse(response.hasData());
	}
	
	@Test
	public void testReadResponseWithData() throws IOException {
		Response response = defaultResponseConverter.readResponse(new ByteArrayInputStream(new byte[] {
				16,0,1,0,0,0,3,1,2,3
		}));
		assertEquals(Status.ADDED, response.getStatus());
		assertTrue(response.hasData());
	}
	
	@Test
	public void testWriteResponseWithoutData() throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Response response = new Response (Status.GOTTEN);
		defaultResponseConverter.writeResponse(out, response);
		assertArrayEquals(new byte[] {16,2,0}, out.toByteArray());
	}
	
	@Test
	public void testWriteResponseWithData() throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Response response =new Response(Status.ADDED, new byte[] {1,2,3});
		defaultResponseConverter.writeResponse(out, response);
		assertArrayEquals(new byte[] {16,0,1,0,0,0,3,1,2,3}, out.toByteArray());
	}
}
