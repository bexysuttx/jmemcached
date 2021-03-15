package bexysuttx.Jmemcached_common.jmemcashed.protocol.model;

import bexysuttx.Jmemcached_common.jmemcashed.exception.JMemCachedException;

public enum Command {
	CLEAR(0),
	
	PUT(1),
	
	GET(2),
	
	REMOVE(3) ;
	
	private byte code ;

	Command(int code) {
		this.code = (byte) code;
	}
	
	
	public static Command valueOf(byte byteCode) {
		for (Command command : Command.values()) {
			if (command.getByteCode() == byteCode) {
				return command;
			}
		}
		throw new JMemCachedException("Unsupported bytecode for command: " + byteCode);
	}
	
	public byte getByteCode () {
		return code;
	}
	

}
