package miceta.game.core.managers;


import com.badlogic.gdx.Gdx;
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
        Gdx.app.log(TAG,"----> todos locos add"+this.currentSolutionValues.toString()+" len "+this.currentSolutionValues.size());
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

        for(int i =this.blocks.size()-1;i>=0;i--){
            Block block = this.blocks.get(i);
            if(block.isBeingTouched() && Math.abs(block.getStartTouching().getTime() - now.getTime()) > 3000){
                return true;
            }
        }
        return false;

    }


}
