package miceta.game.java;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import miceta.game.core.miCeta;


public class miCetaDesktop {
	public static void main (String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();


		config.width = 1366;
		config.height = 768;

		new LwjglApplication(new miCeta(), config);


	}
}
