package bexysuttx.Jmemcached_common.jmemcashed.protocol.impl;

import bexysuttx.Jmemcached_common.jmemcashed.exception.JMemCachedException;
import bexysuttx.Jmemcached_common.jmemcashed.protocol.model.Version;

public abstract class AbstractPackageConverter {

	public void checkProtocolVersion(byte versionByte) {
		Version version = Version.valueOf(versionByte);
		if (version != Version.VERSION_1_0) {
			throw new JMemCachedException("Unsupported protocol version: " + version);
		}
	}
	
	public byte getVersionByte() {
		return Version.VERSION_1_0.getByteCode();
	}

}
