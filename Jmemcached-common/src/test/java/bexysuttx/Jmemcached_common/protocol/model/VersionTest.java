package bexysuttx.Jmemcached_common.protocol.model;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import bexysuttx.Jmemcached_common.jmemcashed.exception.JMemCachedException;
import bexysuttx.Jmemcached_common.jmemcashed.protocol.model.Version;

public class VersionTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void testValueOfSuccess() {
		assertEquals(Version.VERSION_1_0, Version.valueOf((byte) 16));
	}

	@Test
	public void testValueOfFailed() {
		thrown.expect(JMemCachedException.class);
		thrown.expectMessage(is("Unsupported byteCode for version: 127"));
		Version.valueOf(Byte.MAX_VALUE);
	}

	@Test
	public void testGetByteCode() {
		assertEquals(16, Version.VERSION_1_0.getByteCode());
	}

	@Test
	public void testToString() {
		assertEquals("1.0", Version.VERSION_1_0.toString());
	}

}
