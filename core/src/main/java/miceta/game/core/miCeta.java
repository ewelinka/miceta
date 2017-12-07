package miceta.game.core;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import miceta.game.core.managers.TangibleBlocksManager;
import miceta.game.core.receiver.Block;
import miceta.game.core.screens.DirectedGame;
import miceta.game.core.screens.FeedbackScreen;
import miceta.game.core.screens.TestScreen;
import miceta.game.core.transitions.ScreenTransition;
import miceta.game.core.transitions.ScreenTransitionFade;
import miceta.game.core.util.AudioManager;
import miceta.game.core.util.GamePreferences;

import java.util.HashMap;


public class miCeta extends DirectedGame {
	public static final String TAG = miCeta.class.getName();
	private TangibleBlocksManager blocksManager;
	private String myIp="";


	public miCeta(String ip){
		this.myIp = ip;
	}


	@Override
	public void create () {

		Assets.instance.init(new AssetManager());
		AudioManager.instance.play(Assets.instance.music.song01);
		GamePreferences.instance.load();
		ScreenTransition transition = ScreenTransitionFade.init(1);
		blocksManager = new TangibleBlocksManager(this);
		//setScreen(new TestScreen(this),transition);
		setScreen(new FeedbackScreen(this),transition);

	}


	public String getMyIp(){
		return this.myIp;
	}





}


