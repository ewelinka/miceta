package miceta.game.core.controllers;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import edu.ceta.vision.core.blocks.Block;
import miceta.game.core.managers.CvBlocksManager;
import miceta.game.core.managers.CvBlocksManagerAndroid;
import miceta.game.core.managers.CvBlocksManagerDesktop;
import miceta.game.core.managers.LevelsManager;
import miceta.game.core.miCeta;
import miceta.game.core.screens.FeedbackScreen;
import miceta.game.core.screens.IntroScreen;
import miceta.game.core.screens.TestScreen;
import miceta.game.core.util.AudioManager;
import miceta.game.core.util.Constants;
import miceta.game.core.util.GamePreferences;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by ewe on 8/10/17.
 */
public class CvWorldController extends InputAdapter {
    private static final String TAG = CvWorldController.class.getName();
    protected Stage stage;
    public miCeta game;
   // private int randomNumber,previousRandomNumber;
    private int numberToPlay;
    private boolean lastAnswerRight;
    private int error_min = 0;
    private int error_max = 0;
    private float inactivityTime =0; // time that passed since last move
    private int feedback_delay=0;
    private int currentSum=0;
    private int lastSum=0;
    protected float timeToWait, timePassed;
    protected CvBlocksManager cvBlocksManager;
    protected float extraDelayBetweenFeedback;
    protected float waitAfterKnock;


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

        initCommonVariables();
        init();
    }

    protected void init(){
        numberToPlay = LevelsManager.getInstance().get_number_to_play();
        Gdx.app.log(TAG,"init, Number to Play: " + numberToPlay );

        AudioManager.instance.readFeedback(numberToPlay, extraDelayBetweenFeedback); //first we read the random number
        timeToWait = Constants.READ_ONE_UNIT_DURATION+ numberToPlay*Constants.READ_ONE_UNIT_DURATION + waitAfterKnock /*+ ( randomNumber)*(0.3f)*/; // time we should wait before next loop starts
        lastAnswerRight = false;


    }

    protected void initCommonVariables(){
        timePassed = 0;

        extraDelayBetweenFeedback = GamePreferences.instance.getExtraDelayBetweenFeedback();
        waitAfterKnock = GamePreferences.instance.getWaitAfterKnock();
        waitAfterKnock = 3;

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

        //AudioManager.instance.setNewblock_loop(false);
        //AudioManager.instance.playNewBlockSong();

        if(isTimeToStartNewLoop()){
            timePassed = 0; // start to count the time
            Gdx.app.log(TAG,"new loop! with random number "+numberToPlay);
            if(lastAnswerRight){ // if las answer was correct, we get new random number
               // previousRandomNumber = randomNumber;
                //randomNumber = getNewNumber();

                LevelsManager.getInstance().up_operation_index();
                numberToPlay = LevelsManager.getInstance().get_number_to_play();
                saveLevel();

                // timeToWait = Constants.READ_NUMBER_DURATION + randomNumber*Constants.READ_ONE_UNIT_DURATION + Constants.WAIT_AFTER_KNOCK ; // one extra second to read number and feedback
                timeToWait = numberToPlay*(Constants.READ_ONE_UNIT_DURATION+extraDelayBetweenFeedback) + waitAfterKnock; // read feedback and wait

                AudioManager.instance.readFeedback(numberToPlay, extraDelayBetweenFeedback);
                lastAnswerRight = false;

                resetErrorsAndInactivity(); // start from 0
            }else { // if last answer was wrong we check the detected values and read feedback and read blocks detected
                ArrayList<Integer> nowDetected = cvBlocksManager.getNewDetectedVals(); // to know the blocks on the table
                lastSum = currentSum;
                currentSum = 0;


                for (int i = 0; i < nowDetected.size(); i++)
                    currentSum += nowDetected.get(i); // we need to know the sum to decide if response is correct

                checkForErrorsAndInactivity(currentSum, lastSum);
                checkForCorrectAnswer(currentSum,numberToPlay, nowDetected);
            }

        }
    }


    protected boolean isTimeToStartNewLoop(){
        return (timePassed > timeToWait );
    }

    public Set<Block> getCurrentBlocksFromManager(){
        return cvBlocksManager.getCurrentBlocks();
    }

    public int getRandomNumber(){
        return numberToPlay;
    }


    private void checkForErrorsAndInactivity(int currentSum, int lastSum){
        // check for errors
        if((currentSum != lastSum)){ // we count errors or reset inactivity only if (currentSum != lastSum)
            inactivityTime = 0;
            if (currentSum > numberToPlay) { //too much
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

    private void checkForCorrectAnswer(int currentSum, int numberToPlay, ArrayList<Integer> nowDetected ) {
        int biggerNumber =  (currentSum > numberToPlay) ? currentSum : numberToPlay;
        //Gdx.app.log(TAG,"wait after knock!!! "+waitAfterKnock);
        //timeToWait = Constants.READ_NUMBER_DURATION + biggerNumber * Constants.READ_ONE_UNIT_DURATION + Constants.WAIT_AFTER_KNOCK + feedback_delay;
        timeToWait = biggerNumber * (Constants.READ_ONE_UNIT_DURATION + extraDelayBetweenFeedback)+ waitAfterKnock + feedback_delay;

        if (currentSum == numberToPlay) { // correct answer! in next loop we will celebrate
            lastAnswerRight = true;
            timeToWait += (Constants.DELAY_FOR_TADA + Constants.DELAY_FOR_YUJU + Constants.WAIT_AFTER_CORRECT_ANSWER);
        }

        AudioManager.instance.readAllFeedbacks(nowDetected, numberToPlay, lastAnswerRight, extraDelayBetweenFeedback);
    }

    private void resetErrorsAndInactivity(){
        error_min = 0;
        error_max = 0;
        inactivityTime = 0;
    }

    private void touchDownAndroid(int screenX, int screenY, int button){
        if (screenX > 540 && screenY < 60) {
            game.setScreen(new TestScreen(game));
        }
        if (screenX < 60 && screenY < 60) {
            game.setScreen(new FeedbackScreen(game));
        }

        if ((screenX > 10 && screenX < 70) && (screenY > (Constants.ANDROID_HEIGHT-140) && screenY < (Constants.ANDROID_HEIGHT-100))) {
            makeFeedbackSlower();
        }
        if ((screenX > 400 && screenX <460)&& (screenY > (Constants.ANDROID_HEIGHT-140) && screenY < (Constants.ANDROID_HEIGHT-100))) {
            makeFeedbackFaster();
        }
        if ((screenX > 10 && screenX < 70) && (screenY > (Constants.ANDROID_HEIGHT-80) && screenY < (Constants.ANDROID_HEIGHT-40))) {
            makeWaitBigger();
        }
        if ((screenX > 400 && screenX < 460)&& (screenY > (Constants.ANDROID_HEIGHT-80) && screenY < (Constants.ANDROID_HEIGHT-40))) {
            makeWaitSmaller();
        }

    }
    private void touchDownDesktop(int screenX, int screenY, int button){
        if (screenX > 440 && screenY < 10) {
            game.setScreen(new TestScreen(game));
        }

        if (screenX < 40 && screenY < 10) {
            game.setScreen(new FeedbackScreen(game));
        }

        if ((screenX > 10 && screenX < 70) && (screenY > (Constants.DESKTOP_HEIGHT-125) && screenY < (Constants.DESKTOP_HEIGHT-105))) {
            makeFeedbackSlower();
        }
        if ((screenX > 400 && screenX <460)&& (screenY > (Constants.DESKTOP_HEIGHT-125) && screenY < (Constants.DESKTOP_HEIGHT-105))) {
            makeFeedbackFaster();
        }
        if ((screenX > 10 && screenX < 70) && (screenY > (Constants.DESKTOP_HEIGHT-65) && screenY < (Constants.DESKTOP_HEIGHT-45))) {
            makeWaitBigger();
        }
        if ((screenX > 400 && screenX < 460)&& (screenY > (Constants.DESKTOP_HEIGHT-65) && screenY < (Constants.DESKTOP_HEIGHT-45))) {
            makeWaitSmaller();
        }
        if(button == Input.Buttons.RIGHT){
            game.setScreen(new IntroScreen(game));
        }


    }


    @Override
    public boolean touchDown (int screenX, int screenY, int pointer, int button) {
        Gdx.app.log(TAG," TOUCHED "+screenX+ " "+screenY);
        if (Gdx.app.getType() == Application.ApplicationType.Android) {
            touchDownAndroid(screenX, screenY, button);
        }else {
            touchDownDesktop(screenX, screenY, button);
        }
        return true;
    }


    private void makeFeedbackSlower(){
        extraDelayBetweenFeedback =  extraDelayBetweenFeedback + 0.10f;
        saveSettings();
    }

    private void makeFeedbackFaster(){
        extraDelayBetweenFeedback =  extraDelayBetweenFeedback - 0.10f;
        saveSettings();
    }
    private void makeWaitBigger(){
        waitAfterKnock =  waitAfterKnock + 0.50f;
        saveSettings();
    }

    private void makeWaitSmaller(){
        waitAfterKnock =  waitAfterKnock - 0.50f;
        saveSettings();
    }


    public float getExtraDelayBetweenFeedback(){
        return extraDelayBetweenFeedback;
    }

    public float getWaitAfterKnock(){
        return waitAfterKnock;
    }


    private void saveSettings() {
        GamePreferences prefs = GamePreferences.instance;
        prefs.load();
        GamePreferences.instance.setExtraDelayBetweenFeedback(extraDelayBetweenFeedback);
        GamePreferences.instance.setWaitAfterKnock(waitAfterKnock);
        prefs.save();
    }


    private void saveLevel() {
        GamePreferences prefs = GamePreferences.instance;
        prefs.load();
        GamePreferences.instance.setLast_level(LevelsManager.getInstance().get_level());
        GamePreferences.instance.setOperation_index(LevelsManager.getInstance().get_operation_index());
        prefs.save();
    }

}
