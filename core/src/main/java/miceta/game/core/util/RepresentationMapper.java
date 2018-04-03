package miceta.game.core.util;

import com.badlogic.gdx.Gdx;
import miceta.game.core.Assets;
import miceta.game.core.miCeta;
import miceta.game.core.screens.*;

/**
 * Created by ewe on 1/22/18.
 */
public class RepresentationMapper {
    private static final String TAG = RepresentationMapper.class.getName();
    private final miCeta game;

    public RepresentationMapper(miCeta game) {
        this.game = game;
    }

    public AbstractGameScreen getScreenFromScreenName(ScreenName screenName, boolean shouldRepeatTutorial){
        Gdx.app.log(TAG,"screen for represent now "+screenName);
        switch (screenName){
            case CONCRETE_TUTORIAL:
                Gdx.app.log(TAG,"concrete!");
                return new ConcreteTutorial(game,0, 0, true, false);
            case ORGANIC_HELP:
                Gdx.app.log(TAG,"organic!");
                return new OrganicHelpOneScreen(game, true, shouldRepeatTutorial);
            case GAME_STEPS:
                Gdx.app.log(TAG,"steps!");
                return new StepsOneScreen(game, true, shouldRepeatTutorial);
            case GAME_KNOCK:
                Gdx.app.log(TAG,"knock!");
                return new World_1_AudioScreen(game, true, shouldRepeatTutorial);
            case GAME_INGREDIENTS:
                Gdx.app.log(TAG,"ingredients!");
                return new IngredientsScreen(game, true, shouldRepeatTutorial);
            case GAME_MIXING:
                Gdx.app.log(TAG,"mixing!");
                return new BaseScreenWithIntro(game, true, shouldRepeatTutorial);
            case GAME_MUSIC:
                Gdx.app.log(TAG,"music!");
                return new BaseScreenWithIntro(game, true, shouldRepeatTutorial);
            case GAME_BELL:
                Gdx.app.log(TAG,"bell!");
                return new BaseScreenWithIntro(game, true, shouldRepeatTutorial);
            default:
                Gdx.app.log(TAG,"def!");
                return new  IntroScreen(game);

        }
    }


    public static ScreenName getScreenNameFromRepresentation(int representation){
        ScreenName screenName = ScreenName.NONE;
        switch(representation){
            case -1:
                screenName = ScreenName.CONCRETE_TUTORIAL;
                break;
            case 0:
                screenName = ScreenName.ORGANIC_HELP;
                break;
            case 1:
                screenName = ScreenName.GAME_STEPS;
                break;
            case 2:
                screenName = ScreenName.GAME_KNOCK;
                break;
            case 3:
                screenName = ScreenName.GAME_INGREDIENTS;
                break;
            case 4:
                screenName = ScreenName.GAME_MIXING;
                break;
            case 5:
                screenName = ScreenName.GAME_MUSIC;
                break;
            case 6:
                screenName = ScreenName.GAME_BELL;

        }
        return screenName;
    }


    public static GameScreen getGameScreenFromScreenName(ScreenName screenName){
        GameScreen gameScreen = new GameScreen();
        switch(screenName){
            case CONCRETE_TUTORIAL:
                gameScreen.screenName = ScreenName.CONCRETE_TUTORIAL;
                break;
            case ORGANIC_HELP:
                gameScreen.screenName = ScreenName.ORGANIC_HELP; //TODO change me!
                gameScreen.intro = Assets.instance.sounds.organicHelpIntro2;
                gameScreen.positives = Assets.instance.sounds.positivesOrganicHelp;
                gameScreen.tooFew = Assets.instance.sounds.organicHelpTooFew_2;
                gameScreen.tooMuch = Assets.instance.sounds.organicHelpTooMuch_2;
                gameScreen.finalSound = Assets.instance.sounds.organicHelpFinal2;
                gameScreen.feedbackSoundType = FeedbackSoundType.STEP;
            case GAME_STEPS:
                gameScreen.screenName = ScreenName.GAME_STEPS;
                gameScreen.intro = Assets.instance.sounds.stepIntro2;
                gameScreen.positives = Assets.instance.sounds.positivesStep;
                gameScreen.tooFew = Assets.instance.sounds.stepTooFew_2;
                gameScreen.tooMuch = Assets.instance.sounds.stepTooMuch_2;
                gameScreen.finalSound = Assets.instance.sounds.stepFinal2;
                gameScreen.feedbackSoundType = FeedbackSoundType.STEP;
                break;
            case GAME_KNOCK:
                gameScreen.screenName = ScreenName.GAME_KNOCK;
                gameScreen.intro = Assets.instance.sounds.knockIntro;
                gameScreen.positives = Assets.instance.sounds.positivesKnock;
                gameScreen.tooFew = Assets.instance.sounds.knockTooFew;
                gameScreen.tooMuch = Assets.instance.sounds.knockTooMuch;
                gameScreen.finalSound = Assets.instance.sounds.knockFinal;
                gameScreen.feedbackSoundType = FeedbackSoundType.KNOCK;
                break;
            case GAME_INGREDIENTS:
                gameScreen.screenName = ScreenName.GAME_INGREDIENTS;
                gameScreen.intro = Assets.instance.sounds.ingredientsIntro;
                gameScreen.positives = Assets.instance.sounds.positivesIngredients;
                gameScreen.tooFew = Assets.instance.sounds.ingredientsTooFew;
                gameScreen.tooMuch = Assets.instance.sounds.ingredientsTooMuch;
                gameScreen.finalSound = Assets.instance.sounds.ingredientsFinal;
                gameScreen.feedbackSoundType = FeedbackSoundType.INGREDIENT;
                break;
            case GAME_MIXING:
                gameScreen.screenName = ScreenName.GAME_MIXING;
                gameScreen.intro = Assets.instance.sounds.mixingIntro;
                gameScreen.positives = Assets.instance.sounds.positivesMixing;
                gameScreen.tooFew = Assets.instance.sounds.mixingTooFew;
                gameScreen.tooMuch = Assets.instance.sounds.mixingTooMuch;
                gameScreen.finalSound = Assets.instance.sounds.mixingFinal;
                gameScreen.feedbackSoundType = FeedbackSoundType.MIXING;
                break;
            case GAME_MUSIC:
                gameScreen.screenName = ScreenName.GAME_MUSIC;
                gameScreen.intro = Assets.instance.sounds.musicIntro_1; //hay que unir las intro!
                gameScreen.positives =Assets.instance.sounds.positivesMusic;
                gameScreen.tooFew = Assets.instance.sounds.musicTooFew;
                gameScreen.tooMuch = Assets.instance.sounds.musicTooMuch;
                gameScreen.finalSound = Assets.instance.sounds.musicFinal;
                gameScreen.feedbackSoundType = FeedbackSoundType.MUSIC;
                break;
            case GAME_BELL:
                gameScreen.screenName = ScreenName.GAME_BELL;
                gameScreen.intro = Assets.instance.sounds.bellIntro;
                gameScreen.positives = Assets.instance.sounds.positivesBell;
                gameScreen.tooFew = Assets.instance.sounds.bellTooFew;
                gameScreen.tooMuch = Assets.instance.sounds.bellTooMuch;
                gameScreen.finalSound = Assets.instance.sounds.bellFinal;
                gameScreen.feedbackSoundType = FeedbackSoundType.BELL;
        }
        return gameScreen;
    }
}
