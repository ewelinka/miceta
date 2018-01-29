package miceta.game.core.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import miceta.game.core.Assets;
import miceta.game.core.miCeta;
import miceta.game.core.screens.*;

/**
 * Created by ewe on 1/22/18.
 */
public class RepresentationToScreenMapper {
    public static final String TAG = RepresentationToScreenMapper.class.getName();
    private miCeta game;

    public RepresentationToScreenMapper(miCeta game) {
        this.game = game;
    }

    public AbstractGameScreen getScreenFromRepresentation( int representation){
        Gdx.app.log(TAG,"screen for represent now "+representation);
        switch (representation){
            case -1:
                Gdx.app.log(TAG,"concrete!");
                return new ConcreteTutorial(game,0, 0);
            case 0:
                Gdx.app.log(TAG,"organic!");
                return new OrganicTutorial1AudioScreen(game,1,3);
            case 1:
                Gdx.app.log(TAG,"knock!");
                return new World_1_AudioScreen(game,0,0);
            case 2:
                Gdx.app.log(TAG,"ingredients!");
                return new IngredientsScreen(game);
            case 3:
                Gdx.app.log(TAG,"mixing!");
                return new BaseScreenWithIntro(game);
            case 4:
                Gdx.app.log(TAG,"music!");
                return new BaseScreenWithIntro(game);
            case 5:
                Gdx.app.log(TAG,"bell!");
                return new BaseScreenWithIntro(game);
            default:
                Gdx.app.log(TAG,"def!");
                return new OrganicTutorial1AudioScreen(game,1,3);
        }
    }

    public Sound[] getSoundsFromRepresentation(int representation){
        Gdx.app.log(TAG,"sound for representation "+representation);
        Sound [] soundsNow = new Sound[5];
        switch (representation){

            case 3:
                Gdx.app.log(TAG,"sounds mixing!");
                soundsNow[0] = Assets.instance.sounds.mixingIntro;
                soundsNow[1] = Assets.instance.sounds.mixingPositive;
                soundsNow[2] = Assets.instance.sounds.mixingTooFew;
                soundsNow[3] = Assets.instance.sounds.mixingTooMuch;
                soundsNow[4] = Assets.instance.sounds.mixingFinal;
                break;
            case 4:
                Gdx.app.log(TAG,"sounds music!");
                soundsNow[0] = Assets.instance.sounds.musicIntro;
                soundsNow[1] = Assets.instance.sounds.musicPositive;
                soundsNow[2] = Assets.instance.sounds.musicTooFew;
                soundsNow[3] = Assets.instance.sounds.musicTooMuch;
                soundsNow[4] = Assets.instance.sounds.musicFinal;
                break;
            case 5:
                Gdx.app.log(TAG,"sounds bell!");
                soundsNow[0] = Assets.instance.sounds.bellIntro;
                soundsNow[1] = Assets.instance.sounds.bellPositive;
                soundsNow[2] = Assets.instance.sounds.bellTooFew;
                soundsNow[3] = Assets.instance.sounds.bellTooMuch;
                soundsNow[4] = Assets.instance.sounds.bellFinal;

        }
        return soundsNow;
    }


    public FeedbackSoundType getFeedbackTypeFromRepresentation(int representation){ // TODO update when we have more audios
        Gdx.app.log(TAG,"feedback for representation"+representation);
        switch (representation){
            case 3:
                return FeedbackSoundType.MIXING;
            case 4:
                return FeedbackSoundType.MUSIC;
            case 5:
                return FeedbackSoundType.BELL;
            default:
                return FeedbackSoundType.KNOCK;
        }
    }



}
