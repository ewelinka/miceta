package miceta.game.core.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import miceta.game.core.Assets;
import miceta.game.core.managers.CvBlocksManager;
import miceta.game.core.managers.LevelsManager;
import miceta.game.core.miCeta;
import miceta.game.core.screens.IntroScreen;
import miceta.game.core.screens.OrganicTutorial1AudioScreen;
import miceta.game.core.util.AudioManager;
import miceta.game.core.util.Constants;
import miceta.game.core.util.GamePreferences;

/**
 * Created by ewe on 1/12/18.
 */
public class CvOrganicTutorialController extends CvWorldController {
    private static final String TAG = CvOrganicTutorialController.class.getName();
    private int correctAnswersNr;
    private int correctAnswersNeeded;
    //TODO
    // check #correct answers to proceed

    public CvOrganicTutorialController(miCeta game, Stage stage, String feedbackSoundName) {
        super(game, stage, feedbackSoundName, Assets.instance.sounds.tmm1_tooMuch,Assets.instance.sounds.tmm1_tooFew);
    }


    @Override
    protected void init(){
        numberToPlay = getNewRandomNumber(0,1,3);
        correctAnswersNr = 0;
        correctAnswersNeeded = 10; // we need 10 correct answers!
        Gdx.app.log(TAG,"init, Number to Play: " + numberToPlay );

        AudioManager.instance.readFeedback(numberToPlay, extraDelayBetweenFeedback, feedbackSoundName); //first we read the random number
        timeToWait = Constants.READ_ONE_UNIT_DURATION+ numberToPlay*Constants.READ_ONE_UNIT_DURATION + waitAfterKnock /*+ ( randomNumber)*(0.3f)*/; // time we should wait before next loop starts
        lastAnswerRight = false;

        inactivityLimit = 0; // we dont want to wait!
        maxErrorsForHint = 2; // one error and we let you know!

    }

    @Override
    protected void onCorrectAnswer(){
        correctAnswersNr+=1;
        if(correctAnswersNr == correctAnswersNeeded){
            Gdx.app.log(TAG,"we did it!!");
            willGoToNextPart = true;
            game.setScreen(new OrganicTutorial1AudioScreen(game,3,3));
        }else {
            numberToPlay = getNewRandomNumber(getRandomNumber(),1,5);
        }

    }
}
