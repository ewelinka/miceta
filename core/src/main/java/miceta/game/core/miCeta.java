package miceta.game.core;


import java.net.SocketException;

import miceta.game.core.managers.LevelsManager;
import miceta.game.core.managers.ResultsManager;
import miceta.game.core.managers.TangibleBlocksManager;
import miceta.game.core.osc.OSCManager;
import miceta.game.core.screens.AbstractGameScreen;
import miceta.game.core.screens.DirectedGame;
import miceta.game.core.screens.IntroScreen;
import miceta.game.core.transitions.ScreenTransition;
import miceta.game.core.transitions.ScreenTransitionFade;
import miceta.game.core.util.GamePreferences;
import miceta.game.core.util.GameScreen;
import miceta.game.core.util.RepresentationMapper;

import org.opencv.core.Mat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.illposed.osc.OSCPortIn;

import edu.ceta.vision.core.topcode.TopCodeDetector;

public class miCeta extends DirectedGame {
	private static final String TAG = miCeta.class.getName();
	private boolean frameBlocked, hasNewFrame;
	private Mat lastFrame;//, previousFrame;
	private final Object syncObject = new Object();
	private TopCodeDetector topCodeDetector;
	private RepresentationMapper representationMapper;
	public GameScreen gameScreen;
	private boolean silentFeedbackMode; //for tangible blocks only


private TangibleBlocksManager blocksManager;
	private String myIp="";
	private OSCPortIn oscPortIn = null;
	private OSCManager manager;


	public miCeta(String ip){
		this.myIp = ip;
	}


	@Override
	public void create () {

		this.frameBlocked = false;
		Assets.instance.init(new AssetManager());
		//AudioManager.instance.play(Assets.instance.music.song01);
		GamePreferences.instance.load();
		silentFeedbackMode = GamePreferences.instance.getSilentFeedbackMode()==1;
		LevelsManager.instance.init();
		resultsManager = new ResultsManager();
		representationMapper = new RepresentationMapper(this);
		gameScreen = RepresentationMapper.getGameScreenFromScreenName(LevelsManager.instance.getScreenName());
		ScreenTransition transition = ScreenTransitionFade.init(1);
		blocksManager = new TangibleBlocksManager(this);
		initReception();

		topCodeDetector = null;

		//setScreen(new MenuScreen(this),transition);
		setScreen(new IntroScreen(this),transition);
		//setScreen(new AutoInitScreen(this));
		//LevelsManager levelsManager = LevelsManager.getInstance(); // inicializate level manager -- no seria necesario porque es singleton.

	}

	@Override
	public void dispose(){
		super.dispose();
		oscPortIn.stopListening();
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


	public void initReception(){
		System.out.println("init reception");
		try {
			oscPortIn = new OSCPortIn(12345);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		manager = new OSCManager(blocksManager, silentFeedbackMode);
		oscPortIn.addListener("/cetaController", manager);
		oscPortIn.startListening();
	}

	public String getMyIp(){
		return this.myIp;
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

public TangibleBlocksManager getBlocksManager(){
		return blocksManager;
	}
	
	public OSCManager getOscManager(){
		return manager;
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


