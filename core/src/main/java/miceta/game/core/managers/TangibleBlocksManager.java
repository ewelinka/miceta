package miceta.game.core.managers;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import miceta.game.core.miCeta;
import miceta.game.core.receiver.Block;

import java.util.*;


/**
 * Created by ewe on 12/6/17.
 */
public class TangibleBlocksManager {
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

    public void registerBlock(int blockID, int value){
        Block block = new Block(blockID, value);
        this.blocks.put(blockID,block);
    }
    public void addToCurrentSolution(int blockID, int blockValue){
        Gdx.app.log(TAG,"----> add "+blockID+ " "+blockValue);
        Block block = new Block(blockID, blockValue);
        this.currentSolution.put(blockID,block);
        this.currentSolutionValues.add(blockValue);
    }

    public void removeFromCurrentSolution(int blockID, int blockValue){
        Gdx.app.log(TAG,"----> remove "+blockID+ " "+blockValue);
        this.currentSolution.remove(blockID);
        this.currentSolutionValues.remove((Integer) blockValue);

    }

    public void startTouch(int blockId){
        Gdx.app.log(TAG,"----> start touch "+blockId);
        if (this.blocks.get(blockId) == null){
            Block block = new Block(blockId, 1);
            this.blocks.put(blockId,block);
        }
        this.blocks.get(blockId).startTouching();
    }
    public void stopTouch(int blockId){
        Gdx.app.log(TAG,"----> stop touch "+blockId);
        if (this.blocks.get(blockId) == null){
            Block block = new Block(blockId, 1);
            this.blocks.put(blockId,block);
        }

        this.blocks.get(blockId).stopTouching();
    }

    public void joinBlocks(int blockId, int joinedBlockId){
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

    public void unjoinBlocks(int blockId, int joinedBlockId){
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


}
