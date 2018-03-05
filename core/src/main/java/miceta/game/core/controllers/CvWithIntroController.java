package miceta.game.core.controllers;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Stage;
import miceta.game.core.managers.LevelsManager;
import miceta.game.core.miCeta;
import miceta.game.core.util.AudioManager;
import miceta.game.core.util.FeedbackSoundType;

import java.util.ArrayList;

/**
 * Created by ewe on 1/29/18.
 */
public class CvWithIntroController extends CvWorldController {

    public CvWithIntroController(miCeta game, Stage stage, FeedbackSoundType feedbackSound, Sound introSound, ArrayList<Sound> positiveFeedback, Sound tooFewErrorSound, Sound tooMuchErrorSound, Sound finalFeedback, boolean upLevel) {
        super(game, stage, feedbackSound, introSound, positiveFeedback, tooFewErrorSound, tooMuchErrorSound, finalFeedback, upLevel);
    }

    @Override
    protected void init(){
        numberToPlay = LevelsManager.instance.get_number_to_play();
        setDelayForPositiveFeedback();
        timeToWait = AudioManager.instance.reproduceIntro(); //first we read the intro
        answerRight = false;
    }

}
