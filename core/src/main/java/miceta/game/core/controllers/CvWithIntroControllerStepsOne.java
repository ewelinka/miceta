package miceta.game.core.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Stage;
import miceta.game.core.miCeta;

import miceta.game.core.screens.BaseScreenWithIntro;
import miceta.game.core.screens.BaseScreenWithIntroReadNumber;
import miceta.game.core.screens.OrganicHelpOneScreen;
import miceta.game.core.util.AudioManager;
import miceta.game.core.util.Constants;
import miceta.game.core.util.FeedbackSoundType;
import miceta.game.core.util.ScreenName;

import java.util.ArrayList;

/**
 * Created by ewe on 2/2/18.
 */
public class CvWithIntroControllerStepsOne extends CvWithIntroController {

    public CvWithIntroControllerStepsOne(miCeta game, Stage stage, ScreenName screenNameNow, Sound introSound, ArrayList<Sound> positiveFeedback, Sound tooFewErrorSound, Sound tooMuchErrorSound, Sound finalFeedback, boolean upLevel, boolean shouldRepeatTutorial){
        super(game, stage, screenNameNow, introSound, positiveFeedback, tooFewErrorSound, tooMuchErrorSound, finalFeedback, upLevel, shouldRepeatTutorial, false);
    }

    public CvWithIntroControllerStepsOne(miCeta game, Stage stage, ScreenName screenNameNow, Sound introSound, ArrayList<Sound> positiveFeedback, Sound tooFewErrorSound, Sound tooMuchErrorSound, Sound finalFeedback, boolean upLevel, boolean shouldRepeatTutorial, boolean isInOgranicHelpScreen) {
        super(game, stage, screenNameNow, introSound, positiveFeedback, tooFewErrorSound, tooMuchErrorSound, finalFeedback, upLevel, shouldRepeatTutorial, isInOgranicHelpScreen); // we always come from organic to this controller!
    }

    @Override
    protected void goToNextLevel(){
        willGoToNextPart = true;
        game.setScreen(new BaseScreenWithIntroReadNumber(game, upLevel, shouldRepeatTutorial, ScreenName.GAME_STEPS)); // interactive part of the tutorial, we shouldn't do level up! and yes, we came from organic!
    }

    @Override
    protected void onCorrectAnswer() {

    }

    @Override
    protected void init(){
        super.init();
        numberToPlay = 1;
    }



    @Override
    protected void initAnswersNeeded(){
        correctAnswersNow = 0;
        correctAnswersNeeded = 1;
    }

    @Override
    protected void reproduceAllFeedbacks(ArrayList<Integer> nowDetected, int numberToPlay){
        AudioManager.instance.readNumberAndAllFeedbacks(nowDetected, numberToPlay, extraDelayBetweenFeedback);
    }

    @Override
    protected void reproduceAllFeedbacksAndPositive(ArrayList<Integer> nowDetected, int numberToPlay){
        AudioManager.instance.readNumberAllFeedbacksAndPositive(nowDetected, numberToPlay, extraDelayBetweenFeedback);
    }
    @Override
    protected void reproduceAllFeedbacksAndFinal(ArrayList<Integer> nowDetected, int numberToPlay){
        AudioManager.instance.readNumberAllFeedbacksAndFinal(nowDetected, numberToPlay, extraDelayBetweenFeedback);
    }

    @Override
    protected void reproduceClue(){
        AudioManager.instance.readNumberWithFeedback(numberToPlay, extraDelayBetweenFeedback); //first we read the random number
    }

}
