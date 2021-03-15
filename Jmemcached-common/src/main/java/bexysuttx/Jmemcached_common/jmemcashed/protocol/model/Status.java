package bexysuttx.Jmemcached_common.jmemcashed.protocol.model;

import bexysuttx.Jmemcached_common.jmemcashed.exception.JMemCachedException;

public enum Status {
	ADDED(0), REPLACED(1), GOTTEN(2), NOT_FOUND(3), REMOVED(4), CLEARED(5);

	private byte code;

	Status(int code) {
		this.code = (byte) code;

	}

	public static Status valueof(byte byteCode) {
		for (Status status : Status.values()) {
			if (status.getByteCode() == byteCode) {
				return status;
			}
		}
		throw new JMemCachedException("Unsupported byteCode for status: " + byteCode);
	}

	public byte getByteCode() {
		return code;
	}

}
