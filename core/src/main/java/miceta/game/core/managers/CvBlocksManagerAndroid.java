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
import miceta.game.core.Assets;
import miceta.game.core.miCeta;
import miceta.game.core.util.AudioManager;
import miceta.game.core.util.Constants;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class CvBlocksManagerAndroid extends CvBlocksManager {



    public CvBlocksManagerAndroid(miCeta game, Stage stage) {
        super(game, stage);
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
        newDetectedCVBlocks = new ArrayList<Block>();
        // toRemoveCVIds = new ArrayList<Integer>();
        //toRemoveCVValues = new ArrayList<Integer>();

        newIds = new ArrayList<Integer>();
        lastframeids = new ArrayList<Integer>();
        stableIds = new ArrayList<Integer>();
        p_newIds = new ArrayList<Integer>();
        p_lastframeids = new ArrayList<Integer>();
        nowDetectedVals = new ArrayList<Integer>();
        maxStrikes = Constants.STRIKE; // after x frames without marker, we pronounce it deleted
        p_maxStrikes = Constants.P_STRIKE;;
        strikes = new ArrayMap<Integer,Integer>();//id - integer
        p_strikes = new ArrayMap<Integer,Integer>();//id - integer
        idToValue = new ArrayMap<Integer,Integer>();
        tableIdValue = new ArrayMap<Integer,Integer>();
        initStrikesAndBlocksValues();
        currentBlocks = new HashSet<Block>();




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

