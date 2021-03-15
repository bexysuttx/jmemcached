package bexysuttx.Jmemcached_common.protocol.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.isA;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import bexysuttx.Jmemcached_common.jmemcashed.exception.JMemCachedException;
import bexysuttx.Jmemcached_common.jmemcashed.protocol.impl.DefaultObjectSerializer;
import bexysuttx.Jmemcached_common.protocol.test.SeriazableFailedClass;

public class DefaultObjectSerializerTest {
	private final DefaultObjectSerializer defaultObjectSerializer = new DefaultObjectSerializer();

	private final Object object = "test";

	private byte[] testObjectArray = { -84, -19, 0, 5, 116, 0, 4, 116, 101, 115, 116 };
	
	private byte[] testSerialable = {-84, -19, 0, 5, 115, 114, 0, 63, 98, 101, 120, 121, 115, 117, 116, 116, 120, 46, 74, 109, 101, 109, 99, 97, 99, 104, 101, 100, 95, 99, 111, 109, 109, 111, 110, 46, 112, 114, 111, 116, 111, 99, 111, 108, 46, 116, 101, 115, 116, 46, 83, 101, 114, 105, 97, 122, 97, 98, 108, 101, 70, 97, 105, 108, 101, 100, 67, 108, 97, 115, 115, -86, 80, -3, 67, 101, -36, -15, 16, 2, 0, 0, 120, 112};

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void testToByteArraySuccess() {
		byte[] actual = defaultObjectSerializer.toByteArray(object);
		assertArrayEquals(testObjectArray, actual);
	}

	@Test
	public void testToByteArrayNull() {
		assertNull(defaultObjectSerializer.toByteArray(null));
	}

	@Test
	public void testToByteArrayNotSerializ() {
		thrown.expect(JMemCachedException.class);
		thrown.expectMessage(is("Class java.lang.Object should implement java.io.Serializable interface"));
		defaultObjectSerializer.toByteArray(new Object());
	}

	@Test
	public void testToByteArrayIOException() {
		thrown.expect(JMemCachedException.class);
		thrown.expectMessage(is("Can't convert object to byte array: Write IO"));
		thrown.expectCause(isA(IOException.class)); 
		defaultObjectSerializer.toByteArray( new SeriazableFailedClass());

	}
	
	@Test
	public void testFromByteArrayNull() {
		assertNull(defaultObjectSerializer.fromByteArray(null));
	}
	
	@Test
	public void testFromByteArraySuccess() {
		Object actual = defaultObjectSerializer.fromByteArray(testObjectArray);
		assertEquals(object, actual);
	}
	
	@Test
	public void testFromByteArrayIOException() {
		thrown.expect(JMemCachedException.class);
		thrown.expectMessage(is("Can't convert byte array to object: Read IO"));
		thrown.expectCause(isA(IOException.class)); 
		defaultObjectSerializer.fromByteArray(testSerialable);
		 

	}
}
