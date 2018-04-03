package miceta.game.core.controllers;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Stage;
import miceta.game.core.miCeta;

import miceta.game.core.screens.BaseScreenWithIntro;
import miceta.game.core.util.FeedbackSoundType;

import java.util.ArrayList;

/**
 * Created by ewe on 2/2/18.
 */
public class CvWithIntroControllerStepsOne extends CvWithIntroController {

    public CvWithIntroControllerStepsOne(miCeta game, Stage stage, FeedbackSoundType feedbackSound, Sound introSound, ArrayList<Sound> positiveFeedback, Sound tooFewErrorSound, Sound tooMuchErrorSound, Sound finalFeedback, boolean upLevel, boolean shouldRepeatTutorial) {
        super(game, stage, feedbackSound, introSound, positiveFeedback, tooFewErrorSound, tooMuchErrorSound, finalFeedback, upLevel, shouldRepeatTutorial); // we always come from organic to this controller!
    }

    @Override
    protected void goToNextLevel(){
        willGoToNextPart = true;
        game.setScreen(new BaseScreenWithIntro(game, upLevel, shouldRepeatTutorial)); // interactive part of the tutorial, we shouldn't do level up! and yes, we came from organic!
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
}
