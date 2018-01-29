package miceta.game.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.backends.lwjgl.audio.OpenALSound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.Disposable;
import miceta.game.core.util.Constants;


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
    public AssetButtons buttons;
    //  public ArrayMap<Music,Double> m_duration = new ArrayMap<>();



    private Assets() {
    }
    public void init(AssetManager assetManager) {
        this.assetManager = assetManager;
        // set asset manager error handler
        assetManager.setErrorListener(this);
        // load texture atlas
        assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);
        // load sounds

        assetManager.load("sounds/numeros/1.wav", Sound.class);
        assetManager.load("sounds/numeros/2.wav", Sound.class);
        assetManager.load("sounds/numeros/3.wav", Sound.class);
        assetManager.load("sounds/numeros/4.wav", Sound.class);
        assetManager.load("sounds/numeros/5.wav", Sound.class);
        assetManager.load("sounds/numeros/6.wav", Sound.class);
        assetManager.load("sounds/numeros/7.wav", Sound.class);
        assetManager.load("sounds/numeros/8.wav", Sound.class);
        assetManager.load("sounds/numeros/9.wav", Sound.class);
        assetManager.load("sounds/numeros/10.wav", Sound.class);
        assetManager.load("sounds/numeros/11.wav", Sound.class);
        assetManager.load("sounds/numeros/12.wav", Sound.class);
        assetManager.load("sounds/numeros/13.wav", Sound.class);
        assetManager.load("sounds/numeros/14.wav", Sound.class);
        assetManager.load("sounds/numeros/15.wav", Sound.class);

        assetManager.load("sounds/feedback/piano/do.wav", Sound.class);
        assetManager.load("sounds/feedback/piano/re.wav", Sound.class);
        assetManager.load("sounds/feedback/piano/mi.wav", Sound.class);
        assetManager.load("sounds/feedback/piano/fa.wav", Sound.class);
        assetManager.load("sounds/feedback/piano/sol.wav", Sound.class);

        assetManager.load("sounds/feedback/puck.mp3", Sound.class);
        assetManager.load("sounds/feedback/yuju.mp3", Sound.class);
        assetManager.load("sounds/feedback/tada.mp3", Sound.class);
        assetManager.load("sounds/feedback/newblock.wav", Sound.class);
        assetManager.load("sounds/feedback/feedbackLoop.wav", Music.class);
        assetManager.load("sounds/feedback/masPiezas.wav", Sound.class);
        assetManager.load("sounds/feedback/menosPiezas.wav", Sound.class);
        assetManager.load("sounds/feedback/knock.wav", Sound.class);


        assetManager.load("sounds/concreto/1.mp3", Sound.class);
        assetManager.load("sounds/concreto/2.mp3", Sound.class);
        assetManager.load("sounds/concreto/3.mp3", Sound.class);
        assetManager.load("sounds/concreto/4.mp3", Sound.class);
        assetManager.load("sounds/concreto/5.mp3", Sound.class);
        assetManager.load("sounds/concreto/6.mp3", Sound.class);
        assetManager.load("sounds/concreto/7.mp3", Sound.class);
        assetManager.load("sounds/concreto/8.mp3", Sound.class);
        assetManager.load("sounds/concreto/9.mp3", Sound.class);
        //micro mundo 1
        // organic tutorial
        assetManager.load("sounds/mm1/tutorial/mm1_intro1.mp3", Sound.class);
        assetManager.load("sounds/mm1/tutorial/mm1_intro2.mp3", Sound.class);
        assetManager.load("sounds/mm1/tutorial/mm1_tooMuch.mp3", Sound.class);
        assetManager.load("sounds/mm1/tutorial/mm1_tooFew.mp3", Sound.class);
        assetManager.load("sounds/mm1/tutorial/mm1_positive.mp3", Sound.class);
        // ingredients game
        assetManager.load("sounds/mm1/ingredientes/1intro.mp3", Sound.class);
        assetManager.load("sounds/mm1/ingredientes/cocodrilo.mp3", Sound.class);
        assetManager.load("sounds/mm1/ingredientes/final.mp3", Sound.class);
        assetManager.load("sounds/mm1/ingredientes/gato.mp3", Sound.class);
        assetManager.load("sounds/mm1/ingredientes/hormiga.mp3", Sound.class);
        assetManager.load("sounds/mm1/ingredientes/lama.mp3", Sound.class);
        assetManager.load("sounds/mm1/ingredientes/masRunas.mp3", Sound.class);
        assetManager.load("sounds/mm1/ingredientes/menosRunas.mp3", Sound.class);
        assetManager.load("sounds/mm1/ingredientes/positivo.mp3", Sound.class);
        assetManager.load("sounds/mm1/ingredientes/rana.mp3", Sound.class);
        assetManager.load("sounds/mm1/ingredientes/vaca.mp3", Sound.class);
        assetManager.load("sounds/mm1/ingredientes/vinagre.mp3", Sound.class);
        // Game 1
        // knock at the door
        assetManager.load("sounds/mm1/golpes/intro.mp3", Sound.class);
        assetManager.load("sounds/mm1/golpes/masRunas.mp3", Sound.class);
        assetManager.load("sounds/mm1/golpes/menosRunas.mp3", Sound.class);

        assetManager.load("sounds/mm1/timbre/introduccion_timbre.mp3", Sound.class);
        assetManager.load("sounds/mm1/timbre/resolucion_timbre.mp3", Sound.class);


        assetManager.load("sounds/mm1/timbre/timbre_masRunas.mp3", Sound.class);
        assetManager.load("sounds/mm1/timbre/timbre_menosRunas.mp3", Sound.class);
        assetManager.load("sounds/mm1/timbre/res_ensayo_timbre.mp3", Sound.class);

        assetManager.load("sounds/mm1/revolver/revolver_explicacion.mp3", Sound.class);
        assetManager.load("sounds/mm1/revolver/revolver_res_ensayo.mp3", Sound.class);
        assetManager.load("sounds/mm1/revolver/revolver_masRunas.mp3", Sound.class);
        assetManager.load("sounds/mm1/revolver/revolver_menosRunas.mp3", Sound.class);
        assetManager.load("sounds/mm1/revolver/revolver_res_final.mp3", Sound.class);

        assetManager.load("sounds/mm1/instrumentos/instrumentos_introduccion.mp3", Sound.class);
        assetManager.load("sounds/mm1/instrumentos/instrumentos_res_ensayo.mp3", Sound.class);
        assetManager.load("sounds/mm1/instrumentos/instrumentos_masRunas.mp3", Sound.class);
        assetManager.load("sounds/mm1/instrumentos/instrumentos_menosRunas.mp3", Sound.class);
        assetManager.load("sounds/mm1/instrumentos/instrumentos_final.mp3", Sound.class);

        assetManager.load("sounds/opciones/jugar.wav", Sound.class);
        assetManager.load("sounds/opciones/denuevo.wav", Sound.class);
        assetManager.load("sounds/opciones/ayuda.wav", Sound.class);
        assetManager.load("sounds/opciones/salir.wav", Sound.class);

        assetManager.load("music/song1.mp3", Music.class);
        assetManager.load("music/song2.mp3", Music.class);
        // start loading assets and wait until finished
        assetManager.finishLoading();

        Gdx.app.debug(TAG, "# of assets loaded: " + assetManager.getAssetNames().size);
        for (String a : assetManager.getAssetNames())
            Gdx.app.debug(TAG, "asset: " + a);
        TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS_OBJECTS);
        buttons = new AssetButtons(atlas);
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
        public final  Sound number1, number2, number3, number4, number5, number6, number7, number8, number9, number10, number11, number12, number13, number14, number15;
        public final Sound ct_1,ct_2,ct_3,ct_4,ct_5,ct_6,ct_7,ct_8,ct_9; /*ct_10, ct_11;*/
        public final Sound oneDo, oneRe, oneMi, oneFa, oneSol;
        public final Sound puck,yuju, newblock, addblock, quitblock, tada;
        public final Sound knock;
        public final Sound tmm1_1,tmm1_2,tmm1_tooMuch,tmm1_tooFew, tmm1_positive;
        public final Sound jugar, denuevo, ayuda, salir;
        public final Sound ingredientsIntro,ingredientsCrocodile,ingredientsCat,ingredientsAnt,ingredientsLama,ingredientsFrog,ingredientsCow, ingredientsVinegar,ingredientsMore,ingredientsLess, ingredientsPositive,ingredientsFinal;
        public final Sound game_1, game_1_tooFew, game_1_tooMuch;
        public final Sound game_5, game_5_resolution, game_5_tooFew, game_5_tooMuch;//, game_5_test;
        public final Sound game_3, game_3_resolution, game_3_tooFew, game_3_tooMuch, game_3_test;
        public final Sound game_4, game_4_resolution, game_4_tooFew, game_4_tooMuch, game_4_test;

        //concrete tutorial




        public Sound soundWithDuration(String path, AssetManager am) {
            Sound sound_load;
            sound_load = am.get(path, Sound.class);
            Gdx.app.log(TAG, "wowowowowoowow --> path " + path + " dura " + String.valueOf(((OpenALSound) (sound_load)).duration()));
            float soundDuration = ((OpenALSound) (sound_load)).duration();
            s_duration.put(sound_load, soundDuration);
            return sound_load;
        }

        public AssetSounds (AssetManager am) {

            number1 =  soundWithDuration("sounds/numeros/1.wav", am);
            number2 =  soundWithDuration("sounds/numeros/2.wav", am);
            number3 =  soundWithDuration("sounds/numeros/3.wav", am);
            number4 =  soundWithDuration("sounds/numeros/4.wav", am);
            number5 =  soundWithDuration("sounds/numeros/5.wav", am);
            number6 =  soundWithDuration("sounds/numeros/6.wav", am);
            number7 =  soundWithDuration("sounds/numeros/7.wav", am);
            number8 =  soundWithDuration("sounds/numeros/8.wav", am);
            number9 =  soundWithDuration("sounds/numeros/9.wav", am);
            number10 = soundWithDuration("sounds/numeros/10.wav", am);
            number11 = soundWithDuration("sounds/numeros/11.wav", am);
            number12 = soundWithDuration("sounds/numeros/12.wav", am);
            number13 = soundWithDuration("sounds/numeros/13.wav", am);
            number14 = soundWithDuration("sounds/numeros/14.wav", am);
            number15 = soundWithDuration("sounds/numeros/15.wav", am);

            oneDo = soundWithDuration("sounds/feedback/piano/do.wav", am);
            oneRe = soundWithDuration("sounds/feedback/piano/re.wav", am);
            oneMi = soundWithDuration("sounds/feedback/piano/mi.wav", am);
            oneFa = soundWithDuration("sounds/feedback/piano/fa.wav", am);
            oneSol = soundWithDuration("sounds/feedback/piano/sol.wav", am);

            puck = soundWithDuration("sounds/feedback/puck.mp3", am);
            yuju = soundWithDuration("sounds/feedback/yuju.mp3", am);
            tada = soundWithDuration("sounds/feedback/tada.mp3", am);

            newblock = soundWithDuration("sounds/feedback/newblock.wav", am);
            addblock = soundWithDuration("sounds/feedback/masPiezas.wav", am);
            quitblock = soundWithDuration("sounds/feedback/menosPiezas.wav", am);

            knock = soundWithDuration("sounds/feedback/knock.wav", am);

            ct_1 = soundWithDuration("sounds/concreto/1.mp3", am);
            ct_2 = soundWithDuration("sounds/concreto/2.mp3", am);
            ct_3 = soundWithDuration("sounds/concreto/3.mp3", am);
            ct_4 = soundWithDuration("sounds/concreto/4.mp3", am);
            ct_5 = soundWithDuration("sounds/concreto/5.mp3", am);
            ct_6 = soundWithDuration("sounds/concreto/6.mp3", am);
            ct_7 = soundWithDuration("sounds/concreto/7.mp3", am);
            ct_8 = soundWithDuration("sounds/concreto/8.mp3", am);
            ct_9 = soundWithDuration("sounds/concreto/9.mp3", am);

            tmm1_1 = soundWithDuration("sounds/mm1/tutorial/mm1_intro1.mp3", am);
            tmm1_2 = soundWithDuration("sounds/mm1/tutorial/mm1_intro2.mp3", am);
            tmm1_tooMuch = soundWithDuration("sounds/mm1/tutorial/mm1_tooMuch.mp3", am);
            tmm1_tooFew = soundWithDuration("sounds/mm1/tutorial/mm1_tooFew.mp3", am);
            tmm1_positive = soundWithDuration("sounds/mm1/tutorial/mm1_positive.mp3", am);

            ingredientsIntro = soundWithDuration("sounds/mm1/ingredientes/1intro.mp3", am);
            ingredientsCrocodile = soundWithDuration("sounds/mm1/ingredientes/cocodrilo.mp3", am);
            ingredientsCat = soundWithDuration("sounds/mm1/ingredientes/gato.mp3", am);
            ingredientsAnt = soundWithDuration("sounds/mm1/ingredientes/hormiga.mp3", am);
            ingredientsLama = soundWithDuration("sounds/mm1/ingredientes/lama.mp3", am);
            ingredientsFrog = soundWithDuration("sounds/mm1/ingredientes/rana.mp3", am);
            ingredientsCow = soundWithDuration("sounds/mm1/ingredientes/vaca.mp3", am);
            ingredientsVinegar = soundWithDuration("sounds/mm1/ingredientes/vinagre.mp3", am);
            ingredientsMore = soundWithDuration("sounds/mm1/ingredientes/masRunas.mp3", am);
            ingredientsLess = soundWithDuration("sounds/mm1/ingredientes/menosRunas.mp3", am);
            ingredientsPositive = soundWithDuration("sounds/mm1/ingredientes/positivo.mp3", am);
            ingredientsFinal = soundWithDuration("sounds/mm1/ingredientes/final.mp3", am);

            jugar = soundWithDuration("sounds/opciones/jugar.wav", am);
            denuevo = soundWithDuration("sounds/opciones/denuevo.wav", am);
            ayuda = soundWithDuration("sounds/opciones/ayuda.wav", am);
            salir = soundWithDuration("sounds/opciones/salir.wav", am);
            game_1 = soundWithDuration("sounds/mm1/golpes/intro.mp3", am);
            game_1_tooFew = soundWithDuration("sounds/mm1/golpes/masRunas.mp3", am);
            game_1_tooMuch = soundWithDuration("sounds/mm1/golpes/menosRunas.mp3", am);

            game_5 = soundWithDuration("sounds/mm1/timbre/introduccion_timbre.mp3", am);
            game_5_resolution = soundWithDuration("sounds/mm1/timbre/resolucion_timbre.mp3", am);
            game_5_tooFew = soundWithDuration("sounds/mm1/timbre/timbre_masRunas.mp3", am);
            game_5_tooMuch = soundWithDuration("sounds/mm1/timbre/timbre_menosRunas.mp3", am);
         //   game_5_test = soundWithDuration("sounds/mm1/timbre/res_ensayo_timbre.mp3", am);

            game_3 = soundWithDuration("sounds/mm1/revolver/revolver_explicacion.mp3", am);
            game_3_resolution = soundWithDuration("sounds/mm1/revolver/revolver_res_ensayo.mp3", am);
            game_3_tooFew = soundWithDuration("sounds/mm1/revolver/revolver_masRunas.mp3", am);
            game_3_tooMuch = soundWithDuration("sounds/mm1/revolver/revolver_menosRunas.mp3", am);
            game_3_test = soundWithDuration("sounds/mm1/revolver/revolver_res_final.mp3", am);

            game_4 = soundWithDuration("sounds/mm1/instrumentos/instrumentos_introduccion.mp3", am);
            game_4_resolution = soundWithDuration("sounds/mm1/instrumentos/instrumentos_res_ensayo.mp3", am);
            game_4_tooFew = soundWithDuration("sounds/mm1/instrumentos/instrumentos_masRunas.mp3", am);
            game_4_tooMuch = soundWithDuration("sounds/mm1/instrumentos/instrumentos_menosRunas.mp3", am);
            game_4_test = soundWithDuration("sounds/mm1/instrumentos/instrumentos_final.mp3", am);


        }
    }

    public class AssetMusic {
        public final Music song01;
        public final Music song02;
        public final Music new_block_loop;

        public AssetMusic (AssetManager am) {
            song01 = am.get("music/song1.mp3", Music.class);
            song02 = am.get("music/song2.mp3", Music.class);
            new_block_loop  = am.get("sounds/feedback/feedbackLoop.wav", Music.class);

        }
    }

    public class AssetButtons {
        public final ImageButton.ImageButtonStyle playButtonStyle;
        public final ImageButton.ImageButtonStyle newStartButtonStyle;
        public final ImageButton.ImageButtonStyle exitButtonStyle;
        public final ImageButton.ImageButtonStyle helpButtonStyle;

        public  AssetButtons(TextureAtlas atlas) {
            playButtonStyle = new ImageButton.ImageButtonStyle();  //Instaciate
            playButtonStyle.up = new TextureRegionDrawable(atlas.findRegion("jugar-1")); //Set image for not pressed button
            playButtonStyle.down= new TextureRegionDrawable(atlas.findRegion("jugar-2"));  //Set image for pressed
            playButtonStyle.over= new TextureRegionDrawable(atlas.findRegion("jugar-2"));

            exitButtonStyle = new ImageButton.ImageButtonStyle();  //Instaciate
            exitButtonStyle.up = new TextureRegionDrawable(atlas.findRegion("salir-1")); //Set image for not pressed button
            exitButtonStyle.down= new TextureRegionDrawable(atlas.findRegion("salir-2"));  //Set image for pressed
            exitButtonStyle.over= new TextureRegionDrawable(atlas.findRegion("salir-2"));

            newStartButtonStyle = new ImageButton.ImageButtonStyle();  //Instaciate
            newStartButtonStyle.up = new TextureRegionDrawable(atlas.findRegion("reiniciar-1")); //Set image for not pressed button
            newStartButtonStyle.down= new TextureRegionDrawable(atlas.findRegion("reiniciar-2"));  //Set image for pressed
            newStartButtonStyle.over= new TextureRegionDrawable(atlas.findRegion("reiniciar-2"));

            helpButtonStyle = new ImageButton.ImageButtonStyle();  //Instaciate
            helpButtonStyle.up = new TextureRegionDrawable(atlas.findRegion("ayuda-1")); //Set image for not pressed button
            helpButtonStyle.down= new TextureRegionDrawable(atlas.findRegion("ayuda-2"));  //Set image for pressed
            helpButtonStyle.over= new TextureRegionDrawable(atlas.findRegion("ayuda-2"));
        }

    }
}