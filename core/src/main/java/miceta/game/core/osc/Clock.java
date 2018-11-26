package miceta.game.core.osc;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import com.badlogic.gdx.Gdx;



// import UDP library


public class Clock {

	
	public static final String TAG = Clock.class.getName();
	 
	//UDP udp;  // define the UDP object

	DatagramSocket serverSocket;
	
	byte[] packetBuff = new byte[48];

	private long currentSeconds;
	private long currentMillis;
	private long _lastUpdate;
	private boolean isSynch = false;


	public  Clock(){
		/*FIXME uncomment this for time server synch
		try{
			serverSocket = new DatagramSocket(8888);
			DatagramSocket clientSocket = new DatagramSocket();
	
			// fill packetBuff with 0's 
			for (int i = 0; i < 48; i += 1) packetBuff[i] = 0; 
	
			// load packet with required info
			packetBuff[0]   = (byte) (227);   // LI, Version, Mode
			packetBuff[1]   = 0;           // Stratum, or type of clock
			packetBuff[2]   = 6;           // Polling Interval
			packetBuff[3]   = (byte) (236);   // Peer Clock Precision
			// 8 bytes of zero for Root Delay & Root Dispersion
			packetBuff[12]  = 49;
			packetBuff[13]  = 78;
			packetBuff[14]  = 49;
			packetBuff[15]  = 52;
			// all NTP fields have been given values, now
			// you can send a packet requesting a timestamp:
	
			String ip   = "time.nist.gov";       // the destination IP 
			//String ip     = "us.pool.ntp.org"; // this IP also works  
			int port      = 123;                 // the destination port 
			Gdx.app.log(TAG,"-------------- before get ip address--------------");
			InetAddress IPAddress = InetAddress.getByName(ip);
			Gdx.app.log(TAG,"-------------- IP = " + IPAddress.getHostAddress());
			
		   DatagramPacket sendPacket = new DatagramPacket(packetBuff, packetBuff.length, IPAddress, port);
		   clientSocket.send(sendPacket);
			Gdx.app.log(TAG,"-------------- Packet SENT  " );

		   
		   Gdx.app.log(TAG,"-------------- before receive" );
		   DatagramPacket receivePacket = null;
		   while(receivePacket==null){
			   receivePacket = receiveFromSocket(clientSocket);
			   Gdx.app.log(TAG,"Trying to synch clock");
			   if(receivePacket==null)
				   clientSocket.send(sendPacket);

		   } 
		   Gdx.app.log(TAG,"-------------- RECEIVED  " );
		   parseData(receivePacket.getData());
		}catch(SocketTimeoutException e){
			
		}catch(IOException e){
	    	Gdx.app.error(TAG,"------------- ERROR SYNCH CLOCK --------------");
		}
	*/
		   
	}
	
	DatagramPacket receiveFromSocket(DatagramSocket socket){
		byte[] receiveData = new byte[1024];
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		try {
			socket.setSoTimeout(5000);
			socket.receive(receivePacket);
		} catch (Exception e) {
			e.printStackTrace();
			receivePacket = null;
	    	Gdx.app.error(TAG,"------------- ERROR RECEIVING DATA!--------------");
		}
		return receivePacket;
	}
/*	void update() 
	{ 
		String ip   = "time.nist.gov";       // the destination IP 
		//String ip     = "us.pool.ntp.org"; // this IP also works  
		int port      = 123;                 // the destination port 
		// send the buffer
		udp.send(packetBuff, ip, port ); 
		background(random(256), random(256), random(256));
	}
*/

	//This automagically gets called when a packet is received
	void parseData( byte[] data )  
	{   
    	Gdx.app.log(TAG,"-------------- CLOCK DATA RECEIVED --------------");

		for (int i = 0; i < 48; i += 1)
		{
			int k = data[i] & 0xFF;
			if (i >= 40 && i < 44) Gdx.app.log(TAG,"data[" + i + "]" + " = " + k);
		}

		long ab = data[40] & 0xFF; // data[40 to 43] hi byte to low byte
		long ac = data[41] & 0xFF; // is seconds since January 1 1900 
		long ad = data[42] & 0xFF; // UNIX time
		long ae = data[43] & 0xFF;

		long af = ((ab << 24)+(ac << 16)+(ad << 8)+(ae));
		af -= 2208988800L; // subtract 2208988800 seconds for 70 years
		long ag = ((af % 86400) / 3600); // extract UTC/GMT hours

		Gdx.app.log(TAG,"Seconds since January 1 1970 = " + af);

		//FIXE a ver si esto esta bien
		currentSeconds = af;

		//print UTC time in 24 hour format
		Gdx.app.log(TAG,"UTC time = " + ag + ":"); // hours

		// In the first 10 minutes of each hour print a leading '0'
		if (((af % 3600) / 60) < 10)  Gdx.app.log(TAG,"0");

		//print the minute (3600 equals secs per hour)
		Gdx.app.log(TAG,String.valueOf((af  % 3600) / 60)+"\n");
		_lastUpdate =System.currentTimeMillis();
		isSynch = true;
	}

	boolean isSynchronized(){
		return isSynch;
	}

	long getTime(){//FIXME esta devolviendo solo los segundos actuales, estilo 2,3,4.... 
		long milliseconds = System.currentTimeMillis();
		long elapsed = milliseconds - _lastUpdate;
		currentMillis  += elapsed;
		_lastUpdate = milliseconds;

		if (currentMillis > 1000)
		{
			currentSeconds  += currentMillis / 1000;
			currentMillis %= 1000;
		}

		return currentSeconds;
	}  


}