package bexysuttx.Jmemcached_common.protocol.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import bexysuttx.Jmemcached_common.jmemcashed.exception.JMemCachedException;
import bexysuttx.Jmemcached_common.jmemcashed.protocol.impl.AbstractPackageConverter;

public class AbstractPackageConverterTest {

	AbstractPackageConverter abstractPackageConverter = new AbstractPackageConverter() {
	};

	@Rule
	 public ExpectedException thrown = ExpectedException.none();

	@Test
	public void testCheckProtocolVersion() {
		try {
			abstractPackageConverter.checkProtocolVersion((byte) 16);
		} catch (Exception e) {
			fail("Supported protocol version should be 1.0");
		}
	}

	@Test
	public void testCheckProtocolFailed() {
		thrown.expect(JMemCachedException.class);
		thrown.expectMessage(is("Unsupported protocol version: 0.0"));
		abstractPackageConverter.checkProtocolVersion((byte) 0);
	}

	@Test
	public void testGetVersionByte() {
		assertEquals(16, abstractPackageConverter.getVersionByte());
	}
}
