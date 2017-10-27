package miceta.game.core.managers;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ArrayMap;


import edu.ceta.vision.android.topcode.TopCodeDetectorAndroid;
import edu.ceta.vision.core.blocks.Block;
import edu.ceta.vision.core.topcode.TopCodeDetector;
import edu.ceta.vision.core.topcode.TopCodeDetectorDesktop;
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
    private Set<Block> currentBlocks, tempList;



    public CvBlocksManager(miCeta game, Stage stage)
    {
        this.game = game;
        init();
    }

    private void init(){
        currentBlocks = null;
        tempList = null;
        if((Gdx.app.getType() == Application.ApplicationType.Android)) {
            Rect detectionZone = new Rect((640-480),0,480,480);
            topCodeDetector = new TopCodeDetectorAndroid(50, true, 70, 5, true, false, false, true, detectionZone);
        }else{ // pc!
            Rect detectionZone = new Rect(0,0,1280,720); // WARN my mac cam resolution!!
            //Rect detectionZone = new Rect(0,0,640,480); //positivo camera resolution
            topCodeDetector = new TopCodeDetectorDesktop(50, true, 70, 5, true, false, true, detectionZone,true,false);

        }
        detectionReady = false;
    }

    public void updateDetected() {
        if(!detectionReady) {
            if ((Gdx.app.getType() == Application.ApplicationType.Android)) {
                new Thread(new Runnable() {
                    public void run() {
                        Mat frame = game.getAndBlockLastFrame();
                        Core.flip(frame, frame, 0);

                        final Set<Block> finalSet = ((TopCodeDetectorAndroid) topCodeDetector).detectBlocks(frame, 0.85);
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
    }

    public void analyseDetected(){
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
            nowDetectedVals.clear();

            for (Block i : currentBlocks) {
                nowDetectedVals.add(i.getValue()); // we fill the empty array with detected values
            }
            //Gdx.app.log(TAG, "now detected vals "+Arrays.toString(nowDetectedVals.toArray()));
            detectionReady = false;
        }
    }

    public boolean isDetectionReady(){
        return detectionReady;
    }

    public ArrayList<Integer> getNewDetectedVals(){
        return new ArrayList(nowDetectedVals);
    }

    public TopCodeDetector getTopCodeDetector(){
        return topCodeDetector;
    }

    public Set<Block> getCurrentBlocks() {
        return tempList;
    }

}
