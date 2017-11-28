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
        assetManager.load("sounds/re.wav", Sound.class);
        assetManager.load("sounds/mi.wav", Sound.class);
        assetManager.load("sounds/fa.wav", Sound.class);
        assetManager.load("sounds/sol.wav", Sound.class);
        assetManager.load("sounds/puck.mp3", Sound.class);
        assetManager.load("music/song1.mp3", Music.class);
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

        assetManager.load("sounds/masPiezas.wav", Sound.class);
        assetManager.load("sounds/menosPiezas.wav", Sound.class);



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
        public AssetSounds (AssetManager am) {
            f1 = am.get("sounds/1.wav", Sound.class);
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

            oneDo = am.get("sounds/do.wav", Sound.class);
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
            d10 = am.get("sounds/d10.wav", Sound.class);
        }
    }

    public class AssetMusic {
        public final Music song01;

        public AssetMusic (AssetManager am) {
            song01 = am.get("music/song1.mp3", Music.class);

        }
    }
}
