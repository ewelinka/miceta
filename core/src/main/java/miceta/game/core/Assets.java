package miceta.game.core;

import android.media.MediaPlayer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
//import com.badlogic.gdx.backends.lwjgl.audio.OpenALSound;
import com.badlogic.gdx.backends.lwjgl.audio.OpenALSound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.QuickSelect;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;


/**
 * Created by ewe on 8/10/17.
 */
public class Assets implements Disposable, AssetErrorListener {
    public static final String TAG = Assets.class.getName();
    public static final Assets instance = new Assets();
    private AssetManager assetManager;
    public AssetSounds sounds;
    public AssetMusic music;
    public ArrayMap<Sound,Float> s_duration = new ArrayMap<>();
    //  public ArrayMap<Music,Double> m_duration = new ArrayMap<>();


    private Assets() {
    }
    public void init(AssetManager assetManager) {
        this.assetManager = assetManager;
        // set asset manager error handler
        assetManager.setErrorListener(this);
        assetManager.load("sounds/1.wav", Sound.class);
        assetManager.load("sounds/2.wav", Sound.class);
        assetManager.load("sounds/3.wav", Sound.class);
        assetManager.load("sounds/4.wav", Sound.class);
        assetManager.load("sounds/5.wav", Sound.class);
        assetManager.load("sounds/6.wav", Sound.class);
        assetManager.load("sounds/7.wav", Sound.class);
        assetManager.load("sounds/8.wav", Sound.class);
        assetManager.load("sounds/9.wav", Sound.class);
        assetManager.load("sounds/10.wav", Sound.class);
        assetManager.load("sounds/11.wav", Sound.class);
        assetManager.load("sounds/12.wav", Sound.class);
        assetManager.load("sounds/13.wav", Sound.class);
        assetManager.load("sounds/14.wav", Sound.class);
        assetManager.load("sounds/15.wav", Sound.class);
        assetManager.load("sounds/do.wav", Sound.class);
//        assetManager.load("sounds/re_trumpet.wav", Sound.class);
//        assetManager.load("sounds/mi_guitar.wav", Sound.class);
//        assetManager.load("sounds/fa_flaute.wav", Sound.class);
//        assetManager.load("sounds/sol_harp.wav", Sound.class);
        assetManager.load("sounds/re.wav", Sound.class);
        assetManager.load("sounds/mi.wav", Sound.class);
        assetManager.load("sounds/fa.wav", Sound.class);
        assetManager.load("sounds/sol.wav", Sound.class);
        assetManager.load("sounds/puck.mp3", Sound.class);
        assetManager.load("music/song1.mp3", Music.class);
        assetManager.load("music/song2.mp3", Music.class);
        assetManager.load("sounds/yuju.mp3", Sound.class);
        assetManager.load("sounds/tada.mp3", Sound.class);

        assetManager.load("sounds/d1.wav", Sound.class);
        assetManager.load("sounds/d2.wav", Sound.class);
        assetManager.load("sounds/d3.wav", Sound.class);
        assetManager.load("sounds/d4.wav", Sound.class);
        assetManager.load("sounds/d5.wav", Sound.class);
        assetManager.load("sounds/d6.wav", Sound.class);
        assetManager.load("sounds/d7.wav", Sound.class);
        assetManager.load("sounds/d8.wav", Sound.class);
        assetManager.load("sounds/d9.wav", Sound.class);
        assetManager.load("sounds/d10.wav", Sound.class);
        assetManager.load("sounds/newblock.wav", Sound.class);
        assetManager.load("feedback/feedbackLoop.wav", Music.class);
        assetManager.load("sounds/masPiezas.wav", Sound.class);
        assetManager.load("sounds/menosPiezas.wav", Sound.class);

        assetManager.finishLoading();
        sounds = new AssetSounds(assetManager);
        music = new AssetMusic(assetManager);
    }

    public float getSoundDuration(Sound soundToPlay){

        return  s_duration.get(soundToPlay);
    }


    @Override
    public void dispose() {
        assetManager.dispose();
    }
    @Override
    public void error(AssetDescriptor asset, Throwable throwable) {
        Gdx.app.error(TAG, "Couldn't load asset '" +  asset.fileName + "'", (Exception) throwable);
    }

    public class AssetSounds {
        public final Sound f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15;
        public final Sound oneDo, oneRe, oneMi, oneFa, oneSol;
        public final Sound puck,yuju, newblock, addblock, quitblock, tada;
        public final Sound d1,d2,d3,d4,d5,d6,d7,d8,d9,d10;



        public Sound soundWithDuration(String path, AssetManager am) {

            Sound sound_load;
            sound_load = am.get(path, Sound.class);

            Gdx.app.log(TAG, "wowowowowoowow --> path " + path + " dura " + String.valueOf(((OpenALSound) (sound_load)).duration()));
            float soundDuration = ((OpenALSound) (sound_load)).duration();
            s_duration.put(sound_load, soundDuration);

            return sound_load;
        }



        public AssetSounds (AssetManager am) {

            f1 =  soundWithDuration("sounds/1.wav", am);
            f2 =  soundWithDuration("sounds/2.wav", am);
            f3 =  soundWithDuration("sounds/3.wav", am);
            f4 =  soundWithDuration("sounds/4.wav", am);
            f5 =  soundWithDuration("sounds/5.wav", am);
            f6 =  soundWithDuration("sounds/6.wav", am);
            f7 =  soundWithDuration("sounds/7.wav", am);
            f8 =  soundWithDuration("sounds/8.wav", am);
            f9 =  soundWithDuration("sounds/9.wav", am);
            f10 = soundWithDuration("sounds/10.wav", am);
            f11 = soundWithDuration("sounds/11.wav", am);
            f12 = soundWithDuration("sounds/12.wav", am);
            f13 = soundWithDuration("sounds/13.wav", am);
            f14 = soundWithDuration("sounds/14.wav", am);
            f15 = soundWithDuration("sounds/15.wav", am);


            oneDo = soundWithDuration("sounds/do.wav", am);
//            oneRe = am.get("sounds/re_trumpet.wav", Sound.class);
//            oneMi = am.get("sounds/mi_guitar.wav", Sound.class);
//            oneFa = am.get("sounds/fa_flaute.wav", Sound.class);
//            oneSol = am.get("sounds/sol_harp.wav", Sound.class);
            oneRe = soundWithDuration("sounds/re.wav", am);
            oneMi = soundWithDuration("sounds/mi.wav", am);
            oneFa = soundWithDuration("sounds/fa.wav", am);
            oneSol = soundWithDuration("sounds/sol.wav", am);

           /* puck = soundWithDuration("sounds/puck.mp3", am);
            yuju = soundWithDuration("sounds/yuju.mp3", am);
            tada = soundWithDuration("sounds/tada.mp3", am);
*/
            puck = soundWithDuration("sounds/puck.mp3", am);
            yuju = soundWithDuration("sounds/yuju.mp3", am);
            tada = soundWithDuration("sounds/tada.mp3", am);


            newblock = soundWithDuration("sounds/newblock.wav", am);
            addblock = soundWithDuration("sounds/masPiezas.wav", am);
            quitblock = soundWithDuration("sounds/menosPiezas.wav", am);

            d1 = soundWithDuration("sounds/d1.wav", am);
            d2 = soundWithDuration("sounds/d2.wav", am);
            d3 = soundWithDuration("sounds/d3.wav", am);
            d4 = soundWithDuration("sounds/d4.wav", am);
            d5 = soundWithDuration("sounds/d5.wav", am);
            d6 = soundWithDuration("sounds/d6.wav", am);
            d7 = soundWithDuration("sounds/d7.wav", am);
            d8 = soundWithDuration("sounds/d8.wav", am);
            d9 = soundWithDuration("sounds/d9.wav", am);
            d10 = soundWithDuration("sounds/d10.wav", am);

        }
    }

    public class AssetMusic {
        public final Music song01;
        public final Music song02;
        public final Music new_block_loop;

        public AssetMusic (AssetManager am) {
            song01 = am.get("music/song1.mp3", Music.class);
            song02 = am.get("music/song2.mp3", Music.class);
            new_block_loop  = am.get("feedback/feedbackLoop.wav", Music.class);

        }
    }
}