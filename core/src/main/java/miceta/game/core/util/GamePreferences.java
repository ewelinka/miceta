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
    private int last_level;
    private int operation_index;

    private GamePreferences () {
        prefs = Gdx.app.getPreferences(Constants.PREFERENCES);
    }
    public void load () {
        extraDelayBetweenFeedback = prefs.getFloat("extraDelayBetweenFeedback",0);
        waitAfterKnock = prefs.getFloat("waitAfterKnock",Constants.WAIT_AFTER_KNOCK);

        last_level = prefs.getInteger("last_level",0);
        operation_index=0; // we should always start from the beginning!

        prefs.flush();
    }

    public void save(){
        prefs.putFloat("extraDelayBetweenFeedback",extraDelayBetweenFeedback);
        prefs.putFloat("waitAfterKnock",waitAfterKnock);
        prefs.putInteger("last_level", last_level);

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


    public void setLast_level(int new_level){
        last_level = new_level;
        prefs.putInteger("last_level",last_level);
        prefs.flush();
    }
    public void setOperation_index(int new_operation_index){
        operation_index = new_operation_index;
    }


    public float getExtraDelayBetweenFeedback(){
        return extraDelayBetweenFeedback;
    }

    public float getWaitAfterKnock(){
        return waitAfterKnock;
    }


    public int getLast_level(){
        return last_level;
    }

    public int getOperation_index(){
        return operation_index;
    }


}
