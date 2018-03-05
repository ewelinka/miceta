package miceta.game.core.controllers;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
    final miCeta game;
   // private int randomNumber,previousRandomNumber;
    int numberToPlay;
    boolean answerRight;
    private int error_min = 0;
    private int error_max = 0;
    private int gameNumber =0;
    private float inactivityTime =0; // time that passed since last move
    private int currentSum=0;
    float timeToWait;
    float timePassed;
    final CvBlocksManager cvBlocksManager;
    float extraDelayBetweenFeedback;
    private float feedbackDelay;
    float waitAfterKnock;
    private final Sound tooMuchErrorSound;
    private final Sound tooFewErrorSound;
    private final Sound finalFeedback;
    private final Sound introSound;
    private final ArrayList<Sound>  positiveFeedback;
    private final FeedbackSoundType feedbackSound;
    int inactivityLimit;
    private int maxErrorsForHint;
    boolean willGoToNextPart;
    float delayForPositiveFeedback;
    int correctAnswersNow;
    int correctAnswersNeeded;
    private float readNumberDelay;
    final boolean upLevel;


    public CvWorldController(miCeta game, Stage stage){
        this(game,stage,false);
    }
    CvWorldController(miCeta game, Stage stage, boolean upLevel){
        // knock by default
        // too much and too many default values
        this(game,stage,FeedbackSoundType.KNOCK, Assets.instance.sounds.newblock, Assets.instance.sounds.positivesFeedbacks, Assets.instance.sounds.addblock, Assets.instance.sounds.quitblock, Assets.instance.sounds.yuju, upLevel);
    }

    public CvWorldController(miCeta game, Stage stage, FeedbackSoundType feedbackSound, Sound introSound, ArrayList<Sound> positiveFeedback, Sound tooFewErrorSound,  Sound tooMuchErrorSound, Sound finalFeedback, boolean upLevel) {
        this.game = game;
        Stage stage1 = stage;
        this.feedbackSound = feedbackSound;
        this.tooMuchErrorSound = tooMuchErrorSound;
        this.tooFewErrorSound = tooFewErrorSound;
        this.positiveFeedback = positiveFeedback;
        this.finalFeedback = finalFeedback;
        this.introSound = introSound;
        this.upLevel = upLevel;


        if((Gdx.app.getType() == Application.ApplicationType.Android)) {
            cvBlocksManager = new CvBlocksManagerAndroid(game);
        }
        else {
            cvBlocksManager = new CvBlocksManagerDesktop(game);
        }
        AudioManager.instance.setStage(stage); // we set current Stage in AudioManager, if not "reader" actor doesn't work
        initCustomSounds();
        initCommonVariables();
        init();
        initAnswersNeeded();

    }

    private void initCustomSounds(){
        Gdx.app.log(TAG,"init custom sounds!!!");
        AudioManager.instance.setCustomSound(tooFewErrorSound, TOO_FEW);
        AudioManager.instance.setCustomSound(tooMuchErrorSound, TOO_MUCH);
        AudioManager.instance.setCustomSound(finalFeedback, FINAL);
        AudioManager.instance.setCustomSound(introSound, INTRO);
        AudioManager.instance.setCustomSoundArray(POSITIVE, positiveFeedback);
        AudioManager.instance.setFeedbackSoundType(feedbackSound);
        AudioManager.instance.setCurrentClue();

    }

    void initAnswersNeeded(){
        correctAnswersNow = 0;
        correctAnswersNeeded = LevelsManager.instance.getOperationsNumberToFinishLevel();
    }

    void init(){
        numberToPlay = LevelsManager.instance.get_number_to_play();
        setDelayForPositiveFeedback();
        AudioManager.instance.readFeedback(numberToPlay, extraDelayBetweenFeedback); //first we read the random number
        timeToWait = Constants.READ_ONE_UNIT_DURATION+ numberToPlay*Constants.READ_ONE_UNIT_DURATION + waitAfterKnock /*+ ( randomNumber)*(0.3f)*/; // time we should wait before next loop starts
        answerRight = false;
    }

    private void initCommonVariables(){
        timePassed = 0;
        extraDelayBetweenFeedback = GamePreferences.instance.getExtraDelayBetweenFeedback();
        waitAfterKnock = GamePreferences.instance.getWaitAfterKnock();
        inactivityLimit = Constants.INACTIVITY_LIMIT;
        maxErrorsForHint = Constants.ERRORS_FOT_HINT;
        willGoToNextPart = false;
        feedbackDelay = (Assets.instance.getSoundDuration(this.tooFewErrorSound) > Assets.instance.getSoundDuration(this.tooMuchErrorSound)) ? Assets.instance.getSoundDuration(this.tooFewErrorSound) : Assets.instance.getSoundDuration(this.tooMuchErrorSound);
        readNumberDelay = 0;

    }

    void updateCV(){
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
            Gdx.app.log(TAG,"isTimeToStartNewLoop and willGoToNextPart "+willGoToNextPart);
            if(!willGoToNextPart) {
                timePassed = 0; // start to count the time
                ArrayList<Integer> nowDetected = cvBlocksManager.getNewDetectedVals(); // to know the blocks on the table
                int lastSum = currentSum;
                currentSum = 0;

                for (Integer aNowDetected : nowDetected)
                    currentSum += aNowDetected; // we need to know the sum to decide if response is correct

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

    private void reproduceAllFeedbacks(ArrayList<Integer> nowDetected, int numberToPlay){
        if(gameNumber==1)
            AudioManager.instance.readNumberAndAllFeedbacks(nowDetected, numberToPlay, extraDelayBetweenFeedback);
        else
            AudioManager.instance.readAllFeedbacks(nowDetected, numberToPlay, extraDelayBetweenFeedback);
    }

    void onCorrectAnswer(){
        boolean levelFinished = LevelsManager.instance.up_operation_index();
        if(!levelFinished){
            numberToPlay = LevelsManager.instance.get_number_to_play();
        }

    }

    private void addPositiveFeedbackTimeToTimeToWait(){
            timeToWait += (delayForPositiveFeedback + Constants.WAIT_AFTER_CORRECT_ANSWER);
    }

    private float calculateTimeToWait(int currentSum, int numberToPlay){
        int biggerNumber =  (currentSum > numberToPlay) ? currentSum : numberToPlay;

        return readNumberDelay + biggerNumber * (Constants.READ_ONE_UNIT_DURATION + extraDelayBetweenFeedback)+ waitAfterKnock;

    }

    void reproduceAllFeedbacksAndPositive(ArrayList<Integer> nowDetected, int numberToPlay){
        if(gameNumber==1){
            AudioManager.instance.readNumberAllFeedbacksAndPositive(nowDetected, numberToPlay, extraDelayBetweenFeedback);
        }
        else{
            AudioManager.instance.readAllFeedbacksAndPositive(nowDetected, numberToPlay, extraDelayBetweenFeedback);
        }
    }

    private void reproduceAllFeedbacksAndFinal(ArrayList<Integer> nowDetected, int numberToPlay){
        // TODO ig game 1 we read the number
        if(gameNumber==1)
            AudioManager.instance.readNumberAllFeedbacksAndFinal(nowDetected, numberToPlay, extraDelayBetweenFeedback);
        else
            AudioManager.instance.readAllFeedbacksAndFinal(nowDetected, numberToPlay, extraDelayBetweenFeedback);
    }



    boolean isTimeToStartNewLoop(){
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


    private void checkForErrorsAndInactivity(int currentSum, int lastSum){

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

    private void resetErrorsAndInactivity(){
        error_min = 0;
        error_max = 0;
        inactivityTime = 0;
    }

    public void touchDownAndroid(int screenX, int screenY){ //TODO clean what we don't use
        if (screenX > 540 && screenY < 60) {
            game.setScreen(new BaseScreen(game, false));
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
    public void touchDownDesktop(int screenX, int screenY, int button){ //TODO clean what we don't use
        if (screenX > 440 && screenY < 10) {
            game.setScreen(new BaseScreen(game, false));
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

    void setDelayForPositiveFeedback(){
        //- cambiar mas adelante
        delayForPositiveFeedback = Assets.instance.getSoundDuration(this.positiveFeedback.get(0));
    }

    void goToNextLevel(){
        willGoToNextPart = true;
        if(upLevel)
            game.goToNextScreen();
        else
            game.goToLastScreen();
    }

    public void forceScreenFinish(){
        Gdx.app.log(TAG," force screen finish! ");
        goToNextLevel();
    }

    public void setGameNumber(int number){
        if(number == 1)
            readNumberDelay = Constants.READ_NUMBER_DURATION;
        gameNumber = number;
    }
}
