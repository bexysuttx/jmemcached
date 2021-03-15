package bexysuttx.Jmemcached_common.jmemcashed.protocol;

public interface ObjectSerializer {
	
	byte[] toByteArray (Object object);
	
	Object fromByteArray(byte[] array);

}
