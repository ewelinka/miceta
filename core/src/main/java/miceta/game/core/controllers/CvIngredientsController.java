package miceta.game.core.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Stage;
import miceta.game.core.Assets;
import miceta.game.core.managers.LevelsManager;
import miceta.game.core.miCeta;
import miceta.game.core.util.AudioManager;
import miceta.game.core.util.FeedbackSoundType;
import miceta.game.core.util.ScreenName;

import java.util.ArrayList;

/**
 * Created by ewe on 1/18/18.
 */
public class CvIngredientsController extends CvWorldController{
    private static final String TAG = CvIngredientsController.class.getName();


    public CvIngredientsController(miCeta game, Stage stage, Sound tooFewErrorSound, Sound tooMuchErrorSound, Sound finalSound, boolean upLevel, boolean shouldRepeatTutorial) {
        super(game, stage, ScreenName.GAME_INGREDIENTS, Assets.instance.sounds.tada, Assets.instance.sounds.positivesIngredients, tooFewErrorSound, tooMuchErrorSound, finalSound, upLevel, shouldRepeatTutorial); //yuju won't be used but we have to set it
    }

    @Override
    protected void init(){
        numberToPlay = LevelsManager.instance.get_number_to_play();
        setDelayForPositiveFeedback();
        timeToWait = AudioManager.instance.reproduce_ingredients_intro(); //first we read the intro
        answerRight = false;
    }


    @Override
    protected void reproduceAllFeedbacksAndPositive(ArrayList<Integer> nowDetected, int numberToPlay ){
        AudioManager.instance.readAllFeedbacksAndPositiveWithNewIngredient(nowDetected, numberToPlay, extraDelayBetweenFeedback, correctAnswersNow);
    }



    @Override
    void setDelayForPositiveFeedback(){
        delayForPositiveFeedback = Assets.instance.getSoundDuration(Assets.instance.sounds.ingredientsPositive_1) + Assets.instance.getSoundDuration(Assets.instance.sounds.i2);
        //delayForPositiveFeedback = 4.0f;

    }


}


