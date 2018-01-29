package miceta.game.core.controllers;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import edu.ceta.vision.core.blocks.Block;
import miceta.game.core.Assets;
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
import miceta.game.core.util.FeedbackSoundType;
import miceta.game.core.util.GamePreferences;

import java.util.ArrayList;
import java.util.Set;

import static miceta.game.core.util.CommonFeedbacks.POSITIVE;
import static miceta.game.core.util.CommonFeedbacks.TOO_FEW;
import static miceta.game.core.util.CommonFeedbacks.TOO_MUCH;


/**
 * Created by ewe on 8/10/17.
 */
public class CvWorldController extends InputAdapter {
    private static final String TAG = CvWorldController.class.getName();
    protected Stage stage;
    public miCeta game;
    // private int randomNumber,previousRandomNumber;
    protected int numberToPlay;
    protected boolean answerRight;
    private int error_min = 0;
    private int error_max = 0;
    protected float inactivityTime =0; // time that passed since last move
    private Sound test_res;
    private int game_number;
    protected int currentSum=0;
    protected int lastSum=0;
    protected float timeToWait, timePassed, time_to_go;
    protected CvBlocksManager cvBlocksManager;
    protected float extraDelayBetweenFeedback;
    protected float waitAfterKnock;
    protected Sound tooMuchErrorSound, tooFewErrorSound, positiveFeedback;
    protected FeedbackSoundType feedbackSound;
    protected int inactivityLimit;
    protected int maxErrorsForHint;
    protected boolean willGoToNextPart;
    protected float delayForPositiveFeedback;
    protected int correctAnswersNow;
    protected int correctAnswersNeeded;
    private float time_resolution =0;
    private boolean go_to_intro = false;


    public CvWorldController(miCeta game, Stage stage){
        // knock by default
        // too much and too many default values

        this(game,stage,FeedbackSoundType.KNOCK, Assets.instance.sounds.quitblock, Assets.instance.sounds.addblock, Assets.instance.sounds.yuju, 0);
        test_res =  Assets.instance.sounds.yuju;
    }
    public CvWorldController(miCeta game, Stage stage, FeedbackSoundType feedbackSound, Sound tooMuchErrorSound, Sound tooFewErrorSound, Sound test_resolution,  int game_n){
        // yuju by default
       // this(game,stage,feedbackSound, tooMuchErrorSound ,tooFewErrorSound, Assets.instance.sounds.yuju);
        this(game,stage,feedbackSound, tooMuchErrorSound ,tooFewErrorSound, test_resolution, test_resolution, game_n);
        test_res =  test_resolution;
        game_number = game_n;
    }

    public CvWorldController(miCeta game, Stage stage, FeedbackSoundType feedbackSound, Sound tooMuchErrorSound, Sound tooFewErrorSound, Sound positiveFeedback,  Sound test_resolution, int game_n) {
        this.game = game;
        this.stage = stage;
        this.feedbackSound = feedbackSound;
        this.tooMuchErrorSound = tooMuchErrorSound;
        this.tooFewErrorSound = tooFewErrorSound;
        this.positiveFeedback = positiveFeedback;
        test_res =  test_resolution;
        game_number = game_n;

        if((Gdx.app.getType() == Application.ApplicationType.Android)) {
            cvBlocksManager = new CvBlocksManagerAndroid(game, stage);
        }
        else {
            cvBlocksManager = new CvBlocksManagerDesktop(game, stage);
        }
        AudioManager.instance.setStage(stage); // we set current Stage in AudioManager, if not "reader" actor doesn't work
        initCustomSounds();
        initCommonVariables();
        init();
        initAnswersNeeded();

    }

    protected void initCustomSounds(){
        AudioManager.instance.setCustomSound(tooFewErrorSound, TOO_FEW);
        AudioManager.instance.setCustomSound(tooMuchErrorSound, TOO_MUCH);
        AudioManager.instance.setCustomSound(positiveFeedback, POSITIVE);
        AudioManager.instance.setFeedbackSoundType(FeedbackSoundType.KNOCK);
    }

    protected void initAnswersNeeded(){
        correctAnswersNow = 0;
        correctAnswersNeeded = LevelsManager.getInstance().get_level_size();
    }

    protected void init(){
        numberToPlay = LevelsManager.getInstance().get_number_to_play();
        setDelayForPositiveFeedback();
        Gdx.app.log(TAG,"init, Number to Play: " + numberToPlay );
        AudioManager.instance.readFeedback(numberToPlay, extraDelayBetweenFeedback); //first we read the random number
        timeToWait = Constants.READ_ONE_UNIT_DURATION+ numberToPlay*Constants.READ_ONE_UNIT_DURATION + waitAfterKnock /*+ ( randomNumber)*(0.3f)*/; // time we should wait before next loop starts
        answerRight = false;
    }

    protected void initCommonVariables(){
        timePassed = 0;
        extraDelayBetweenFeedback = GamePreferences.instance.getExtraDelayBetweenFeedback();
        waitAfterKnock = GamePreferences.instance.getWaitAfterKnock();
        waitAfterKnock = 3;
        inactivityTime = Constants.INACTIVITY_LIMIT;
        maxErrorsForHint = Constants.ERRORS_FOT_HINT;
        willGoToNextPart = false;
    }

    protected void updateCV(){
        if(cvBlocksManager.canBeUpdated()) { //ask before in order to not accumulate new threads.
            cvBlocksManager.updateDetected();
        }
        if(cvBlocksManager.isDetectionReady()){
            cvBlocksManager.analyseDetected();
        }
    }

    public void render(float deltaTime) {


        if ((go_to_intro)&&(timePassed > time_to_go)) {
            game.setScreen(new IntroScreen(game));
        }

        //Gdx.gl.glClearColor(0, 0, 0, 1);
    }


        public void update(float deltaTime) {
        timePassed+=deltaTime; // variable used to check in isTimeToStartNewLoop() to decide if new feedback loop should be started
        inactivityTime+=deltaTime;
        updateCV();

        if(isTimeToStartNewLoop()){
            if(!willGoToNextPart) {
                timePassed = 0; // start to count the time
                ArrayList<Integer> nowDetected = cvBlocksManager.getNewDetectedVals(); // to know the blocks on the table
                lastSum = currentSum;
                currentSum = 0;

                for (int i = 0; i < nowDetected.size(); i++)
                    currentSum += nowDetected.get(i); // we need to know the sum to decide if response is correct

                answerRight = (currentSum == numberToPlay);
                timeToWait = calculateTimeToWait(currentSum, numberToPlay);
                if (answerRight) {
                    correctAnswersNow+=1;
                    addPositiveFeedbackTimeToTimeToWait();
                    reproduceAllFeedbacksAndPositive(nowDetected, numberToPlay);
                    if(correctAnswersNow == correctAnswersNeeded) {
                        willGoToNextPart = true;
                    }
                    onCorrectAnswer(); //change number!
                    resetErrorsAndInactivity();
                    currentSum = 0; // we "reset" current sum to detect errors
                } else {
                    checkForErrorsAndInactivity(currentSum, lastSum);
                    reproduceAllFeedbacks(nowDetected, numberToPlay);
                }
            }else{
                timePassed = 0;
                go_to_intro = true;

                //audio de resolucion antes de cambiar la pantalla

                switch(game_number){
                    case 2:
                    case 3:
                        time_to_go = AudioManager.instance.reproduce_Game_3(1,1);
                    case 4:
                        time_to_go = AudioManager.instance.reproduce_Game_4(1,1);
                    case 5:
                        time_to_go = AudioManager.instance.reproduce_Game_5(1,1);
                }
               // time_to_go = AudioManager.instance.reproduce_Game_5(1,1);
            }
        }
    }

    protected void reproduceAllFeedbacks(ArrayList<Integer> nowDetected, int numberToPlay ){
        AudioManager.instance.readAllFeedbacks(nowDetected, numberToPlay, extraDelayBetweenFeedback);
    }

    protected void onCorrectAnswer(){
        LevelsManager.getInstance().up_operation_index();
        numberToPlay = LevelsManager.getInstance().get_number_to_play();
        saveLevel();
    }

    protected void addPositiveFeedbackTimeToTimeToWait(){
        timeToWait += (delayForPositiveFeedback + Constants.WAIT_AFTER_CORRECT_ANSWER);
    }

    protected float calculateTimeToWait( int currentSum, int numberToPlay){
        int biggerNumber =  (currentSum > numberToPlay) ? currentSum : numberToPlay;
        float currentTimeToWait = biggerNumber * (Constants.READ_ONE_UNIT_DURATION + extraDelayBetweenFeedback)+ waitAfterKnock;

        return currentTimeToWait;

    }

    protected void reproduceAllFeedbacksAndPositive(ArrayList<Integer> nowDetected, int numberToPlay ){
        AudioManager.instance.readAllFeedbacksAndPositive(nowDetected, numberToPlay, extraDelayBetweenFeedback);
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

    public int getNewRandomNumber(int previous_rand, int min, int max){
        int rand = MathUtils.random(min, max);
        while (rand == previous_rand) {
            rand = MathUtils.random(min, max);
        }
        return rand;
    }


    protected void checkForErrorsAndInactivity(int currentSum, int lastSum){
        Gdx.app.log(TAG,currentSum+" "+lastSum+" "+error_max+" "+error_min);
        // check for errors
        if(currentSum != lastSum){ // we count errors or reset inactivity only if (currentSum != lastSum)
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

        if(inactivityTime >= inactivityLimit){ // this condition is important for both: min and max
            if (error_max >= maxErrorsForHint) {
                AudioManager.instance.setDelay_add(tooMuchErrorSound);
                Gdx.app.log(TAG,"%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%ERROR MAX ");
                error_max = maxErrorsForHint-1;
                error_min = maxErrorsForHint-1;
                inactivityTime = 0;
                addFeedbackDelayToTimeToWait();
            }
            if (error_min >= maxErrorsForHint){
                AudioManager.instance.setDelay_quit(tooFewErrorSound);
                Gdx.app.log(TAG,"########################################ERROR MIN ");
                error_min = maxErrorsForHint-1;
                error_max = maxErrorsForHint-1;
                addFeedbackDelayToTimeToWait();

            }
        }
    }

    private void addFeedbackDelayToTimeToWait(){
        timeToWait += Constants.FEEDBACK_DELAY;
    }






    protected void resetErrorsAndInactivity(){
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


    protected void saveLevel() {
        GamePreferences prefs = GamePreferences.instance;
        prefs.load();
        GamePreferences.instance.setLast_level(LevelsManager.getInstance().get_level());
        GamePreferences.instance.setOperation_index(LevelsManager.getInstance().get_operation_index());
        prefs.save();
    }

    protected void setDelayForPositiveFeedback(){
//        delayForPositiveFeedback = Assets.instance.getSoundDuration(Assets.instance.sounds.yuju);
        delayForPositiveFeedback = Assets.instance.getSoundDuration(test_res);
    }

}