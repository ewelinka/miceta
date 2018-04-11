package miceta.game.core;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import edu.ceta.vision.core.topcode.TopCodeDetector;
import miceta.game.core.managers.LevelsManager;
import miceta.game.core.managers.ResultsManager;
import miceta.game.core.screens.AbstractGameScreen;
import miceta.game.core.screens.DirectedGame;
import miceta.game.core.screens.IntroScreen;
import miceta.game.core.transitions.ScreenTransition;
import miceta.game.core.transitions.ScreenTransitionFade;
import miceta.game.core.util.*;
import org.opencv.core.Mat;


public class miCeta extends DirectedGame {
	private static final String TAG = miCeta.class.getName();
	private boolean frameBlocked, hasNewFrame;
	private Mat lastFrame;//, previousFrame;
	private final Object syncObject = new Object();
	private TopCodeDetector topCodeDetector;
	private RepresentationMapper representationMapper;
	public GameScreen gameScreen;

	@Override
	public void create () {

		this.frameBlocked = false;
		Assets.instance.init(new AssetManager());
		//AudioManager.instance.play(Assets.instance.music.song01);
		GamePreferences.instance.load();
		LevelsManager.instance.init();
		resultsManager = new ResultsManager();
		representationMapper = new RepresentationMapper(this);
		gameScreen = RepresentationMapper.getGameScreenFromScreenName(LevelsManager.instance.getScreenName());
		ScreenTransition transition = ScreenTransitionFade.init(1);
		topCodeDetector = null;

		setScreen(new IntroScreen(this),transition);
		//setScreen(new AutoInitScreen(this));
		//LevelsManager levelsManager = LevelsManager.getInstance(); // inicializate level manager -- no seria necesario porque es singleton.

	}

	public void setLastFrame(Mat frame){
		//	Gdx.app.log(TAG,"Setting last frame setLastFrame!");
		synchronized (syncObject) {
			if(!this.frameBlocked) {
				//	Gdx.app.log(TAG,"Setting new frame!");
				this.lastFrame = frame.clone();
				this.hasNewFrame = true;
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

	public RepresentationMapper getRepresentationMapper(){ return representationMapper;}

	public GameScreen getGameScreen(){
		return gameScreen;
	}

	public GameScreen updateGameScreen(){
		gameScreen = RepresentationMapper.getGameScreenFromScreenName(LevelsManager.instance.getScreenName());
		return gameScreen;

	}

	public void goToNextScreen(){
		LevelsManager.instance.upLevelAndLoadParams();
		gameScreen = RepresentationMapper.getGameScreenFromScreenName(LevelsManager.instance.getScreenName());
		Gdx.app.log(TAG," go to next level nr "+LevelsManager.instance.get_level()+" with screen "+gameScreen.screenName );
		AbstractGameScreen nowScreen = getRepresentationMapper().getScreenFromScreenName(gameScreen.screenName, false);
		setScreen(nowScreen);
	}

	public void goToLastScreen(boolean shouldRepeatTutorial){
		LevelsManager.instance.loadLevelParams();
		gameScreen = RepresentationMapper.getGameScreenFromScreenName(LevelsManager.instance.getScreenName());
		AbstractGameScreen nowScreen = getRepresentationMapper().getScreenFromScreenName(gameScreen.screenName, shouldRepeatTutorial);
		setScreen(nowScreen);
	}
}


