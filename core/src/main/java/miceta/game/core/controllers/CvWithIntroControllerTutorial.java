package miceta.game.core.controllers;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Stage;
import miceta.game.core.managers.LevelsManager;
import miceta.game.core.miCeta;
import miceta.game.core.screens.BaseScreenWithIntro;

import miceta.game.core.util.AudioManager;
import miceta.game.core.util.Constants;
import miceta.game.core.util.FeedbackSoundType;

import java.util.ArrayList;

/**
 * Created by ewe on 2/2/18.
 */
public class CvWithIntroControllerTutorial extends CvWithIntroController {
    public CvWithIntroControllerTutorial(miCeta game, Stage stage, FeedbackSoundType feedbackSound, Sound introSound, ArrayList<Sound> positiveFeedback, Sound tooFewErrorSound, Sound tooMuchErrorSound, Sound finalFeedback) {
        super(game, stage, feedbackSound, introSound, positiveFeedback, tooFewErrorSound, tooMuchErrorSound, finalFeedback);
        inactivityLimit = 0; // we dont want to wait!
    }

    @Override
    protected void goToNextLevel(){
        willGoToNextPart = true;
        game.setScreen(new BaseScreenWithIntro(game));
    }

    @Override
    protected void onCorrectAnswer() {

    }

    @Override
    protected void init(){
        numberToPlay = 1;
        setDelayForPositiveFeedback();
        timeToWait = AudioManager.instance.reproduceIntro(); //first we read the intro
        answerRight = false;
    }

    @Override
    protected void initAnswersNeeded(){
        correctAnswersNow = 0;
        correctAnswersNeeded = 1;
    }
}
