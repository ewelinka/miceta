package miceta.game.core.osc;
import java.net.DatagramPacket;



// import UDP library


public class Clock {

	
	 DatagramPacket packet = new DatagramPacket(message, message.length,
	          address, port);
	 
	UDP udp;  // define the UDP object

	byte[] packetBuff = new byte[48];

	private long currentSeconds;
	private long currentMillis;
	private long _lastUpdate;
	private boolean isSynch = false;


	public  Clock(){


		// create a new datagram connection on port 8888
		// and wait for an incoming message
		udp = new UDP( this, 8888 );
		udp.listen( true );

		// fill packetBuff with 0's 
		for (int i = 0; i < 48; i += 1) packetBuff[i] = 0; 

		// load packet with required info
		packetBuff[0]   = byte(227);   // LI, Version, Mode
		packetBuff[1]   = 0;           // Stratum, or type of clock
		packetBuff[2]   = 6;           // Polling Interval
		packetBuff[3]   = byte(236);   // Peer Clock Precision
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
		// send the buffer
		udp.send(packetBuff, ip, port ); 

	}

	void update() 
	{ 
		String ip   = "time.nist.gov";       // the destination IP 
		//String ip     = "us.pool.ntp.org"; // this IP also works  
		int port      = 123;                 // the destination port 
		// send the buffer
		udp.send(packetBuff, ip, port ); 
		background(random(256), random(256), random(256));
	}


	//This automagically gets called when a packet is received
	void receive( byte[] data )  
	{   
		println("-------------- DATA RECEIVED --------------");
		for (int i = 0; i < 48; i += 1)
		{
			int k = data[i] & 0xFF;
			if (i >= 40 && i < 44) println("data[" + i + "]" + " = " + k);
		}

		long ab = data[40] & 0xFF; // data[40 to 43] hi byte to low byte
		long ac = data[41] & 0xFF; // is seconds since January 1 1900 
		long ad = data[42] & 0xFF; // UNIX time
		long ae = data[43] & 0xFF;

		long af = ((ab << 24)+(ac << 16)+(ad << 8)+(ae));
		af -= 2208988800L; // subtract 2208988800 seconds for 70 years
		long ag = ((af % 86400) / 3600); // extract UTC/GMT hours

		println("Seconds since January 1 1970 = " + af);

		//FIXE a ver si esto esta bien
		currentSeconds = af;

		//print UTC time in 24 hour format
		print("UTC time = " + ag + ":"); // hours

		// In the first 10 minutes of each hour print a leading '0'
		if (((af % 3600) / 60) < 10)  print('0');

		//print the minute (3600 equals secs per hour)
		println((af  % 3600) / 60);
		println();
		_lastUpdate =millis();
		isSynch = true;
	}

	boolean isSynchronized(){
		return isSynch;
	}

	long getTime(){//FIXME esta devolviendo solo los segundos actuales, estilo 2,3,4.... 
		long milliseconds = millis();
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