package miceta.game.java;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import miceta.game.core.miCeta;
import org.opencv.core.Core;

public class miCetaDesktop {
	public static void main (String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.height = 1366; // screen size
		config.width = 768;
		System.out.println(System.getProperty("java.library.path")+"  "+Core.NATIVE_LIBRARY_NAME);
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		new LwjglApplication(new miCeta(), config);


	}
}
