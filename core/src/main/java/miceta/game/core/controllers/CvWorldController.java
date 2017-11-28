package miceta.game.core.controllers;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import edu.ceta.vision.core.blocks.Block;
import miceta.game.core.managers.CvBlocksManager;
import miceta.game.core.managers.CvBlocksManagerAndroid;
import miceta.game.core.managers.CvBlocksManagerDesktop;
import miceta.game.core.miCeta;
import miceta.game.core.screens.FeedbackScreen;
import miceta.game.core.screens.TestScreen;
import miceta.game.core.util.AudioManager;
import miceta.game.core.util.Constants;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by ewe on 8/10/17.
 */
public class CvWorldController extends InputAdapter {
    private static final String TAG = CvWorldController.class.getName();
    protected Stage stage;
    public miCeta game;
    private int randomNumber,previousRandomNumber;
    private boolean lastAnswerRight;
    private int error_min = 0;
    private int error_max = 0;
    private float inactivityTime =0; // time that passed since last move
    private int feedback_delay=0;
    private int currentSum=0;
    private int lastSum=0;
    protected float timeToWait, timePassed;
    protected CvBlocksManager cvBlocksManager;

    public CvWorldController(miCeta game, Stage stage){
        this.game = game;
        this.stage = stage;

        if((Gdx.app.getType() == Application.ApplicationType.Android)) {
            cvBlocksManager = new CvBlocksManagerAndroid(game, stage);
        }
        else {
            cvBlocksManager = new CvBlocksManagerDesktop(game, stage);
        }
        AudioManager.instance.setStage(stage); // we set current Stage in AudioManager, if not "reader" actor doesn't work

        init();
    }

    protected void init(){
        Gdx.app.log(TAG,"init in the cv blocks manager");
        timePassed = 0;
        randomNumber = getNewNumber();

        AudioManager.instance.readFeedback(randomNumber); //first we read the random number
        timeToWait = Constants.READ_ONE_UNIT_DURATION+ randomNumber*Constants.READ_ONE_UNIT_DURATION+ Constants.WAIT_AFTER_KNOCK; // time we should wait before next loop starts
        lastAnswerRight = false;
    }

    protected void updateCV(){

        if(cvBlocksManager.canBeUpdated()) { //ask before in order to not accumulate new threads.
            cvBlocksManager.updateDetected();
        }

        if(cvBlocksManager.isDetectionReady()){
            cvBlocksManager.analyseDetected();
        }
    }



    public void update(float deltaTime) {
        timePassed+=deltaTime; // variable used to check in isTimeToStartNewLoop() to decide if new feedback loop should be started
        inactivityTime+=deltaTime;
        updateCV();

        if(isTimeToStartNewLoop()){
            timePassed = 0; // start to count the time
            Gdx.app.log(TAG,"new loop! with random number "+randomNumber);
            if(lastAnswerRight){ // if las answer was correct, we get new random number
                previousRandomNumber = randomNumber;
                randomNumber = getNewNumber();
               // timeToWait = Constants.READ_NUMBER_DURATION + randomNumber*Constants.READ_ONE_UNIT_DURATION + Constants.WAIT_AFTER_KNOCK ; // one extra second to read number and feedback
                timeToWait = randomNumber*Constants.READ_ONE_UNIT_DURATION + Constants.WAIT_AFTER_CORRECT_ANSWER ; // read feedback and wait

                AudioManager.instance.readFeedback(randomNumber);
                lastAnswerRight = false;

                resetErrorsAndInactivity(); // start from 0
            }else { // if last answer was wrong we check the detected values and read feedback and read blocks detected
                ArrayList<Integer> nowDetected = cvBlocksManager.getNewDetectedVals(); // to know the blocks on the table
                lastSum = currentSum;
                currentSum = 0;
                for (int i = 0; i < nowDetected.size(); i++)
                    currentSum += nowDetected.get(i); // we need to know the sum to decide if response is correct

                checkForErrorsAndInactivity(currentSum, lastSum);
                checkForCorrectAnswer(currentSum,randomNumber, nowDetected);
            }

        }
    }


    private int getNewNumber(){
        int candidate = MathUtils.random(1,5);
        if(candidate == previousRandomNumber)
            candidate = (candidate+1)%6;
        if(candidate == 0) candidate = 1;

        return candidate;
    }

    protected boolean isTimeToStartNewLoop(){
        return (timePassed > timeToWait );
    }

    public Set<Block> getCurrentBlocksFromManager(){
        return cvBlocksManager.getCurrentBlocks();
    }

    public int getRandomNumber(){
        return randomNumber;
    }


    private void checkForErrorsAndInactivity(int currentSum, int lastSum){
        // check for errors
        if((currentSum != lastSum)){ // we count errors or reset inactivity only if (currentSum != lastSum)
            inactivityTime = 0;
            if (currentSum > randomNumber) { //too much
                error_max++;
                error_min = 0;

            } else { // too few
                error_min++;
                error_max =0;
            }
        }
        // check if there are sufficient errors and inactivity time
        feedback_delay = 0; // by default we assume that feedback delay should be 0
        if(inactivityTime >= Constants.INACTIVITY_LIMIT){ // this condition is important for both: min and max
            if (error_max >= Constants.ERRORS_FOT_HINT) {
                AudioManager.instance.setDelay_add(true);
                Gdx.app.log(TAG,"%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%ERROR MAX ");
                error_max = Constants.ERRORS_FOT_HINT-1;
                error_min = Constants.ERRORS_FOT_HINT-1;
                inactivityTime = 0;
                feedback_delay = Constants.FEEDBACK_DELAY;
            }
            if (error_min >= Constants.ERRORS_FOT_HINT){
                AudioManager.instance.setDelay_quit(true);
                Gdx.app.log(TAG,"########################################ERROR MIN ");
                error_min = Constants.ERRORS_FOT_HINT-1;
                error_max = Constants.ERRORS_FOT_HINT-1;
                feedback_delay = Constants.FEEDBACK_DELAY;
            }
        }
    }

    private void checkForCorrectAnswer(int currentSum, int randomNumber, ArrayList<Integer> nowDetected ) {
        int biggerNumber =  (currentSum > randomNumber) ? currentSum : randomNumber;
        //timeToWait = Constants.READ_NUMBER_DURATION + biggerNumber * Constants.READ_ONE_UNIT_DURATION + Constants.WAIT_AFTER_KNOCK + feedback_delay;
        timeToWait = biggerNumber * Constants.READ_ONE_UNIT_DURATION + Constants.WAIT_AFTER_KNOCK + feedback_delay;

        if (currentSum == randomNumber) { // correct answer! in next loop we will celebrate
            lastAnswerRight = true;
            timeToWait += Constants.DELAY_FOR_YUJU;
        }

        AudioManager.instance.readAllFeedbacks(nowDetected, randomNumber, lastAnswerRight);
    }

    private void resetErrorsAndInactivity(){
        error_min = 0;
        error_max = 0;
        inactivityTime = 0;
    }


    @Override
    public boolean touchDown (int screenX, int screenY, int pointer, int button) {
        Gdx.app.log(TAG," TOUCHED "+screenX+ " "+screenY);
        if (Gdx.app.getType() == Application.ApplicationType.Android) {
            if (screenX > 570 && screenY < 40) {
                game.setScreen(new TestScreen(game));
            }
            if (screenX < 30 && screenY < 40) {
                game.setScreen(new FeedbackScreen(game));
            }
        }else {

            if (screenX > 440 && screenY < 10) {
                game.setScreen(new TestScreen(game));
            }

            if (screenX < 40 && screenY < 10) {
                game.setScreen(new FeedbackScreen(game));
            }
        }
        return true;
    }




}
