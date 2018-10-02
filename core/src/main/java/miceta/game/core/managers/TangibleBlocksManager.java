package miceta.game.core.managers;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.illposed.osc.OSCPortOut;

import miceta.game.core.miCeta;
import miceta.game.core.osc.OSCManager;
import miceta.game.core.receiver.Block;
import miceta.game.core.util.CompositionData;
import miceta.game.core.util.Constants;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.*;


/**
 * Created by ewe on 12/6/17.
 */
public class TangibleBlocksManager {
   
	
	//COMPOSITION PARAMETERS
		final static int START_DELAY = 1000;

		final static int CICLE_DELAY = 1500;
		
		final static int INTERBEEP_DELAY = 500;

		
		
	private ArrayList<CompositionData> currentCompositions;
	public static final String TAG = TangibleBlocksManager.class.getName();
    private HashMap<Integer,Block> blocks, currentSolution;
    private ArrayList<Integer> currentSolutionValues;
    private HashMap<Integer, ArrayList<Integer>> joinedBlocks; // we save id with array of joined ids. perhaps array is not necessary because we can join mas 2 blocks to one single block...

	private boolean workingAreaStrongerThanTouch;



    public TangibleBlocksManager(miCeta game){
        this.initBlocksAndSolution();
        workingAreaStrongerThanTouch = true; //if true, then tablet feedback has priority over blocks' feedback
        //TODO workingAreaStrongerThanTouch as a configurable parameter in the app
    }

    public void initBlocksAndSolution(){
        Gdx.app.log(TAG,"-----> init blocks");
        this.blocks = new HashMap<>(); // all the block not just solution to see if touched
        this.currentSolution = new HashMap<>();
        this.currentSolutionValues = new ArrayList<>();
        this.joinedBlocks = new HashMap<>();
        this.currentCompositions = new ArrayList<CompositionData>();

    }

    public void registerBlock(int blockID, int value, String address, int port){
    	if(this.blocks.get(blockID)==null){//check if it doesnt exist first
    		OSCPortOut out;
			try {
				out = new OSCPortOut(InetAddress.getByName(address), port);
	    		Block block = new Block(blockID, value, out);
	            this.blocks.put(blockID,block);	
	            Gdx.app.log(TAG,"----> Block "+blockID+ " registrered");
			} catch (SocketException e) {
				e.printStackTrace();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} 
    	}
        
    }
    
    public void addToCurrentSolution(int blockID, OSCManager oscManager){
        Gdx.app.log(TAG,"----> add "+blockID);
        Block block = this.blocks.get(blockID);
        if(block!=null && (!this.currentSolution.containsKey(blockID))){
	        this.currentSolution.put(blockID,block);
	        this.currentSolutionValues.add(block.getValue());
	        oscManager.sendSilence(Constants.START_SILENCE,Constants.INFINITE_SILENCE,null);
        }else{
        	Gdx.app.error(TAG,"### Error, block "+blockID + " is not registred");
        }
    }

    public void removeFromCurrentSolution(int blockID, OSCManager oscManager){
        Gdx.app.log(TAG,"----> remove "+blockID);
        Block block = this.blocks.get(blockID);
        if(block!=null && this.currentSolution.containsKey(blockID)){
        	this.currentSolution.remove(blockID);
        	int index_value = this.currentSolutionValues.indexOf(block.getValue());
        	if(index_value>=0){
        		this.currentSolutionValues.remove(index_value);
        	}
        }else{
        	Gdx.app.error(TAG,"### Error, block "+blockID + " is not registred");
        }
        if(this.currentSolution.isEmpty()){
        	oscManager.sendSilence(Constants.STOP_SILENCE,Constants.INFINITE_SILENCE,null);
        }

    }

    //TEST the function when blocks are joined
    public void startTouch(int blockId, OSCManager oscManager){
        Gdx.app.log(TAG,"----> start touch "+blockId);
        if (this.blocks.get(blockId) == null){
        	Gdx.app.error(TAG,"----> unexistent block touched :"+blockId);
        }else{
        	Block block = this.blocks.get(blockId);
        	block.startTouching();
        	if(this.currentSolution.isEmpty()){ //working area empty
        		CompositionData compositionData = getCompositionData(block);        		
        		if(!compositionData.hasNeighbourTouched && compositionData.composed_n > block.getValue()){
                    Gdx.app.log(TAG,"START COMPOSITION MESSAGE FOR BLOCKS --->>> "+compositionData.oscIDs);
                    if(this.currentCompositions.contains(compositionData)){	//resend the composition but without changing the sentTime, otherwise it could cause and desynchronization
                    	CompositionData existingComp = this.currentCompositions.get(this.currentCompositions.indexOf(compositionData));
                		oscManager.sendComposition(existingComp.composed_n,START_DELAY,CICLE_DELAY, INTERBEEP_DELAY, existingComp.oscIDs,existingComp.sentTime);
                    }else{
                    	compositionData.sentTime = oscManager.sendComposition(compositionData.composed_n,START_DELAY,CICLE_DELAY, INTERBEEP_DELAY, compositionData.oscIDs,-1);
                    	this.currentCompositions.add(compositionData);
                    }
        		}
        	}
        }
    }
    
    public int composite(List<Block> blocks){
    	int res = 0;
    	if(blocks!=null){
    		for(Iterator<Block> iter = blocks.iterator();iter.hasNext();){
    			res += iter.next().getValue();
    		}
    	}
    	return res;
    }
    public void stopTouch(int blockId, OSCManager oscManager){
    	//TODO stop composition confirmation?
    	Block block = this.blocks.get(blockId);
        Gdx.app.log(TAG,"----> stop touch "+blockId);
        if (this.blocks.get(blockId) == null){
        	Gdx.app.error(TAG,"----> unexistent block untouched :"+blockId);
        }else{
        	this.blocks.get(blockId).stopTouching();
        	CompositionData data= getCompositionData(block);
        	if(!data.hasNeighbourTouched && hasAnyNeighbour(block)){
        		//stop composition feedback
            	Gdx.app.log(TAG,"STOP COMPOSITION MESSAGE FOR BLOCKS --->>> "+data.oscIDs);
        		oscManager.sendStopComposition(data.oscIDs);
        	}
        }
    	

    }

//	old version for 2 ports	
//    private CompositionData getCompositionData(Block block) {
//		CompositionData data = new CompositionData();
//		for(int port = 0;port<2;port++){
//			int neighbour_id = block.getNeighbour(port);        			
//			int current_block = block.getId();
//			while(current_block!=neighbour_id && neighbour_id>0 ){
//				Block neighbour = this.blocks.get(neighbour_id);
//				if(neighbour == null){
//					//weird case
//		            Gdx.app.log(TAG,"WARNING "+block.getId()+" has the neighbour  "+neighbour_id+ " that is not registrered!");
//		            break; 
//				}else{
//    				data.composed_n+=neighbour.getValue();
//					data.oscIDs+=neighbour.getId()+",";
//					data.hasNeighbourTouched = data.hasNeighbourTouched || neighbour.isBeingTouched();
//					neighbour_id = neighbour.getNeighbour(port);
//					if(neighbour_id==current_block){
//						neighbour_id = neighbour.getNeighbour((port+1)%2); //check the other port
//					}
//					current_block = neighbour.getId();
//
//		        	Gdx.app.log(TAG,"neighbour_id = "+neighbour_id);
//				}	
//			}
//		}
//		data.oscIDs+=block.getId()+"#";
//		return data;
//	}
    
    private CompositionData getCompositionData(Block block) {
		CompositionData data = new CompositionData();
		for(Iterator<Integer> iter = block.getNeighbours().iterator();iter.hasNext();){
			int neighbour_id = iter.next();   
			Block neighbour = this.blocks.get(neighbour_id);
			data.composed_n+=neighbour.getValue();
			data.oscIDs+=neighbour.getId()+",";
			data.hasNeighbourTouched = data.hasNeighbourTouched || neighbour.isBeingTouched();
		}
		data.oscIDs+=block.getId()+"#";
		data.composed_n+=block.getValue();
		return data;
	}

    private boolean hasAnyNeighbour(Block block) {
		return block!=null && block.getNeighbours()!=null && block.getNeighbours().size()>0;
	}

    /*check the block or any neighbor is being touched and send composition message*/
    public void compositeAndSend(Block block, OSCManager oscManager){
		CompositionData compData = getCompositionData(block);
        if(!compData.oscIDs.isEmpty() && compData.composed_n>block.getValue()){
        	if(block.isBeingTouched() || compData.hasNeighbourTouched){        		
        		if(this.currentCompositions.contains(compData)){	//resend the composition but without changing the sentTime, otherwise it could cause and desynchronization
                	CompositionData existingComp = this.currentCompositions.get(this.currentCompositions.indexOf(compData));
            		oscManager.sendComposition(existingComp.composed_n,START_DELAY,CICLE_DELAY, INTERBEEP_DELAY, existingComp.oscIDs,existingComp.sentTime);
                }else{
                	compData.sentTime = oscManager.sendComposition(compData.composed_n,START_DELAY,CICLE_DELAY, INTERBEEP_DELAY, compData.oscIDs,-1);
                	this.currentCompositions.add(compData);
                }
        	}else{
            	Gdx.app.log(TAG,"STOP COMPOSITION MESSAGE FOR BLOCKS --->>> "+compData.oscIDs);
        		oscManager.sendStopComposition(compData.oscIDs);
        	}
        }
    	
    }
    
	public void joinBlocks(int blockId, int joinedBlockId,  OSCManager oscManager){
		Gdx.app.log(TAG,"JOINING : " + blockId + "-" + joinedBlockId);
    	Block block = this.blocks.get(blockId);
    	block.addNeighbour(joinedBlockId);
    	if(this.blocks.get(joinedBlockId)==null){
    		oscManager.request_registrer(joinedBlockId);
    	}else{
    		compositeAndSend(block, oscManager);
    		Gdx.app.log(TAG,"salgo joining");
    	}

    }

    public void unjoinBlocks(int blockId, int joinedBlockId, OSCManager oscManager){
        Gdx.app.log(TAG,"UNJOINING : " + blockId + "-" + joinedBlockId);
    	Block block = this.blocks.get(blockId);
    	if(block!=null){
	    	block.removeNeighbour(joinedBlockId);
	    	compositeAndSend(block, oscManager);
    	}
    }


    // we need it to play feedback (important!!)
    public ArrayList<Integer> getDetectedVals(){
        Gdx.app.log(TAG,"----> detected vals "+this.currentSolutionValues.toString());
        return new ArrayList<Integer>(this.currentSolutionValues); //return a copy
    }

    

    // we need it to draw block on the screen (not so important now)
    public ArrayList<Integer> getCurrentBlocksValues(){
        return currentSolutionValues;
    }

    public boolean shouldStopLoop(){
    	if(!workingAreaStrongerThanTouch){
	        Date now = new Date();
	        for (Integer key : this.blocks.keySet()) {
	            Block block = this.blocks.get(key);
	            if(block.isBeingTouched()){
	                if(Math.abs(block.getStartTouching().getTime() - now.getTime()) > 3000){ //after 3seconds of touching we stop the loop
	                    //Gdx.app.log(TAG," --> is being touched "+block.getId()+" since "+Math.abs(block.getStartTouching().getTime() - now.getTime()));
	                    return true;
	                }
	
	            }
	        }
    	}
        return false;

    }
    
    public boolean isBlockRegistred(int blockID){
    	return this.blocks.get(blockID)!=null;
    }

    public Block findBlock(int block_id){
    	return this.blocks.get(block_id);
    }

}
