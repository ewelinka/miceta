package miceta.game.core.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ArrayMap;
import edu.ceta.vision.core.blocks.Block;
import edu.ceta.vision.core.topcode.TopCodeDetector;
import edu.ceta.vision.core.utils.BlocksMarkersMap;
import miceta.game.core.miCeta;
import miceta.game.core.util.AudioManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public abstract class CvBlocksManager {

    public static final String TAG = CvBlocksManager.class.getName();
    TopCodeDetector topCodeDetector;
    final miCeta game;
    boolean detectionReady;
    boolean detectionInProgress;
    final ArrayList<Set> results = new ArrayList<>();
    ArrayList<Integer> nowDetectedVals = new ArrayList<>();
    Set<Block> tempList;
    private final ArrayList<Integer> nowDetectedValsId = new ArrayList<>();
    ArrayList<Block> newDetectedCVBlocks;
    ArrayList<Integer> lastframeids;
    ArrayList<Integer> p_lastframeids;
    ArrayList<Integer> newIds;
    ArrayList<Integer> p_newIds;
    ArrayList<Integer> stableIds;
    ArrayMap<Integer,Integer> strikes;
    ArrayMap<Integer,Integer> p_strikes;
    int maxStrikes;
    int p_maxStrikes;
    ArrayMap<Integer,Integer> idToValue;
    ArrayMap<Integer,Integer> tableIdValue;
    Set<Block> currentBlocks;

//
    CvBlocksManager(miCeta game)
    {
        this.game = game;
        init();



    }

    protected abstract void init(


    );
    public abstract void updateDetected();

    public void updateTangibleBlocksDetected(Set<Block> detectedBlocks){//TODO smarichal poner este metodo en el manager de android
    	results.clear();
    	results.add(detectedBlocks);
    }
    
    public void analyseDetected(){
       //- Gdx.app.log(TAG,"analyse -> detectionReady " + detectionReady + " detectionInProgress " + detectionInProgress);
        if(detectionReady) {
            if(results.size() > 0) {
                currentBlocks = results.get(0); //here we have our set of detected blocks
                tempList = new HashSet<>(currentBlocks);
            }
            else {
               //- Gdx.app.error(TAG," very very wrong -> empty result!");
                currentBlocks = new HashSet<>();
                tempList = new HashSet<>();

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

            p_newIds = new ArrayList(newIds);
            p_compareIds(newIds , lastframeids);
            p_lastframeids = new ArrayList(stableIds);
            compareIds(p_lastframeids , p_newIds);

            detectionInProgress = false;
            detectionReady = false; // we should do next detection

        }
    }
    
    public void analyseTangibleBlocksDetected(){
        //- Gdx.app.log(TAG,"analyse -> detectionReady " + detectionReady + " detectionInProgress " + detectionInProgress);
             if(results.size() > 0) {
                 currentBlocks = results.get(0); //here we have our set of detected blocks
                 tempList = new HashSet<>(currentBlocks);
             }
             else {
                 Gdx.app.error(TAG,"tangibleBocksDetected -> empty result!");
                 currentBlocks = new HashSet<>();
                 tempList = new HashSet<>();
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

             p_newIds = new ArrayList(newIds);
             p_compareIds(newIds , lastframeids);
             p_lastframeids = new ArrayList(stableIds);
             compareIds(p_lastframeids , p_newIds);

     }
    
    


    private void compareIds(ArrayList<Integer> newBlocksIds, ArrayList<Integer> oldBlocksIds){

        for(int i = newBlocksIds.size()-1;i>=0;i--){ // we start from the end to avoid ids problems

            int newId = newBlocksIds.get(i);
            if(oldBlocksIds.contains(newId)) {
                resetStrikes(newId);
            }
        }

        newBlocksIds.removeAll(oldBlocksIds);
        for (Integer newBlocksId : newBlocksIds) { //solo puedo eliminar bloques que estan en stableIds

            //Gdx.app.log(TAG,"  ");
            checkStrikesAndDecideIfRemove(newBlocksId);
        }
    }

    private void checkStrikesAndDecideIfRemove(int id){


            if(strikes.get(id) > maxStrikes ) {
           //-      Gdx.app.log(TAG, " remove block with id: " + id + "because its gone and has max strikes!");

                if (stableIds.contains(id)) {

                    p_resetStrikes(id);
                    stableIds.remove((Integer) id);

//                    if (stableIds.size()==0){
//
//                        AudioManager.instance.setNewblock_loop(false);
//                        AudioManager.instance.playNewBlockSong_loop();
//                    }

                }
            }
             else{
                 updateStrikes(id);
              //-  Gdx.app.log(TAG," STRIKE "+ strikes.get(id) + "for:" + id);
           }
    }

    void initStrikesAndBlocksValues(){
        int [][] allMarkers = {BlocksMarkersMap.block1,BlocksMarkersMap.block2,BlocksMarkersMap.block3,BlocksMarkersMap.block4,BlocksMarkersMap.block5};

        for(int arrIdx = 0;arrIdx < allMarkers.length; arrIdx++){
            for(int i =0; i< allMarkers[arrIdx].length;i++){
                //Gdx.app.log(TAG, " putting "+allMarkers[arrIdx][i]);
                strikes.put(allMarkers[arrIdx][i],0);
                p_strikes.put(allMarkers[arrIdx][i],0);
                idToValue.put(allMarkers[arrIdx][i],arrIdx+1);
            }
        }

       // Gdx.app.log(TAG," idToVal "+idToValue);
//
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

    TopCodeDetector getTopCodeDetector(){
        return topCodeDetector;
    }

    public abstract boolean canBeUpdated();






    public boolean isDetectionReady(){
        return detectionReady;
    }

    public ArrayList<Integer> getNewDetectedVals(){

        int size = stableIds.size();
        ArrayList<Integer> result = new ArrayList();

        for (Integer stableId : stableIds) {

            result.add(tableIdValue.get(stableId));
        }
        return result;
    }

    public ArrayList<Integer> getNewDetectedIds(){return stableIds;}

    public Set<Block> getCurrentBlocks() {
        return tempList;
    }

    private void checkStrikesAndDecideIfAdd(int id){
        if((p_strikes.get(id) == p_maxStrikes )&&!(stableIds.contains(id))) {

            stableIds.add(id);
            resetStrikes(id);

            AudioManager.instance.playNewBlockSong();
//            if (AudioManager.instance.getNewblock_loop()== false) {
//                AudioManager.instance.setNewblock_loop((true));
//                AudioManager.instance.playNewBlockSong_loop();
//            }

           //- Gdx.app.log(TAG, " ADD BLOCK: " + id + "BECAUSE HAS POSITIVE STRIKES!");

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

        for (Integer newBlocksId : newBlocksIds) {
            checkStrikesAndDecideIfAdd(newBlocksId);
        }
    }


}
