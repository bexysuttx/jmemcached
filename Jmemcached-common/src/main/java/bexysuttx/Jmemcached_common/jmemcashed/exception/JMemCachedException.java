package bexysuttx.Jmemcached_common.jmemcashed.exception;

public class JMemCachedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public JMemCachedException(String message, Throwable cause) {
		super(message, cause);
	}

	public JMemCachedException(String message) {
		super(message);
	}

	public JMemCachedException(Throwable cause) {
		super(cause);
	}

}
