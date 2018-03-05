package miceta.game.core.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ArrayMap;
import edu.ceta.vision.android.topcode.TopCodeDetectorAndroid;
import edu.ceta.vision.core.blocks.Block;
import miceta.game.core.miCeta;
import miceta.game.core.util.Constants;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class CvBlocksManagerAndroid extends CvBlocksManager {



    public CvBlocksManagerAndroid(miCeta game) {
        super(game);
    }


    public  void init() {

        currentBlocks = null;
        tempList = null;

        Rect detectionZone = new Rect((640-480),0,480,480);
        if(game.getTopCodeDetector() == null) {
            topCodeDetector = new TopCodeDetectorAndroid(50, true, 70, 5, true, false, false, true, detectionZone);
            game.setTopCodeDetector(topCodeDetector);
        }else{
            topCodeDetector = game.getTopCodeDetector();
        }
        detectionReady = false;
        detectionInProgress = false;
        newDetectedCVBlocks = new ArrayList<>();
        // toRemoveCVIds = new ArrayList<Integer>();
        //toRemoveCVValues = new ArrayList<Integer>();

        newIds = new ArrayList<>();
        lastframeids = new ArrayList<>();
        stableIds = new ArrayList<>();
        p_newIds = new ArrayList<>();
        p_lastframeids = new ArrayList<>();
        nowDetectedVals = new ArrayList<>();
        maxStrikes = Constants.STRIKE; // after x frames without marker, we pronounce it deleted
        p_maxStrikes = Constants.P_STRIKE;
        strikes = new ArrayMap<>();//id - integer
        p_strikes = new ArrayMap<>();//id - integer
        idToValue = new ArrayMap<>();
        tableIdValue = new ArrayMap<>();
        initStrikesAndBlocksValues();
        currentBlocks = new HashSet<>();




    }


    public void updateDetected() {
        if(!detectionInProgress && !detectionReady) {
            detectionInProgress = true;
            new Thread(new Runnable() {
                public void run() {
                    Mat frame = game.getAndBlockLastFrame();
                    Core.flip(frame, frame, 0);

                    final Set<Block> finalSet = ((TopCodeDetectorAndroid) topCodeDetector).detectBlocks(frame, 0.85);

                    game.releaseFrame();

                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            // process the result, e.g. add it to an Array<Result> field of the ApplicationListener.
                            results.clear();
                            results.add(finalSet);
                            detectionReady = true;
                        }

                    });
                }
            }).start();
        }

    }




    public boolean canBeUpdated(){
        return  game.hasNewFrame();
    }



}

