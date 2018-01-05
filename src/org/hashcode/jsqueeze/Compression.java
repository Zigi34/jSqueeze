package org.hashcode.jsqueeze;

public interface Compression {
	public byte[] encode(byte[] data);

	public byte[] decode(byte[] data);
}
