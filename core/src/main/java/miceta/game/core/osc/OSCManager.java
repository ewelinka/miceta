package miceta.game.core.osc;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import miceta.game.core.managers.TangibleBlocksManager;
import miceta.game.core.receiver.Block;
import miceta.game.core.util.Constants;

import com.badlogic.gdx.Gdx;
import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortOut;



public class OSCManager implements OSCListener{
	
	private TangibleBlocksManager blocksManager;
	private Object syncObj = new Object();
	private OSCPortOut broadcastPortOut;
	private Clock clock;
	private boolean config_blocks_values;
	private boolean silentFeedbackMode;
	
	public static final String TAG = OSCManager.class.getName();

	
	public OSCManager(TangibleBlocksManager blocksManager, boolean silentFeedbackMode){
		this.blocksManager = blocksManager;
		this.clock = new Clock();
		this.config_blocks_values = Constants.CONFIGURE_BLOCK_VALUES;
		this.silentFeedbackMode = silentFeedbackMode;
		Gdx.app.log(TAG,"----------- Clock Time: " + clock.getTime());
		try {
			this.broadcastPortOut = new OSCPortOut( InetAddress.getByName("192.168.43.255"), 54321);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void acceptMessage(Date arg0, OSCMessage theOscMessage) {
		Gdx.app.log(TAG,"message received!!!");

		for(int i =0;i< theOscMessage.getArguments().size();i++){
			Gdx.app.log(TAG,"arg("+i+")="+theOscMessage.getArguments().get(i));
		}
		Gdx.app.log(TAG,"----------- end of message ------------");
		//synchronized (syncObj) {
			String str1 = (String)theOscMessage.getArguments().get(0);
			if(str1!=null){
				if(str1.equals("registrer")){
					int action = (Integer)theOscMessage.getArguments().get(1);
					int block_id = (Integer)theOscMessage.getArguments().get(2);
					int block_value = (Integer)theOscMessage.getArguments().get(3);
					String actionName;

					switch(action){
						case 0:
							actionName ="stop";
							break;
						case 1:
							actionName ="start";
							String address = (String)theOscMessage.getArguments().get(4);
							if(config_blocks_values){
								block_value = Constants.getBlockValue(block_id);
							}
							if(!blocksManager.isBlockRegistred(block_id)){
								blocksManager.registerBlock(block_id, block_value, address, 54321); //third param ->value
							}else{
								Gdx.app.log(TAG,"el bloque ya estaba registrado, no hacemos nada");
							}
							if(!(blocksManager.getDetectedVals()==null || blocksManager.getDetectedVals().isEmpty())){
						        this.sendSilence(Constants.START_SILENCE,Constants.INFINITE_SILENCE,null);
							}
							if(config_blocks_values){
								configBlock(block_id, block_value);
							}
							break;
						case 2:
							actionName ="inArea";
							if(!blocksManager.isBlockRegistred(block_id)){
								request_registrer(block_id);
								Gdx.app.log(TAG,"#-inArea -# Block" + block_id + "not registrered, sending req_registrer");
							}else{
								Gdx.app.log(TAG,"Adding block " + block_id + "to current solution");
								blocksManager.addToCurrentSolution(block_id, this);
							}
							break;
						case 3:
							actionName ="outOfArea";
							if(!blocksManager.isBlockRegistred(block_id)){
								request_registrer(block_id);
								Gdx.app.log(TAG,"#-outOfArea -# Block" + block_id + "not registrered, sending req_registrer");
							}else{
								Gdx.app.log(TAG,"Removing block " + block_id + "from current solution");
								blocksManager.removeFromCurrentSolution(block_id, this);
							}
							break;
						default:
							actionName="xxxx";
							Gdx.app.error(TAG,"UNKNOWN REGISTER RECEIVED");
							break;
					}

					Gdx.app.log(TAG, "register: " + actionName + " - blockID: " + block_id);

				}else if(str1.equals("event")){
					//event received (touched, joined, released)
					int event_id = (Integer)theOscMessage.getArguments().get(1);
					int block_id = (Integer)theOscMessage.getArguments().get(2);
					Gdx.app.log(TAG, "event: " + event_id + " - blockID: " + block_id);
					
					int neighbour_id = (event_id==Constants.JOINED || event_id==Constants.UNJOINED)?(Integer)theOscMessage.getArguments().get(3):-1;
					int commPort = (event_id==Constants.JOINED || event_id==Constants.UNJOINED)?(Integer)theOscMessage.getArguments().get(4):-1;

					switch(event_id){
						case Constants.TOUCHED:
							Gdx.app.log(TAG,blocksManager==null?"ERROR ES NULL":"NO ES NULL)");
							if(!blocksManager.isBlockRegistred(block_id)){
								request_registrer(block_id);
								Gdx.app.log(TAG,"&&& Block with ID= " + block_id +" was TOUCHED but is not registrered. SENDING REGISTRATION REQUEST! &&&&");
							}else{
								blocksManager.startTouch(block_id, this);
							}
							if(!(blocksManager.getDetectedVals()==null || blocksManager.getDetectedVals().isEmpty())){
						        this.sendSilence(Constants.START_SILENCE,Constants.INFINITE_SILENCE,null);
							}
							break;
						case Constants.TOUCH_FREE:
							if(!blocksManager.isBlockRegistred(block_id)){
								request_registrer(block_id);
								Gdx.app.log(TAG,"&&& Block with ID= " + block_id +" was UNTOUCHED but is not registrered. SENDING REGISTRATION REQUEST! &&&&");
							}else{
								blocksManager.stopTouch(block_id,this);
							}
							break;
						case Constants.JOINED:
							join_unjoin_blocks(block_id, neighbour_id, commPort, Constants.JOINED);
							break;
						case Constants.UNJOINED:
							neighbour_id =  (Integer)theOscMessage.getArguments().get(3);
							join_unjoin_blocks(block_id, neighbour_id, commPort, Constants.UNJOINED);
							break;
						default:
							Gdx.app.error(TAG,"UNKNOWN EVENT RECEIVED");
							break;
					}



				}else if(str1.equals("debug")){
					//event received (touched, joined, released)
					String debug_name = (String)theOscMessage.getArguments().get(1);
					int block_id = (Integer)theOscMessage.getArguments().get(2);
					int debug_value = (Integer)theOscMessage.getArguments().get(3);
					Gdx.app.log(TAG,  "debug: " + debug_name + " = " + debug_value +" - blockID: " + block_id);
				}else{
					//unknown str1 command
					Gdx.app.log(TAG,  "unknown command = " + str1);
				}
			}
		//}
	}
		
	@Deprecated /**commPort is not used anymore*/
	public void join_unjoin_blocks(int block_id,int neighbour_id,int commPort,int event){
		if(!blocksManager.isBlockRegistred(block_id)){
	    	Gdx.app.log(TAG,"&&& Block with ID= " + block_id +" was JOINED/UNJOINED but is not registrered. SENDING REGISTRATION REQUEST! &&&&");
	    	request_registrer(block_id);
	    }else {
	       	if(event==Constants.JOINED){
	       		blocksManager.joinBlocks(block_id, neighbour_id,this);
	       	}else if(event==Constants.UNJOINED){
	       		blocksManager.unjoinBlocks(block_id, neighbour_id,this);
	       	}else{
		    	Gdx.app.error(TAG,"#### UNKOWN EVENT ####");
	       	}
	        confirm(event, block_id);
	    }
	}
	
	public void join_unjoin_blocks(int block_id,int neighbour_id,int event){
		if(!blocksManager.isBlockRegistred(block_id)){
	    	Gdx.app.log(TAG,"&&& Block with ID= " + block_id +" was JOINED/UNJOINED but is not registrered. SENDING REGISTRATION REQUEST! &&&&");
	    	request_registrer(block_id);
	    }else {
	       	if(event==Constants.JOINED){
	       		blocksManager.joinBlocks(block_id, neighbour_id,this);
	       	}else if(event==Constants.UNJOINED){
	       		blocksManager.unjoinBlocks(block_id, neighbour_id, this);
	       	}else{
		    	Gdx.app.error(TAG,"#### UNKOWN EVENT ####");
	       	}
	        confirm(event, block_id);
	    }
	}
		    

	
	public boolean isSilentFeedbackMode() {
		return silentFeedbackMode;
	}

	public void setSilentFeedbackMode(boolean silentFeedbackMode) {
		this.silentFeedbackMode = silentFeedbackMode;
	}

	public void send_silent_mode_feedback(){
	   	Gdx.app.log(TAG,"->>>> SENDING SILENT FEEDBACK ="+ (this.silentFeedbackMode?"YES":"NO") +"<<<<<<-");
    	OSCMessage message = new OSCMessage("/block");//, collectionToSend);
		message.addArgument("feedback");
		message.addArgument(this.silentFeedbackMode?1:0);
		sendBroadcast(message);
	}
	
	public void request_registrer(int block_id){
		OSCMessage message = new OSCMessage("/block");//, collectionToSend);
		message.addArgument("registrer");
		message.addArgument(Constants.REQUEST_REGISTRER);
		message.addArgument(block_id);
		message.addArgument(Constants.REQUEST_REGISTRER);//If we remove these two extra arguments the esp fails to receive the message, i don't know why...
		message.addArgument(Constants.REQUEST_REGISTRER);
		
		//message.addArgument(1175);
		sendBroadcast(message);
	}
	
	
	//FIXME remove port (-1) parameter and update arduino code
	public void confirm(int event/*joined or unjoined*/, int block_id) {
    	Gdx.app.log(TAG,"->>>> SENDING CONFIRMATION ->>>> block: " + block_id + (event==Constants.JOINED?"JOINED":"UNJOINED") );
    	OSCMessage message = new OSCMessage("/block");//, collectionToSend);
		message.addArgument("confirmmm");
		message.addArgument(event);
		message.addArgument(-1); //port not used anymore
		message.addArgument(Constants.REQUEST_REGISTRER);//If we remove these two extra arguments the esp fails to receive the message, i don't know why...
		message.addArgument(Constants.REQUEST_REGISTRER);
		
		
		Block block = blocksManager.findBlock(block_id);
		try {
			if(block.getPortOut()!=null){
				block.getPortOut().send(message);
			}
		} catch (IOException e) {
	    	Gdx.app.error(TAG,"#### ERROR SENDING CONFIRMATION ####");
			e.printStackTrace();
		}

	}
	
	public void configBlock(int block_id, int value) {
    	Gdx.app.log(TAG,"->>>> SENDING CONFIG MESSAGE ->>>> block: " + block_id + "SET VALUE= "+value);
    	OSCMessage message = new OSCMessage("/block");//, collectionToSend);
		message.addArgument("configggg");
		message.addArgument(block_id);
		message.addArgument(value);
		message.addArgument(this.silentFeedbackMode?1:0);
		message.addArgument(Constants.REQUEST_REGISTRER);

		Block block = blocksManager.findBlock(block_id);
		try {
			try {//FIXME request confirmation instead of repeating the message
				if(block.getPortOut()!=null){
					block.getPortOut().send(message);
			    	Gdx.app.log(TAG,"->>>> MSG 1");
					Thread.sleep(250);
					block.getPortOut().send(message);
					Gdx.app.log(TAG,"->>>> MSG 2");
					Thread.sleep(250);
					Gdx.app.log(TAG,"->>>> MSG 3");
					block.getPortOut().send(message);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
	    	Gdx.app.error(TAG,"#### ERROR SENDING CONFIRMATION ####");
			e.printStackTrace();
		}

	}

	public void resetAllBlocks() {
    	Gdx.app.log(TAG,"->>>> SENDING RESET ALL <<<<<<-");
    	OSCMessage message = new OSCMessage("/block");//, collectionToSend);
		message.addArgument("reset");
		message.addArgument(Constants.REQUEST_REGISTRER);//If we remove these two extra arguments the esp fails to receive the message, i don't know why...
		message.addArgument(Constants.REQUEST_REGISTRER);
		message.addArgument(Constants.REQUEST_REGISTRER);//If we remove these two extra arguments the esp fails to receive the message, i don't know why...
		message.addArgument(Constants.REQUEST_REGISTRER);
		
		sendBroadcast(message);
	}
	
	
	public long sendComposition(int n, int startDelay, int cicleDelay, int interbeepDelay, String ids, long time) {
    	Gdx.app.log(TAG,"->>>> SENDING COMPOSITION MESSAGE");
    	OSCMessage message = new OSCMessage("/block");//, collectionToSend);
    	message.addArgument("event");
    	message.addArgument("composedd");
    	message.addArgument(n);
		Long seconds = time==-1?clock.getTime():time;
		double secdouble = seconds;
		//  println("timestamp seconds (long) = " + seconds);
		 // println("timestamp secdouble (double) = " + secdouble);
		message.addArgument(secdouble);
    	Gdx.app.log(TAG,"@@@@ TIMESTAMP:  " + secdouble);
		message.addArgument(startDelay); //start_delay in milliseconds
		message.addArgument(cicleDelay); //cycle_delay in milliseconds
		message.addArgument(interbeepDelay); //beep distance in milliseconds
		message.addArgument(ids);
		sendBroadcast(message);
		return seconds;
	
	}
	
	
	public long sendOneTimeComposition(int n, int startDelay, int cicleDelay, int interbeepDelay, String ids, long time) {
    	Gdx.app.log(TAG,"->>>> SENDING COMPOSITION MESSAGE");
    	OSCMessage message = new OSCMessage("/block");//, collectionToSend);
    	message.addArgument("event");
    	message.addArgument("oneTimeC");
    	message.addArgument(n);
		Long seconds = time==-1?clock.getTime():time;
		double secdouble = seconds;
		//  println("timestamp seconds (long) = " + seconds);
		 // println("timestamp secdouble (double) = " + secdouble);
		message.addArgument(secdouble);
    	Gdx.app.log(TAG,"@@@@ TIMESTAMP:  " + secdouble);
		message.addArgument(startDelay); //start_delay in milliseconds
		message.addArgument(cicleDelay); //cycle_delay in milliseconds
		message.addArgument(interbeepDelay); //beep distance in milliseconds
		message.addArgument(ids);
		sendBroadcast(message);
		return seconds;
	
	}
	
	
	
	
	public void sendBroadcast(OSCMessage message){
		if(this.broadcastPortOut!=null){
			try {
				this.broadcastPortOut.send(message);
			} catch (IOException e) {
		    	Gdx.app.error(TAG,"#### ERROR SENDING REQUEST REGISTRER ####");
				e.printStackTrace();
			}
		}
	}

	public void sendStopComposition(String ids) {//TODO test it, is block side coded?
    	Gdx.app.log(TAG,"->>>> SENDING STOP COMPOSITION MESSAGE");
    	OSCMessage message = new OSCMessage("/block");//, collectionToSend);
    	message.addArgument("event");
    	message.addArgument("uncomposed");
    	message.addArgument(ids);
    	
    	//FIXME ask for confirmation rather than repeating the message
    	sendBroadcast(message);
    	sendBroadcast(message);
    	sendBroadcast(message);
	}

	public void sendSilence(int event /*start or stop*/, int duration, Collection<Block> blocks) { //TODO test
		Gdx.app.log(TAG,"->>>> SENDING SILENCE MESSAGE: " + (event==Constants.START_SILENCE?" START":" STOP"));
		OSCMessage message = new OSCMessage("/block");
    	message.addArgument("silenced");
    	message.addArgument(event);
    	message.addArgument(duration);
    	message.addArgument(Constants.REQUEST_REGISTRER); //FIXME las two arguments just to get it working 
    	message.addArgument(Constants.REQUEST_REGISTRER);
    	

    	
		if(blocks==null || blocks.isEmpty()){ //broadcast message
	    	//FIXME ask for confirmation rather than repeating the message
			sendBroadcast(message);
	    	sendBroadcast(message);
	    	sendBroadcast(message);
		}else{									//individual silence, not used so far
			for(int i=0;i<3;i++){ //FIXME ask confirmation rather than repeating the message
				for(Iterator<Block> iter = blocks.iterator();iter.hasNext();){
					Block block = iter.next();
					try {
						if(block.getPortOut()!=null){
							block.getPortOut().send(message);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
