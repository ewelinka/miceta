package miceta.game.core;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortIn;

import miceta.game.core.managers.TangibleBlocksManager;
import miceta.game.core.osc.OSCManager;
import miceta.game.core.receiver.Block;
import miceta.game.core.screens.DirectedGame;
import miceta.game.core.screens.FeedbackScreen;
import miceta.game.core.transitions.ScreenTransition;
import miceta.game.core.transitions.ScreenTransitionFade;
import miceta.game.core.util.AudioManager;
import miceta.game.core.util.GamePreferences;

import java.net.SocketException;
import java.util.Date;
import java.util.HashMap;


public class miCeta extends DirectedGame {
	public static final String TAG = miCeta.class.getName();
	private TangibleBlocksManager blocksManager;
	private String myIp="";
	private OSCPortIn oscPortIn = null;



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
		initReception();
		setScreen(new FeedbackScreen(this),transition);
	}

	@Override
	public void dispose(){
		super.dispose();
		oscPortIn.stopListening();
	}

	public void initReception(){
		System.out.println("init reception");
		try {
			oscPortIn = new OSCPortIn(12345);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		OSCManager manager = new OSCManager(blocksManager);
		oscPortIn.addListener("/cetaController", manager);
		oscPortIn.startListening();
	}
	

	public String getMyIp(){
		return this.myIp;
	}

	public TangibleBlocksManager getBlocksManager(){
		return blocksManager;
	}

}


