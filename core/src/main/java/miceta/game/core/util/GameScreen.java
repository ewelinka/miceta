package miceta.game.core.util;

import com.badlogic.gdx.audio.Sound;
import miceta.game.core.screens.AbstractGameScreen;

import java.util.ArrayList;

/**
 * Created by ewe on 2/2/18.
 */
public class GameScreen {
    public ScreenName screenName;
    public Sound intro;
    public ArrayList<Sound> positives = new ArrayList<Sound>();
    public Sound tooFew;
    public Sound tooMuch;
    public Sound finalSound;
    public FeedbackSoundType feedbackSoundType;

}
