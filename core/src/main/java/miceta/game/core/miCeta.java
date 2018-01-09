package miceta.game.core;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import edu.ceta.vision.core.topcode.TopCodeDetector;
import miceta.game.core.screens.DirectedGame;
import miceta.game.core.screens.IntroScreen;
import miceta.game.core.transitions.ScreenTransition;
import miceta.game.core.transitions.ScreenTransitionFade;
import miceta.game.core.util.AudioManager;
import miceta.game.core.util.GamePreferences;
import org.opencv.core.Mat;


public class miCeta extends DirectedGame {
	public static final String TAG = miCeta.class.getName();
	private boolean frameBlocked, hasNewFrame;
	private Mat lastFrame;//, previousFrame;
	private Object syncObject = new Object();
	private TopCodeDetector topCodeDetector;

	@Override
	public void create () {

		this.frameBlocked = false;
		Assets.instance.init(new AssetManager());
		AudioManager.instance.play(Assets.instance.music.song01);


		GamePreferences.instance.load();
		ScreenTransition transition = ScreenTransitionFade.init(1);
		topCodeDetector = null;
		//setScreen(new TestScreen(this),transition);
		setScreen(new IntroScreen(this),transition);
		//LevelsManager levelsManager = LevelsManager.getInstance(); // inicializate level manager -- no seria necesario porque es singleton.






	}

	public void setLastFrame(Mat frame){
		//	Gdx.app.log(TAG,"Setting last frame setLastFrame!");
		synchronized (syncObject) {
			if(!this.frameBlocked) {
				//	Gdx.app.log(TAG,"Setting new frame!");
				this.lastFrame = frame.clone();
				this.hasNewFrame = true;
			}else{
				//		Gdx.app.log(TAG,"blocked frame!");
			}
		}
	}

	public Mat getAndBlockLastFrame(){
		synchronized (syncObject) {
			//		Gdx.app.log(TAG,"blocking frame!");
			this.frameBlocked = true;
			this.hasNewFrame = false;
			return this.lastFrame;
		}

	}

	public void releaseFrame(){

		synchronized (syncObject) {
			Gdx.app.log(TAG,"Frame released!");
			if(this.lastFrame!=null){
				//this.lastFrame.release();
				this.frameBlocked = false;
			}
		}
	}

	public boolean hasNewFrame(){
		return hasNewFrame;
	}

	public TopCodeDetector getTopCodeDetector(){ return topCodeDetector;}
	public void setTopCodeDetector(TopCodeDetector topCodeDetector){ this.topCodeDetector = topCodeDetector;}
}


