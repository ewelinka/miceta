package miceta.game.core.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import static java.util.UUID.randomUUID;

/**
 * Created by ewe on 12/6/17.
 */
public class GamePreferences {
    public static final String TAG = GamePreferences.class.getName();
    public static final GamePreferences instance = new GamePreferences();
    private final Preferences prefs;
    private float extraDelayBetweenFeedback;
    private float waitAfterKnock;
    private int last_level;
    private int operation_index;
    private int world;
    private int silentFeedbackMode, mixedFeedbackMode, errorOrSuccessBlocksFeedbackMode;
    public String randomId;
    


    private GamePreferences () {
        prefs = Gdx.app.getPreferences(Constants.PREFERENCES);
    }
    public void load () {
        extraDelayBetweenFeedback = prefs.getFloat("extraDelayBetweenFeedback",0);
        waitAfterKnock = prefs.getFloat("waitAfterKnock",Constants.WAIT_AFTER_KNOCK);
        last_level = prefs.getInteger("last_level",1);
        //last_level = 7;
        world = prefs.getInteger("world",0);
        randomId = prefs.getString("randomId", randomUUID ().toString());
        operation_index=0; // we should always start from the beginning!
        prefs.putString("randomId", randomId);
        silentFeedbackMode = prefs.getInteger("silentFeedbackMode",0);
        mixedFeedbackMode = prefs.getInteger("mixedFeedbackMode",0);
        errorOrSuccessBlocksFeedbackMode = prefs.getInteger("errorOrSuccessBlocksFeedbackMode",0);
        prefs.flush();
    }

    public void save(){
        prefs.putFloat("extraDelayBetweenFeedback",extraDelayBetweenFeedback);
        prefs.putFloat("waitAfterKnock",waitAfterKnock);
        prefs.putInteger("last_level", last_level);
        prefs.putInteger("world", world);
        prefs.putString("randomId", randomId);
        prefs.putInteger("silentFeedbackMode",silentFeedbackMode);
        prefs.putInteger("mixedFeedbackMode",mixedFeedbackMode);
        prefs.putInteger("errorOrSuccessBlocksFeedbackMode", errorOrSuccessBlocksFeedbackMode);
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


    public void setLast_world(int new_world){
        world = new_world;
        prefs.putInteger("world",world);
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

    public int getLast_world(){
        return world;
    }


    public int getOperation_index(){
        return operation_index;
    }
    
	public int getSilentFeedbackMode() {
		return silentFeedbackMode;
	}
	
	public int getMixedFeedbackMode(){
		return mixedFeedbackMode;
	}
	
	public void setSilentFeedbackMode(int silentFeedbackMode) {
		this.silentFeedbackMode = silentFeedbackMode;
	}

	public void setMixedFeedbackMode(int mixedFeedbackMode ) {
		this.mixedFeedbackMode = mixedFeedbackMode ;
	}
	
	public int getErrorOrSuccessBlocksFeedbackMode() {
		return errorOrSuccessBlocksFeedbackMode;
	}
	
	public void setErrorOrSuccessBlocksFeedbackMode(
			int errorOrSuccessBlocksFeedbackMode) {
		this.errorOrSuccessBlocksFeedbackMode = errorOrSuccessBlocksFeedbackMode;
	}

	

}
