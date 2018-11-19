package miceta.game.core.controllers;

import static miceta.game.core.util.CommonFeedbacks.FINAL;
import static miceta.game.core.util.CommonFeedbacks.INTRO;
import static miceta.game.core.util.CommonFeedbacks.POSITIVE;
import static miceta.game.core.util.CommonFeedbacks.TOO_FEW;
import static miceta.game.core.util.CommonFeedbacks.TOO_MUCH;
import static miceta.game.core.util.ScreenName.ORGANIC_HELP;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import miceta.game.core.Assets;
import miceta.game.core.miCeta;
import miceta.game.core.managers.CvBlocksManager;
import miceta.game.core.managers.CvBlocksManagerAndroid;
import miceta.game.core.managers.CvBlocksManagerDesktop;
import miceta.game.core.managers.LevelsManager;
import miceta.game.core.managers.TangibleBlocksManager;
import miceta.game.core.screens.IntroScreen;
import miceta.game.core.screens.OrganicHelpOneScreen;
import miceta.game.core.util.AudioManager;
import miceta.game.core.util.CompositionData;
import miceta.game.core.util.Constants;
import miceta.game.core.util.GamePreferences;
import miceta.game.core.util.ScreenName;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;

import edu.ceta.vision.core.blocks.Block;


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
    private final ScreenName screenNameNow;
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
    protected TangibleBlocksManager blocksManager;

	private boolean timeTosendComposition;

	
	/*smarichal*/
	private boolean singleLoopMode = true;
	private boolean noBlocksFeedbackMode = true; // only feedback of the number to build TODO: add menu configuration for this mode
	
	
	private int silentFeedbackMode;
	private boolean interruptLoopnewBlockDetected;
	private int errorOrSuccessBlocksFeedbackMode;
	private float hintsDelay;
	
	/*----------*/
	
    public CvWorldController(miCeta game, Stage stage, ScreenName screenNameNow, Sound introSound, ArrayList<Sound> positiveFeedback, Sound tooFewErrorSound,  Sound tooMuchErrorSound, Sound finalFeedback, boolean upLevel, boolean shouldRepeatTutorial) {
        this.game = game;
        this.blocksManager = game.getBlocksManager(); //TEST 
        this.screenNameNow = screenNameNow;
        this.tooMuchErrorSound = tooMuchErrorSound;
        this.tooFewErrorSound = tooFewErrorSound;
        this.positiveFeedback = positiveFeedback;
        this.finalFeedback = finalFeedback;
        this.introSound = introSound;
        this.upLevel = upLevel;
        this.shouldRepeatTutorial = shouldRepeatTutorial;
        this.interruptLoopnewBlockDetected = false;
        this.timeTosendComposition = false;
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
        AudioManager.instance.setScreenNameAndLastClueIndex(screenNameNow);
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
        silentFeedbackMode = GamePreferences.instance.getSilentFeedbackMode();
        errorOrSuccessBlocksFeedbackMode = GamePreferences.instance.getErrorOrSuccessBlocksFeedbackMode();
        AudioManager.instance.setSuccessErrorBlocksFeedbackMode(errorOrSuccessBlocksFeedbackMode==1);
        
        inactivityLimit = Constants.INACTIVITY_LIMIT;
        maxErrorsForHint = Constants.ERRORS_FOT_HINT;
        willGoToNextPart = false;
        feedbackDelay = (Assets.instance.getSoundDuration(this.tooFewErrorSound) > Assets.instance.getSoundDuration(this.tooMuchErrorSound)) ? Assets.instance.getSoundDuration(this.tooFewErrorSound) : Assets.instance.getSoundDuration(this.tooMuchErrorSound);
        hintsDelay = getMaxHintsDuration();
        totalErrors = 0;
        goToThePast = false;
        readNumberDelay = 0;
        firstLoopWithNewPrice = true;

    }

    private float getMaxHintsDuration(){
    	float max = 0;
        for(Iterator<Sound> iter = Assets.instance.sounds.hints.iterator();iter.hasNext();){
        	Sound sound = iter.next();
        	if(Assets.instance.getSoundDuration(sound) > max )
        		max = Assets.instance.getSoundDuration(sound);
        }
        return max;
    }
    
    void updateCV(){
        if(cvBlocksManager.canBeUpdated()) { //ask before in order to not accumulate new threads.
            cvBlocksManager.updateDetected();
        }
        if(cvBlocksManager.isDetectionReady()){
            cvBlocksManager.analyseDetected();
        }
    }

    void updateTangible(){
    	cvBlocksManager.updateTangibleBlocksDetected(convertTangibleToCVblocks(blocksManager.getDetectedBlocks()));
    	cvBlocksManager.analyseTangibleBlocksDetected();    	
    }
    
    private Set<Block> convertTangibleToCVblocks(Set<miceta.game.core.receiver.Block> tangibleBlocks){
    	Set<Block> result = new HashSet<Block>();
    	for(Iterator<miceta.game.core.receiver.Block> iter =tangibleBlocks.iterator();iter.hasNext(); ){
    		miceta.game.core.receiver.Block tangibleBlock = iter.next();
    		Block cvBlock = new Block(tangibleBlock.getValue());
    		cvBlock.setId(tangibleBlock.getId());
    		result.add(cvBlock);
    	}
    	return result;    	
    }

    private long lastChange = new Date().getTime();
	
    public void update(float deltaTime) {
        timePassed+=deltaTime; // variable used to check in isTimeToStartNewLoop() to decide if new feedback loop should be started
        inactivityTime+=deltaTime;
       // updateCV();// TEST removed the updateCV
        if(!firstLoopWithNewPrice) {
            //ArrayList<Integer> nowDetectedIds = cvBlocksManager.getNewDetectedIds(); TEST: smarichal
        	ArrayList<Integer> nowDetectedIds = blocksManager.getDetectedIds();
        	updateTangible();
            if(game.resultsManager.analyseDetectedIds(nowDetectedIds, blocksManager.getDetectedBlocks(), numberToPlay)){
//            	long now = new Date().getTime();
//            	if(now-lastChange > 3000){
//            		lastChange = now;
//	                Gdx.app.log(TAG,"INTERRUPT, SOMETHING CHANGED");//TODO continuar aca, cuando saco una pieza no corta el loop en respuesta correcta.
//	            	interruptLoopnewBlockDetected = true;
//	            	answerRight = false;
//	            	AudioManager.instance.stop_sounds(this.game.getGameScreen().screenName);
//            	}
            }else if(timeTosendComposition){ 
                	executeMixedFeedback();  //add time to wait and send a composition message to the blocks on the working area  
                	timeTosendComposition = false;
            }
        }else{
          //  Gdx.app.log(TAG,"firstLoopWithNewPrice");
            /*el problema es que cuando el sistema detecta la respuesta correcta la registra inmediatamente. Lo ideal seria esperar
             * a que termine el loop y si al final del loop los bloques son los correctos entonces registrarla. 
             * */
        }

        if(isTimeToStartNewLoop()){
            Gdx.app.log(TAG,"STARTING NEW LOOP");
            checkIfFirstLoopWithNewPrice();
            checkForTotalErrors();
            if(!willGoToNextPart) {
            	interruptLoopnewBlockDetected = false;
                timePassed = 0; // start to count the time
                int thisLoopNumerToPlay = numberToPlay;
                ArrayList<Integer> nowDetected = blocksManager.getDetectedVals();
                int lastSum = currentSum;
                currentSum = 0;
                for (Integer aNowDetected : nowDetected)
                    currentSum += aNowDetected; // we need to know the sum to decide if response is correct

                answerRight = (currentSum == numberToPlay);
                timeToWait = calculateTimeToWait(currentSum, numberToPlay);
                if (answerRight) {
                    correctAnswersNow+=1;
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
                    //currentSum = -1; // we "reset" current sum to detect errors
                } else {
                    checkForErrorsAndInactivity(currentSum, lastSum);
                    if(noBlocksFeedbackMode)
                    	reproduceFeedbackWithoutBlocks(numberToPlay);
                    else
                    	reproduceAllFeedbacks(nowDetected, numberToPlay);
                }
                checkIfTimeToAdd(timeToWait);
                checkIfNewIntentToRegister(currentSum,lastBlocksSum,answerRight? thisLoopNumerToPlay: numberToPlay,answerRight,nowDetected);
                lastBlocksSum = currentSum;
            }else{
                if(!goToThePast)
                    goToNextLevel();

            }
            if(currentSum > 0 && GamePreferences.instance.getMixedFeedbackMode()==1){//---> no funciona ponerlo aqui porq primero inicia el loop y luego envia la composicion
            	timeTosendComposition = true;
            }
        }
    }


    void executeMixedFeedback(){
    	CompositionData compData = blocksManager.composeAndSendBlocksInArea();
    	timeToWait+=((float)calculateBlocksCompositionTime(compData)/1000.f);
    }
    
    int calculateBlocksCompositionTime(CompositionData compData){
    	int res = 0;
    	res = (compData.composed_n * compData.interbeep_delay) + compData.start_delay+ compData.cicle_delay;
    	return res;
    }
    
    void onCorrectAnswer(){
        boolean levelFinished = LevelsManager.instance.up_operation_index();
        if(!levelFinished){
            numberToPlay = LevelsManager.instance.get_number_to_play();
        }

    }

    void checkIfFirstLoopWithNewPrice(){
        if(firstLoopWithNewPrice){
            game.resultsManager.newPriceAppeared(LevelsManager.instance.getOperationIndex()+1, game.gameScreen.screenName == ORGANIC_HELP? -1 : LevelsManager.instance.get_level());
//            firstLoopWithNewPrice = false;
        }
    }

    void checkIfTimeToAdd(float timeToAdd){
        if(firstLoopWithNewPrice) {
            game.resultsManager.setFeedbackDuration(timeToAdd);
            firstLoopWithNewPrice = false;
        }
    }

    void checkIfNewIntentToRegister(int currentSumNow ,int lastSum, int numberToPlayNow, boolean answerWasRight, ArrayList<Integer>  nowDetected){
        if(currentSumNow!=lastSum) {
            game.resultsManager.addIntentFromActionSubmit(answerWasRight, currentSumNow, numberToPlayNow, nowDetected);
            if(answerWasRight) firstLoopWithNewPrice = true;
        }
    }

    private void addPositiveFeedbackTimeToTimeToWait(){
            timeToWait += (delayForPositiveFeedback + Constants.WAIT_AFTER_CORRECT_ANSWER);
    }

    private void addFinalAudioToTimeToWait(){
        timeToWait += Assets.instance.getSoundDuration(this.finalFeedback);
    }

    private void addNextHintToTimeToWait(){
//        Gdx.app.log(TAG,"adding hintdelay= "+ (hintsDelay + 1));
   // 	timeToWait += 10;//hintsDelay + 1;
    }
    
    private float calculateTimeToWait(int currentSum, int numberToPlay){
        int biggerNumber =  (currentSum > numberToPlay) ? currentSum : numberToPlay;
        Gdx.app.log(TAG,"biggerNumber is: "+biggerNumber);
        return readNumberDelay + biggerNumber * (Constants.READ_ONE_UNIT_DURATION + extraDelayBetweenFeedback)+ waitAfterKnock + hintsDelay;
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
    
    protected void reproduceFeedbackWithoutBlocks(int numberToPlay){
    	AudioManager.instance.readFeedbackWithoutBlocks(numberToPlay,extraDelayBetweenFeedback);
    }

    protected void reproduceClue(){
        AudioManager.instance.readFeedback(numberToPlay, extraDelayBetweenFeedback); //first we read the random number
    }



    boolean isTimeToStartNewLoop(){
        return (timePassed > timeToWait ) || interruptLoopnewBlockDetected;
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
       // Gdx.app.log(TAG,"check errors! "+totalErrors+" "+(totalErrors >= Constants.ERRORS_FOR_REPEAT_TUTORIAL));
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
        }else if(singleLoopMode){
            Gdx.app.log(TAG,"%%%%%%%% SINGLE LOOP MODE - PLAYING HINT ");
        	addNextHintToTimeToWait();
        	 if(currentSum > numberToPlay){ // too much!
                 AudioManager.instance.setDelay_hint_add();
                 Gdx.app.log(TAG,"hint add");
             }else{ // too few!
            	 AudioManager.instance.setDelay_hint_quit();
                 Gdx.app.log(TAG,"hint quit");
             }
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
    
    private void toogleSilentMode(){
        silentFeedbackMode = (silentFeedbackMode+1)%2;
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
        GamePreferences.instance.setSilentFeedbackMode(silentFeedbackMode);
        prefs.save();
    }

    void setDelayForPositiveFeedback(){
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
