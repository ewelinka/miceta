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
import miceta.game.core.screens.OrganicHelpOneScreen;
import miceta.game.core.screens.IntroScreen;
import miceta.game.core.util.*;

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
    private int errors_now = 0;
    private int totalErrors;
    private int gameNumber =0;
    protected float inactivityTime =0; // time that passed since last move
    private int currentSum=-1; // we induce first error if nothing on the table
    private  int lastBlocksSum=0;
    float timeToWait;
    float timePassed;
    final CvBlocksManager cvBlocksManager;
    float extraDelayBetweenFeedback;
    private float feedbackDelay;
    float waitAfterKnock;
    private final Sound tooMuchErrorSound;
    protected final Sound tooFewErrorSound;
    private final Sound finalFeedback;
    private final Sound introSound;
    private final ArrayList<Sound>  positiveFeedback;
    private final FeedbackSoundType feedbackSound;
    int inactivityLimit;
    protected int maxErrorsForHint;
    boolean willGoToNextPart;
    float delayForPositiveFeedback;
    int correctAnswersNow;
    int correctAnswersNeeded;
    final boolean upLevel;
    private boolean goToThePast;
    final boolean shouldRepeatTutorial;
    protected float readNumberDelay;
    private boolean firstLoopWithNewPrice;


    public CvWorldController(miCeta game, Stage stage, FeedbackSoundType feedbackSound, Sound introSound, ArrayList<Sound> positiveFeedback, Sound tooFewErrorSound,  Sound tooMuchErrorSound, Sound finalFeedback, boolean upLevel, boolean shouldRepeatTutorial) {
        this.game = game;
        Stage stage1 = stage;
        this.feedbackSound = feedbackSound;
        this.tooMuchErrorSound = tooMuchErrorSound;
        this.tooFewErrorSound = tooFewErrorSound;
        this.positiveFeedback = positiveFeedback;
        this.finalFeedback = finalFeedback;
        this.introSound = introSound;
        this.upLevel = upLevel;
        this.shouldRepeatTutorial = shouldRepeatTutorial;


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
        AudioManager.instance.setFeedbackSoundTypeAndLastClueIndex(feedbackSound);
        AudioManager.instance.setCurrentClue();

    }

    void initAnswersNeeded(){
        correctAnswersNow = 0;
        correctAnswersNeeded = LevelsManager.instance.getOperationsNumberToFinishLevel();
    }

    void init(){
        numberToPlay = LevelsManager.instance.get_number_to_play();
        setDelayForPositiveFeedback();
        reproduceClue();
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
        totalErrors = 0;
        goToThePast = false;
        readNumberDelay = 0;
        firstLoopWithNewPrice = true;

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
            checkIfFirstLoopWithNewPrice();
            checkForTotalErrors();
            if(!willGoToNextPart) {
                timePassed = 0; // start to count the time
                ArrayList<Integer> nowDetected = cvBlocksManager.getNewDetectedVals(); // to know the blocks on the table
                int lastSum = currentSum;

                currentSum = 0;

                for (Integer aNowDetected : nowDetected)
                    currentSum += aNowDetected; // we need to know the sum to decide if response is correct

                answerRight = (currentSum == numberToPlay);
                checkIfNewIntentToRegister(currentSum,lastBlocksSum,numberToPlay,answerRight,nowDetected);
                lastBlocksSum = currentSum;

                timeToWait = calculateTimeToWait(currentSum, numberToPlay);
                if (answerRight) {
                    correctAnswersNow+=1;
                    Gdx.app.log(TAG,"correctAnswersNow "+correctAnswersNow +" correctAnswersNeeded "+correctAnswersNeeded);
                    if(correctAnswersNow == correctAnswersNeeded) {
                        willGoToNextPart = true;
                        addFinalAudioToTimeToWait();
                        reproduceAllFeedbacksAndFinal(nowDetected, numberToPlay);
                    }else{
                        addPositiveFeedbackTimeToTimeToWait();
                        reproduceAllFeedbacksAndPositive(nowDetected, numberToPlay);
                    }
                    onCorrectAnswer(); //change number!
                    resetErrorsAndInactivity();
                    currentSum = -1; // we "reset" current sum to detect errors
                } else {
                    checkForErrorsAndInactivity(currentSum, lastSum);
                    reproduceAllFeedbacks(nowDetected, numberToPlay);
                }
            }else{
                if(!goToThePast)
                    goToNextLevel();

            }
        }
    }



    void onCorrectAnswer(){
        boolean levelFinished = LevelsManager.instance.up_operation_index();
        if(!levelFinished){
            numberToPlay = LevelsManager.instance.get_number_to_play();
        }

    }

    void checkIfFirstLoopWithNewPrice(){
        if(firstLoopWithNewPrice){
            game.resultsManager.newPriceAppeared(LevelsManager.instance.getOperationIndex()+1,LevelsManager.instance.get_level());
            firstLoopWithNewPrice = false;
        }
    }

    void checkIfNewIntentToRegister(int currentSumNow ,int lastSum, int numberToPlayNow, boolean answerWasRight, ArrayList<Integer>  nowDetected){
        if(currentSum!=lastSum) {
            game.resultsManager.addIntent(answerWasRight, currentSumNow, numberToPlayNow, nowDetected);
            if(answerWasRight) firstLoopWithNewPrice = true;
        }
    }

    private void addPositiveFeedbackTimeToTimeToWait(){
            timeToWait += (delayForPositiveFeedback + Constants.WAIT_AFTER_CORRECT_ANSWER);
    }

    private void addFinalAudioToTimeToWait(){
        timeToWait += Assets.instance.getSoundDuration(this.finalFeedback);
    }

    private float calculateTimeToWait(int currentSum, int numberToPlay){
        int biggerNumber =  (currentSum > numberToPlay) ? currentSum : numberToPlay;

        return readNumberDelay + biggerNumber * (Constants.READ_ONE_UNIT_DURATION + extraDelayBetweenFeedback)+ waitAfterKnock;

    }

    protected void reproduceAllFeedbacksAndPositive(ArrayList<Integer> nowDetected, int numberToPlay){
        AudioManager.instance.readAllFeedbacksAndPositive(nowDetected, numberToPlay, extraDelayBetweenFeedback);
    }

    protected void reproduceAllFeedbacksAndFinal(ArrayList<Integer> nowDetected, int numberToPlay){
        AudioManager.instance.readAllFeedbacksAndFinal(nowDetected, numberToPlay, extraDelayBetweenFeedback);
    }

    protected void reproduceAllFeedbacks(ArrayList<Integer> nowDetected, int numberToPlay){
        AudioManager.instance.readAllFeedbacks(nowDetected, numberToPlay, extraDelayBetweenFeedback);
    }

    protected void reproduceClue(){
        AudioManager.instance.readFeedback(numberToPlay, extraDelayBetweenFeedback); //first we read the random number
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

    protected void checkForTotalErrors(){
        Gdx.app.log(TAG,"check errors! "+totalErrors+" "+(totalErrors >= Constants.ERRORS_FOR_REPEAT_TUTORIAL));
        if(totalErrors >= Constants.ERRORS_FOR_REPEAT_TUTORIAL){
            AudioManager.instance.stop_sounds(game.getGameScreen().screenName);
            goToThePast = true;
            willGoToNextPart = true;
            resetErrorsAndInactivity();
            game.setScreen(new OrganicHelpOneScreen(game, false, true)); // we do not do upLevel but we repeat tutorial
        }
    }


    protected void checkForErrorsAndInactivity(int currentSum, int lastSum){
        // check for errors
        if(currentSum != lastSum){ // we count errors or reset inactivity only if (currentSum != lastSum)
            inactivityTime = 0;
            totalErrors+=1;
            errors_now +=1;
        }
        Gdx.app.log(TAG,currentSum+" "+lastSum+" "+errors_now+" total errors: "+totalErrors);

        if((inactivityTime >= inactivityLimit) && (errors_now >= maxErrorsForHint)){ // we have enough errors!
            if(currentSum > numberToPlay){ // too much!
                AudioManager.instance.setDelay_add();
                Gdx.app.log(TAG,"%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%ERROR MAX ");
                addFeedbackDelayToTimeToWait();
            }else{ // too few!
                AudioManager.instance.setDelay_quit();
                Gdx.app.log(TAG,"########################################ERROR MIN ");
                addFeedbackDelayToTimeToWait();

            }
            //errors_now = 0; to repeat clue if the child didn't changed anything
            inactivityTime = 0;
        }
    }

    private void addFeedbackDelayToTimeToWait(){
        timeToWait += feedbackDelay;
    }

    private void resetErrorsAndInactivity(){
        errors_now = 0;
        inactivityTime = 0;
        totalErrors = 0;
    }

    public void touchDownDesktop(int screenX, int screenY, int button){ //TODO clean what we don't use

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
            game.goToLastScreen(shouldRepeatTutorial);
    }

    public void forceScreenFinish(){
        Gdx.app.log(TAG," force screen finish! ");
        goToNextLevel();
    }

}
