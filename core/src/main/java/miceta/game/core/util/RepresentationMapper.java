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
                return new ConcreteTutorial(game,true, false);
            case ORGANIC_HELP:
                Gdx.app.log(TAG,"organic!");
                return new OrganicHelpOneScreen(game, true, shouldRepeatTutorial);
            case GAME_STEPS:
                Gdx.app.log(TAG,"steps!");
                return new StepsOneScreen(game, true, shouldRepeatTutorial);
            case GAME_INGREDIENTS:
                Gdx.app.log(TAG,"ingredients!");
                return new IngredientsScreen(game, true, shouldRepeatTutorial);
            case GAME_KNOCK:
            case GAME_MIXING:
            case GAME_MUSIC:
            case GAME_GREETING:
            case GAME_HOLES:
            case GAME_WINGS:
            case GAME_BIRD:
            case GAME_NUMERUS:
                return new BaseScreenWithIntro(game, true, shouldRepeatTutorial);
            case FINAL_MM1:
            case INTRO_MM2:
            case FINAL_MM2:
                return new OneAudioScreen(game, true, shouldRepeatTutorial);
            default:
                Gdx.app.log(TAG,"defult in getScreenFromScreenName!");
                return new  IntroScreen(game);

        }
    }


    public static ScreenName getScreenNameFromRepresentation(int representation){
        ScreenName screenName = ScreenName.NONE;
        switch(representation){
            case 0:
                screenName = ScreenName.CONCRETE_TUTORIAL;
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
                screenName = ScreenName.GAME_GREETING;
                break;
            case 7:
                screenName = ScreenName.FINAL_MM1;
                break;
            case 8:
                screenName = ScreenName.INTRO_MM2;
                break;
            case 9:
                screenName = ScreenName.GAME_HOLES;
                break;
            case 10:
                screenName = ScreenName.GAME_WINGS;
                break;
            case 11:
                screenName = ScreenName.GAME_BIRD;
                break;
            case 12:
                screenName = ScreenName.GAME_NUMERUS;
                break;
            case 13:
                screenName = ScreenName.FINAL_MM2;
                break;
        }
        return screenName;
    }

    public static String getMessageNameFromScreenName(ScreenName screenName){
        switch (screenName){

            case GAME_STEPS:
                return "pasos";
            case GAME_INGREDIENTS:
                return "ingredientes";
            case GAME_KNOCK:
                return "golpes";
            case GAME_MIXING:
                return "revolver";
            case GAME_MUSIC:
                return "musica";
            case GAME_GREETING:
                return "saludos";
            case GAME_HOLES:
                return "agujeros";
            case GAME_WINGS:
                return "gotas";
            case GAME_BIRD:
                return "pajaro";
            case GAME_NUMERUS:
                return "numerus";
            default:
                return "timbre";

        }
    }


    public static GameScreen getGameScreenFromScreenName(ScreenName screenName){
        GameScreen gameScreen = new GameScreen();
        gameScreen.imgBackground = Assets.instance.backgrounds.generic;
        gameScreen.screenName = screenName;
        switch(screenName){
            case ORGANIC_HELP:
                gameScreen.intro = Assets.instance.sounds.organicHelpIntro2;
                gameScreen.positives = Assets.instance.sounds.positivesTada;
                gameScreen.tooFew = Assets.instance.sounds.organicHelpTooFew_2;
                gameScreen.tooMuch = Assets.instance.sounds.organicHelpTooMuch_2;
                gameScreen.finalSound = Assets.instance.sounds.organicHelpFinal2;
                gameScreen.imgBackground = Assets.instance.backgrounds.clap;
                break;
            case GAME_STEPS:
                gameScreen.intro = Assets.instance.sounds.stepIntro2;
                gameScreen.positives = Assets.instance.sounds.positivesTada;
                gameScreen.tooFew = Assets.instance.sounds.stepTooFew_2;
                gameScreen.tooMuch = Assets.instance.sounds.stepTooMuch_2;
                gameScreen.finalSound = Assets.instance.sounds.stepFinal2;
                gameScreen.imgBackground = Assets.instance.backgrounds.walk;
                break;
            case GAME_KNOCK:
                gameScreen.intro = Assets.instance.sounds.knockIntro;
                gameScreen.positives = Assets.instance.sounds.positivesTada;
                gameScreen.tooFew = Assets.instance.sounds.knockTooFew;
                gameScreen.tooMuch = Assets.instance.sounds.knockTooMuch;
                gameScreen.finalSound = Assets.instance.sounds.knockFinal;
                gameScreen.imgBackground = Assets.instance.backgrounds.door;
                break;
            case GAME_INGREDIENTS:
                gameScreen.intro = Assets.instance.sounds.ingredientsIntro;
                gameScreen.positives = Assets.instance.sounds.positivesIngredients;
                gameScreen.tooFew = Assets.instance.sounds.ingredientsTooFew;
                gameScreen.tooMuch = Assets.instance.sounds.ingredientsTooMuch;
                gameScreen.finalSound = Assets.instance.sounds.ingredientsFinal;
                gameScreen.imgBackground = Assets.instance.backgrounds.ingredients;
                break;
            case GAME_MIXING:
                gameScreen.intro = Assets.instance.sounds.mixingIntro;
                gameScreen.positives = Assets.instance.sounds.positivesTada;
                gameScreen.tooFew = Assets.instance.sounds.mixingTooFew;
                gameScreen.tooMuch = Assets.instance.sounds.mixingTooMuch;
                gameScreen.finalSound = Assets.instance.sounds.mixingFinal;
                gameScreen.imgBackground = Assets.instance.backgrounds.mixing;
                break;
            case GAME_MUSIC:
                gameScreen.intro = Assets.instance.sounds.musicIntro; //hay que unir las intro!
                gameScreen.positives =Assets.instance.sounds.positivesTada;
                gameScreen.tooFew = Assets.instance.sounds.musicTooFew;
                gameScreen.tooMuch = Assets.instance.sounds.musicTooMuch;
                gameScreen.finalSound = Assets.instance.sounds.musicFinal;
                gameScreen.imgBackground = Assets.instance.backgrounds.music;
                break;
            case GAME_GREETING:
                gameScreen.intro = Assets.instance.sounds.greetingIntro;
                gameScreen.positives = Assets.instance.sounds.positivesTada;
                gameScreen.tooFew = Assets.instance.sounds.greetingTooFew;
                gameScreen.tooMuch = Assets.instance.sounds.greetingTooMuch;
                gameScreen.finalSound = Assets.instance.sounds.greetingFinal;
                gameScreen.imgBackground = Assets.instance.backgrounds.greeting;
                break;
            case FINAL_MM1:
                gameScreen.intro = Assets.instance.sounds.finalMM1;
                gameScreen.imgBackground = Assets.instance.backgrounds.finalMM1;
                break;
            case INTRO_MM2:
                gameScreen.intro = Assets.instance.sounds.introMM2;
                gameScreen.imgBackground = Assets.instance.backgrounds.introMM2;
                break;
            case GAME_HOLES:
                gameScreen.intro = Assets.instance.sounds.holesIntro;
                gameScreen.positives = Assets.instance.sounds.positivesTada;
                gameScreen.tooFew = Assets.instance.sounds.holesTooFew;
                gameScreen.tooMuch = Assets.instance.sounds.holesTooMuch;
                gameScreen.finalSound = Assets.instance.sounds.holesFinal;
                gameScreen.imgBackground = Assets.instance.backgrounds.holes;
                break;
            case GAME_WINGS:
                gameScreen.intro = Assets.instance.sounds.wingsIntro;
                gameScreen.positives = Assets.instance.sounds.positivesTada;
                gameScreen.tooFew = Assets.instance.sounds.addblock;
                gameScreen.tooMuch = Assets.instance.sounds.quitblock;
                gameScreen.finalSound = Assets.instance.sounds.wingsFinal;
                gameScreen.imgBackground = Assets.instance.backgrounds.wings;
                break;
            case GAME_BIRD:
                gameScreen.intro = Assets.instance.sounds.birdIntro;
                gameScreen.positives = Assets.instance.sounds.positivesTada;
                gameScreen.tooFew = Assets.instance.sounds.addblock;
                gameScreen.tooMuch = Assets.instance.sounds.quitblock;
                gameScreen.finalSound = Assets.instance.sounds.birdFinal;
                gameScreen.imgBackground = Assets.instance.backgrounds.bird;
                break;
            case GAME_NUMERUS:
                gameScreen.intro = Assets.instance.sounds.numerusIntro;
                gameScreen.positives = Assets.instance.sounds.positivesTada;
                gameScreen.tooFew = Assets.instance.sounds.numerusTooFew;
                gameScreen.tooMuch = Assets.instance.sounds.numerusTooMuch;
                gameScreen.finalSound = Assets.instance.sounds.numerusFinal;
                gameScreen.imgBackground = Assets.instance.backgrounds.numerus;
                break;
            case FINAL_MM2:
                gameScreen.intro = Assets.instance.sounds.finalMM2;
                gameScreen.imgBackground = Assets.instance.backgrounds.finalMM2;
                break;
        }
        return gameScreen;
    }
}
