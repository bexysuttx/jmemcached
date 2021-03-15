package bexysuttx.Jmemcached_common.protocol.model;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import bexysuttx.Jmemcached_common.jmemcashed.exception.JMemCachedException;
import bexysuttx.Jmemcached_common.jmemcashed.protocol.model.Command;

public class CommandTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void testValueOfSuccess() {
		assertEquals(Command.CLEAR, Command.valueOf((byte) 0));
		assertEquals(Command.PUT, Command.valueOf((byte) 1));
		assertEquals(Command.GET, Command.valueOf((byte) 2));
		assertEquals(Command.REMOVE, Command.valueOf((byte) 3));
	}

	@Test
	public void testValueOfFailed() {
		thrown.expect(JMemCachedException.class);
		thrown.expectMessage(is("Unsupported bytecode for command: 7"));
		Command.valueOf((byte) 7);
	}

	@Test
	public void testGetByteCode() {
		assertEquals(0, Command.CLEAR.getByteCode());
		assertEquals(1, Command.PUT.getByteCode());
		assertEquals(2, Command.GET.getByteCode());
		assertEquals(3, Command.REMOVE.getByteCode());
	}

}
