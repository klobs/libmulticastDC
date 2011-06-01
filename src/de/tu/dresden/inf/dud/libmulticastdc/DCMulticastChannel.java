package de.tu.dresden.inf.dud.libmulticastdc;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import de.tu.dresden.dud.dc.Util;

public class DCMulticastChannel {

	private DCMulticastParticipant assocMulticastParticipant;
	private DCMulticastChannelPrefix channelPrefix = null;
	private LinkedBlockingQueue<byte[]> messageQueue = new LinkedBlockingQueue<byte[]>();

	public DCMulticastChannel(DCMulticastChannelPrefix channelNo, DCMulticastParticipant amp){
		channelPrefix = channelNo;
		
		assocMulticastParticipant = amp;
	}

	public DCMulticastParticipant getDCMulticastParticipant(){
		return assocMulticastParticipant;
	}
	
	protected void multicastMessageArrived(byte[] message){
		try {
			messageQueue.put(message);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public byte[] poll(long timeout,
            TimeUnit unit){
		try {
			return messageQueue.poll(timeout, unit);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return new byte[0];
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
				.concatenate(channelPrefix.getPrefix(), message));
	}
	
}
