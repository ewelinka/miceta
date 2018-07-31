package miceta.game.core.osc;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Date;

import miceta.game.core.managers.TangibleBlocksManager;
import miceta.game.core.receiver.Block;

import com.badlogic.gdx.Gdx;
import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortOut;

public class OSCManager implements OSCListener{

	
	/* -------- Constants and events -------*/

	//Block events
	final static int TOUCHED = 1;

	final static int TOUCH_RELEASED = 2;
	final static int TOUCH_FREE = 3;

	final static int JOINED =  4;
	final static int UNJOINED = 5;
	final static int JOIN_FREE = 6;

	final static int BLOCK_INIT = 7;
	final static int BLOCK_SHUTDOWN = 8;

	final static int COMPOSITION_FEEDBACK = 9; //not used so far

	final static int REQUEST_REGISTRER = 4;
	/*----------------------------------------*/
	
	private TangibleBlocksManager blocksManager;
	private Object syncObj = new Object();
	private OSCPortOut broadcastPortOut;
	private Clock clock;
	
	public static final String TAG = OSCManager.class.getName();

	
	public OSCManager(TangibleBlocksManager blocksManager){
		this.blocksManager = blocksManager;
		this.clock = new Clock();
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
		synchronized (syncObj) {
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
							if(!blocksManager.isBlockRegistred(block_id)){
								blocksManager.registerBlock(block_id, block_value, address, 54321); //third param ->value
							}else{
								Gdx.app.log(TAG,"el bloque ya estaba registrado, no hacemos nada");
							}
							break;
						case 2:
							actionName ="inArea";
							if(!blocksManager.isBlockRegistred(block_id)){
								request_registrer(block_id);
								Gdx.app.log(TAG,"#-inArea -# Block" + block_id + "not registrered, sending req_registrer");
							}else{
								Gdx.app.log(TAG,"Adding block " + block_id + "to current solution");
								blocksManager.addToCurrentSolution(block_id);
							}
							break;
						case 3:
							actionName ="outOfArea";
							if(!blocksManager.isBlockRegistred(block_id)){
								request_registrer(block_id);
								Gdx.app.log(TAG,"#-outOfArea -# Block" + block_id + "not registrered, sending req_registrer");
							}else{
								Gdx.app.log(TAG,"Removing block " + block_id + "from current solution");
								blocksManager.removeFromCurrentSolution(block_id);
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
					
					int neighbour_id = (event_id==JOINED || event_id==UNJOINED)?(Integer)theOscMessage.getArguments().get(3):-1;
					int commPort = (event_id==JOINED || event_id==UNJOINED)?(Integer)theOscMessage.getArguments().get(4):-1;

					switch(event_id){
						case TOUCHED:
							Gdx.app.log(TAG,blocksManager==null?"ERROR ES NULL":"NO ES NULL)");
							if(!blocksManager.isBlockRegistred(block_id)){
								request_registrer(block_id);
								Gdx.app.log(TAG,"&&& Block with ID= " + block_id +" was TOUCHED but is not registrered. SENDING REGISTRATION REQUEST! &&&&");
							}else{
								blocksManager.startTouch(block_id, this);
							}
							break;
						case TOUCH_FREE:
							if(!blocksManager.isBlockRegistred(block_id)){
								request_registrer(block_id);
								Gdx.app.log(TAG,"&&& Block with ID= " + block_id +" was UNTOUCHED but is not registrered. SENDING REGISTRATION REQUEST! &&&&");
							}else{
								blocksManager.stopTouch(block_id);
							}
							break;
						case JOINED:
							join_unjoin_blocks(block_id, neighbour_id, commPort, JOINED);
							break;
						case UNJOINED:
							neighbour_id =  (Integer)theOscMessage.getArguments().get(3);
							join_unjoin_blocks(block_id, neighbour_id, commPort, UNJOINED);
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
		}
	}
		
	public void join_unjoin_blocks(int block_id,int neighbour_id,int commPort,int event){
		boolean ok = true;
		if(!blocksManager.isBlockRegistred(block_id)){
	    	ok=false;
		Gdx.app.log(TAG,"&&& Block with ID= " + block_id +" was JOINED/UNJOINED but is not registrered. SENDING REGISTRATION REQUEST! &&&&");
	    	request_registrer(block_id);
	    }
	    if(event==JOINED && !blocksManager.isBlockRegistred(neighbour_id)){ //for unjoin we don't request registration
	    	ok=false;
	    	Gdx.app.log(TAG,"&&& Block with ID= " + neighbour_id +" was JOINED/UNJOINED but is not registrered. SENDING REGISTRATION REQUEST! &&&&");
	    	request_registrer(neighbour_id);
	    }
	    if(ok){
	       	if(event==JOINED){
	       		blocksManager.joinBlocks(block_id, neighbour_id, commPort);
	       	}else if(event==UNJOINED){
	       		blocksManager.unjoinBlocks(block_id, neighbour_id, commPort);
	       	}else{
		    	Gdx.app.error(TAG,"#### UNKOWN EVENT ####");
	       	}
	        confirm(event, block_id, commPort);
	    }
	}
		    

	public void request_registrer(int block_id){
		OSCMessage message = new OSCMessage("/block");//, collectionToSend);
		message.addArgument("registrer");
		message.addArgument(REQUEST_REGISTRER);
		message.addArgument(block_id);
		message.addArgument(REQUEST_REGISTRER);//If we remove these two extra arguments the esp fails to receive the message, i don't know why...
		message.addArgument(REQUEST_REGISTRER);
		
		//message.addArgument(1175);
		sendBroadcast(message);
	}
	
	
	
	public void confirm(int event/*joined or unjoined*/, int block_id, int port) {
    	Gdx.app.log(TAG,"->>>> SENDING CONFIRMATION ->>>> block: " + block_id + (event==JOINED?"JOINED":"UNJOINED") + "port: "+ port);
    	OSCMessage message = new OSCMessage("/block");//, collectionToSend);
		message.addArgument("confirmmm");
		message.addArgument(event);
		message.addArgument(port);
		message.addArgument(REQUEST_REGISTRER);//If we remove these two extra arguments the esp fails to receive the message, i don't know why...
		message.addArgument(REQUEST_REGISTRER);
		
		
		Block block = blocksManager.findBlock(block_id);
		try {
			block.getPortOut().send(message);
		} catch (IOException e) {
	    	Gdx.app.error(TAG,"#### ERROR SENDING CONFIRMATION ####");
			e.printStackTrace();
		}

	}

	public void sendComposition(int n, int startDelay, int cicleDelay, int interbeepDelay, String ids) {
    	Gdx.app.log(TAG,"->>>> SENDING COMPOSITION MESSAGE");
    	OSCMessage message = new OSCMessage("/block");//, collectionToSend);
    	message.addArgument("event");
    	message.addArgument("composedd");
    	message.addArgument(n);
		Long seconds = clock.getTime();
		double secdouble = seconds;
		//  println("timestamp seconds (long) = " + seconds);
		 // println("timestamp secdouble (double) = " + secdouble);
		message.addArgument(secdouble);
    	Gdx.app.log(TAG,"@@@@ TIMESTAMP:  " + secdouble);
		message.addArgument(startDelay); //start_delay in milliseconds
		message.addArgument(cicleDelay); //cycle_delay in milliseconds
		message.addArgument(interbeepDelay); //beep distance in milliseconds
		//message.addArgument(ids);
		message.addArgument("123,222,666,999");
		sendBroadcast(message);
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
}
