package bexysuttx.Jmemcached_common.protocol.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import bexysuttx.Jmemcached_common.jmemcashed.protocol.model.AbstractPackage;
public class AbstractPackageTest {

	private static AbstractPackage newInstance(byte[] array) {
		return new AbstractPackage(array) {};
	}

	@Test
	public void testHasDataFailedNull() {
		AbstractPackage nullData= newInstance(null);
		assertFalse(nullData.hasData());
	}
	
	@Test
	public void testHasDataFailedLength() {
		AbstractPackage failedLength = newInstance(new byte[0]);
		assertFalse(failedLength.hasData());
	}
	
	@Test
	public void testHasDataSuccess() {
		AbstractPackage data = newInstance(new byte[] {1,2,3});
		assertTrue(data.hasData());
	}
	
}
