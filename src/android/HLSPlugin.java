package com.bakata.plugins;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

public class HLSPlugin extends CordovaPlugin {
	
	private static final String TAG = HLSPlugin.class.getSimpleName();
	
	private HLSPlayerService service;
	
	private String resourceURL;
	private String id;
	
	@Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		
		Log.d(TAG, "Ha llegado el mensaje de invocacion, action " + action + " args "+ args.toString());

		PluginResult.Status status = PluginResult.Status.OK;
		
		if("create".equalsIgnoreCase(action)){
			
			id = args.getString(0);
            resourceURL = args.getString(1);
            
            Intent intent= new Intent(this.cordova.getActivity(), HLSPlayerService.class);
            this.cordova.getActivity().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    		
			
		} else if ("startPlayingAudio".equalsIgnoreCase(action)){
			
			this.cordova.getActivity().startService(new Intent(this.cordova.getActivity(), HLSPlayerService.class));
		
		} else if ("stopPlayingAudio".equalsIgnoreCase(action)){
			
			stopService();
			
		} else if ("seekToAudio".equalsIgnoreCase(action)){
			
		} else if ("pausePlayingAudio".equalsIgnoreCase(action)){
			
		} else if ("getCurrentPositionAudio".equalsIgnoreCase(action)){
			float f = 0;
            callbackContext.sendPluginResult(new PluginResult(status, f));
            return true;
		} else if ("setVolume".equalsIgnoreCase(action)){
			try{
				service.setVolume(100 * Float.parseFloat(args.getString(1)));
				Log.i(TAG, "setVolume "+ Float.parseFloat(args.getString(1)));
			} catch (Exception ex){
				
			}
			return true;
		} else {
			return true;
		}
		
		callbackContext.sendPluginResult(new PluginResult(status, ""));

        return true;
	}
	
	
	private void stopService(){
		if(service != null){
			service.stopStreaming();
			service.stopService(new Intent(this.cordova.getActivity(), HLSPlayerService.class));
		} else {
			this.cordova.getActivity().stopService(new Intent(this.cordova.getActivity(), HLSPlayerService.class));
		}
		Log.i(TAG, "Detener");
	}
	
	
	@Override
	public void onResume(boolean multitasking) {
		// TODO Auto-generated method stub
		super.onResume(multitasking);
				
	}
	
	@Override
	public void onPause(boolean multitasking) {
		// TODO Auto-generated method stub
		super.onPause(multitasking);
		//this.cordova.getActivity().unbindService(mConnection);
		//Log.i(TAG, "onPause - Desconectarse desde el Plugin");
	}


	@Override
	public void onReset() {
		// TODO Auto-generated method stub
		super.onReset();
	}



	@Override
	public void onDestroy() {
		super.onDestroy();
		stopService();
		this.cordova.getActivity().unbindService(mConnection);
		Log.i(TAG, "onDestroy - Desconectarse desde el Plugin");
	}
	
	private ServiceConnection mConnection = new ServiceConnection() {

	    public void onServiceConnected(ComponentName className, IBinder binder) {
	    	HLSPlayerService.LocalBinder b = (HLSPlayerService.LocalBinder) binder;
	    	service = b.getService();
	    	Log.i(TAG, "Servicio Connectado");
	    	
	    	boolean playing = service.isPlaying();
	    	if(resourceURL != null){
	    		service.setPath(resourceURL);
	    	}
	    	
			if(playing){
				//showStop();
			}  else {
				//showPlay();
			}
	    }

	    public void onServiceDisconnected(ComponentName className) {
	      service = null;
	      Log.i(TAG, "Servicio Desconnectado");
	    }
	  };

}
