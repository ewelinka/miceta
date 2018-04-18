package miceta.game.core.util;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import java.util.ArrayList;

/**
 * Created by ewe on 2/2/18.
 */
public class GameScreen {
    public ScreenName screenName;
    public Sound intro;
    public ArrayList<Sound> positives = new ArrayList<>();
    public Sound tooFew;
    public Sound tooMuch;
    public Sound finalSound;
    public Texture imgBackground;

}
