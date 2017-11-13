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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class CvBlocksManagerDesktop extends CvBlocksManager {


    public static final String TAG = CvBlocksManager.class.getName();
    private TopCodeDetector topCodeDetector;
    private miCeta game;
    private boolean detectionReady;
    public ArrayList<Set> results = new ArrayList<Set>();
    ArrayList<Integer> nowDetectedVals = new ArrayList<Integer>();
    private Set<Block> tempList;
    ArrayList<Integer> nowDetectedValsId = new ArrayList<Integer>();
    private ArrayList<Block> newDetectedCVBlocks;
    // private ArrayList<Integer> toRemoveCVIds;
    //private ArrayList<Integer> toRemoveCVValues;
    private ArrayList<Integer> lastframeids, p_lastframeids;
    private ArrayList<Integer> newIds, p_newIds, stableIds;
    private ArrayMap<Integer,Integer> strikes;
    private ArrayMap<Integer,Integer> p_strikes;
    private int maxStrikes;
    private int p_maxStrikes;
    private ArrayMap<Integer,Integer> idToValue;
    private ArrayMap<Integer,Integer> tableIdValue;
    private Set<Block> currentBlocks;


    public CvBlocksManagerDesktop(miCeta game, Stage stage) {
        super(game, stage);
    }

    private  void init() {

        currentBlocks = null;
        tempList = null;

        Rect detectionZone = new Rect(0,0,640,480); // WARN my mac cam resolution!!
        // / TODO check the orientation of the frame
        //Rect detectionZone = new Rect(0,0,1920,768); //positivo camera resolution
        topCodeDetector = new TopCodeDetectorDesktop(50, true, 70, 5, true, false, true, detectionZone,true,false);
        detectionReady = false;
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
        if(!detectionReady) {

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



}

