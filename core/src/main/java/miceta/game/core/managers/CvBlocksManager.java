package miceta.game.core.managers;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ArrayMap;

import edu.ceta.vision.core.utils.BlocksMarkersMap;
import edu.ceta.vision.android.topcode.TopCodeDetectorAndroid;
import edu.ceta.vision.core.blocks.Block;
import edu.ceta.vision.core.topcode.TopCodeDetector;
import edu.ceta.vision.core.topcode.TopCodeDetectorDesktop;
import miceta.game.core.miCeta;
import miceta.game.core.util.AudioManager;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class CvBlocksManager {

    public static final String TAG = CvBlocksManager.class.getName();
    private TopCodeDetector topCodeDetector;
    private miCeta game;
    private boolean detectionReady;
    public ArrayList<Set> results = new ArrayList<Set>();
    ArrayList<Integer> nowDetectedVals = new ArrayList<Integer>();
    ArrayList<Integer> nowDetectedValsId = new ArrayList<Integer>();
    private ArrayList<Block> newDetectedCVBlocks;
    private ArrayList<Integer> toRemoveCVIds;
    private ArrayList<Integer> toRemoveCVValues;
    private ArrayList<Integer> lastframeids, p_lastframeids;
    private ArrayList<Integer> oldIds;
    private ArrayList<Integer> newIds, p_newIds, stableIds;
    private ArrayMap<Integer,Integer> strikes;
    private ArrayMap<Integer,Integer> p_strikes;
    private int maxStrikes;
    private int p_maxStrikes;
    private ArrayMap<Integer,Integer> idToValue;
    private ArrayMap<Integer,Integer> tableIdValue;
    private Set<Block> currentBlocks;
    private Set<Block> currentBlocksId;
    int i=0;
    int x;
    int y;


    public CvBlocksManager(miCeta game, Stage stage)
    {
        this.game = game;
        init();
    }

    private void init(){

        if((Gdx.app.getType() == Application.ApplicationType.Android)) {
            Rect detectionZone = new Rect((640-480),0,480,480);
            topCodeDetector = new TopCodeDetectorAndroid(50, true, 70, 5, true, false, false, true, detectionZone);
        }else{ // pc!
            Rect detectionZone = new Rect(0,0,640,480); // WARN my mac cam resolution!!
            // TODO check the orientation of the frame
            //Rect detectionZone = new Rect(0,0,1920,768); //positivo camera resolution
            topCodeDetector = new TopCodeDetectorDesktop(50, true, 70, 5, true, false, true, detectionZone,true);

        }
        detectionReady = false;

        newDetectedCVBlocks = new ArrayList<Block>();
        toRemoveCVIds = new ArrayList<Integer>();
        toRemoveCVValues = new ArrayList<Integer>();

        newIds = new ArrayList<Integer>();
        lastframeids = new ArrayList<Integer>();
        stableIds = new ArrayList<Integer>();

        p_newIds = new ArrayList<Integer>();
        p_lastframeids = new ArrayList<Integer>();

        nowDetectedVals = new ArrayList<Integer>();



        maxStrikes = 3; // after x frames without marker, we pronounce it deleted
        p_maxStrikes = 3;

        strikes = new ArrayMap<Integer,Integer>();//id - integer
        p_strikes = new ArrayMap<Integer,Integer>();//id - integer

        idToValue = new ArrayMap<Integer,Integer>();
        tableIdValue = new ArrayMap<Integer,Integer>();

        initStrikesAndBlocksValues();
        currentBlocks = new HashSet<Block>();

    }

    public void updateDetected() {
        if ((Gdx.app.getType() == Application.ApplicationType.Android)) {
            new Thread(new Runnable() {
                public void run() {
                    Mat frame = game.getAndBlockLastFrame();
                    Core.flip(frame, frame, 0);

                    final Set<Block> finalSet = ((TopCodeDetectorAndroid) topCodeDetector).detectBlocks(frame, 0.85);
                    //final Set<Block> finalSet = topCodeDetector.detectBlocks(((CetaGame) game).getAndBlockLastFrame());
                    i++;
                    Gdx.app.log(TAG, "" + i);

                    detectionReady = true;
                    game.releaseFrame();

                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            // process the result, e.g. add it to an Array<Result> field of the ApplicationListener.
                            results.clear();
                            results.add(finalSet);
                        }
                    });
                }
            }).start();
        } else { // PC!
            new Thread(new Runnable() {
                public void run() {
                    // Mat frame = ((CetaGame) game).getAndBlockLastFrame();
                    //Core.flip(frame, frame, 0);

                    final Set<Block> finalSet = ((TopCodeDetectorDesktop) topCodeDetector).detectBlocks();
                    // Gdx.app.log(TAG, "ready with the detection!! framerateee"+Gdx.graphics.getFramesPerSecond());
                    detectionReady = true;
                    //((CetaGame) game).releaseFrame();

                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            // process the result, e.g. add it to an Array<Result> field of the ApplicationListener.
                            results.clear();
                            results.add(finalSet);
                        }
                    });
                }
            }).start();
        }
    }

    public void analyseDetected(){
        if(detectionReady) {
            if(results.size() > 0) {
                currentBlocks = results.get(0); //here we have our set of detected blocks
                //currentBlocksId = results.get(0)

            }
            else {
                Gdx.app.error(TAG," very very wrong -> empty result!");
                currentBlocks =  new HashSet<Block>();
                //currentBlocksId =  new HashSet<Block>();
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

            Gdx.app.log(TAG, "now detected vals "+ Arrays.toString(nowDetectedVals.toArray()));
            Gdx.app.log(TAG, "now detected ids "+ Arrays.toString(nowDetectedValsId.toArray()));
            Gdx.app.log(TAG, "last frame ids "+ Arrays.toString(lastframeids.toArray()));
            Gdx.app.log(TAG, "stable ids "+ Arrays.toString(stableIds.toArray()));

            p_newIds = new ArrayList(newIds);
            p_compareIds(newIds , lastframeids);
            p_lastframeids = new ArrayList(stableIds);
            compareIds(p_lastframeids , p_newIds);
            detectionReady = false;
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


    private void initStrikesAndBlocksValues(){
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

    private void checkpStrikesAndDecideIfAdd(int id){

        Gdx.app.log(TAG,  p_maxStrikes + " Entro aca: "  + id + "p_strikes: " + p_strikes.get(id));

        if((p_strikes.get(id) == p_maxStrikes )&&!(stableIds.contains(id))) {

            stableIds.add(id);
            resetStrikes(id);
            AudioManager.instance.playNumber(16);
            Gdx.app.log(TAG, " ADD BLOCK: " + id + "BECAUSE HAS POSITIVE STRIKES!");

        }

    }


    private void p_compareIds(ArrayList<Integer> newBlocksIds, ArrayList<Integer> oldBlocksIds){


        for(int i = newBlocksIds.size()-1;i>=0;i--){ // we start from the end to avoid ids problems

            int newId = newBlocksIds.get(i);

            if(oldBlocksIds.contains(newId)) {
                p_updateStrikes(newId);
                boolean shouldBeUpdated = false; //paraa que sirve esto?
            }
            else{
/////dds
               p_resetStrikes(newId);
            }
        }
        // we won't use more oldBlocksIds so we use it to get unique ids that should be removed
        //newBlocksIds.removeAll(oldBlocksIds);

        for(int i=0;i<newBlocksIds.size();i++){
            checkpStrikesAndDecideIfAdd(newBlocksIds.get(i));
        }
    }








}
