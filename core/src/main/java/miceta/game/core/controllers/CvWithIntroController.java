package miceta.game.core.controllers;

import com.badlogic.gdx.Gdx;
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
    private static final String TAG = CvWithIntroController.class.getName();
    private boolean cameFromOrganic;

    public CvWithIntroController(miCeta game, Stage stage, FeedbackSoundType feedbackSound, Sound introSound, ArrayList<Sound> positiveFeedback, Sound tooFewErrorSound, Sound tooMuchErrorSound, Sound finalFeedback, boolean upLevel, boolean shouldRepeatTutorial){
        this(game, stage, feedbackSound, introSound, positiveFeedback, tooFewErrorSound, tooMuchErrorSound, finalFeedback, upLevel, shouldRepeatTutorial, false);
    }

    public CvWithIntroController(miCeta game, Stage stage, FeedbackSoundType feedbackSound, Sound introSound, ArrayList<Sound> positiveFeedback, Sound tooFewErrorSound, Sound tooMuchErrorSound, Sound finalFeedback, boolean upLevel, boolean shouldRepeatTutorial, boolean cameFromOrganic) {
        super(game, stage, feedbackSound, introSound, positiveFeedback, tooFewErrorSound, tooMuchErrorSound, finalFeedback, upLevel, shouldRepeatTutorial);
        this.cameFromOrganic = cameFromOrganic;
    }

    @Override
    protected void init(){
        numberToPlay = LevelsManager.instance.get_number_to_play();
        setDelayForPositiveFeedback();

        Gdx.app.log(TAG,"cameFromOrganic "+cameFromOrganic+" shouldRepeatTutorial "+shouldRepeatTutorial);
        if(cameFromOrganic)
            timeToWait = AudioManager.instance.reproduceTutorial(false); //we do not repeat "volvimos al pasado"
        else
            timeToWait = AudioManager.instance.reproduceTutorial(shouldRepeatTutorial); //first we read the intro
        answerRight = false;
    }

    @Override
    protected void checkForTotalErrors(){
        if(!cameFromOrganic){ // we do not repeat tutorial if we are in the tutorial!
            super.checkForTotalErrors();
        }
    }

}
