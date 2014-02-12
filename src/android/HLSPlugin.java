package com.bakata.plugins;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONException;


import android.util.Log;

public class HLSPlugin extends CordovaPlugin {
	
	private static final String TAG = HLSPlugin.class.getSimpleName();
	
	private HLSPlayerService mBoundService;
	private boolean mIsBound = false;
	
	@Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		
		Log.d(TAG, "Ha llegado el mensaje de invocacion, action " + action + " args "+ args.toString());

		if("create".equalsIgnoreCase(action)){
			return true;
			
		} else if ("startPlayingAudio".equalsIgnoreCase(action)){
			return true;
		} else if ("stopPlayingAudio".equalsIgnoreCase(action)){
			return true;
		} else if ("seekToAudio".equalsIgnoreCase(action)){
			return true;
		} else if ("pausePlayingAudio".equalsIgnoreCase(action)){
			return true;
		} else if ("getCurrentPositionAudio".equalsIgnoreCase(action)){
			return true;
		} else if ("setVolume".equalsIgnoreCase(action)){
			return true;
		}
		return false;
	}

}
