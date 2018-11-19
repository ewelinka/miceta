package miceta.game.core.util;

/**
 * Created by ewe on 8/10/17.
 */
public class Constants {
    
    public static final float WAIT_AFTER_KNOCK = 2; // in seconds
    public static final float READ_ONE_UNIT_DURATION = 0.57f;
    public static final float READ_NUMBER_DURATION = 0.7f; //Range: 0.495f - 0.90
    public static final float DELAY_FOR_YUJU =  0.91f; //0.5f;
    public static final float DELAY_FOR_TADA = 2.0f;//0.5f; //2.0 is large
    public static final int BASE = 40;
    public static final int STRIKE = 3;
    public static final int P_STRIKE = 3;
    public static final int INACTIVITY_LIMIT = 30;
    public static final int ERRORS_FOT_HINT = 2;
    public static final int ERRORS_FOR_REPEAT_TUTORIAL = 20;
    public static final int WAIT_AFTER_CORRECT_ANSWER = 1;
    public static final String PREFERENCES = "settings.prefs";
    public static final int DESKTOP_WIDTH = 1366;
    public static final int DESKTOP_HEIGHT = 768;
    public static final int ANDROID_WIDTH = 600;
    public static final int ANDROID_HEIGHT = 1024;
    public static final String  TEXTURE_ATLAS_OBJECTS ="images/pack.atlas";
    
	/* -------- COMMUNICATION Constants and events -------*/

	//Block events
	public final static int TOUCHED = 1;

	public final static int TOUCH_RELEASED = 2;
	public final static int TOUCH_FREE = 3;

	public final static int JOINED =  4;
	public final static int UNJOINED = 5;
	public final static int JOIN_FREE = 6;

	public final static int BLOCK_INIT = 7;
	public final static int BLOCK_SHUTDOWN = 8;

	public final static int COMPOSITION_FEEDBACK = 9; //not used so far

	public final static int REQUEST_REGISTRER = 4;
	
	public final static int START_SILENCE = 1;
	public final static int STOP_SILENCE = 0;
	
	public final static int INFINITE_SILENCE = 0;
	
	public final static boolean CONFIGURE_BLOCK_VALUES = true;
	public final static int getBlockValue(int blockId){//FIXME update the ids using the pjon ids
		switch(blockId){
			case 11757251%255:
				return 2;
			case 11755994:
				return 2;
			case 11756426%255:
				return 1;
			case 11757275%255:
				return 3;
			case 11755520%255:
				return 2;
			default:
				return 1;
		}
	}
	
//	   2
//	11755994   2
//	
//	   3
//	
//	11755520   1
	/*----------------------------------------*/
    
}
