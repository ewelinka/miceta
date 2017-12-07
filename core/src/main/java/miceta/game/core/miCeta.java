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
import miceta.game.core.receiver.Block;
import miceta.game.core.screens.DirectedGame;
import miceta.game.core.screens.FeedbackScreen;
import miceta.game.core.screens.TestScreen;
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
	private Object syncObj = new Object();



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

	public void initReception(){
		OSCPortIn oscPortIn = null;
		try {
			oscPortIn = new OSCPortIn(12345);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		oscPortIn.addListener("/wizardOfOz", new OSCListener() {
			@Override
			public void acceptMessage(Date arg0, OSCMessage arg1) {
				Gdx.app.log(TAG,"message received!!!");

				for(int i =0;i< arg1.getArguments().size();i++){
					Gdx.app.log(TAG,"arg("+i+")="+arg1.getArguments().get(i));
				}
				Gdx.app.log(TAG,"----------- end of message ------------");
				synchronized (syncObj) {
					String str1 = (String)arg1.getArguments().get(0);
					if(str1!=null){
						if(str1.equals("register")){
							int action = (Integer)arg1.getArguments().get(1);
							int block_id = (Integer)arg1.getArguments().get(2);
							String actionName;

							switch(action){
								case 0:
									actionName ="stop";
									break;
								case 1:
									actionName ="start";
									//registerBlock(block_id, getBlockValue(block_id));
									break;
								case 2:
									actionName ="inArea";
									//addToCurrentSolution(block_id,getBlockValue(block_id));
									break;
								case 3:
									actionName ="outOfArea";
									//removeFromCurrentSolution(block_id, getBlockValue(block_id));
									break;
								default:
									actionName="xxxx";
									break;
							}

							Gdx.app.log(TAG, "register: " + actionName + " - blockID: " + block_id);

						}else if(str1.equals("event")){
							//event received (touched, joined, released)
							String event_id = (String)arg1.getArguments().get(1);
							int block_id = (Integer)arg1.getArguments().get(2);
							Gdx.app.log(TAG, "event: " + event_id + " - blockID: " + block_id);
							if(event_id.equals("touched")){
								//TODO touched
								//startTouch(block_id);
							}else if(event_id.equals("untouched")){
								//TODO untouched
								//stopTouch(block_id);
							}
						}else if(str1.equals("debug")){
							//event received (touched, joined, released)
							String debug_name = (String)arg1.getArguments().get(1);
							int block_id = (Integer)arg1.getArguments().get(2);
							int debug_value = (Integer)arg1.getArguments().get(3);
							Gdx.app.log(TAG,  "debug: " + debug_name + " = " + debug_value +" - blockID: " + block_id);
						}else{
							//unknown str1 command
							Gdx.app.log(TAG,  "unknown command = " + str1);
						}

					}
				}
			}
		});
		oscPortIn.startListening();
	}



	public String getMyIp(){
		return this.myIp;
	}

}


