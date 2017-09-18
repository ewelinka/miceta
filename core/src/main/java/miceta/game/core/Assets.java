package miceta.game.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Disposable;

/**
 * Created by ewe on 8/10/17.
 */
public class Assets implements Disposable, AssetErrorListener {
    public static final String TAG = Assets.class.getName();
    public static final Assets instance = new Assets();
    private AssetManager assetManager;

    public AssetSounds sounds;
    public AssetMusic music;

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
        assetManager.load("sounds/do.mp3", Sound.class);
        assetManager.load("sounds/re.mp3", Sound.class);
        assetManager.load("sounds/mi.mp3", Sound.class);
        assetManager.load("sounds/puck.mp3", Sound.class);
        assetManager.load("music/song1.mp3", Music.class);
        assetManager.load("sounds/yuju.mp3", Sound.class);

        assetManager.load("sounds/distancia1.wav", Sound.class);
        assetManager.load("sounds/distancia2.wav", Sound.class);
        assetManager.load("sounds/distancia3.wav", Sound.class);
        assetManager.load("sounds/distancia4.wav", Sound.class);
        assetManager.load("sounds/distancia5.wav", Sound.class);
        assetManager.load("sounds/distancia6.wav", Sound.class);
        assetManager.load("sounds/distancia7.wav", Sound.class);
        assetManager.load("sounds/distancia8.wav", Sound.class);
        assetManager.load("sounds/distancia9.wav", Sound.class);
        assetManager.load("sounds/distancia10.wav", Sound.class);


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
        public final Sound one, two, three, four, five;
        public final Sound oneDo, oneRe, oneMi;
        public final Sound puck,yuju;
        public final Sound d1,d2,d3,d4,d5,d6,d7,d8,d9,d10;
        public AssetSounds (AssetManager am) {
            one = am.get("sounds/1.wav", Sound.class);
            two = am.get("sounds/2.wav", Sound.class);
            three = am.get("sounds/3.wav", Sound.class);
            four = am.get("sounds/4.wav", Sound.class);
            five = am.get("sounds/5.wav", Sound.class);
            oneDo = am.get("sounds/do.mp3", Sound.class);
            oneRe = am.get("sounds/re.mp3", Sound.class);
            oneMi = am.get("sounds/mi.mp3", Sound.class);
            puck = am.get("sounds/puck.mp3", Sound.class);
            yuju = am.get("sounds/yuju.mp3", Sound.class);
            d1 = am.get("sounds/distancia1.wav", Sound.class);
            d2 = am.get("sounds/distancia2.wav", Sound.class);
            d3 = am.get("sounds/distancia3.wav", Sound.class);
            d4 = am.get("sounds/distancia4.wav", Sound.class);
            d5 = am.get("sounds/distancia5.wav", Sound.class);
            d6 = am.get("sounds/distancia6.wav", Sound.class);
            d7 = am.get("sounds/distancia7.wav", Sound.class);
            d8 = am.get("sounds/distancia8.wav", Sound.class);
            d9 = am.get("sounds/distancia9.wav", Sound.class);
            d10 = am.get("sounds/distancia10.wav", Sound.class);
        }
    }

    public class AssetMusic {
        public final Music song01;

        public AssetMusic (AssetManager am) {
            song01 = am.get("music/song1.mp3", Music.class);

        }
    }
}
