package miceta.game.core.managers;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.illposed.osc.OSCPortOut;

import miceta.game.core.miCeta;
import miceta.game.core.osc.OSCManager;
import miceta.game.core.receiver.Block;

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

		
		
		
	public static final String TAG = TangibleBlocksManager.class.getName();
    private HashMap<Integer,Block> blocks, currentSolution;
    private ArrayList<Integer> currentSolutionValues;
    private HashMap<Integer, ArrayList<Integer>> joinedBlocks; // we save id with array of joined ids. perhaps array is not necessary because we can join mas 2 blocks to one single block...



    public TangibleBlocksManager(miCeta game){
        this.initBlocksAndSolution();
    }

    public void initBlocksAndSolution(){
        Gdx.app.log(TAG,"-----> init blocks");
        this.blocks = new HashMap<>(); // all the block not just solution to see if touched
        this.currentSolution = new HashMap<>();
        this.currentSolutionValues = new ArrayList<>();
        this.joinedBlocks = new HashMap<>();

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
    
    public void addToCurrentSolution(int blockID){
        Gdx.app.log(TAG,"----> add "+blockID);
        Block block = this.blocks.get(blockID);
        if(block!=null){
	        this.currentSolution.put(blockID,block);
	        this.currentSolutionValues.add(block.getValue());
        }else{
        	Gdx.app.error(TAG,"### Error, block "+blockID + " is not registred");
        }
    }

    public void removeFromCurrentSolution(int blockID){
        Gdx.app.log(TAG,"----> remove "+blockID);
        Block block = this.blocks.get(blockID);
        if(block!=null){
        	this.currentSolution.remove(blockID);
        	int index_value = this.currentSolutionValues.indexOf(block.getValue());
        	if(index_value>=0){
        		this.currentSolutionValues.remove(index_value);
        	}
        }else{
        	Gdx.app.error(TAG,"### Error, block "+blockID + " is not registred");
        }

    }

    public void startTouch(int blockId, OSCManager oscManager){
        Gdx.app.log(TAG,"----> start touch "+blockId);
        if (this.blocks.get(blockId) == null){
        	Gdx.app.error(TAG,"----> unexistent block touched :"+blockId);
        }else{
        	Block block = this.blocks.get(blockId);
        	block.startTouching();
        	if(this.currentSolution.isEmpty()){ //working area empty
        		boolean alreadyTouched = false; //if any of the composed blocks in any port was already touched then we do nothing
        		List<Block> composition = new ArrayList<Block>();
        		for(int port = 0;(port<2)&&(!alreadyTouched);port++){
            		int neighbour_id = block.getNeighbour(port);
        			while(neighbour_id>0 && !alreadyTouched){
            			Block neighbour = this.blocks.get(neighbour_id);
            			if(neighbour == null){
            				//weird case
                            Gdx.app.log(TAG,"WARNING "+blockId+" has the neighbour  "+neighbour_id+ " that is not registrered!");
                            break; 
            			}else{
            				alreadyTouched = alreadyTouched || neighbour.isBeingTouched();
            				composition.add(neighbour);
            				neighbour_id = neighbour.getNeighbour(port);
            			}	
            		}
        		}
        		if(composition.size()>0){
        			int n = composite(composition) + block.getValue();
        			String ids = "";
            		for(Iterator<Block> iter = composition.iterator();iter.hasNext();){
            			  ids+= iter.next().getId() + ",";
            		}
            		ids+="#";
                    Gdx.app.log(TAG,"COMPOSITION MESSAGE FOR BLOCKS --->>> "+ids);
            		oscManager.sendComposition(n,START_DELAY,CICLE_DELAY, INTERBEEP_DELAY, ids);
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
    public void stopTouch(int blockId){
    	//TODO stop composition
        Gdx.app.log(TAG,"----> stop touch "+blockId);
        if (this.blocks.get(blockId) == null){
        	Gdx.app.error(TAG,"----> unexistent block untouched :"+blockId);
        }else{
        	this.blocks.get(blockId).stopTouching();
        }
    }

    public void joinBlocks(int blockId, int joinedBlockId, int commPort){
    	//TODO check composition if any block is being touched
    	Block block = this.blocks.get(blockId);
    	block.addNeighbour(blockId, commPort);
        // we connect reference block with joined one---------
        if(joinedBlocks.containsKey(blockId)){
            // joined block already there so we do nothing
            if(!joinedBlocks.get(blockId).contains(joinedBlockId)){
                // new joined block!
                joinedBlocks.get(blockId).add(joinedBlockId);
                Gdx.app.log(TAG,"join "+blockId+" to "+joinedBlockId+ " "+joinedBlocks.toString());
            }
        }else{
            // totally new block id in joined blocks!
            ArrayList<Integer> toAdd = new ArrayList<>();
            toAdd.add(joinedBlockId);
            joinedBlocks.put(blockId,toAdd);
            Gdx.app.log(TAG,"NEW join "+blockId+" to "+joinedBlockId+" "+joinedBlocks.toString());
        }

        // we connect joined block with the reference block -----
        if(joinedBlocks.containsKey(joinedBlockId)){
            // reference block already there so we do nothing
            if(!joinedBlocks.get(joinedBlockId).contains(blockId)){
                // new reference block!
                joinedBlocks.get(joinedBlockId).add(blockId);
                Gdx.app.log(TAG,"join -- joined-- "+blockId+" to "+joinedBlockId+ " "+joinedBlocks.toString());

            }
        }
        else{
            // totally new block id in joined blocks!
            ArrayList<Integer> toAdd = new ArrayList<>();
            toAdd.add(blockId);
            joinedBlocks.put(joinedBlockId,toAdd);
            Gdx.app.log(TAG,"NEW join --joined-- "+blockId+" to "+joinedBlockId+" "+joinedBlocks.toString());

        }
    }

    public void unjoinBlocks(int blockId, int joinedBlockId, int commPort){
    	//TODO check composition if any block is being touched
    	Block block = this.blocks.get(blockId);
    	block.removeNeighbour(commPort);
        // we separate reference block from previous joined one ---
        if(joinedBlocks.containsKey(blockId)){
            // joined block has to be there
            if(joinedBlocks.get(blockId).contains(joinedBlockId)){
                // new joined block!
                joinedBlocks.get(blockId).remove((Integer) joinedBlockId);
                Gdx.app.log(TAG,"unjoin "+blockId+" to "+joinedBlockId+ " "+joinedBlocks.toString());
            }
        }

        // we separate joined block from previous reference
        if(joinedBlocks.containsKey(joinedBlockId)){
            // joined block has to be there
            if(joinedBlocks.get(joinedBlockId).contains(blockId)){
                // new joined block!
                joinedBlocks.get(joinedBlockId).remove((Integer) blockId);
                Gdx.app.log(TAG,"unjoin --joined-- "+blockId+" to "+joinedBlockId+ " "+joinedBlocks.toString());
            }
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
        Date now = new Date();
        for (Integer key : this.blocks.keySet()) {
            Block block = this.blocks.get(key);
            if(block.isBeingTouched()){
                if(Math.abs(block.getStartTouching().getTime() - now.getTime()) > 3000){ //after 3seconds of touching we stop the loop
                    Gdx.app.log(TAG," --> is being touched "+block.getId()+" since "+Math.abs(block.getStartTouching().getTime() - now.getTime()));
                    return true;
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
