package bexysuttx.Jmemcached_common.jmemcashed.protocol.model;

public abstract class AbstractPackage {
	private byte[] data;

	public AbstractPackage(byte[] data) {
		this.data = data;
	}

	 public AbstractPackage() {
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public boolean hasData() {
		return data != null && data.length > 0;
	}

}
