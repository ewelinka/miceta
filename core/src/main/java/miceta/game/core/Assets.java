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

import java.util.ArrayList;


/**
 * Created by ewe on 8/10/17.
 */
public class Assets implements Disposable, AssetErrorListener {
    private static final String TAG = Assets.class.getName();
    public static final Assets instance = new Assets();
    private AssetManager assetManager;
    public AssetSounds sounds;
    public AssetMusic music;
    private final ArrayMap<Sound,Float> s_duration = new ArrayMap<>();
    public AssetButtons buttons;


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
        assetManager.load("sounds/feedback/magic/fuerte2.mp3", Sound.class);
        assetManager.load("sounds/feedback/magic/medio2.mp3", Sound.class);
        assetManager.load("sounds/feedback/magic/suave2.mp3", Sound.class);

        assetManager.load("sounds/feedback/puck.mp3", Sound.class);
        assetManager.load("sounds/feedback/yuju.mp3", Sound.class);
        assetManager.load("sounds/feedback/tada.mp3", Sound.class);
        assetManager.load("sounds/feedback/newblock.wav", Sound.class);
        assetManager.load("sounds/feedback/feedbackLoop.wav", Music.class);
        assetManager.load("sounds/feedback/masPiezas.wav", Sound.class);
        assetManager.load("sounds/feedback/menosPiezas.wav", Sound.class);
        assetManager.load("sounds/feedback/knock.wav", Sound.class);
        assetManager.load("sounds/feedback/knock_2.mp3", Sound.class);
        assetManager.load("sounds/feedback/knock_3.mp3", Sound.class);
        assetManager.load("sounds/feedback/bell.mp3", Sound.class);
        assetManager.load("sounds/feedback/bell_2.mp3", Sound.class);
        assetManager.load("sounds/feedback/bell_3.mp3", Sound.class);
        assetManager.load("sounds/feedback/mixing.mp3", Sound.class);
        assetManager.load("sounds/feedback/music.wav", Sound.class);

        assetManager.load("sounds/concreto/1.mp3", Sound.class);
        assetManager.load("sounds/concreto/2.mp3", Sound.class);
        assetManager.load("sounds/concreto/3.mp3", Sound.class);
        assetManager.load("sounds/concreto/4.mp3", Sound.class);
        assetManager.load("sounds/concreto/5.mp3", Sound.class);
        assetManager.load("sounds/concreto/6.mp3", Sound.class);
        assetManager.load("sounds/concreto/7.mp3", Sound.class);
        assetManager.load("sounds/concreto/8.mp3", Sound.class);
        assetManager.load("sounds/concreto/9.mp3", Sound.class);
        assetManager.load("sounds/concreto/TutConcreto_10_otroVolumen.mp3", Sound.class);
        assetManager.load("sounds/concreto/11.mp3", Sound.class);
        //micro mundo 1
        // organic tutorial
        assetManager.load("sounds/mm1/tutorial/mm1_intro.mp3", Sound.class);
        assetManager.load("sounds/mm1/tutorial/TutMicroMundo1_2_1.mp3", Sound.class);
        assetManager.load("sounds/mm1/tutorial/TutMicroMundo1_3.mp3", Sound.class);
        assetManager.load("sounds/mm1/tutorial/TutMicroMundo1_3_a.mp3", Sound.class);
        assetManager.load("sounds/mm1/tutorial/TutMicroMundo1_4.mp3", Sound.class);
        assetManager.load("sounds/mm1/tutorial/TutMicroMundo1_4_a.mp3", Sound.class);
        assetManager.load("sounds/mm1/tutorial/TutMicroMundo1_ResEnsayo1.mp3", Sound.class);
        assetManager.load("sounds/mm1/tutorial/TutMicroMundo1_ResEnsayo2.mp3", Sound.class);
        assetManager.load("sounds/mm1/tutorial/TutMicroMundo1_ResJuego.mp3", Sound.class);
        assetManager.load("sounds/mm1/tutorial/vueltaAlPasado_Reset.mp3", Sound.class);

        // ingredients game
        assetManager.load("sounds/mm1/ingredientes/MicroMundo_Juego1_1.mp3", Sound.class);
        assetManager.load("sounds/mm1/ingredientes/MicroMundo_Juego1_Cocodrilo.mp3", Sound.class);
        assetManager.load("sounds/mm1/ingredientes/MicroMundo_Juego1_ResolucionJuego.mp3", Sound.class);
        assetManager.load("sounds/mm1/ingredientes/MicroMundo_Juego1_Gato.mp3", Sound.class);
        assetManager.load("sounds/mm1/ingredientes/MicroMundo_Juego1_Hormiga.mp3", Sound.class);
        assetManager.load("sounds/mm1/ingredientes/MicroMundo_Juego1_Lama.mp3", Sound.class);
        assetManager.load("sounds/mm1/ingredientes/MicroMundo_Juego1_MasRunas.mp3", Sound.class);
        assetManager.load("sounds/mm1/ingredientes/MicroMundo_Juego1_MenosRunas.mp3", Sound.class);
        assetManager.load("sounds/mm1/ingredientes/MicroMundo_Juego1_ResolucionEnsayo0.mp3", Sound.class);
        assetManager.load("sounds/mm1/ingredientes/MicroMundo_Juego1_ResolucionEnsayo1.mp3", Sound.class);
        assetManager.load("sounds/mm1/ingredientes/MicroMundo_Juego1_ResolucionEnsayo2.mp3", Sound.class);
        assetManager.load("sounds/mm1/ingredientes/MicroMundo_Juego1_ResolucionEnsayo3.mp3", Sound.class);
        assetManager.load("sounds/mm1/ingredientes/MicroMundo_Juego1_Rana.mp3", Sound.class);
        assetManager.load("sounds/mm1/ingredientes/MicroMundo_Juego1_Pezuña.mp3", Sound.class);
        assetManager.load("sounds/mm1/ingredientes/MicroMundo_Juego1_Vinagre.mp3", Sound.class);
        // Game 1
        // knock at the door
        assetManager.load("sounds/mm1/golpes/MicroMundo1_Juego0_Explicacion.mp3", Sound.class);
        assetManager.load("sounds/mm1/golpes/MicroMundo1_Juego0_MasRunas.mp3", Sound.class);
        assetManager.load("sounds/mm1/golpes/MicroMundo1_Juego0_MenosRunas.mp3", Sound.class);
        assetManager.load("sounds/mm1/golpes/MicroMundo1_Juego0_ResolucionEnsayo1.mp3", Sound.class);
        assetManager.load("sounds/mm1/golpes/MicroMundo1_Juego0_ResolucionEnsayo2.mp3", Sound.class);
        assetManager.load("sounds/mm1/golpes/MicroMundo1_Juego0_ResolucionEnsayo3.mp3", Sound.class);
        assetManager.load("sounds/mm1/golpes/MicroMundo1_Juego0_ResolucionJuego.mp3", Sound.class);
        // door bell
        assetManager.load("sounds/mm1/timbre/MicroMundo1_Juego5_Introduccion.mp3", Sound.class);
        assetManager.load("sounds/mm1/timbre/MicroMundo1_Juego5_ResolucionJuego.mp3", Sound.class);
        assetManager.load("sounds/mm1/timbre/MicroMundo1_Juego5_MasRunas.mp3", Sound.class);
        assetManager.load("sounds/mm1/timbre/MicroMundo1_Juego5_MenosRunas.mp3", Sound.class);
        assetManager.load("sounds/mm1/timbre/MicroMundo1_Juego5_ResolucionEnsayo.mp3", Sound.class);
        // mixing
        assetManager.load("sounds/mm1/revolver/MicroMundo_Juego3_Explicacion.mp3", Sound.class);
        assetManager.load("sounds/mm1/revolver/MicroMundo_Juego3_ResolucionEnsayo.mp3", Sound.class);
        assetManager.load("sounds/mm1/revolver/MicroMundo_Juego3_MasRunas.mp3", Sound.class);
        assetManager.load("sounds/mm1/revolver/MicroMundo_Juego3_MenosRunas.mp3", Sound.class);
        assetManager.load("sounds/mm1/revolver/MicroMundo_Juego3_ResolucionJuego.mp3", Sound.class);
        assetManager.load("sounds/mm1/revolver/MicroMundo_Juego3_ResolucionEnsayo1.mp3", Sound.class);
        assetManager.load("sounds/mm1/revolver/MicroMundo_Juego3_ResolucionEnsayo2.mp3", Sound.class);
        assetManager.load("sounds/mm1/revolver/MicroMundo_Juego3_ResolucionEnsayo3.mp3", Sound.class);
        // music
        assetManager.load("sounds/mm1/instrumentos/MicroMundo_Juego4_Inicio1.mp3", Sound.class);
        assetManager.load("sounds/mm1/instrumentos/MicroMundo_Juego4_Inicio2.mp3", Sound.class);
        assetManager.load("sounds/mm1/instrumentos/MicroMundo_Juego4_ResolucionEnsayo.mp3", Sound.class);
        assetManager.load("sounds/mm1/instrumentos/MicroMundo_Juego4_ResolucionEnsayo1.mp3", Sound.class);
        assetManager.load("sounds/mm1/instrumentos/MicroMundo_Juego4_ResolucionEnsayo2.mp3", Sound.class);
        assetManager.load("sounds/mm1/instrumentos/MicroMundo_Juego4_MasRunas.mp3", Sound.class);
        assetManager.load("sounds/mm1/instrumentos/MicroMundo_Juego4_MenosRunas.mp3", Sound.class);
        assetManager.load("sounds/mm1/instrumentos/MicroMundo_Juego4_ResolucionJuego.mp3", Sound.class);

        assetManager.load("sounds/opciones/jugar.wav", Sound.class);
        assetManager.load("sounds/opciones/denuevo.wav", Sound.class);
        assetManager.load("sounds/opciones/ayuda.wav", Sound.class);
        assetManager.load("sounds/opciones/salir.wav", Sound.class);

        assetManager.load("music/song1.mp3", Music.class);
        assetManager.load("music/song2.mp3", Music.class);

        assetManager.load("sounds/mm1/vueltaAlPasado.mp3", Sound.class);
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
        Gdx.app.error(TAG, "Couldn't load asset '" +  asset.fileName + "'", throwable);
    }

    public class AssetSounds {
        public final Sound number1, number2, number3, number4, number5, number6, number7, number8, number9, number10, number11, number12, number13, number14, number15;
        public final Sound ct_1,ct_2,ct_3,ct_4,ct_5,ct_6,ct_7,ct_8,ct_9, ct_10, ct_11;
        public final Sound oneDo, oneRe, oneMi, oneFa, oneSol;
        public final Sound puck,yuju, newblock, addblock, quitblock, tada;
        public final Sound music, mixing, bell, bell_2,knock, knock_2, knock_3, bell_3;
        public final Sound organicHelpIntro,organicHelpTooMuch_1,organicHelpTooFew_1, organicHelpTooMuch_2,organicHelpTooFew_2, organicHelpPositive_1,organicHelpPositive_2, organicHelpFinal, organicHelpIntro2, organicHelpFinal2;
        public final Sound stepIntro,stepTooMuch_1,stepTooFew_1, stepTooMuch_2,stepTooFew_2, stepPositive_1,stepPositive_2, stepFinal, stepIntro2, stepFinal2;
        public final Sound jugar, denuevo, ayuda, salir;
        public final Sound ingredientsIntro,ingredientsCrocodile,ingredientsCat,ingredientsAnt,ingredientsLama,ingredientsFrog,ingredientsCow, ingredientsVinegar,ingredientsTooFew,ingredientsTooMuch, ingredientsPositive_1,ingredientsPositive_2, ingredientsPositive_3, ingredientsPositive_4,ingredientsFinal;
        public final Sound knockIntro, knockTooFew, knockTooMuch, knockPositive_1, knockPositive_2, knockPositive_3, knockFinal;
        public final Sound bellIntro, bellFinal, bellTooFew, bellTooMuch, bellPositive;
        public final Sound mixingIntro, mixingFinal, mixingTooFew, mixingTooMuch, mixingPositive_1, mixingPositive_2, mixingPositive_3, mixingPositive_4;
        public final Sound  musicIntro_1, musicIntro_2, musicFinal, musicTooFew, musicTooMuch, musicPositive_1, musicPositive_2, musicPositive_3;
        public final Sound magicStrong, magicMiddle, magicNomal;
        public final Sound goToThePast, cameFromPast;

        public  ArrayList<Sound> positivesOrganicHelp = new ArrayList<>();
        public  ArrayList<Sound> positivesStep= new ArrayList<>();
        public  ArrayList<Sound> positivesKnock = new ArrayList<>();
        public  ArrayList<Sound> positivesMixing = new ArrayList<>();
        public  ArrayList<Sound> positivesIngredients = new ArrayList<>();
        public  ArrayList<Sound> positivesMusic = new ArrayList<>();
        public  ArrayList<Sound> positivesBell = new ArrayList<>();
        public  ArrayList<Sound> positivesFeedbacks = new ArrayList<>();

        public final ArrayList<Sound> cluesOrganicHelp = new ArrayList<>();
        public final ArrayList<Sound> cluesSteps = new ArrayList<>();
        public final ArrayList<Sound> cluesKnock = new ArrayList<>();
        public final ArrayList<Sound> cluesIngredients = new ArrayList<>();
        public final ArrayList<Sound> cluesMixing = new ArrayList<>();
        public final ArrayList<Sound> cluesMusic = new ArrayList<>();
        public final ArrayList<Sound> cluesBell = new ArrayList<>();



        public Sound soundWithDuration(String path, AssetManager am) {
            Sound sound_load;
            sound_load = am.get(path, Sound.class);
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
            magicStrong = soundWithDuration("sounds/feedback/magic/fuerte2.mp3", am);
            magicMiddle = soundWithDuration("sounds/feedback/magic/medio2.mp3", am);
            magicNomal = soundWithDuration("sounds/feedback/magic/suave2.mp3", am);

            puck = soundWithDuration("sounds/feedback/puck.mp3", am);
            yuju = soundWithDuration("sounds/feedback/yuju.mp3", am);
            tada = soundWithDuration("sounds/feedback/tada.mp3", am);

            newblock = soundWithDuration("sounds/feedback/newblock.wav", am);
            addblock = soundWithDuration("sounds/feedback/masPiezas.wav", am);
            quitblock = soundWithDuration("sounds/feedback/menosPiezas.wav", am);

            ct_1 = soundWithDuration("sounds/concreto/1.mp3", am);
            ct_2 = soundWithDuration("sounds/concreto/2.mp3", am);
            ct_3 = soundWithDuration("sounds/concreto/3.mp3", am);
            ct_4 = soundWithDuration("sounds/concreto/4.mp3", am);
            ct_5 = soundWithDuration("sounds/concreto/5.mp3", am);
            ct_6 = soundWithDuration("sounds/concreto/6.mp3", am);
            ct_7 = soundWithDuration("sounds/concreto/7.mp3", am);
            ct_8 = soundWithDuration("sounds/concreto/8.mp3", am);
            ct_9 = soundWithDuration("sounds/concreto/9.mp3", am);
            ct_10 = soundWithDuration("sounds/concreto/TutConcreto_10_otroVolumen.mp3", am);
            ct_11 = soundWithDuration("sounds/concreto/11.mp3", am);

            // tutorial audios fist part
            organicHelpIntro = soundWithDuration("sounds/mm1/tutorial/mm1_intro.mp3", am);
            organicHelpTooMuch_1 = soundWithDuration("sounds/mm1/tutorial/mm1_intro.mp3", am);
            organicHelpTooFew_1 = soundWithDuration("sounds/mm1/tutorial/mm1_intro.mp3", am);
            organicHelpPositive_1 = soundWithDuration("sounds/mm1/tutorial/mm1_intro.mp3", am);
            organicHelpFinal = tada;
            // second part
            organicHelpIntro2 = soundWithDuration("sounds/mm1/tutorial/mm1_intro.mp3", am);
            organicHelpTooMuch_2= soundWithDuration("sounds/mm1/tutorial/mm1_intro.mp3", am);
            organicHelpTooFew_2= soundWithDuration("sounds/mm1/tutorial/mm1_intro.mp3", am);
            organicHelpPositive_2= soundWithDuration("sounds/mm1/tutorial/mm1_intro.mp3", am);
            organicHelpFinal2 = soundWithDuration("sounds/mm1/tutorial/mm1_intro.mp3", am);
            //positivesOrganicHelp.add(organicHelpPositive_2);
            cluesOrganicHelp.add(puck); // TODO change when we have audios with clap

            //steps
            stepIntro = soundWithDuration("sounds/mm1/tutorial/mm1_intro.mp3", am);
            stepTooMuch_1 = soundWithDuration("sounds/mm1/tutorial/TutMicroMundo1_4.mp3", am);
            stepTooFew_1 = soundWithDuration("sounds/mm1/tutorial/TutMicroMundo1_3.mp3", am);
            stepPositive_1 = soundWithDuration("sounds/mm1/tutorial/TutMicroMundo1_ResEnsayo1.mp3", am);
            stepFinal= soundWithDuration("sounds/mm1/tutorial/TutMicroMundo1_ResJuego.mp3", am);

            stepIntro2 = soundWithDuration("sounds/mm1/tutorial/TutMicroMundo1_2_1.mp3", am);
            stepTooMuch_2 = soundWithDuration("sounds/mm1/tutorial/TutMicroMundo1_4_a.mp3", am);
            stepTooFew_2 = soundWithDuration("sounds/mm1/tutorial/TutMicroMundo1_3_a.mp3", am);
            stepPositive_2 = soundWithDuration("sounds/mm1/tutorial/TutMicroMundo1_ResEnsayo2.mp3", am);
            stepFinal2 = soundWithDuration("sounds/mm1/tutorial/TutMicroMundo1_ResJuego.mp3", am);
//            positivesStep.add(tmm1_positive_1);
//            positivesStep.add(tmm1_positive_2);
            cluesSteps.add(puck); // TODO change when we have audios with steps

            // knock game audios
            knockIntro = soundWithDuration("sounds/mm1/golpes/MicroMundo1_Juego0_Explicacion.mp3", am);
            knockTooFew = soundWithDuration("sounds/mm1/golpes/MicroMundo1_Juego0_MasRunas.mp3", am);
            knockTooMuch = soundWithDuration("sounds/mm1/golpes/MicroMundo1_Juego0_MenosRunas.mp3", am);
            knockPositive_1 = soundWithDuration("sounds/mm1/golpes/MicroMundo1_Juego0_ResolucionEnsayo1.mp3", am);
            knockPositive_2 = soundWithDuration("sounds/mm1/golpes/MicroMundo1_Juego0_ResolucionEnsayo2.mp3", am);
            knockPositive_3 = soundWithDuration("sounds/mm1/golpes/MicroMundo1_Juego0_ResolucionEnsayo3.mp3", am);
            knockFinal = soundWithDuration("sounds/mm1/golpes/MicroMundo1_Juego0_ResolucionJuego.mp3", am);
//            positivesKnock.add(knockPositive_1);
//            positivesKnock.add(knockPositive_2);
            //positivesKnock.add(knockPositive_3);  TODO now is "ya casi estamos" - makes no sense!
            knock = soundWithDuration("sounds/feedback/knock.wav", am);
            knock_2 = soundWithDuration("sounds/feedback/knock_2.mp3", am);
            knock_3 = soundWithDuration("sounds/feedback/knock_3.mp3", am);
            //cluesKnock.add(knock);
            cluesKnock.add(knock_2);
            //cluesKnock.add(knock_3);

            ingredientsIntro = soundWithDuration("sounds/mm1/ingredientes/MicroMundo_Juego1_1.mp3", am);
            ingredientsCrocodile = soundWithDuration("sounds/mm1/ingredientes/MicroMundo_Juego1_Cocodrilo.mp3", am);
            ingredientsCat = soundWithDuration("sounds/mm1/ingredientes/MicroMundo_Juego1_Gato.mp3", am);
            ingredientsAnt = soundWithDuration("sounds/mm1/ingredientes/MicroMundo_Juego1_Hormiga.mp3", am);
            ingredientsLama = soundWithDuration("sounds/mm1/ingredientes/MicroMundo_Juego1_Lama.mp3", am);
            ingredientsFrog = soundWithDuration("sounds/mm1/ingredientes/MicroMundo_Juego1_Rana.mp3", am);
            ingredientsCow = soundWithDuration("sounds/mm1/ingredientes/MicroMundo_Juego1_Pezuña.mp3", am);
            ingredientsVinegar = soundWithDuration("sounds/mm1/ingredientes/MicroMundo_Juego1_Vinagre.mp3", am);
            ingredientsTooFew = soundWithDuration("sounds/mm1/ingredientes/MicroMundo_Juego1_MasRunas.mp3", am);
            ingredientsTooMuch = soundWithDuration("sounds/mm1/ingredientes/MicroMundo_Juego1_MenosRunas.mp3", am);
            ingredientsPositive_1 = soundWithDuration("sounds/mm1/ingredientes/MicroMundo_Juego1_ResolucionEnsayo0.mp3", am);
            ingredientsPositive_2 = soundWithDuration("sounds/mm1/ingredientes/MicroMundo_Juego1_ResolucionEnsayo1.mp3", am);
            ingredientsPositive_3 = soundWithDuration("sounds/mm1/ingredientes/MicroMundo_Juego1_ResolucionEnsayo2.mp3", am);
            ingredientsPositive_4 = soundWithDuration("sounds/mm1/ingredientes/MicroMundo_Juego1_ResolucionEnsayo3.mp3", am);
            ingredientsFinal = soundWithDuration("sounds/mm1/ingredientes/MicroMundo_Juego1_ResolucionJuego.mp3", am);
//            positivesIngredients.add(ingredientsPositive_1);
//            positivesIngredients.add(ingredientsPositive_2);
//            positivesIngredients.add(ingredientsPositive_3);
//            positivesIngredients.add(ingredientsPositive_4);
            cluesIngredients.add(puck);

            mixingIntro = soundWithDuration("sounds/mm1/revolver/MicroMundo_Juego3_Explicacion.mp3", am);
            mixingPositive_1 = soundWithDuration("sounds/mm1/revolver/MicroMundo_Juego3_ResolucionEnsayo.mp3", am);
            mixingPositive_2 = soundWithDuration("sounds/mm1/revolver/MicroMundo_Juego3_ResolucionEnsayo1.mp3", am);
            mixingPositive_3 = soundWithDuration("sounds/mm1/revolver/MicroMundo_Juego3_ResolucionEnsayo2.mp3", am);
            mixingPositive_4 = soundWithDuration("sounds/mm1/revolver/MicroMundo_Juego3_ResolucionEnsayo3.mp3", am);
            mixingTooFew = soundWithDuration("sounds/mm1/revolver/MicroMundo_Juego3_MasRunas.mp3", am);
            mixingTooMuch = soundWithDuration("sounds/mm1/revolver/MicroMundo_Juego3_MenosRunas.mp3", am);
            mixingFinal = soundWithDuration("sounds/mm1/revolver/MicroMundo_Juego3_ResolucionJuego.mp3", am);
//            positivesMixing.add(mixingPositive_1);
//            positivesMixing.add(mixingPositive_2);
//            positivesMixing.add(mixingPositive_3);
//            positivesMixing.add(mixingPositive_4);
            mixing = soundWithDuration("sounds/feedback/mixing.mp3", am);
            cluesMixing.add(mixing);

            musicIntro_1 = soundWithDuration("sounds/mm1/instrumentos/MicroMundo_Juego4_Inicio1.mp3", am);
            musicIntro_2 = soundWithDuration("sounds/mm1/instrumentos/MicroMundo_Juego4_Inicio2.mp3", am);
            musicPositive_1 = soundWithDuration("sounds/mm1/instrumentos/MicroMundo_Juego4_ResolucionEnsayo.mp3", am);
            musicPositive_2 = soundWithDuration("sounds/mm1/instrumentos/MicroMundo_Juego4_ResolucionEnsayo1.mp3", am);
            musicPositive_3 = soundWithDuration("sounds/mm1/instrumentos/MicroMundo_Juego4_ResolucionEnsayo2.mp3", am);
            musicTooFew = soundWithDuration("sounds/mm1/instrumentos/MicroMundo_Juego4_MasRunas.mp3", am);
            musicTooMuch = soundWithDuration("sounds/mm1/instrumentos/MicroMundo_Juego4_MenosRunas.mp3", am);
            musicFinal = soundWithDuration("sounds/mm1/instrumentos/MicroMundo_Juego4_ResolucionJuego.mp3", am);
//            positivesMusic.add(musicPositive_1);
//            positivesMusic.add(musicPositive_2);
//            positivesMusic.add(musicPositive_3);
            music = soundWithDuration("sounds/feedback/music.wav", am);
            cluesMusic.add(music);

            // bell game
            bellIntro = soundWithDuration("sounds/mm1/timbre/MicroMundo1_Juego5_Introduccion.mp3", am);
            bellFinal = soundWithDuration("sounds/mm1/timbre/MicroMundo1_Juego5_ResolucionJuego.mp3", am);
            bellTooFew = soundWithDuration("sounds/mm1/timbre/MicroMundo1_Juego5_MasRunas.mp3", am);
            bellTooMuch = soundWithDuration("sounds/mm1/timbre/MicroMundo1_Juego5_MenosRunas.mp3", am);
            bellPositive = soundWithDuration("sounds/mm1/timbre/MicroMundo1_Juego5_ResolucionEnsayo.mp3", am);
//            positivesBell.add(bellPositive);
            bell = soundWithDuration("sounds/feedback/bell.mp3", am);
            bell_2 = soundWithDuration("sounds/feedback/bell_2.mp3", am);
            bell_3 = soundWithDuration("sounds/feedback/bell_3.mp3", am);
            cluesBell.add(bell);
            cluesBell.add(bell_2);
            cluesBell.add(bell_3);

            jugar = soundWithDuration("sounds/opciones/jugar.wav", am);
            denuevo = soundWithDuration("sounds/opciones/denuevo.wav", am);
            ayuda = soundWithDuration("sounds/opciones/ayuda.wav", am);
            salir = soundWithDuration("sounds/opciones/salir.wav", am);
            positivesFeedbacks.add(tada);
            // tada for all!
            positivesOrganicHelp = positivesStep = positivesKnock = positivesIngredients = positivesMixing = positivesMusic = positivesBell = positivesFeedbacks;

            goToThePast = soundWithDuration("sounds/mm1/vueltaAlPasado.mp3", am);
            cameFromPast = soundWithDuration("sounds/feedback/newblock.wav", am);
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