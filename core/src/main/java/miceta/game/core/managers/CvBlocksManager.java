package miceta.game.core.managers;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ArrayMap;
import edu.ceta.vision.core.utils.BlocksMarkersMap;
import edu.ceta.vision.android.topcode.TopCodeDetectorAndroid;
import edu.ceta.vision.core.blocks.Block;
import edu.ceta.vision.core.topcode.TopCodeDetector;
import edu.ceta.vision.core.topcode.TopCodeDetectorDesktop;
import miceta.game.core.miCeta;
import miceta.game.core.util.AudioManager;
import miceta.game.core.util.Constants;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;


import java.util.*;


public abstract class CvBlocksManager {

    public static final String TAG = CvBlocksManager.class.getName();
    public TopCodeDetector topCodeDetector;
    public miCeta game;
    public boolean detectionReady, detectionInProgress;
    public ArrayList<Set> results = new ArrayList<Set>();
    public ArrayList<Integer> nowDetectedVals = new ArrayList<Integer>();
    public Set<Block> tempList;
    public ArrayList<Integer> nowDetectedValsId = new ArrayList<Integer>();
    public ArrayList<Block> newDetectedCVBlocks;
    public ArrayList<Integer> lastframeids, p_lastframeids;
    public ArrayList<Integer> newIds, p_newIds, stableIds;
    public ArrayMap<Integer,Integer> strikes;
    public ArrayMap<Integer,Integer> p_strikes;
    public int maxStrikes;
    public int p_maxStrikes;
    public ArrayMap<Integer,Integer> idToValue;
    public ArrayMap<Integer,Integer> tableIdValue;
    public Set<Block> currentBlocks;


    public CvBlocksManager(miCeta game, Stage stage)
    {
        this.game = game;
        init();
    }

    public abstract void init();
    public abstract void updateDetected();

    public void analyseDetected(){
        Gdx.app.log(TAG,"analyse -> detectionReady " + detectionReady + " detectionInProgress " + detectionInProgress);
        if(detectionReady) {
            if(results.size() > 0) {
                currentBlocks = results.get(0); //here we have our set of detected blocks
                tempList = new HashSet<Block>(currentBlocks);
            }
            else {
                Gdx.app.error(TAG," very very wrong -> empty result!");
                currentBlocks =  new HashSet<Block>();
                tempList = new HashSet<Block>();
            }


            lastframeids = new ArrayList(nowDetectedValsId);
            newIds.clear();
            nowDetectedValsId.clear();
            newDetectedCVBlocks.clear();
            nowDetectedVals.clear();


            nowDetectedValsId.clear();
            for (Block i : currentBlocks) {

                newIds.add(i.getId());
                nowDetectedValsId.add(i.getId());
                nowDetectedVals.add(i.getValue());
                newDetectedCVBlocks.add(i);
                tableIdValue.put(i.getId(), i.getValue());

            }

//            Gdx.app.log(TAG, "now detected vals "+ Arrays.toString(nowDetectedVals.toArray()));
//            Gdx.app.log(TAG, "now detected ids "+ Arrays.toString(nowDetectedValsId.toArray()));
//            Gdx.app.log(TAG, "last frame ids "+ Arrays.toString(lastframeids.toArray()));
//            Gdx.app.log(TAG, "stable ids "+ Arrays.toString(stableIds.toArray()));

            p_newIds = new ArrayList(newIds);
            p_compareIds(newIds , lastframeids);
            p_lastframeids = new ArrayList(stableIds);
            compareIds(p_lastframeids , p_newIds);

            detectionInProgress = false;
            detectionReady = false; // we should do next detection

        }
    }


    private void compareIds(ArrayList<Integer> newBlocksIds, ArrayList<Integer> oldBlocksIds){

        for(int i = newBlocksIds.size()-1;i>=0;i--){ // we start from the end to avoid ids problems

            int newId = newBlocksIds.get(i);
            if(oldBlocksIds.contains(newId)) {
                resetStrikes(newId);
                boolean shouldBeUpdated = false;
            }
        }

        newBlocksIds.removeAll(oldBlocksIds);
        for(int i=0;i<newBlocksIds.size();i++){ //solo puedo eliminar bloques que estan en stableIds

            //Gdx.app.log(TAG,"  ");
            checkStrikesAndDecideIfRemove(newBlocksIds.get(i));
        }
    }

    private void checkStrikesAndDecideIfRemove(int id){


            if(strikes.get(id) > maxStrikes ) {
                 Gdx.app.log(TAG, " remove block with id: " + id + "because its gone and has max strikes!");

                if (stableIds.contains(id)) {

                    p_resetStrikes(id);
                    stableIds.remove((Integer) id);
                }
            }
             else{
                 updateStrikes(id);
                Gdx.app.log(TAG," STRIKE "+ strikes.get(id) + "for:" + id);
           }
    }

//lo pase de privado a publico, revisar
    public void initStrikesAndBlocksValues(){
        int [][] allMarkers = {BlocksMarkersMap.block1,BlocksMarkersMap.block2,BlocksMarkersMap.block3,BlocksMarkersMap.block4,BlocksMarkersMap.block5};

        for(int arrIdx = 0;arrIdx < allMarkers.length; arrIdx++){
            for(int i =0; i< allMarkers[arrIdx].length;i++){
                //Gdx.app.log(TAG, " putting "+allMarkers[arrIdx][i]);
                strikes.put(allMarkers[arrIdx][i],0);
                p_strikes.put(allMarkers[arrIdx][i],0);
                idToValue.put(allMarkers[arrIdx][i],arrIdx+1);
            }
        }

        Gdx.app.log(TAG," idToVal "+idToValue);

    }



    private void updateStrikes(int id){
        strikes.put(id,strikes.get(id)+1); // add one!
    }

    private void resetStrikes(int id){
        strikes.put(id,0); // reset strikes!
    }

    private void p_updateStrikes(int id){
        p_strikes.put(id,p_strikes.get(id)+1); // add one!
    }

    private void p_resetStrikes(int id){
        p_strikes.put(id,0); // reset strikes!
    }

    public TopCodeDetector getTopCodeDetector(){
        return topCodeDetector;
    }

    public abstract boolean canBeUpdated();






    public boolean isDetectionReady(){
        return detectionReady;
    }

    // the posta is here
    public ArrayList<Integer> getNewDetectedVals(){

        int size = stableIds.size();
        ArrayList<Integer> result = new ArrayList();

        for (int i = 0; i < size; i++ ){

            result.add(tableIdValue.get(stableIds.get(i)));
        }
        return result;
    }

    public Set<Block> getCurrentBlocks() {
        return tempList;
    }

    private void checkStrikesAndDecideIfAdd(int id){

        //Gdx.app.log(TAG,  p_maxStrikes + " Entro aca: "  + id + "p_strikes: " + p_strikes.get(id));

        if((p_strikes.get(id) == p_maxStrikes )&&!(stableIds.contains(id))) {

            stableIds.add(id);
            resetStrikes(id);

          //  AudioManager.instance.playNewBlockSong();
            Gdx.app.log(TAG, " ADD BLOCK: " + id + "BECAUSE HAS POSITIVE STRIKES!");

        }

    }


    private void p_compareIds(ArrayList<Integer> newBlocksIds, ArrayList<Integer> oldBlocksIds){
        for(int i = newBlocksIds.size()-1;i>=0;i--){ // we start from the end to avoid ids problems

            int newId = newBlocksIds.get(i);

            if(oldBlocksIds.contains(newId)) {
                p_updateStrikes(newId);
            }
            else{
               p_resetStrikes(newId);
            }
        }

        for(int i=0;i<newBlocksIds.size();i++){
            checkStrikesAndDecideIfAdd(newBlocksIds.get(i));
        }
    }


}
