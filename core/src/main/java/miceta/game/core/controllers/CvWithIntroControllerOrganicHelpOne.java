package miceta.game.core.controllers;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Stage;
import miceta.game.core.miCeta;
import miceta.game.core.screens.BaseScreenWithIntroOrganicHelp;
import miceta.game.core.util.AudioManager;
import miceta.game.core.util.FeedbackSoundType;

import java.util.ArrayList;

/**
 * Created by ewe on 4/3/18.
 */
public class CvWithIntroControllerOrganicHelpOne extends CvWithIntroControllerStepsOne {
    public CvWithIntroControllerOrganicHelpOne(miCeta game, Stage stage, FeedbackSoundType feedbackSound, Sound introSound, ArrayList<Sound> positiveFeedback, Sound tooFewErrorSound, Sound tooMuchErrorSound, Sound finalFeedback, boolean upLevel, boolean shouldRepeatTutorial) {
        super(game, stage, feedbackSound, introSound, positiveFeedback, tooFewErrorSound, tooMuchErrorSound, finalFeedback, upLevel, shouldRepeatTutorial, true);
    }


    @Override
    protected void init(){
        numberToPlay = 1;
        setDelayForPositiveFeedback();
        timeToWait = AudioManager.instance.reproduceIntro() + 1; //first we read the intro and add some delay
    }

    @Override
    protected void goToNextLevel(){
        willGoToNextPart = true;
        game.setScreen(new BaseScreenWithIntroOrganicHelp(game, upLevel, shouldRepeatTutorial)); // interactive part of the tutorial, we shouldn't do level up! and yes, we came from organic!
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