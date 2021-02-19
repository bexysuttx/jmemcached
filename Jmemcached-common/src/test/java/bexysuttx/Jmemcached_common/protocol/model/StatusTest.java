package bexysuttx.Jmemcached_common.protocol.model;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import bexysuttx.Jmemcached_common.jmemcashed.exception.JMemCachedException;
import bexysuttx.Jmemcached_common.jmemcashed.protocol.model.Status;

public class StatusTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void testValueOfSuccess() {
		assertEquals(Status.ADDED, Status.valueof((byte) 0));
		assertEquals(Status.REPLACED, Status.valueof((byte) 1));
		assertEquals(Status.GOTTEN, Status.valueof((byte) 2));
		assertEquals(Status.NOT_FOUND, Status.valueof((byte) 3));
		assertEquals(Status.REMOVED, Status.valueof((byte) 4));
		assertEquals(Status.CLEARED, Status.valueof((byte) 5));
	}

	@Test
	public void testValueOfFailed() {
		thrown.expect(JMemCachedException.class);
		thrown.expectMessage(is("Unsupported byteCode for status: 6"));
		Status.valueof((byte) 6);
	}

	@Test
	public void testGetByteCode() {
		assertEquals(0, Status.ADDED.getByteCode());
		assertEquals(1, Status.REPLACED.getByteCode());
		assertEquals(2, Status.GOTTEN.getByteCode());
		assertEquals(3, Status.NOT_FOUND.getByteCode());
		assertEquals(4, Status.REMOVED.getByteCode());
		assertEquals(5, Status.CLEARED.getByteCode());
	}
}
