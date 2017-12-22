package miceta.game.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.Disposable;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

import java.io.File;

/**
 * Created by ewe on 8/10/17.
 */
public class Assets implements Disposable, AssetErrorListener {
    public static final String TAG = Assets.class.getName();
    public static final Assets instance = new Assets();
    private AssetManager assetManager;

    public AssetSounds sounds;
    public AssetMusic music;
    public ArrayMap<Sound,Double> s_duration = new ArrayMap<>();
    public ArrayMap<Music,Double> m_duration = new ArrayMap<>();


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

        //--------------------------------------------------------/
/*
        assetManager.load("sounds/e1.wav", Sound.class);
        assetManager.load("sounds/e2.wav", Sound.class);
        assetManager.load("sounds/e3.wav", Sound.class);
        assetManager.load("sounds/e4.wav", Sound.class);
        assetManager.load("sounds/e5.wav", Sound.class);
        assetManager.load("sounds/e6.wav", Sound.class);
        assetManager.load("sounds/e7.wav", Sound.class);
        assetManager.load("sounds/e8.wav", Sound.class);
        assetManager.load("sounds/e9.wav", Sound.class);
        assetManager.load("sounds/e10.wav", Sound.class);

        assetManager.load("sounds/d11.wav", Sound.class);
        assetManager.load("sounds/d12.wav", Sound.class);
        assetManager.load("sounds/d13.wav", Sound.class);
        assetManager.load("sounds/d14.wav", Sound.class);
        assetManager.load("sounds/d15.wav", Sound.class);
        assetManager.load("sounds/d16.wav", Sound.class);
        assetManager.load("sounds/d17.wav", Sound.class);
        assetManager.load("sounds/d18.wav", Sound.class);
        assetManager.load("sounds/d19.wav", Sound.class);
        assetManager.load("sounds/d20.wav", Sound.class);

        assetManager.load("sounds/d21.wav", Sound.class);
        assetManager.load("sounds/d22.wav", Sound.class);
        assetManager.load("sounds/d23.wav", Sound.class);
        assetManager.load("sounds/d24.wav", Sound.class);
        assetManager.load("sounds/d25.wav", Sound.class);
        assetManager.load("sounds/d26.wav", Sound.class);
        assetManager.load("sounds/d27.wav", Sound.class);
        assetManager.load("sounds/d28.wav", Sound.class);
        assetManager.load("sounds/d29.wav", Sound.class);
        assetManager.load("sounds/d30.wav", Sound.class);

        assetManager.load("sounds/d31.wav", Sound.class);
        assetManager.load("sounds/d32.wav", Sound.class);
        assetManager.load("sounds/d33.wav", Sound.class);
        assetManager.load("sounds/d34.wav", Sound.class);
        assetManager.load("sounds/d35.wav", Sound.class);
        assetManager.load("sounds/d36.wav", Sound.class);
        assetManager.load("sounds/d37.wav", Sound.class);
        assetManager.load("sounds/d38.wav", Sound.class);
        assetManager.load("sounds/d39.wav", Sound.class);
        assetManager.load("sounds/d40.wav", Sound.class);

        assetManager.load("sounds/d41.wav", Sound.class);
        assetManager.load("sounds/d42.wav", Sound.class);
        assetManager.load("sounds/d43.wav", Sound.class);
        assetManager.load("sounds/d44.wav", Sound.class);
        assetManager.load("sounds/d45.wav", Sound.class);
        assetManager.load("sounds/d46.wav", Sound.class);
        assetManager.load("sounds/d47.wav", Sound.class);
        assetManager.load("sounds/d48.wav", Sound.class);
        assetManager.load("sounds/d49.wav", Sound.class);
        assetManager.load("sounds/d50.wav", Sound.class);


        assetManager.load("sounds/s1.wav", Sound.class);
        assetManager.load("sounds/s2.wav", Sound.class);
        assetManager.load("sounds/s3.wav", Sound.class);
        assetManager.load("sounds/s4.wav", Sound.class);
        assetManager.load("sounds/s5.wav", Sound.class);
        assetManager.load("sounds/s6.wav", Sound.class);
        assetManager.load("sounds/s7.wav", Sound.class);
        assetManager.load("sounds/s8.wav", Sound.class);
        assetManager.load("sounds/s9.wav", Sound.class);
        assetManager.load("sounds/s10.wav", Sound.class);

        assetManager.load("sounds/s11.wav", Sound.class);
        assetManager.load("sounds/s12.wav", Sound.class);
        assetManager.load("sounds/s13.wav", Sound.class);
        assetManager.load("sounds/s14.wav", Sound.class);
        assetManager.load("sounds/s15.wav", Sound.class);
        assetManager.load("sounds/s16.wav", Sound.class);
        assetManager.load("sounds/s17.wav", Sound.class);
        assetManager.load("sounds/s18.wav", Sound.class);
        assetManager.load("sounds/s19.wav", Sound.class);
        assetManager.load("sounds/s20.wav", Sound.class);

        assetManager.load("sounds/s21.wav", Sound.class);
        assetManager.load("sounds/s22.wav", Sound.class);
        assetManager.load("sounds/s23.wav", Sound.class);
        assetManager.load("sounds/s24.wav", Sound.class);
        assetManager.load("sounds/s25.wav", Sound.class);
        assetManager.load("sounds/s26.wav", Sound.class);
        assetManager.load("sounds/s27.wav", Sound.class);
        assetManager.load("sounds/s28.wav", Sound.class);
        assetManager.load("sounds/s29.wav", Sound.class);
        assetManager.load("sounds/s30.wav", Sound.class);

*/

        assetManager.finishLoading();

        sounds = new AssetSounds(assetManager);
        music = new AssetMusic(assetManager);
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

//---------------------------------------------------------------------------------------------------------//
/*  audio prueba
        public final Sound e1,e2,e3,e4,e5,e6,e7,e8,e9,e10;
        public final Sound d11,d12,d13,d14,d15,d16,d17,d18,d19,d20;
        public final Sound d21,d22,d23,d24,d25,d26,d27,d28,d29,d30;
        public final Sound d31,d32,d33,d34,d35,d36,d37,d38,d39,d40;
        public final Sound d41,d42,d43,d44,d45,d46,d47,d48,d49,d50;


        public final Sound s1,s2,s3,s4,s5,s6,s7,s8,s9,s10;
        public final Sound s11,s12,s13,s14,s15,s16,s17,s18,s19,s20;
        public final Sound s21,s22,s23,s24,s25,s26,s27,s28,s29,s30;*/


        //-----------------------------------------------------------------------------//




        public double get_Duration(String sound_path) {

            File soundFile = new File(sound_path);
            double durationInSecs =0;

            try {
                AudioInputStream sound = AudioSystem.getAudioInputStream(soundFile);
                AudioFormat format = sound.getFormat();

                if (format.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
                    format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, format
                            .getSampleRate(), format.getSampleSizeInBits() * 2, format
                            .getChannels(), format.getFrameSize() * 2, format
                            .getFrameRate(), true); // big endian
                    sound = AudioSystem.getAudioInputStream(format, sound);
                }

                DataLine.Info info = new DataLine.Info(Clip.class, sound.getFormat(),
                        ((int) sound.getFrameLength() * format.getFrameSize()));

                try {

                    Clip clip = (Clip) AudioSystem.getLine(info);
                    clip.close();

                    durationInSecs = clip.getBufferSize()
                            / (clip.getFormat().getFrameSize() * clip.getFormat()
                            .getFrameRate());

                    Gdx.app.log(TAG,"-------------------------------------------" + sound_path + " --DURATION ! "+ durationInSecs);

                } catch (LineUnavailableException e) {
                    e.printStackTrace();
                }

            } catch (UnsupportedAudioFileException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            soundFile.delete();
            return durationInSecs;
        }


        public Sound Sound_with_Duration(String path, AssetManager am){

            Sound sound_load;
            sound_load = am.get(path, Sound.class);
            s_duration.put(sound_load, get_Duration(path));

            return sound_load;
        }

        public Music Music_with_Duration(String path, AssetManager am){

            Music music_load;
            music_load = am.get(path, Music.class);
            m_duration.put(music_load, get_Duration(path));

            return music_load;
        }




        public AssetSounds (AssetManager am) {

            f1 =  Sound_with_Duration("sounds/1.wav", am);
            f2 =  Sound_with_Duration("sounds/2.wav", am);
            f3 =  Sound_with_Duration("sounds/3.wav", am);
            f4 =  Sound_with_Duration("sounds/4.wav", am);
            f5 =  Sound_with_Duration("sounds/5.wav", am);
            f6 =  Sound_with_Duration("sounds/6.wav", am);
            f7 =  Sound_with_Duration("sounds/7.wav", am);
            f8 =  Sound_with_Duration("sounds/8.wav", am);
            f9 =  Sound_with_Duration("sounds/9.wav", am);
            f10 = Sound_with_Duration("sounds/10.wav", am);
            f11 = Sound_with_Duration("sounds/11.wav", am);
            f12 = Sound_with_Duration("sounds/12.wav", am);
            f13 = Sound_with_Duration("sounds/13.wav", am);
            f14 = Sound_with_Duration("sounds/14.wav", am);
            f15 = Sound_with_Duration("sounds/15.wav", am);

            oneDo = Sound_with_Duration("sounds/do.wav", am);
//            oneRe = am.get("sounds/re_trumpet.wav", Sound.class);
//            oneMi = am.get("sounds/mi_guitar.wav", Sound.class);
//            oneFa = am.get("sounds/fa_flaute.wav", Sound.class);
//            oneSol = am.get("sounds/sol_harp.wav", Sound.class);
            oneRe = Sound_with_Duration("sounds/re.wav", am);
            oneMi = Sound_with_Duration("sounds/mi.wav", am);
            oneFa = Sound_with_Duration("sounds/fa.wav", am);
            oneSol = Sound_with_Duration("sounds/sol.wav", am);


            puck = Sound_with_Duration("sounds/puck.mp3", am);
            yuju = Sound_with_Duration("sounds/yuju.mp3", am);
            tada = Sound_with_Duration("sounds/tada.mp3", am);

          /*  puck = am.get("sounds/puck.mp3", Sound.class);
            yuju = am.get("sounds/yuju.mp3", Sound.class);
            tada = am.get("sounds/tada.mp3", Sound.class);
*/
            newblock = Sound_with_Duration("sounds/newblock.wav", am);
            addblock = Sound_with_Duration("sounds/masPiezas.wav", am);
            quitblock = Sound_with_Duration("sounds/menosPiezas.wav", am);


            d1 = Sound_with_Duration("sounds/d1.wav", am);
            d2 = Sound_with_Duration("sounds/d2.wav", am);
            d3 = Sound_with_Duration("sounds/d3.wav", am);
            d4 = Sound_with_Duration("sounds/d4.wav", am);
            d5 = Sound_with_Duration("sounds/d5.wav", am);
            d6 = Sound_with_Duration("sounds/d6.wav", am);
            d7 = Sound_with_Duration("sounds/d7.wav", am);
            d8 = Sound_with_Duration("sounds/d8.wav", am);
            d9 = Sound_with_Duration("sounds/d9.wav", am);
            d10 = Sound_with_Duration("sounds/d10.wav", am);



            /*f1 = am.get("sounds/1.wav", Sound.class);
            f2 = am.get("sounds/2.wav", Sound.class);
            f3 = am.get("sounds/3.wav", Sound.class);
            f4 = am.get("sounds/4.wav", Sound.class);
            f5 = am.get("sounds/5.wav", Sound.class);
            f6 = am.get("sounds/6.wav", Sound.class);
            f7 = am.get("sounds/7.wav", Sound.class);
            f8 = am.get("sounds/8.wav", Sound.class);
            f9 = am.get("sounds/9.wav", Sound.class);
            f10 = am.get("sounds/10.wav", Sound.class);
            f11 = am.get("sounds/11.wav", Sound.class);
            f12 = am.get("sounds/12.wav", Sound.class);
            f13 = am.get("sounds/13.wav", Sound.class);
            f14 = am.get("sounds/14.wav", Sound.class);
            f15 = am.get("sounds/15.wav", Sound.class);

//dd
            oneDo = am.get("sounds/do.wav", Sound.class);
//            oneRe = am.get("sounds/re_trumpet.wav", Sound.class);
//            oneMi = am.get("sounds/mi_guitar.wav", Sound.class);
//            oneFa = am.get("sounds/fa_flaute.wav", Sound.class);
//            oneSol = am.get("sounds/sol_harp.wav", Sound.class);
            oneRe = am.get("sounds/re.wav", Sound.class);
            oneMi = am.get("sounds/mi.wav", Sound.class);
            oneFa = am.get("sounds/fa.wav", Sound.class);
            oneSol = am.get("sounds/sol.wav", Sound.class);
            puck = am.get("sounds/puck.mp3", Sound.class);
            yuju = am.get("sounds/yuju.mp3", Sound.class);
            tada = am.get("sounds/tada.mp3", Sound.class);

            newblock = am.get("sounds/newblock.wav", Sound.class);


            addblock = am.get("sounds/masPiezas.wav", Sound.class);
            quitblock = am.get("sounds/menosPiezas.wav", Sound.class);


            d1 = am.get("sounds/d1.wav", Sound.class);
            d2 = am.get("sounds/d2.wav", Sound.class);
            d3 = am.get("sounds/d3.wav", Sound.class);
            d4 = am.get("sounds/d4.wav", Sound.class);
            d5 = am.get("sounds/d5.wav", Sound.class);
            d6 = am.get("sounds/d6.wav", Sound.class);
            d7 = am.get("sounds/d7.wav", Sound.class);
            d8 = am.get("sounds/d8.wav", Sound.class);
            d9 = am.get("sounds/d9.wav", Sound.class);
            d10 = am.get("sounds/d10.wav", Sound.class); */


//sound prueba
/*===============================================================

            e1 = am.get("sounds/e1.wav", Sound.class);
            e2 = am.get("sounds/e2.wav", Sound.class);
            e3 = am.get("sounds/e3.wav", Sound.class);
            e4 = am.get("sounds/e4.wav", Sound.class);
            e5 = am.get("sounds/e5.wav", Sound.class);
            e6 = am.get("sounds/e6.wav", Sound.class);
            e7 = am.get("sounds/e7.wav", Sound.class);
            e8 = am.get("sounds/e8.wav", Sound.class);
            e9 = am.get("sounds/e9.wav", Sound.class);
            e10 = am.get("sounds/e10.wav", Sound.class);

            d11 = am.get("sounds/d11.wav", Sound.class);
            d12 = am.get("sounds/d12.wav", Sound.class);
            d13 = am.get("sounds/d13.wav", Sound.class);
            d14 = am.get("sounds/d14.wav", Sound.class);
            d15 = am.get("sounds/d15.wav", Sound.class);
            d16 = am.get("sounds/d16.wav", Sound.class);
            d17 = am.get("sounds/d17.wav", Sound.class);
            d18 = am.get("sounds/d18.wav", Sound.class);
            d19 = am.get("sounds/d19.wav", Sound.class);
            d20 = am.get("sounds/d20.wav", Sound.class);

            d21 = am.get("sounds/d21.wav", Sound.class);
            d22 = am.get("sounds/d22.wav", Sound.class);
            d23 = am.get("sounds/d23.wav", Sound.class);
            d24 = am.get("sounds/d24.wav", Sound.class);
            d25 = am.get("sounds/d25.wav", Sound.class);
            d26 = am.get("sounds/d26.wav", Sound.class);
            d27 = am.get("sounds/d27.wav", Sound.class);
            d28 = am.get("sounds/d28.wav", Sound.class);
            d29 = am.get("sounds/d29.wav", Sound.class);
            d30 = am.get("sounds/d30.wav", Sound.class);

            d31 = am.get("sounds/d31.wav", Sound.class);
            d32 = am.get("sounds/d32.wav", Sound.class);
            d33 = am.get("sounds/d33.wav", Sound.class);
            d34 = am.get("sounds/d34.wav", Sound.class);
            d35 = am.get("sounds/d35.wav", Sound.class);
            d36 = am.get("sounds/d36.wav", Sound.class);
            d37 = am.get("sounds/d37.wav", Sound.class);
            d38 = am.get("sounds/d38.wav", Sound.class);
            d39 = am.get("sounds/d39.wav", Sound.class);
            d40 = am.get("sounds/d40.wav", Sound.class);

            d41 = am.get("sounds/d41.wav", Sound.class);
            d42 = am.get("sounds/d42.wav", Sound.class);
            d43 = am.get("sounds/d43.wav", Sound.class);
            d44 = am.get("sounds/d44.wav", Sound.class);
            d45 = am.get("sounds/d45.wav", Sound.class);
            d46 = am.get("sounds/d46.wav", Sound.class);
            d47 = am.get("sounds/d47.wav", Sound.class);
            d48 = am.get("sounds/d48.wav", Sound.class);
            d49 = am.get("sounds/d49.wav", Sound.class);
            d50 = am.get("sounds/d50.wav", Sound.class);



            s1 = am.get("sounds/s1.wav", Sound.class);
            s2 = am.get("sounds/s2.wav", Sound.class);
            s3 = am.get("sounds/s3.wav", Sound.class);
            s4 = am.get("sounds/s4.wav", Sound.class);
            s5 = am.get("sounds/s5.wav", Sound.class);
            s6 = am.get("sounds/s6.wav", Sound.class);
            s7 = am.get("sounds/s7.wav", Sound.class);
            s8 = am.get("sounds/s8.wav", Sound.class);
            s9 = am.get("sounds/s9.wav", Sound.class);
            s10 = am.get("sounds/s10.wav", Sound.class);


            s11 = am.get("sounds/s11.wav", Sound.class);
            s12 = am.get("sounds/s12.wav", Sound.class);
            s13 = am.get("sounds/s13.wav", Sound.class);
            s14 = am.get("sounds/s14.wav", Sound.class);
            s15 = am.get("sounds/s15.wav", Sound.class);
            s16 = am.get("sounds/s16.wav", Sound.class);
            s17 = am.get("sounds/s17.wav", Sound.class);
            s18 = am.get("sounds/s18.wav", Sound.class);
            s19 = am.get("sounds/s19.wav", Sound.class);
            s20 = am.get("sounds/s20.wav", Sound.class);


            s21 = am.get("sounds/s21.wav", Sound.class);
            s22 = am.get("sounds/s22.wav", Sound.class);
            s23 = am.get("sounds/s23.wav", Sound.class);
            s24 = am.get("sounds/s24.wav", Sound.class);
            s25 = am.get("sounds/s25.wav", Sound.class);
            s26 = am.get("sounds/s26.wav", Sound.class);
            s27 = am.get("sounds/s27.wav", Sound.class);
            s28 = am.get("sounds/s28.wav", Sound.class);
            s29 = am.get("sounds/s29.wav", Sound.class);
            s30 = am.get("sounds/s30.wav", Sound.class);
*/
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