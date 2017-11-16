package miceta.game.core.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import miceta.game.core.miCeta;
import miceta.game.core.util.AudioManager;
import miceta.game.core.util.Constants;

import java.util.ArrayList;

/**
 * Created by ewe on 11/16/17.
 */
public class CvWorldControllerFeedback extends CvWorldController {
    private static final String TAG = CvWorldControllerFeedback.class.getName();
    public CvWorldControllerFeedback(miCeta game, Stage stage) {
        super(game, stage);
    }

    @Override
    protected void init(){
        Gdx.app.log(TAG,"init in the cv blocks manager");
        timePassed = 0;
        timeToWait = 2; // two seconds before we start!

    }

    @Override
    public void update(float deltaTime) {
        timePassed+=deltaTime; // variable used to check in isTimeToStartNewLoop() to decide if new feedback loop should be started
        update2();

        if(isTimeToStartNewLoop()){
            ArrayList<Integer> nowDetected = cvBlocksManager.getNewDetectedVals(); // to know the blocks on the table
            int sum = 0;
            for (int i = 0; i < nowDetected.size(); i++)
                sum += nowDetected.get(i); // we need to know the sum to decide if response is correct
            timeToWait =  sum*Constants.READ_ONE_UNIT_DURATION + Constants.FEEDBACK_DELAY;
            timePassed = 0;

            AudioManager.instance.readBlocks(nowDetected);

        }
    }
}
