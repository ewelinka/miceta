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
    private int myPort = 12345;
    private OSCPortIn oscPortIn;
    Object syncObj = new Object();


    public TangibleBlocksManager(miCeta game){
        this.initBlocksAndSolution();
        this.initReception();
    }

    public void initBlocksAndSolution(){
        this.blocks = new HashMap<Integer,Block>();
        this.currentSolution = new HashMap<Integer,Block>();
        this.currentSolutionValues = new ArrayList<Integer>();

    }

    private void registerBlock(int blockID, int value){
        Block block = new Block(blockID, value);
        this.blocks.put(blockID,block);
    }
    private void addToCurrentSolution(int blockID, int value){
        Block block = new Block(blockID, value);
        this.currentSolution.put(blockID,block);
        this.currentSolutionValues.add(value);
    }

    private void removeFromCurrentSolution(int blockID, int blockValue){

        this.currentSolution.remove(blockID);
        this.currentSolutionValues.remove(blockValue); //TODO right way to remove?
    }

    private void startTouch(int blockId){
        this.blocks.get(blockId).startTouching();
    }
    private void stopTouch(int blockId){
        this.blocks.get(blockId).stopTouching();

    }


    int getBlockValue(int blockId){
        Gdx.app.log(TAG,"block id "+blockId);
        return blockId; //id = value TODO change!
    }


    private void initReception(){

        try{
            // Connect to some IP address and port
            oscPortIn = new OSCPortIn(myPort);
            oscPortIn.addListener("/wizardOfOz", new OSCListener() {
                @Override
                public void acceptMessage(Date arg0, OSCMessage arg1) {
                    Gdx.app.log(TAG,"message received!!!");

                    for(int i =0;i< arg1.getArguments().size();i++){
                        Gdx.app.log(TAG,"arg("+i+")="+arg1.getArguments().get(i));
                    }
                    Gdx.app.log(TAG,"----------- end of message ------------");
                    synchronized (syncObj) {
                        String str1 = (String)arg1.getArguments().get(0);
                        if(str1!=null){
                            if(str1.equals("register")){
                                int action = (Integer)arg1.getArguments().get(1);
                                int block_id = (Integer)arg1.getArguments().get(2);
                                String actionName;

                                switch(action){
                                    case 0:
                                        actionName ="stop";
                                        break;
                                    case 1:
                                        actionName ="start";
                                        registerBlock(block_id, getBlockValue(block_id));
                                        break;
                                    case 2:
                                        actionName ="inArea";
                                        addToCurrentSolution(block_id,getBlockValue(block_id));
                                        break;
                                    case 3:
                                        actionName ="outOfArea";
                                        removeFromCurrentSolution(block_id, getBlockValue(block_id));
                                        break;
                                    default:
                                        actionName="xxxx";
                                        break;
                                }

                                Gdx.app.log(TAG, "register: " + actionName + " - blockID: " + block_id);

                            }else if(str1.equals("event")){
                                //event received (touched, joined, released)
                                String event_id = (String)arg1.getArguments().get(1);
                                int block_id = (Integer)arg1.getArguments().get(2);
                                Gdx.app.log(TAG, "event: " + event_id + " - blockID: " + block_id);
                                if(event_id.equals("touched")){
                                    //TODO touched
                                    startTouch(block_id);
                                }else if(event_id.equals("untouched")){
                                    //TODO untouched
                                    stopTouch(block_id);
                                }
                            }else if(str1.equals("debug")){
                                //event received (touched, joined, released)
                                String debug_name = (String)arg1.getArguments().get(1);
                                int block_id = (Integer)arg1.getArguments().get(2);
                                int debug_value = (Integer)arg1.getArguments().get(3);
                                Gdx.app.log(TAG,  "debug: " + debug_name + " = " + debug_value +" - blockID: " + block_id);
                            }else{
                                //unknown str1 command
                                Gdx.app.log(TAG,  "unknown command = " + str1);
                            }

                        }
                    }
                }
            });
            oscPortIn.startListening();
        }catch(SocketException e){
            e.printStackTrace();
        }

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

}
