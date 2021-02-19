package bexysuttx.Jmemcached_common.jmemcashed.protocol.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import bexysuttx.Jmemcached_common.jmemcashed.exception.JMemCachedException;
import bexysuttx.Jmemcached_common.jmemcashed.protocol.ObjectSerializer;

public class DefaultObjectSerializer implements ObjectSerializer {

	@Override
	public byte[] toByteArray(Object object) {
		if (object == null) {
			return null;
		}
		if (!(object instanceof Serializable)) {
			throw new JMemCachedException(
					"Class " + object.getClass().getName() + " should implement java.io.Serializable interface");
		}
		try {
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(byteArrayOutputStream);
			out.writeObject(object);
			out.flush();
			return byteArrayOutputStream.toByteArray();
		} catch (IOException e) {
			throw new JMemCachedException("Can't convert object to byte array: " + e.getMessage(), e);
		}
	}

	@Override
	public Object fromByteArray(byte[] array) {
		if (array == null) {
			return null;
		}
		try {
			ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(array));
			return in.readObject();
		} catch (IOException | ClassNotFoundException e) {
			throw new JMemCachedException("Can't convert byte array to object: " + e.getMessage(), e);
		}
	}

}
