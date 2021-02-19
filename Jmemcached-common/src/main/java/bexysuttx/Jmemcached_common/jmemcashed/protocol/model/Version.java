package bexysuttx.Jmemcached_common.jmemcashed.protocol.model;

import bexysuttx.Jmemcached_common.jmemcashed.exception.JMemCachedException;

public enum Version {
	VERSION_1_0(1, 0), VERSION_0_0(0, 0);

	private byte high;
	private byte low;

	Version(int high, int low) {
		this.high = (byte) (high & 0x7);
		this.low = (byte) (low & 0xF);
	}

	public static Version valueOf(byte byteCode) {
		for (Version version : Version.values()) {
			if (version.getByteCode() == byteCode) {
				return version;
			}
		}
		throw new JMemCachedException("Unsupported byteCode for version: " + byteCode);
	}

	public byte getByteCode() {
		return (byte) (low + (high << 4));
	}

	@Override
	public String toString() {
		return String.format("%s.%s", high, low);
	}

}
