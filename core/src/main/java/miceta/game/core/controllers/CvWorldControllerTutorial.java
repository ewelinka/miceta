package miceta.game.core.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import miceta.game.core.miCeta;
import miceta.game.core.screens.ConcreteTutorial;
import miceta.game.core.util.AudioManager;
import miceta.game.core.util.Constants;

import java.util.ArrayList;

/**
 * Created by ewe on 11/16/17.
 */
public class CvWorldControllerTutorial extends CvWorldController {
    private static final String TAG = CvWorldControllerFeedback.class.getName();
    public CvWorldControllerTutorial(miCeta game, Stage stage) {
        super(game, stage);
    }
    private boolean first_time = true;
    private float tutorialDuration =0;
    private float timePassed_t = 0;
    ArrayList<Integer> nowDetected;
    int suma = 0;
    private int inactivity =0;


    @Override
    protected void init(){
        Gdx.app.log(TAG,"init in the cv blocks manager");
        timeToWait = 2; // two seconds before we start!

    }

    @Override
    public void update(float deltaTime) {

        if ((!first_time) && (timePassed_t > tutorialDuration)){

               game.setScreen(new ConcreteTutorial(game,1,suma));
             //  AudioManager.instance.readBlocks(nowDetected, extraDelayBetweenFeedback);
               //first_time = false;
        }


        timePassed+=deltaTime;
        timePassed_t+=deltaTime;// variable used to check in isTimeToStartNewLoop() to decide if new feedback loop should be started
        updateCV();

        if(isTimeToStartNewLoop()){

            // ArrayList<Integer>  nowDetected = cvBlocksManager.getNewDetectedVals(); // to know the blocks on the table

             nowDetected = cvBlocksManager.getNewDetectedVals(); // to know the blocks on the table

            int sum = 0;
            for (int i = 0; i < nowDetected.size(); i++)
                sum += nowDetected.get(i); // we need to know the sum to decide if response is correct
            timeToWait =  sum*(Constants.READ_ONE_UNIT_DURATION + extraDelayBetweenFeedback)+ waitAfterKnock;
            timePassed = 0;

            if((sum > 0)&&(first_time)) {
                timePassed_t =0;
                first_time = false;
                tutorialDuration = AudioManager.instance.reproduce_concrete_tutorial(6,6);
                suma = sum;

            }
            else
            if( sum > 0){

               // AudioManager.instance.readBlocks(nowDetected, extraDelayBetweenFeedback);
            }
            if((timePassed_t> 5)) {
                tutorialDuration = AudioManager.instance.reproduce_concrete_tutorial(5,5);
                timePassed_t =0;
            }


        }}
}
