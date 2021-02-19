package bexysuttx.Jmemcached_common.protocol.model;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import bexysuttx.Jmemcached_common.jmemcashed.protocol.model.Response;
import bexysuttx.Jmemcached_common.jmemcashed.protocol.model.Status;

public class ResponseTest {

	Response response;
	
	@Before
	public void before() {
		response = new Response(Status.ADDED);
	}
	
	@Test
	public void testToStringAdded() {
		assertEquals("ADDED", response.toString());
	}
	
	@Test
	public void testToStringReplaced() {
		response = new Response(Status.REPLACED);
		response.setData(new byte[] {1,2,3});
		assertEquals("REPLACED [3 bytes]", response.toString());
	}
}
