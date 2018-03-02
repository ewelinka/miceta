package miceta.game.core.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import miceta.game.core.Assets;
import miceta.game.core.miCeta;
import miceta.game.core.screens.*;

/**
 * Created by ewe on 1/22/18.
 */
public class RepresentationMapper {
    public static final String TAG = RepresentationMapper.class.getName();
    private miCeta game;

    public RepresentationMapper(miCeta game) {
        this.game = game;
    }

    public AbstractGameScreen getScreenFromScreenName(ScreenName screenName){
        Gdx.app.log(TAG,"screen for represent now "+screenName);
        switch (screenName){
            case CONCRETE_TUTORIAL:
                Gdx.app.log(TAG,"concrete!");
                return new ConcreteTutorial(game,0, 0, false);
            case ORGANIC_TUTORIAL1:
                Gdx.app.log(TAG,"organic!");
                return new OrganicOneScreen(game);
            case GAME_KNOCK:
                Gdx.app.log(TAG,"knock!");
                return new World_1_AudioScreen(game,0,0);
            case GAME_INGREDIENTS:
                Gdx.app.log(TAG,"ingredients!");
                return new IngredientsScreen(game);
            case GAME_MIXING:
                Gdx.app.log(TAG,"mixing!");
                return new BaseScreenWithIntro(game);
            case GAME_MUSIC:
                Gdx.app.log(TAG,"music!");
                return new BaseScreenWithIntro(game);
            case GAME_BELL:
                Gdx.app.log(TAG,"bell!");
                return new BaseScreenWithIntro(game);
            default:
                Gdx.app.log(TAG,"def!");
              //  return new OrganicTutorial1AudioScreen(game,1,3);
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
                screenName = ScreenName.ORGANIC_TUTORIAL1;
                break;
            case 1:
                screenName = ScreenName.GAME_KNOCK;
                break;
            case 2:
                screenName = ScreenName.GAME_INGREDIENTS;
                break;
            case 3:
                screenName = ScreenName.GAME_MIXING;
                break;
            case 4:
                screenName = ScreenName.GAME_MUSIC;
                break;
            case 5:
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
            case ORGANIC_TUTORIAL1:
                gameScreen.screenName = ScreenName.ORGANIC_TUTORIAL1;
                gameScreen.intro = Assets.instance.sounds.tmm1_firstPositive;
                gameScreen.positives = Assets.instance.sounds.positivesTutorial;
                gameScreen.tooFew = Assets.instance.sounds.tmm1_tooFew_1;
                gameScreen.tooMuch = Assets.instance.sounds.tmm1_tooMuch_1;
                gameScreen.finalSound = Assets.instance.sounds.tmm1_final;
                gameScreen.feedbackSoundType = FeedbackSoundType.KNOCK;
                break;
            case GAME_KNOCK:
                gameScreen.screenName = ScreenName.GAME_KNOCK;
                gameScreen.positives = Assets.instance.sounds.positivesKnock;
                gameScreen.feedbackSoundType = FeedbackSoundType.KNOCK;
                break;
            case GAME_INGREDIENTS:
                gameScreen.screenName = ScreenName.GAME_INGREDIENTS;
                gameScreen.positives = Assets.instance.sounds.positivesIngredients;
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
               // gameScreen.intro = Assets.instance.sounds.musicIntro_2;
                //gameScreen.positives = Assets.instance.sounds.musicPositive_1;
                //gameScreen.positives = Assets.instance.sounds.musicPositive_2;
                //gameScreen.positives = Assets.instance.sounds.musicPositive_3;
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
