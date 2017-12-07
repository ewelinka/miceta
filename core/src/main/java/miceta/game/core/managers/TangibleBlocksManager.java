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


    public TangibleBlocksManager(miCeta game){
        this.initBlocksAndSolution();
    }

    public void initBlocksAndSolution(){
        Gdx.app.log(TAG,"-----> init blocks");
        this.blocks = new HashMap<Integer,Block>(); // all the block not just solution to see if touched
        this.currentSolution = new HashMap<Integer,Block>();
        this.currentSolutionValues = new ArrayList<Integer>();

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


    // we need it to play feedback (important!!)
    public ArrayList<Integer> getDetectedVals(){
        Gdx.app.log(TAG,"----> detected vals "+this.currentSolutionValues.toString());
        return new ArrayList<Integer>(this.currentSolutionValues); //return a copy
    }

    

    // we need it to draw block on the screen (not so important now)
    public Set<Block> getCurrentBlocks(){
        return null;
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
