package miceta.game.core.controllers;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Stage;
import miceta.game.core.miCeta;
import miceta.game.core.screens.BaseScreenWithIntroReadNumber;
import miceta.game.core.util.AudioManager;
import miceta.game.core.util.FeedbackSoundType;
import miceta.game.core.util.ScreenName;

import java.util.ArrayList;

/**
 * Created by ewe on 4/3/18.
 */
public class CvWithIntroControllerOrganicHelpOne extends CvWithIntroControllerStepsOne {
    public CvWithIntroControllerOrganicHelpOne(miCeta game, Stage stage, ScreenName screenNameNow, Sound introSound, ArrayList<Sound> positiveFeedback, Sound tooFewErrorSound, Sound tooMuchErrorSound, Sound finalFeedback, boolean upLevel, boolean shouldRepeatTutorial) {
        super(game, stage, screenNameNow, introSound, positiveFeedback, tooFewErrorSound, tooMuchErrorSound, finalFeedback, upLevel, shouldRepeatTutorial, true);
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
        game.setScreen(new BaseScreenWithIntroReadNumber(game, upLevel, shouldRepeatTutorial, ScreenName.ORGANIC_HELP)); // interactive part of the tutorial, we shouldn't do level up! and yes, we came from organic!
    }


}
