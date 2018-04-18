package miceta.game.core.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Stage;
import miceta.game.core.managers.LevelsManager;
import miceta.game.core.miCeta;
import miceta.game.core.util.AudioManager;
import miceta.game.core.util.FeedbackSoundType;
import miceta.game.core.util.ScreenName;

import java.util.ArrayList;

/**
 * Created by ewe on 1/29/18.
 */
public class CvWithIntroController extends CvWorldController {
    private static final String TAG = CvWithIntroController.class.getName();
    private boolean isInOgranicHelpScreen;

    public CvWithIntroController(miCeta game, Stage stage, ScreenName screenNameNow, Sound introSound, ArrayList<Sound> positiveFeedback, Sound tooFewErrorSound, Sound tooMuchErrorSound, Sound finalFeedback, boolean upLevel, boolean shouldRepeatTutorial){
        this(game, stage, screenNameNow, introSound, positiveFeedback, tooFewErrorSound, tooMuchErrorSound, finalFeedback, upLevel, shouldRepeatTutorial, false);
    }

    public CvWithIntroController(miCeta game, Stage stage, ScreenName screenNameNow, Sound introSound, ArrayList<Sound> positiveFeedback, Sound tooFewErrorSound, Sound tooMuchErrorSound, Sound finalFeedback, boolean upLevel, boolean shouldRepeatTutorial, boolean isInOgranicHelpScreen) {
        super(game, stage, screenNameNow, introSound, positiveFeedback, tooFewErrorSound, tooMuchErrorSound, finalFeedback, upLevel, shouldRepeatTutorial);
        this.isInOgranicHelpScreen = isInOgranicHelpScreen;
    }

    @Override
    protected void init(){
        numberToPlay = LevelsManager.instance.get_number_to_play();
        setDelayForPositiveFeedback();
        Gdx.app.log(TAG,"isInOgranicHelpScreen "+ isInOgranicHelpScreen +" shouldRepeatTutorial "+shouldRepeatTutorial);
        timeToWait = AudioManager.instance.reproduceIntro();
        answerRight = false;
    }

    @Override
    protected void checkForTotalErrors(){
        if(!isInOgranicHelpScreen){ // we do not repeat tutorial if we are in the tutorial!
            super.checkForTotalErrors();
        }
    }

}
