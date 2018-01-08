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
    private         boolean first_time = true;
;

    @Override
    protected void init(){
        Gdx.app.log(TAG,"init in the cv blocks manager");
        timeToWait = 2; // two seconds before we start!

//necesito poder indicar los indices de los audios
        AudioManager.instance.reproduce_concrete_tutorial();


    }

    @Override
    public void update(float deltaTime) {
        timePassed+=deltaTime; // variable used to check in isTimeToStartNewLoop() to decide if new feedback loop should be started
        updateCV();

        if(isTimeToStartNewLoop()){
            ArrayList<Integer> nowDetected = cvBlocksManager.getNewDetectedVals(); // to know the blocks on the table
            int sum = 0;
            for (int i = 0; i < nowDetected.size(); i++)
                sum += nowDetected.get(i); // we need to know the sum to decide if response is correct
            timeToWait =  sum*(Constants.READ_ONE_UNIT_DURATION + extraDelayBetweenFeedback)+ waitAfterKnock;
            timePassed = 0;

            if((sum > 0)&&(first_time)) {
               // AudioManager.instance.readBlocks(nowDetected, extraDelayBetweenFeedback);
                AudioManager.instance.reproduce_concrete_tutorial();
                //cuidado que no se pisen los audios


            }
            else
              if( sum > 0){
                AudioManager.instance.readBlocks(nowDetected, extraDelayBetweenFeedback);}


    }}
}
