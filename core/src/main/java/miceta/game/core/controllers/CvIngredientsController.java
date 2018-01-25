package miceta.game.core.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Stage;
import miceta.game.core.Assets;
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
 * Created by ewe on 1/18/18.
 */
public class CvIngredientsController extends CvWorldController{
    private static final String TAG = CvIngredientsController.class.getName();


    public CvIngredientsController(miCeta game, Stage stage, FeedbackSoundType feedbackSoundName, Sound tooMuchErrorSound, Sound tooFewErrorSound,  Sound test_resolution) {
        super(game, stage, feedbackSoundName, tooMuchErrorSound, tooFewErrorSound, test_resolution);
        initAnswersNeeded();
    }

    @Override
    protected void init(){
        numberToPlay = LevelsManager.getInstance().get_number_to_play();
        setDelayForPositiveFeedback();
        timeToWait = AudioManager.instance.reproduce_ingredients_intro(); //first we read the intro
        answerRight = false;
    }


    @Override
    protected void reproduceAllFeedbacksAndPositive(ArrayList<Integer> nowDetected, int numberToPlay ){
        if(correctAnswersNow == correctAnswersNeeded)
            AudioManager.instance.readAllFeedbacksAndFinalIngredientsScreen(nowDetected, numberToPlay, extraDelayBetweenFeedback, correctAnswersNow);
        else
            AudioManager.instance.readAllFeedbacksAndPositiveWithNewIngredient(nowDetected, numberToPlay, extraDelayBetweenFeedback, correctAnswersNow);


    }


    @Override
    protected void setDelayForPositiveFeedback(){
        Gdx.app.log(TAG,"setDelayForPositiveFeedback "+Assets.instance.getSoundDuration(Assets.instance.sounds.ingredientsPositive)+" === "+Assets.instance.getSoundDuration(Assets.instance.sounds.ingredientsCrocodile));
        delayForPositiveFeedback = Assets.instance.getSoundDuration(Assets.instance.sounds.ingredientsPositive) + Assets.instance.getSoundDuration(Assets.instance.sounds.ingredientsCrocodile)  ; // TODO should we add new ingredient vocalization??
    }


}


