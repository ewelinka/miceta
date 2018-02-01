package miceta.game.core.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Stage;
import miceta.game.core.Assets;
import miceta.game.core.managers.CvBlocksManager;
import miceta.game.core.managers.LevelsManager;
import miceta.game.core.miCeta;
import miceta.game.core.screens.IntroScreen;
import miceta.game.core.screens.OrganicTutorial1AudioScreen;
import miceta.game.core.util.AudioManager;
import miceta.game.core.util.Constants;
import miceta.game.core.util.FeedbackSoundType;
import miceta.game.core.util.GamePreferences;

import java.util.ArrayList;

/**
 * Created by ewe on 1/12/18.
 */
public class CvOrganicTutorialController extends CvWorldController {
    private static final String TAG = CvOrganicTutorialController.class.getName();


    public CvOrganicTutorialController(miCeta game, Stage stage, FeedbackSoundType feedbackSound, Sound introSound, Sound positiveFeedback, Sound tooFewErrorSound, Sound tooMuchErrorSound, Sound finalFeedback) {
        super(game, stage, feedbackSound, introSound, positiveFeedback, tooFewErrorSound, tooMuchErrorSound, finalFeedback);
        inactivityLimit = 0; // we dont want to wait!
        maxErrorsForHint = 2; // two errors and we let you know!
    }


}

