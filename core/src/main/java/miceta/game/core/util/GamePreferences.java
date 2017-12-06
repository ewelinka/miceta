package miceta.game.core.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * Created by ewe on 12/6/17.
 */
public class GamePreferences {
    public static final String TAG = GamePreferences.class.getName();
    public static final GamePreferences instance = new GamePreferences();
    private Preferences prefs;
    private float extraDelayBetweenFeedback;
    private float waitAfterKnock;

    private GamePreferences () {
        prefs = Gdx.app.getPreferences(Constants.PREFERENCES);
    }
    public void load () {
        extraDelayBetweenFeedback = prefs.getFloat("extraDelayBetweenFeedback",0);
        waitAfterKnock = prefs.getFloat("waitAfterKnock",Constants.WAIT_AFTER_KNOCK);
        prefs.flush();
    }

    public void save(){
        prefs.putFloat("extraDelayBetweenFeedback",extraDelayBetweenFeedback);
        prefs.putFloat("waitAfterKnock",waitAfterKnock);
        prefs.flush();
    }

    public void setExtraDelayBetweenFeedback(float newExtraDelay){
        extraDelayBetweenFeedback = newExtraDelay;
        prefs.putFloat("extraDelayBetweenFeedback",extraDelayBetweenFeedback);
        prefs.flush();
    }
    public void setWaitAfterKnock(float newWaitTime){
        waitAfterKnock = newWaitTime;
        prefs.putFloat("waitAfterKnock",waitAfterKnock);
        prefs.flush();
    }

    public float getExtraDelayBetweenFeedback(){
        return extraDelayBetweenFeedback;
    }

    public float getWaitAfterKnock(){
        return waitAfterKnock;
    }

}
