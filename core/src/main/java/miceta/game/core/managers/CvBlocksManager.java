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
import miceta.game.core.miCeta;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import java.util.*;


/**
 * Created by ewe on 8/10/17.
 */

public class CvBlocksManager {
    public static final String TAG = CvBlocksManager.class.getName();
    private TopCodeDetector topCodeDetector;
    private miCeta game;
    private boolean detectionReady;
    public ArrayList<Set> results = new ArrayList<Set>();
    ArrayList<Integer> nowDetectedVals = new ArrayList<Integer>();
    ArrayList<Integer> nowDetectedValsId = new ArrayList<Integer>();

    //ArrayList<Integer> auxValsId = new ArrayList<Integer>();
    //int[] idBlocks;
    //{
    //  idBlocks = new int[1300];
    //}
    //hacer funcion modulo

 // ArrayMap<Integer,Integer> ids;

    private ArrayList<Block> newDetectedCVBlocks;
    private ArrayList<Integer> toRemoveCVIds;
    private ArrayList<Integer> toRemoveCVValues;
    private ArrayList<Integer> lastframeids;
    private ArrayList<Integer> oldIds;
    private ArrayList<Integer> newIds, stableIds;
    private ArrayMap<Integer,Integer> strikes;
    private int maxStrikes;
    private ArrayMap<Integer,Integer> idToValue;
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
        Rect detectionZone = new Rect((640-480),0,480,480);
        if((Gdx.app.getType() == Application.ApplicationType.Android)) {
            topCodeDetector = new TopCodeDetectorAndroid(50, true, 70, 5, true, false, false, true, detectionZone);
        }
        detectionReady = false;
        newDetectedCVBlocks = new ArrayList<Block>();
        toRemoveCVIds = new ArrayList<Integer>();
        toRemoveCVValues = new ArrayList<Integer>();

        newIds = new ArrayList<Integer>();
        lastframeids = stableIds = new ArrayList<Integer>();
        nowDetectedVals = new ArrayList<Integer>();



        maxStrikes = 3; // after x frames without marker, we pronounce it deleted
        strikes = new ArrayMap<Integer,Integer>();//id - integer
        idToValue = new ArrayMap<Integer,Integer>();

        initStrikesAndBlocksValues();
        currentBlocks = new HashSet<Block>();


    }

    public void updateDetected() {
        if((Gdx.app.getType() == Application.ApplicationType.Android)) {
            new Thread(new Runnable() {
                public void run() {
                    Mat frame = game.getAndBlockLastFrame();
                    Core.flip(frame, frame, 0);

                    final Set<Block> finalSet = ((TopCodeDetectorAndroid) topCodeDetector).detectBlocks(frame, 0.85);
                    //final Set<Block> finalSet = topCodeDetector.detectBlocks(((CetaGame) game).getAndBlockLastFrame());
                    i++;
                    Gdx.app.log(TAG, ""+ i);

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
        }
        else{
            //PC case
        }
    }

/*
    public void idLastFrame(){

        int size;

        size = nowDetectedValsId.size();

        if (size > 0) {

            for (int i = 0; i <= size; i++) {

                auxValsId.set(i, nowDetectedValsId.get(i));

            }
        }
    }
  */      //nowDetectedValsId.add(i.getId());


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


            lastframeids = new ArrayList(newIds);
            stableIds = new ArrayList(newIds);
            newIds.clear();

            nowDetectedValsId.clear();
            newDetectedCVBlocks.clear();
            nowDetectedVals.clear();
            //nowDetectedBlocks.clear();

            nowDetectedValsId.clear();
            for (Block i : currentBlocks) {

                newIds.add(i.getId());
                nowDetectedValsId.add(i.getId());
                nowDetectedVals.add(i.getValue());
                newDetectedCVBlocks.add(i);

      //            int aux = idBlocks[i.getId()];
                //nowDetectedBlocks.add(getBlockById(i.getId()));
        //        idBlocks[i.getId()] = idBlocks[i.getId()] +1;
          //      Gdx.app.log(TAG, "ZZ " + idBlocks[i.getId()]);
                //if (nowDetectedValsId.contains(i.getId())){
                  //}
                  //else
                // nowDetectedValsId.add(i.getId());
                  //}
                //auxValsId.add(i.getId());
               // i.getId();// we fill the empty array with detected values
            }

            Gdx.app.log(TAG, "now detected vals "+ Arrays.toString(nowDetectedVals.toArray()));
            //Gdx.app.log(TAG, "now detected ids "+ Arrays.toString(nowDetectedValsId.toArray()));
            //Gdx.app.log(TAG, "last frame ids "+ Arrays.toString(lastframeids.toArray()));

            compareIds(newIds , lastframeids);

            //ArrayList<Integer> newIdBlocks = new ArrayList<Integer>();
            //ArrayList<Integer> pastIdBlocks = new ArrayList<Integer>();

            //newIdBlocks = (ArrayList<Integer>)nowDetectedValsId.clone();
            //newIdBlocks.removeAll(lastframeids); //obtengo los nuevos

            //pastIdBlocks = (ArrayList<Integer>)lastframeids.clone();//copio lista con ids nuevos
            //pastIdBlocks .removeAll(nowDetectedValsId);

            //Gdx.app.log(TAG, "News blocks!:->"+ Arrays.toString(newIdBlocks.toArray()));
            //Gdx.app.log(TAG, "old blocks!:->"+ Arrays.toString(pastIdBlocks.toArray()));

/*            for(int i = nowDetectedValsId .size()-1;i>=0;i--){
                Block nBlock = newDetectedCVBlocks.get(i);
                int newId = nowDetectedValsId .get(i);
                if(lastframeids.contains(newId)) {
                    resetStrikes(newId);
                    boolean shouldBeUpdated = false;
                }
            }
            lastframeids.removeAll(nowDetectedValsId);

            for(int i=0;i<lastframeids.size();i++){
                checkStrikesAndDecideIfRemove(lastframeids.get(i));
            }
            */
            detectionReady = false;
        }
    }


    private void compareIds(ArrayList<Integer> newBlocksIds, ArrayList<Integer> oldBlocksIds){
        for(int i = newBlocksIds.size()-1;i>=0;i--){ // we start from the end to avoid ids problems
            Block nBlock = newDetectedCVBlocks.get(i);
            int newId = newBlocksIds.get(i);
            if(oldBlocksIds.contains(newId)) {
                resetStrikes(newId);
                boolean shouldBeUpdated = false;
            }
        }
        // we won't use more oldBlocksIds so we use it to get unique ids that should be removed
        oldBlocksIds.removeAll(newBlocksIds);
        for(int i=0;i<oldBlocksIds.size();i++){
            checkStrikesAndDecideIfRemove(oldBlocksIds.get(i));
        }
    }

    private void checkStrikesAndDecideIfRemove(int id){
        if(strikes.get(id) > maxStrikes ) {
            Gdx.app.log(TAG, " remove block with id: " + id + "because its gone and has max strikes!");
            //removeBlockCV(id, idToValue.get(id));
        }
        else{
            Gdx.app.log(TAG," STRIKE for "+id);
            //update strikes
            updateStrikes(id);
            // add to new
            newIds.add(id);
        }
    }


    private void initStrikesAndBlocksValues(){
        int [][] allMarkers = {BlocksMarkersMap.block1,BlocksMarkersMap.block2,BlocksMarkersMap.block3,BlocksMarkersMap.block4,BlocksMarkersMap.block5};

        for(int arrIdx = 0;arrIdx < allMarkers.length; arrIdx++){
            for(int i =0; i< allMarkers[arrIdx].length;i++){
                //Gdx.app.log(TAG, " putting "+allMarkers[arrIdx][i]);
                strikes.put(allMarkers[arrIdx][i],0);
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





    public boolean isDetectionReady(){
        return detectionReady;
    }

    public ArrayList<Integer> getNewDetectedVals(){

        return new ArrayList(nowDetectedVals);
    }


}
