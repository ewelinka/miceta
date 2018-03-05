package miceta.game.core.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ArrayMap;
import edu.ceta.vision.core.blocks.Block;
import edu.ceta.vision.core.topcode.TopCodeDetectorDesktop;
import miceta.game.core.miCeta;
import miceta.game.core.util.Constants;
import org.opencv.core.Rect;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class CvBlocksManagerDesktop extends CvBlocksManager {

    public CvBlocksManagerDesktop(miCeta game) {
        super(game);
    }


    public  void init() {

        currentBlocks = null;
        tempList = null;

       // Rect detectionZone = new Rect(0,0,640,480); // WARN my mac cam resolution!!
        // / TODO check the orientation of the frame
        Rect detectionZone = new Rect(0,0,1920,768); //positivo camera resolution
        if(game.getTopCodeDetector() == null) {
            topCodeDetector = new TopCodeDetectorDesktop(50, true, 70, 5, true, false, true, detectionZone, true, false);
            game.setTopCodeDetector(topCodeDetector);
        }else{
            topCodeDetector = game.getTopCodeDetector();
        }
        detectionReady = false;
        detectionInProgress = false;
        newDetectedCVBlocks = new ArrayList<>();



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
        if(!detectionInProgress) {
            detectionInProgress = true;
            new Thread(new Runnable() {
                public void run() {
                    // Mat frame = ((CetaGame) game).getAndBlockLastFrame();
                    //Core.flip(frame, frame, 0);

                    final Set<Block> finalSet = ((TopCodeDetectorDesktop) topCodeDetector).detectBlocks();

                    // Gdx.app.log(TAG, "ready with the detection!! framerateee"+Gdx.graphics.getFramesPerSecond());

                    //((CetaGame) game).releaseFrame();

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
        return  !(((TopCodeDetectorDesktop)getTopCodeDetector()).isBusy());
    }

}

