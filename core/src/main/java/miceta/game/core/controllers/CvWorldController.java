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
import miceta.game.core.screens.BaseScreen;
import miceta.game.core.screens.FeedbackScreen;
import miceta.game.core.screens.IntroScreen;
import miceta.game.core.util.AudioManager;
import miceta.game.core.util.Constants;
import miceta.game.core.util.FeedbackSoundType;
import miceta.game.core.util.GamePreferences;

import java.util.ArrayList;
import java.util.Set;

import static miceta.game.core.util.CommonFeedbacks.*;


/**
 * Created by ewe on 8/10/17.
 */
public class CvWorldController {
    private static final String TAG = CvWorldController.class.getName();
    protected Stage stage;
    public miCeta game;
   // private int randomNumber,previousRandomNumber;
    protected int numberToPlay;
    protected boolean answerRight;
    private int error_min = 0;
    private int error_max = 0;
    private int gameNumber =0;
    protected float inactivityTime =0; // time that passed since last move
    protected int currentSum=0;
    protected int lastSum=0;
    protected float timeToWait, timePassed;
    protected CvBlocksManager cvBlocksManager;
    protected float extraDelayBetweenFeedback;
    protected float feedbackDelay;
    protected float waitAfterKnock;
    protected Sound tooMuchErrorSound, tooFewErrorSound, finalFeedback, introSound;
    protected ArrayList<Sound>  positiveFeedback;
    protected FeedbackSoundType feedbackSound;
    protected int inactivityLimit;
    protected int maxErrorsForHint;
    protected boolean willGoToNextPart;
    protected float delayForPositiveFeedback;
    protected int correctAnswersNow;
    protected int correctAnswersNeeded;


    public CvWorldController(miCeta game, Stage stage){
        // knock by default
        // too much and too many default values
        this(game,stage,FeedbackSoundType.KNOCK, Assets.instance.sounds.newblock, Assets.instance.sounds.positivesFeedbacks, Assets.instance.sounds.addblock, Assets.instance.sounds.quitblock, Assets.instance.sounds.yuju);
    }
    public CvWorldController(miCeta game, Stage stage, FeedbackSoundType feedbackSound,  Sound tooFewErrorSound, Sound tooMuchErrorSound){
        // yuju by default

       // Assets.instance.sounds.positivesFeedbacks.add(Assets.instance.sounds.yuju);

        this(game,stage,feedbackSound, Assets.instance.sounds.newblock, Assets.instance.sounds.positivesFeedbacks, tooFewErrorSound, tooMuchErrorSound ,Assets.instance.sounds.yuju );
    }

    public CvWorldController(miCeta game, Stage stage, FeedbackSoundType feedbackSound, Sound introSound, ArrayList<Sound> positiveFeedback, Sound tooFewErrorSound,  Sound tooMuchErrorSound, Sound finalFeedback) {
        this.game = game;
        this.stage = stage;
        this.feedbackSound = feedbackSound;
        this.tooMuchErrorSound = tooMuchErrorSound;
        this.tooFewErrorSound = tooFewErrorSound;
        this.positiveFeedback = positiveFeedback;
        this.finalFeedback = finalFeedback;
        this.introSound = introSound;


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
        AudioManager.instance.setCustomSound(tooFewErrorSound, TOO_FEW, null);
        AudioManager.instance.setCustomSound(tooMuchErrorSound, TOO_MUCH, null);
        AudioManager.instance.setCustomSound(null, POSITIVE, positiveFeedback);
        AudioManager.instance.setCustomSound(finalFeedback, FINAL, null);
        AudioManager.instance.setCustomSound(introSound, INTRO, null);
        AudioManager.instance.setFeedbackSoundType(feedbackSound);

    }

    protected void initAnswersNeeded(){
        correctAnswersNow = 0;
        correctAnswersNeeded = LevelsManager.instance.getOperationsNumberToFinishLevel();
    }

    protected void init(){
        numberToPlay = LevelsManager.getInstance().get_number_to_play();
        setDelayForPositiveFeedback();


            AudioManager.instance.readFeedback(numberToPlay, extraDelayBetweenFeedback); //first we read the random number

        timeToWait = Constants.READ_ONE_UNIT_DURATION+ numberToPlay*Constants.READ_ONE_UNIT_DURATION + waitAfterKnock /*+ ( randomNumber)*(0.3f)*/; // time we should wait before next loop starts
        answerRight = false;
    }

    protected void initCommonVariables(){
        timePassed = 0;
        extraDelayBetweenFeedback = GamePreferences.instance.getExtraDelayBetweenFeedback();
        waitAfterKnock = GamePreferences.instance.getWaitAfterKnock();
        inactivityLimit = Constants.INACTIVITY_LIMIT;
        maxErrorsForHint = Constants.ERRORS_FOT_HINT;
        willGoToNextPart = false;
        feedbackDelay = (Assets.instance.getSoundDuration(this.tooFewErrorSound) > Assets.instance.getSoundDuration(this.tooMuchErrorSound)) ? Assets.instance.getSoundDuration(this.tooFewErrorSound) : Assets.instance.getSoundDuration(this.tooMuchErrorSound);


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
            Gdx.app.log(TAG,"GAME NUMBER " + gameNumber);

            Gdx.app.log(TAG,"isTimeToStartNewLoop and willGoToNextPart "+willGoToNextPart);
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

                    Gdx.app.log(TAG,"correctAnswersNow "+correctAnswersNow +" correctAnswersNeeded "+correctAnswersNeeded);
                    if(correctAnswersNow == correctAnswersNeeded) {
                        willGoToNextPart = true;
                        reproduceAllFeedbacksAndFinal(nowDetected, numberToPlay);
                    }else{
                        reproduceAllFeedbacksAndPositive(nowDetected, numberToPlay);
                    }
                    onCorrectAnswer(); //change number!
                    resetErrorsAndInactivity();
                    currentSum = 0; // we "reset" current sum to detect errors
                } else {
                    checkForErrorsAndInactivity(currentSum, lastSum);
                    reproduceAllFeedbacks(nowDetected, numberToPlay);
                }
            }else{
                goToNextLevel();

            }
        }
    }

    protected void reproduceAllFeedbacks(ArrayList<Integer> nowDetected, int numberToPlay ){

        if (gameNumber == 1)
        {
            AudioManager.instance.setNumberToPlayAndIsWithNumber(numberToPlay, true);
        }
        else
        {
            AudioManager.instance.setNumberToPlayAndIsWithNumber(numberToPlay, false);
        }

        AudioManager.instance.readAllFeedbacks(nowDetected, numberToPlay, extraDelayBetweenFeedback);

    }

    protected void onCorrectAnswer(){
        boolean levelFinished = LevelsManager.getInstance().up_operation_index();
        if(!levelFinished){
            numberToPlay = LevelsManager.getInstance().get_number_to_play();
        }

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

    protected void reproduceAllFeedbacksAndFinal(ArrayList<Integer> nowDetected, int numberToPlay ){
        AudioManager.instance.readAllFeedbacksAndFinal(nowDetected, numberToPlay, extraDelayBetweenFeedback);
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
        Gdx.app.log(TAG,currentSum+" "+lastSum+" "+error_max+" "+error_min);
        // check if there are sufficient errors and inactivity time

        if(inactivityTime >= inactivityLimit){ // this condition is important for both: min and max
            if (error_max >= maxErrorsForHint) {
                AudioManager.instance.setDelay_add();
                Gdx.app.log(TAG,"%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%ERROR MAX ");
                error_max = maxErrorsForHint-1;
                error_min = maxErrorsForHint-1;
                inactivityTime = 0;
                addFeedbackDelayToTimeToWait();
            }
            if (error_min >= maxErrorsForHint){
                AudioManager.instance.setDelay_quit();
                Gdx.app.log(TAG,"########################################ERROR MIN ");
                error_min = maxErrorsForHint-1;
                error_max = maxErrorsForHint-1;
                addFeedbackDelayToTimeToWait();

            }
        }
    }

    private void addFeedbackDelayToTimeToWait(){
        timeToWait += feedbackDelay;
    }

    protected void resetErrorsAndInactivity(){
        error_min = 0;
        error_max = 0;
        inactivityTime = 0;
    }

    public void touchDownAndroid(int screenX, int screenY, int button){
        if (screenX > 540 && screenY < 60) {
            game.setScreen(new BaseScreen(game));
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
    public void touchDownDesktop(int screenX, int screenY, int button){
        if (screenX > 440 && screenY < 10) {
            game.setScreen(new BaseScreen(game));
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

    protected void setDelayForPositiveFeedback(){
        //- cambiar mas adelante
        delayForPositiveFeedback = Assets.instance.getSoundDuration(this.positiveFeedback.get(0));
    }

    protected void goToNextLevel(){
        willGoToNextPart = true;
        game.goToNextScreen();
    }

    public void setGameNumber(int number){
        gameNumber = number;
    }
}
