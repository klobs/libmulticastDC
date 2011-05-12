package de.tu.dresden.inf.dud.libmulticastdc;

import java.util.concurrent.LinkedBlockingQueue;

import de.tu.dresden.dud.dc.Util;

public class DCMulticastChannel {

	private long assocChannelNo;
	private DCMulticastParticipant assocMulticastParticipant;
	private byte[] channelPrefix = null;
	private LinkedBlockingQueue<byte[]> messageQueue = new LinkedBlockingQueue<byte[]>();
	
	
	public DCMulticastChannel(long channelNo, DCMulticastParticipant amp){
		assocChannelNo = channelNo;
		channelPrefix = Util.fillAndMergeSending(new byte[8], Util
				.stuffLongIntoLong(assocChannelNo));
		
		assocMulticastParticipant = amp;
	}
	
	protected void multicastMessageArrived(byte[] message){
		try {
			messageQueue.put(message);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public byte[] read(){
		try {
			return messageQueue.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return new byte[0];
	}
	
	public void write(byte[] message){
		assocMulticastParticipant.write(Util
				.concatenate(channelPrefix, message));
	}
	
}
