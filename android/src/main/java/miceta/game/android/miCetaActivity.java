package miceta.game.android;

import android.graphics.ImageFormat;
import android.text.format.Formatter;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import miceta.game.android.utils.CameraUtils;
import miceta.game.core.miCeta;

import android.net.wifi.WifiManager;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;

public class miCetaActivity extends AndroidApplication implements SurfaceTexture.OnFrameAvailableListener{
	public static final String TAG = miCetaActivity.class.getName();
	private Camera mCamera;
	private int mCameraPreviewThousandFps;
	private int VIDEO_WIDTH,VIDEO_HEIGHT,DESIRED_PREVIEW_FPS;
	private SurfaceTexture mCameraTexture;
	private byte[] buffer;
	private CustomPreviewCallback cameracallback;

	private boolean openCvInit = false;
	private boolean useGrayScaleNativeScanner = false;
	private miCeta cetaGame;

	private String myIP;// = "192.168.1.42";//"12.34.56.78";




	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WifiManager wm = (WifiManager)getSystemService(WIFI_SERVICE);
		myIP = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useImmersiveMode = true;
		cetaGame = new miCeta(myIP);
		//cetaGame.initReception();

		initialize(cetaGame, config);
	}

	static {
		if (!OpenCVLoader.initDebug()) {
			// Handle initialization error
			Log.e(TAG,"Severe error, openCV could not be loaded!");
		}else{

			Log.i(TAG, "OpenCV loaded successfully");
		}
	}


	@Override
	protected void onResume() {
		super.onResume();


		//smarichal: New code for static initialization
		//OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, this, mLoaderCallback);
		System.loadLibrary("ceta-vision-core-library-android");
		initCameraListener();

		// Ideally, the frames from the camera are at the same resolution as the input to
		// the video encoder so we don't have to scale.
		if(this.mCamera==null /*&& openCvInit*/)
			openCamera(VIDEO_WIDTH, VIDEO_HEIGHT, DESIRED_PREVIEW_FPS);

	}

	@Override
	protected void onPause() {
		super.onPause();

		releaseCamera();
	}

	private void initCameraListener(){
		this.VIDEO_WIDTH = 640;
		this.VIDEO_HEIGHT = 480;
		this.DESIRED_PREVIEW_FPS = 10;
		this.buffer = new byte[this.VIDEO_HEIGHT*this.VIDEO_WIDTH];
		this.cameracallback = new CustomPreviewCallback();

		openCamera(VIDEO_WIDTH,VIDEO_HEIGHT,DESIRED_PREVIEW_FPS);
		surfaceCreated(null);
	}



	/**
	 * Stops camera preview, and releases the camera to the system.
	 */
	private void releaseCamera() {
		if (mCamera != null) {
			mCamera.stopPreview();
			mCamera.setPreviewCallback(null);
			mCamera.release();
			mCamera = null;
			Log.d(TAG, "releaseCamera -- done");
		}
	}

	private void openCamera(int desiredWidth, int desiredHeight, int desiredFps) {
		if (mCamera != null) {
			//throw new RuntimeException("camera already initialized");
		}
		else {

			Camera.CameraInfo info = new Camera.CameraInfo();

			// Try to find a front-facing camera (e.g. for videoconferencing).
			int numCameras = Camera.getNumberOfCameras();
			for (int i = 0; i < numCameras; i++) {
				Camera.getCameraInfo(i, info);
				if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
					mCamera = Camera.open(i);
					break;
				}
			}
			if (mCamera == null) {
				Log.d(TAG, "No front-facing camera found; opening default");
				mCamera = Camera.open();    // opens first back-facing camera
			}
			if (mCamera == null) {
				throw new RuntimeException("Unable to open camera");
			}

			Camera.Parameters parms = mCamera.getParameters();

			CameraUtils.choosePreviewSize(parms, desiredWidth, desiredHeight);
			CameraUtils.choosePreviewFormat(parms, ImageFormat.NV21);
			// Try to set the frame rate to a constant value.
			mCameraPreviewThousandFps = CameraUtils.chooseFixedPreviewFps(parms, desiredFps * 1000);

			// Give the camera a hint that we're recording video.  This can have a big
			// impact on frame rate.
//		parms.setRecordingHint(true);

			mCamera.setParameters(parms);

			Camera.Size cameraPreviewSize = parms.getPreviewSize();
			String previewFacts = cameraPreviewSize.width + "x" + cameraPreviewSize.height +
					" @" + (mCameraPreviewThousandFps / 1000.0f) + "fps";
			Log.i(TAG, "Camera config: " + previewFacts);
		}
	}



	public void surfaceCreated(SurfaceHolder holder) {
		Log.d(TAG, "surfaceCreated, holder=" + holder);

		mCameraTexture = new SurfaceTexture(0);
		mCameraTexture.setOnFrameAvailableListener(this);

		Log.d(TAG, "starting camera preview");
		try {
			mCamera.setPreviewTexture(mCameraTexture);

			mCamera.addCallbackBuffer(buffer);
			mCamera.setPreviewCallback(this.cameracallback);
			mCamera.setPreviewDisplay(holder);
		} catch (IOException ioe) {
			throw new RuntimeException(ioe);
		}
		mCamera.startPreview();
	}

	public class CustomPreviewCallback implements Camera.PreviewCallback{

		@Override
		public void onPreviewFrame(byte[] bytes, Camera camera) {
			Camera.Size cameraPreviewSize = camera.getParameters().getPreviewSize();


			Mat mYuv = new Mat( cameraPreviewSize.height + cameraPreviewSize.height/2, cameraPreviewSize.width, CvType.CV_8UC1 );
			mYuv.put( 0, 0, bytes);
			Mat mRgba = new Mat();

			if(useGrayScaleNativeScanner){
				Imgproc.cvtColor( mYuv, mRgba, Imgproc.COLOR_YUV2GRAY_NV21, 1 );
			}else{
				Imgproc.cvtColor( mYuv, mRgba, Imgproc.COLOR_YUV2RGBA_NV21, 4 );
			}
    	/*	Context	context	=	getApplicationContext();
	    	File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
	    	Rect detectionZone = new Rect(160,0,480,480);
	    	Mat subImg = mRgba.submat(detectionZone);
			ceta.game.Utils.saveImage(mRgba, count++, path.toString(), context,3);
			ceta.game.Utils.saveImage(subImg, count++, path.toString(), context,3);*/
			cetaGame.setLastFrame(mRgba);
			mCamera.addCallbackBuffer(buffer);
		}
	}

	//private int count = 0;

	@Override   // SurfaceTexture.OnFrameAvailableListener; runs on arbitrary thread
	public void onFrameAvailable(SurfaceTexture surfaceTexture) {
		//Log.d(TAG, "frame available");
		//mHandler.sendEmptyMessage(MainHandler.MSG_FRAME_AVAILABLE);
	}
}
