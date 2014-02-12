package com.bakata.plugins;

import java.io.IOException;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnErrorListener;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class HLSPlayerService extends Service {
	
	private static MediaPlayer mMediaPlayer;

	private final static String TAG = HLSPlayerService.class.getSimpleName();

	// This is the object that receives interactions from clients. See
	// RemoteService for a more complete example.
	private final IBinder mBinder = new LocalBinder();
	
	/**
	 * TODO: Set the path variable to a local audio file path.
	 */
	private static final String PATH = "http://cdns840stu0010.multistream.net/rtvcRadionicalive/smil:rtvcRadionica.smil/playlist.m3u8";
	
	
	@Override
	public void onCreate() {
		
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(TAG, "Received start id " + startId + ": " + intent);


		stopMediaPlayer();

		if (isNetworkOnline()) {
			Log.e(TAG, "CONECTADO");
			try {
				mMediaPlayer = new MediaPlayer(this);
				mMediaPlayer.setDataSource(PATH);
				mMediaPlayer.setOnErrorListener(onErrorListener);
				mMediaPlayer.prepare();
				mMediaPlayer.start();

			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			Log.e(TAG, "We are out of connection... playin local audio");
		}

		// We want this service to continue running until it is explicitly
		// stopped, so return sticky.
		return START_STICKY;
	}

	private OnErrorListener onErrorListener = new OnErrorListener() {
		@Override
		public boolean onError(MediaPlayer mp, int what, int extra) {
			return true;
		}
	};

	private boolean isNetworkOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	private void stopMediaPlayer(){
		if (mMediaPlayer != null) {
			if (mMediaPlayer.isPlaying()) {
				mMediaPlayer.stop();
				mMediaPlayer.release();
				mMediaPlayer = null;
			}
		}
	}

	public void stopStreaming() {
		stopMediaPlayer();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		stopMediaPlayer();
	}
	
	public boolean isPlaying(){
		if(mMediaPlayer != null && mMediaPlayer.isPlaying()){
			return true;
		} else {
			return false;
		}
	}
	

	@Override
	public IBinder onBind(Intent arg0) {
		return mBinder;
	}

	/**
	 * Class for clients to access. Because we know this service always runs in
	 * the same process as its clients, we don't need to deal with IPC.
	 */
	public class LocalBinder extends Binder {
		public HLSPlayerService getService() {
			return HLSPlayerService.this;
		}
	}

}
