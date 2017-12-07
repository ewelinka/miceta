package miceta.game.core.managers;


import com.badlogic.gdx.Gdx;
import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortIn;
import miceta.game.core.miCeta;
import miceta.game.core.receiver.Block;

import java.net.SocketException;
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
        //this.initReception();
    }

    public void initBlocksAndSolution(){
        this.blocks = new HashMap<Integer,Block>();
        this.currentSolution = new HashMap<Integer,Block>();
        this.currentSolutionValues = new ArrayList<Integer>();

    }

    public void registerBlock(int blockID, int value){
        Block block = new Block(blockID, value);
        this.blocks.put(blockID,block);
    }
    public void addToCurrentSolution(int blockID, int value){
        Block block = new Block(blockID, value);
        this.currentSolution.put(blockID,block);
        this.currentSolutionValues.add(value);
    }

    public void removeFromCurrentSolution(int blockID, int blockValue){

        this.currentSolution.remove(blockID);
        this.currentSolutionValues.remove(blockValue); //TODO right way to remove?
    }

    public void startTouch(int blockId){
        this.blocks.get(blockId).startTouching();
    }
    public void stopTouch(int blockId){
        this.blocks.get(blockId).stopTouching();

    }

    
    // we need it to play feedback (important!!)
    public ArrayList<Integer> getDetectedVals(){
        return new ArrayList<Integer>(currentSolutionValues); //return a copy
    }

    // we need it to draw block on the screen (not so important now)
    public Set<Block> getCurrentBlocks(){
        return null;
    }

    public boolean shouldStopLoop(){
        //TODO check if any block is being touched loneger then X seconds
        return false;

    }
    private int getBlockValue(int blockId){
        Gdx.app.log(TAG,"block id "+blockId);
        return blockId; //id = value TODO change!
    }


}
