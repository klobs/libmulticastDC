package de.tu.dresden.inf.dud.libmulticastdc;

import java.util.Queue;

public class DCMulticastChannel {

	private long assocChannelNo;
	private Queue<byte[]> messageQueue;
	
	public DCMulticastChannel(long channelNo){
		assocChannelNo = channelNo;
	}
	
}
