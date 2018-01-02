package miceta.game.core;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import miceta.game.core.managers.LevelsManager;
import miceta.game.core.screens.DirectedGame;
import miceta.game.core.screens.FeedbackScreen;
import miceta.game.core.screens.TestScreen;
import miceta.game.core.transitions.ScreenTransition;
import miceta.game.core.transitions.ScreenTransitionFade;
import miceta.game.core.util.AudioManager;
import miceta.game.core.util.GamePreferences;
import org.opencv.core.Mat;

import java.util.logging.Level;


public class miCeta extends DirectedGame {
	public static final String TAG = miCeta.class.getName();
	private boolean frameBlocked, hasNewFrame;
	private Mat lastFrame;//, previousFrame;
	private Object syncObject = new Object();

	@Override
	public void create () {

		this.frameBlocked = false;
		Assets.instance.init(new AssetManager());
		AudioManager.instance.play(Assets.instance.music.song01);
		GamePreferences.instance.load();
		ScreenTransition transition = ScreenTransitionFade.init(1);
		//setScreen(new TestScreen(this),transition);
		setScreen(new FeedbackScreen(this),transition);
		LevelsManager levelsManager = LevelsManager.getInstance(); // inicializate level manager -- no seria necesario porque es singleton.

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
}


