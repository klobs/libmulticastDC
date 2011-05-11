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
	
	private HashMap<Long,DCMulticastChannel> multicastChannels = new HashMap<Long,DCMulticastChannel>();
	
	public Connection getAssocConnection(){
		return assocConnection;
	}
	
	public Participant getAssocParticipant(){
		return assocParticipant;
	}

	public DCMulticastChannel getDCMulticastChannel(long channelNo){
		DCMulticastChannel dcmc = multicastChannels.get(Long.valueOf(channelNo));
		if (dcmc != null)
			return dcmc;
		return listenToMulicastChannel(channelNo);
	}
	
	public DCMulticastChannel listenToMulicastChannel(long channelNo){
		DCMulticastChannel dcmc = new DCMulticastChannel(channelNo);
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
				
				if (m.getPayloadLength() >8){
					Long channelNo = Long.valueOf(Util.stuffBytesIntoLong(Util
							.getFirstBytes(m.getPayload(), 8)));
					
					getDCMulticastChannel(channelNo);
				}
			}
		}
	}

	
}
