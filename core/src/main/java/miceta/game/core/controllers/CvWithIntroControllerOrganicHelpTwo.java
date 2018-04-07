package miceta.game.core.controllers;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Stage;
import miceta.game.core.miCeta;
import miceta.game.core.util.AudioManager;
import miceta.game.core.util.Constants;
import miceta.game.core.util.FeedbackSoundType;

import java.util.ArrayList;

/**
 * Created by ewe on 3/23/18.
 */
public class CvWithIntroControllerOrganicHelpTwo extends  CvWithIntroController{


    public CvWithIntroControllerOrganicHelpTwo(miCeta game, Stage stage, FeedbackSoundType feedbackSound, Sound introSound, ArrayList<Sound> positiveFeedback, Sound tooFewErrorSound, Sound tooMuchErrorSound, Sound finalFeedback, boolean upLevel, boolean shouldRepeatTutorial) {
        super(game, stage, feedbackSound, introSound, positiveFeedback, tooFewErrorSound, tooMuchErrorSound, finalFeedback, upLevel, shouldRepeatTutorial, true); // isInOgranicHelpScreen = true
        // in organic we will read the numbers
        readNumberDelay =  Constants.READ_NUMBER_DURATION ;
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
