package de.tu.dresden.inf.dud.libmulticastdc;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import de.tu.dresden.dud.dc.Connection;
import de.tu.dresden.dud.dc.Participant;
import de.tu.dresden.dud.dc.ManagementMessage.ManagementMessageAdded;
import de.tu.dresden.dud.dc.Util;


public class DCMulticastParticipant implements Observer{

	private Participant assocParticipant = null;
	private Connection  assocConnection  = null;
	
	
	private HashMap<DCMulticastChannelPrefix,DCMulticastChannel> multicastChannels = new HashMap<DCMulticastChannelPrefix,DCMulticastChannel>();
	
	protected static byte[] generatePrefix(final long channelNo){
		return Util.fillAndMergeSending(new byte[8], Util
				.stuffLongIntoLong(channelNo));
	}
	
	public Connection getAssocConnection(){
		return assocConnection;
	}
	
	public Participant getAssocParticipant(){
		return assocParticipant;
	}

	public DCMulticastChannel getDCMulticastChannel(DCMulticastChannelPrefix channelNo){
		DCMulticastChannel dcmc = multicastChannels.get(channelNo);
		if (dcmc != null)
			return dcmc;
		return listenToMulicastChannel(channelNo);
	}
	
	public boolean islisteningToMulticastChannel(DCMulticastChannelPrefix channelNo){
		return multicastChannels.containsKey(channelNo);
	}
	
	public DCMulticastChannel listenToMulicastChannel(long channelNo){
		return listenToMulicastChannel(new DCMulticastChannelPrefix(
				generatePrefix(channelNo)));
	}
	
	public DCMulticastChannel listenToMulicastChannel(DCMulticastChannelPrefix channelNo){
		DCMulticastChannel dcmc = new DCMulticastChannel(channelNo, this);
		multicastChannels.put(channelNo, dcmc);
		return dcmc;
	}
	
	public void setConnection(Connection c){
		assocConnection = c;
	}
	
	public void setParticipant(Participant p){
		assocParticipant = p;
		assocParticipant.addObserver(this);
	}

	@Override
	public void update(Observable o, Object arg) {
		if(o instanceof Participant){
		
			if(arg instanceof ManagementMessageAdded){
				ManagementMessageAdded m = (ManagementMessageAdded) arg;
				
				if (m.getPayloadLength() > 8) {
					DCMulticastChannelPrefix channelNo = new DCMulticastChannelPrefix(
							Util.getFirstBytes(m.getPayload(), 8));
	
					byte[] message = Util.getLastBytes(m.getPayload(), m.getPayloadLength() - 8);
					
					if(islisteningToMulticastChannel(channelNo))
						getDCMulticastChannel(channelNo).multicastMessageArrived(message);
				}
			}
		}
	}

	public void write(DCMulticastChannelPrefix channelNo, byte[] message){
		getDCMulticastChannel(channelNo).write(message);
	}
	
	protected void write(byte[] multicastMessage){
		if (assocConnection != null)
			assocConnection.feedWorkCycleManager(multicastMessage);
			
	}
}
