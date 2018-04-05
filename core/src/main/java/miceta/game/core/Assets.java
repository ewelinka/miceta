package miceta.game.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.backends.lwjgl.audio.OpenALSound;
import com.badlogic.gdx.graphics.Texture;
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
    public AssetBackground backgrounds;


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
        assetManager.load("sounds/feedback/magic/fuerte.mp3", Sound.class);
        assetManager.load("sounds/feedback/magic/medio.mp3", Sound.class);
        assetManager.load("sounds/feedback/magic/suave.mp3", Sound.class);

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
        assetManager.load("sounds/organic/intro1.mp3", Sound.class);
        assetManager.load("sounds/organic/intro2.mp3", Sound.class);
        assetManager.load("sounds/organic/final1.mp3", Sound.class);
        assetManager.load("sounds/organic/final2.mp3", Sound.class);
        assetManager.load("sounds/organic/masMenos1.mp3", Sound.class);
        assetManager.load("sounds/organic/mas2.mp3", Sound.class);
        assetManager.load("sounds/organic/menos2.mp3", Sound.class);
        assetManager.load("sounds/organic/aplauso.mp3", Sound.class);

        //steps
        assetManager.load("sounds/pasos/intro1.mp3", Sound.class);
        assetManager.load("sounds/pasos/intro2.mp3", Sound.class);
        assetManager.load("sounds/pasos/menos2.mp3", Sound.class);
        assetManager.load("sounds/pasos/final1.mp3", Sound.class);
        assetManager.load("sounds/pasos/final2.mp3", Sound.class);
        assetManager.load("sounds/pasos/paso1.mp3", Sound.class);
        assetManager.load("sounds/pasos/paso2.mp3", Sound.class);

        // ingredients game
        assetManager.load("sounds/ingredientes/intro.mp3", Sound.class);
        assetManager.load("sounds/ingredientes/final.mp3", Sound.class);
        assetManager.load("sounds/ingredientes/cae.mp3", Sound.class);
        assetManager.load("sounds/ingredientes/cae2.mp3", Sound.class);
        assetManager.load("sounds/ingredientes/mas.mp3", Sound.class);
        assetManager.load("sounds/ingredientes/menos.mp3", Sound.class);
        assetManager.load("sounds/ingredientes/mm1_juego2_ingrediente1.mp3", Sound.class);
        assetManager.load("sounds/ingredientes/mm1_juego2_ingrediente2.mp3", Sound.class);
        assetManager.load("sounds/ingredientes/mm1_juego2_ingrediente3.mp3", Sound.class);
        assetManager.load("sounds/ingredientes/mm1_juego2_ingrediente4.mp3", Sound.class);
        assetManager.load("sounds/ingredientes/mm1_juego2_ingrediente5.mp3", Sound.class);
        assetManager.load("sounds/ingredientes/mm1_juego2_ingrediente6.mp3", Sound.class);
        assetManager.load("sounds/ingredientes/mm1_juego2_ingrediente7.mp3", Sound.class);
        assetManager.load("sounds/ingredientes/mm1_juego2_ingrediente8.mp3", Sound.class);
        assetManager.load("sounds/ingredientes/mm1_juego2_ingrediente9.mp3", Sound.class);
        assetManager.load("sounds/ingredientes/mm1_juego2_ingrediente10.mp3", Sound.class);
        assetManager.load("sounds/ingredientes/mm1_juego2_ingrediente11.mp3", Sound.class);
        assetManager.load("sounds/ingredientes/mm1_juego2_ingrediente12.mp3", Sound.class);
        assetManager.load("sounds/ingredientes/mm1_juego2_ingrediente13.mp3", Sound.class);
        assetManager.load("sounds/ingredientes/mm1_juego2_ingrediente14.mp3", Sound.class);
        assetManager.load("sounds/ingredientes/mm1_juego2_ingrediente15.mp3", Sound.class);
        assetManager.load("sounds/ingredientes/mm1_juego2_ingrediente16.mp3", Sound.class);
        assetManager.load("sounds/ingredientes/mm1_juego2_ingrediente17.mp3", Sound.class);
        assetManager.load("sounds/ingredientes/positivo1.mp3", Sound.class);
        assetManager.load("sounds/ingredientes/positivo2.mp3", Sound.class);
        assetManager.load("sounds/ingredientes/positivo3.mp3", Sound.class);
        assetManager.load("sounds/ingredientes/positivo4.mp3", Sound.class);

        // Game 1
        // knock at the door
        assetManager.load("sounds/golpes/intro.mp3", Sound.class);
        assetManager.load("sounds/golpes/final.mp3", Sound.class);
        assetManager.load("sounds/golpes/knock1.mp3", Sound.class);
        assetManager.load("sounds/golpes/knock2.mp3", Sound.class);
        assetManager.load("sounds/golpes/mas.mp3", Sound.class);
        assetManager.load("sounds/golpes/menos.mp3", Sound.class);
        assetManager.load("sounds/golpes/positive1.mp3", Sound.class);
        assetManager.load("sounds/golpes/positive2.mp3", Sound.class);
        // door bell
        assetManager.load("sounds/saludos/intro.mp3", Sound.class);
        assetManager.load("sounds/saludos/final.mp3", Sound.class);
        assetManager.load("sounds/saludos/hola1.mp3", Sound.class);
        assetManager.load("sounds/saludos/hola2.mp3", Sound.class);
        assetManager.load("sounds/saludos/hola3.mp3", Sound.class);
        assetManager.load("sounds/saludos/hola4.mp3", Sound.class);
        assetManager.load("sounds/saludos/mas.mp3", Sound.class);
        assetManager.load("sounds/saludos/menos.mp3", Sound.class);

        // mixing
        assetManager.load("sounds/revolver/intro.mp3", Sound.class);
        assetManager.load("sounds/revolver/final.mp3", Sound.class);
        assetManager.load("sounds/revolver/mas.mp3", Sound.class);
        assetManager.load("sounds/revolver/menos.mp3", Sound.class);
        assetManager.load("sounds/revolver/revuelve1.mp3", Sound.class);
        assetManager.load("sounds/revolver/revuelve2.mp3", Sound.class);
        // music
        assetManager.load("sounds/instrumentos/intro.mp3", Sound.class);
        assetManager.load("sounds/instrumentos/bajo.mp3", Sound.class);
        assetManager.load("sounds/instrumentos/bell.mp3", Sound.class);
        assetManager.load("sounds/instrumentos/final.mp3", Sound.class);
        assetManager.load("sounds/instrumentos/guit.mp3", Sound.class);
        assetManager.load("sounds/instrumentos/mas.mp3", Sound.class);
        assetManager.load("sounds/instrumentos/menos.mp3", Sound.class);

        assetManager.load("sounds/opciones/jugar.wav", Sound.class);
        assetManager.load("sounds/opciones/denuevo.wav", Sound.class);
        assetManager.load("sounds/opciones/ayuda.wav", Sound.class);
        assetManager.load("sounds/opciones/salir.wav", Sound.class);

        assetManager.load("music/song1.mp3", Music.class);
        assetManager.load("music/song2.mp3", Music.class);

        assetManager.load("images/backGeneric.png", Texture.class);
        assetManager.load("images/backDoor.png", Texture.class);
        assetManager.load("images/backIngredients.png", Texture.class);
        // start loading assets and wait until finished
        assetManager.finishLoading();

        Gdx.app.debug(TAG, "# of assets loaded: " + assetManager.getAssetNames().size);
        for (String a : assetManager.getAssetNames())
            Gdx.app.debug(TAG, "asset: " + a);
        TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS_OBJECTS);
        buttons = new AssetButtons(atlas);
        sounds = new AssetSounds(assetManager);
        music = new AssetMusic(assetManager);
        backgrounds = new AssetBackground(assetManager);

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
        public final Sound organicHelpIntro,organicHelpTooMuch_1,organicHelpTooFew_1, organicHelpTooMuch_2,organicHelpTooFew_2, organicHelpPositive_1,organicHelpPositive_2, organicHelpFinal, organicHelpIntro2, organicHelpFinal2, organicClap;
        public final Sound stepIntro,stepTooMuch_1,stepTooFew_1, stepTooMuch_2,stepTooFew_2, stepPositive_1,stepPositive_2, stepFinal, stepIntro2, stepFinal2;
        public final Sound jugar, denuevo, ayuda, salir;
        public final Sound ingredientsIntro,i1, i2, i3,i4,i5,i6,i7,i8,i9,i10,i11,i12,i13,i14,i15,i16,i17,ingredientsTooFew,ingredientsTooMuch, ingredientsPositive_1,ingredientsPositive_2, ingredientsPositive_3, ingredientsPositive_4,ingredientsFinal;
        public final Sound knockIntro, knockTooFew, knockTooMuch, knockPositive_1, knockPositive_2, knockFinal;
        public final Sound greetingIntro, greetingFinal, greetingTooFew, greetingTooMuch, greetingPositive;
        public final Sound mixingIntro, mixingFinal, mixingTooFew, mixingTooMuch;
        public final Sound musicIntro, musicFinal, musicTooFew, musicTooMuch;
        public final Sound magicStrong, magicMiddle, magicNomal;


        public  ArrayList<Sound> positivesOrganicHelp = new ArrayList<>();
        public  ArrayList<Sound> positivesStep= new ArrayList<>();
        public  ArrayList<Sound> positivesKnock = new ArrayList<>();
        public  ArrayList<Sound> positivesMixing = new ArrayList<>();
        public  ArrayList<Sound> positivesIngredients = new ArrayList<>();
        public  ArrayList<Sound> positivesMusic = new ArrayList<>();
        public  ArrayList<Sound> positivesGreeting = new ArrayList<>();
        public  ArrayList<Sound> positivesFeedbacks = new ArrayList<>();

        public final ArrayList<Sound> cluesOrganicHelp = new ArrayList<>();
        public final ArrayList<Sound> cluesSteps = new ArrayList<>();
        public final ArrayList<Sound> cluesKnock = new ArrayList<>();
        public final ArrayList<Sound> cluesIngredients = new ArrayList<>();
        public final ArrayList<Sound> cluesMixing = new ArrayList<>();
        public final ArrayList<Sound> cluesMusic = new ArrayList<>();
        public final ArrayList<Sound> cluesGreeting = new ArrayList<>();



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
            magicStrong = soundWithDuration("sounds/feedback/magic/fuerte.mp3", am);
            magicMiddle = soundWithDuration("sounds/feedback/magic/medio.mp3", am);
            magicNomal = soundWithDuration("sounds/feedback/magic/suave.mp3", am);

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
            organicHelpIntro = soundWithDuration("sounds/organic/intro1.mp3", am);
            organicHelpTooMuch_1 = soundWithDuration("sounds/organic/masMenos1.mp3", am);
            organicHelpTooFew_1 = organicHelpTooMuch_1;
            organicHelpPositive_1 = tada;
            organicHelpFinal = soundWithDuration("sounds/organic/final1.mp3", am);
            // second part
            organicHelpIntro2 = soundWithDuration("sounds/organic/intro2.mp3", am);
            organicHelpTooMuch_2 = soundWithDuration("sounds/organic/menos2.mp3", am);
            organicHelpTooFew_2 = soundWithDuration("sounds/organic/mas2.mp3", am);
            organicHelpPositive_2 =  tada;
            organicHelpFinal2 = soundWithDuration("sounds/organic/final2.mp3", am);
            //positivesOrganicHelp.add(organicHelpPositive_2);
            organicClap = soundWithDuration("sounds/organic/aplauso.mp3", am);
            cluesOrganicHelp.add(organicClap); //just one clue

            //steps
            stepIntro = soundWithDuration("sounds/pasos/intro1.mp3", am);
            stepTooMuch_1 = organicHelpTooMuch_1; // pone la pieza mas pequena
            stepTooFew_1 = stepTooMuch_1;
            stepPositive_1 = tada;
            stepFinal= soundWithDuration("sounds/pasos/final1.mp3", am);

            stepIntro2 = soundWithDuration("sounds/pasos/intro2.mp3", am);
            stepTooMuch_2 = soundWithDuration("sounds/pasos/menos2.mp3", am);
            stepTooFew_2 = organicHelpTooFew_2 ;
            stepPositive_2 = tada;
            stepFinal2 = soundWithDuration("sounds/pasos/final2.mp3", am);
//            positivesStep.add(tmm1_positive_1);
//            positivesStep.add(tmm1_positive_2);
            cluesSteps.add(soundWithDuration("sounds/pasos/paso1.mp3", am));
            cluesSteps.add(soundWithDuration("sounds/pasos/paso2.mp3", am));

            // knock game audios
            knockIntro = soundWithDuration("sounds/golpes/intro.mp3", am);
            knockTooFew = soundWithDuration("sounds/golpes/mas.mp3", am);
            knockTooMuch = soundWithDuration("sounds/golpes/menos.mp3", am);
            knockPositive_1 = soundWithDuration("sounds/golpes/positive1.mp3", am);
            knockPositive_2 = soundWithDuration("sounds/golpes/positive2.mp3", am);
            //knockPositive_3 = tada;
            knockFinal = soundWithDuration("sounds/golpes/final.mp3", am);
            positivesKnock.add(knockPositive_1);
            positivesKnock.add(knockPositive_2);
            //positivesKnock.add(knockPositive_3);
            cluesKnock.add(soundWithDuration("sounds/golpes/knock1.mp3", am));
            cluesKnock.add(soundWithDuration("sounds/golpes/knock2.mp3", am));
            //cluesKnock.add(knock_3);

            ingredientsIntro = soundWithDuration("sounds/ingredientes/intro.mp3", am);
            i1= soundWithDuration("sounds/ingredientes/mm1_juego2_ingrediente1.mp3", am);
            i2= soundWithDuration("sounds/ingredientes/mm1_juego2_ingrediente2.mp3", am);
            i3= soundWithDuration("sounds/ingredientes/mm1_juego2_ingrediente3.mp3", am);
            i4= soundWithDuration("sounds/ingredientes/mm1_juego2_ingrediente4.mp3", am);
            i5= soundWithDuration("sounds/ingredientes/mm1_juego2_ingrediente5.mp3", am);
            i6= soundWithDuration("sounds/ingredientes/mm1_juego2_ingrediente6.mp3", am);
            i7= soundWithDuration("sounds/ingredientes/mm1_juego2_ingrediente7.mp3", am);
            i8= soundWithDuration("sounds/ingredientes/mm1_juego2_ingrediente8.mp3", am);
            i9= soundWithDuration("sounds/ingredientes/mm1_juego2_ingrediente9.mp3", am);
            i10= soundWithDuration("sounds/ingredientes/mm1_juego2_ingrediente10.mp3", am);
            i11= soundWithDuration("sounds/ingredientes/mm1_juego2_ingrediente11.mp3", am);
            i12= soundWithDuration("sounds/ingredientes/mm1_juego2_ingrediente12.mp3", am);
            i13= soundWithDuration("sounds/ingredientes/mm1_juego2_ingrediente13.mp3", am);
            i14= soundWithDuration("sounds/ingredientes/mm1_juego2_ingrediente14.mp3", am);
            i15= soundWithDuration("sounds/ingredientes/mm1_juego2_ingrediente15.mp3", am);
            i16= soundWithDuration("sounds/ingredientes/mm1_juego2_ingrediente16.mp3", am);
            i17= soundWithDuration("sounds/ingredientes/mm1_juego2_ingrediente16.mp3", am);
            ingredientsTooFew = soundWithDuration("sounds/ingredientes/mas.mp3", am);
            ingredientsTooMuch = soundWithDuration("sounds/ingredientes/menos.mp3", am);
            ingredientsPositive_1 = soundWithDuration("sounds/ingredientes/positivo1.mp3", am);
            ingredientsPositive_2 = soundWithDuration("sounds/ingredientes/positivo2.mp3", am);
            ingredientsPositive_3 = soundWithDuration("sounds/ingredientes/positivo3.mp3", am);
            ingredientsPositive_4 = soundWithDuration("sounds/ingredientes/positivo4.mp3", am);
            ingredientsFinal = soundWithDuration("sounds/ingredientes/final.mp3", am);
            positivesIngredients.add(ingredientsPositive_1);
            positivesIngredients.add(ingredientsPositive_2);
            positivesIngredients.add(ingredientsPositive_3);
            positivesIngredients.add(ingredientsPositive_4);
            cluesIngredients.add(soundWithDuration("sounds/ingredientes/cae.mp3", am));
            cluesIngredients.add(soundWithDuration("sounds/ingredientes/cae2.mp3", am));

            mixingIntro = soundWithDuration("sounds/revolver/intro.mp3", am);
//            mixingPositive_1 = soundWithDuration("sounds/revolver/MicroMundo_Juego3_ResolucionEnsayo.mp3", am);
//            mixingPositive_2 = soundWithDuration("sounds/revolver/MicroMundo_Juego3_ResolucionEnsayo1.mp3", am);
//            mixingPositive_3 = soundWithDuration("sounds/revolver/MicroMundo_Juego3_ResolucionEnsayo2.mp3", am);
//            mixingPositive_4 = soundWithDuration("sounds/revolver/MicroMundo_Juego3_ResolucionEnsayo3.mp3", am);
            mixingTooFew = soundWithDuration("sounds/revolver/mas.mp3", am);
            mixingTooMuch = soundWithDuration("sounds/revolver/menos.mp3", am);
            mixingFinal = soundWithDuration("sounds/revolver/final.mp3", am);
//            positivesMixing.add(mixingPositive_1);
//            positivesMixing.add(mixingPositive_2);
//            positivesMixing.add(mixingPositive_3);
//            positivesMixing.add(mixingPositive_4);
            //mixing = soundWithDuration("sounds/feedback/revuelve1.mp3", am);
            cluesMixing.add(soundWithDuration("sounds/revolver/revuelve1.mp3", am));
            cluesMixing.add(soundWithDuration("sounds/revolver/revuelve2.mp3", am));

            musicIntro = soundWithDuration("sounds/instrumentos/intro.mp3", am);
            musicTooFew = soundWithDuration("sounds/instrumentos/mas.mp3", am);
            musicTooMuch = soundWithDuration("sounds/instrumentos/menos.mp3", am);
            musicFinal = soundWithDuration("sounds/instrumentos/final.mp3", am);
//            positivesMusic.add(musicPositive_1);
//            positivesMusic.add(musicPositive_2);
//            positivesMusic.add(musicPositive_3);
            cluesMusic.add(soundWithDuration("sounds/instrumentos/bajo.mp3", am));
            cluesMusic.add(soundWithDuration("sounds/instrumentos/bell.mp3", am));
            cluesMusic.add(soundWithDuration("sounds/instrumentos/guit.mp3", am));

            // bell game
            greetingIntro = soundWithDuration("sounds/saludos/intro.mp3", am);
            greetingFinal = soundWithDuration("sounds/saludos/final.mp3", am);
            greetingTooFew = soundWithDuration("sounds/saludos/mas.mp3", am);
            greetingTooMuch = soundWithDuration("sounds/saludos/menos.mp3", am);
            greetingPositive = tada;
//            positivesBell.add(greetingPositive);
            cluesGreeting.add(soundWithDuration("sounds/saludos/hola1.mp3", am));
            cluesGreeting.add(soundWithDuration("sounds/saludos/hola2.mp3", am));
            cluesGreeting.add(soundWithDuration("sounds/saludos/hola3.mp3", am));
            cluesGreeting.add(soundWithDuration("sounds/saludos/hola4.mp3", am));

            jugar = soundWithDuration("sounds/opciones/jugar.wav", am);
            denuevo = soundWithDuration("sounds/opciones/denuevo.wav", am);
            ayuda = soundWithDuration("sounds/opciones/ayuda.wav", am);
            salir = soundWithDuration("sounds/opciones/salir.wav", am);
            positivesFeedbacks.add(tada);
            // tada for all!
            //positivesKnock
            //positivesIngredients
            positivesOrganicHelp = positivesStep   = positivesMixing = positivesMusic = positivesGreeting = positivesFeedbacks;



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
    public class AssetBackground {
        public final Texture generic, ingredients, door;
        public AssetBackground (AssetManager am){
            generic = am.get("images/backGeneric.png",Texture.class);
            ingredients = am.get("images/backIngredients.png",Texture.class);
            door = am.get("images/backDoor.png",Texture.class);
        }

    }

}