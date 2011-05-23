package de.tu.dresden.inf.dud.libmulticastdc;

import java.util.Arrays;

public class DCMulticastChannelPrefix {

	private final byte[] data;

	public DCMulticastChannelPrefix(byte[] data) {
		if (data == null) {
			throw new NullPointerException();
		}
		this.data = data;
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof DCMulticastChannelPrefix)) {
			return false;
		}
		return Arrays.equals(data, ((DCMulticastChannelPrefix) other).data);
	}

	public byte[] getPrefix(){
		return data;
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode(data);
	}
}
