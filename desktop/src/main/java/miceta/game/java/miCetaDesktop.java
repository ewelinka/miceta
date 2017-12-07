package miceta.game.java;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortIn;
import miceta.game.core.controllers.CvWorldController;
import miceta.game.core.miCeta;

import java.net.SocketException;
import java.util.Date;


public class miCetaDesktop {
	private static final String TAG = miCetaDesktop.class.getName();



	public static void main (String[] args) throws SocketException {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1366;
		config.height = 768;

		miCeta cetaGame = new miCeta("");
		cetaGame.initReception();
		new LwjglApplication(cetaGame, config);


	}
}
