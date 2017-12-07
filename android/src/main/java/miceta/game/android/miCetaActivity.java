package miceta.game.android;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import miceta.game.core.miCeta;

import android.net.wifi.WifiManager;
import android.os.Bundle;

import android.text.format.Formatter;


public class miCetaActivity extends AndroidApplication {
	public static final String TAG = miCetaActivity.class.getName();

	private miCeta cetaGame;

	private String myIP;// = "192.168.1.42";//"12.34.56.78";




	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		WifiManager wm = (WifiManager)getSystemService(WIFI_SERVICE);
		myIP = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
		//Gdx.app.log(TAG,"---> my ip "+myIP);

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useImmersiveMode = true;
		cetaGame = new miCeta(myIP);

		initialize(cetaGame, config);
	}



}
