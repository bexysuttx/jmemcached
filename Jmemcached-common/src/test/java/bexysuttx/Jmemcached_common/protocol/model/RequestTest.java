package bexysuttx.Jmemcached_common.protocol.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import bexysuttx.Jmemcached_common.jmemcashed.protocol.model.Command;
import bexysuttx.Jmemcached_common.jmemcashed.protocol.model.Request;

public class RequestTest {

	Request request;

	@Before
	public void before() {
		request = new Request(Command.CLEAR);
	}

	@Test
	public void hasKeyTrue() {
		request.setKey("test");
		assertTrue(request.hasKey());
	}

	@Test
	public void hasKeyFalse() {
		assertFalse(request.hasKey());
	}

	@Test
	public void hasTtlTrue() {
		request.setTtl(123L);
		assertTrue(request.hasTtl());
	}

	@Test
	public void hasTtlFalse() {
		assertFalse(request.hasTtl());
	}

	@Test
	public void testToStringClear() {
		assertEquals("CLEAR", request.toString());
	}

	@Test
	public void testToStringRemove() {
		request = new Request(Command.REMOVE);
		request.setKey("test");
		assertEquals("REMOVE[test]", request.toString());
	}
	
	@Test
	public void testToStringPut() {
		request = new Request(Command.PUT);
		request.setKey("test");
		request.setData(new byte[] {1,2,3});
		assertEquals("PUT[test]=3 bytes", request.toString());
		
	}
	
	@Test
	public void testToStringGet() {
		request = new Request(Command.GET);
		request.setKey("test");
		assertEquals("GET[test]", request.toString());
		
	}

}
