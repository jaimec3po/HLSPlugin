cordova.define("com.bakata.plugins.HLSPlugin.HLSPlugin", function(require, exports, module) { /*
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
*/

var argscheck = require('cordova/argscheck'),
    utils = require('cordova/utils'),
    exec = require('cordova/exec');

var mediaObjects = {};

/**
 * This class provides access to the device media, interfaces to both sound and video
 *
 * @constructor
 * @param src                   The file name or url to play
 * @param successCallback       The callback to be called when the file is done playing or recording.
 *                                  successCallback()
 * @param errorCallback         The callback to be called if there is an error.
 *                                  errorCallback(int errorCode) - OPTIONAL
 * @param statusCallback        The callback to be called when media status has changed.
 *                                  statusCallback(int statusCode) - OPTIONAL
 */
var HLSPlugin = function(src, successCallback, errorCallback, statusCallback) {
	console.log('HLSPlugin creation');
    argscheck.checkArgs('SFFF', 'HLSPlugin', arguments);
    this.id = utils.createUUID();
    mediaObjects[this.id] = this;
    this.src = src;
    this.successCallback = successCallback;
    this.errorCallback = errorCallback;
    this.statusCallback = statusCallback;
    this._duration = -1;
    this._position = -1;
    exec(null, this.errorCallback, "HLSPlugin", "create", [this.id, this.src]);
};

// Media messages
HLSPlugin.MEDIA_STATE = 1;
HLSPlugin.MEDIA_DURATION = 2;
HLSPlugin.MEDIA_POSITION = 3;
HLSPlugin.MEDIA_ERROR = 9;

// Media states
HLSPlugin.MEDIA_NONE = 0;
HLSPlugin.MEDIA_STARTING = 1;
HLSPlugin.MEDIA_RUNNING = 2;
HLSPlugin.MEDIA_PAUSED = 3;
HLSPlugin.MEDIA_STOPPED = 4;
HLSPlugin.MEDIA_MSG = ["None", "Starting", "Running", "Paused", "Stopped"];

// "static" function to return existing objs.
HLSPlugin.get = function(id) {
    return mediaObjects[id];
};

/**
 * Start or resume playing audio file.
 */
HLSPlugin.prototype.play = function(options) {
    console.log('play en el HLSPlugin');
    exec(null, null, "HLSPlugin", "startPlayingAudio", [this.id, this.src, options]);
};

/**
 * Stop playing audio file.
 */
HLSPlugin.prototype.stop = function() {
    var me = this;
    exec(function() {
        me._position = 0;
    }, this.errorCallback, "HLSPlugin", "stopPlayingAudio", [this.id]);
};

/**
 * Seek or jump to a new time in the track..
 */
HLSPlugin.prototype.seekTo = function(milliseconds) {
    var me = this;
    exec(function(p) {
        me._position = p;
    }, this.errorCallback, "HLSPlugin", "seekToAudio", [this.id, milliseconds]);
};

/**
 * Pause playing audio file.
 */
HLSPlugin.prototype.pause = function() {
    exec(null, this.errorCallback, "HLSPlugin", "pausePlayingAudio", [this.id]);
};

/**
 * Get duration of an audio file.
 * The duration is only set for audio that is playing, paused or stopped.
 *
 * @return      duration or -1 if not known.
 */
HLSPlugin.prototype.getDuration = function() {
    return this._duration;
};

/**
 * Get position of audio.
 */
HLSPlugin.prototype.getCurrentPosition = function(success, fail) {
    var me = this;
    exec(function(p) {
        me._position = p;
        success(p);
    }, fail, "HLSPlugin", "getCurrentPositionAudio", [this.id]);
};

/**
 * Start recording audio file.
 */
HLSPlugin.prototype.startRecord = function() {
    exec(null, this.errorCallback, "HLSPlugin", "startRecordingAudio", [this.id, this.src]);
};

/**
 * Stop recording audio file.
 */
HLSPlugin.prototype.stopRecord = function() {
    exec(null, this.errorCallback, "HLSPlugin", "stopRecordingAudio", [this.id]);
};

/**
 * Release the resources.
 */
HLSPlugin.prototype.release = function() {
    exec(null, this.errorCallback, "HLSPlugin", "release", [this.id]);
};

/**
 * Adjust the volume.
 */
HLSPlugin.prototype.setVolume = function(volume) {
    exec(null, null, "HLSPlugin", "setVolume", [this.id, volume]);
};

/**
 * Audio has status update.
 * PRIVATE
 *
 * @param id            The media object id (string)
 * @param msgType       The 'type' of update this is
 * @param value         Use of value is determined by the msgType
 */
HLSPlugin.onStatus = function(id, msgType, value) {

    var media = mediaObjects[id];

    if(media) {
        switch(msgType) {
            case Media.MEDIA_STATE :
                media.statusCallback && media.statusCallback(value);
                if(value == Media.MEDIA_STOPPED) {
                    media.successCallback && media.successCallback();
                }
                break;
            case Media.MEDIA_DURATION :
                media._duration = value;
                break;
            case Media.MEDIA_ERROR :
                media.errorCallback && media.errorCallback(value);
                break;
            case Media.MEDIA_POSITION :
                media._position = Number(value);
                break;
            default :
                console.error && console.error("Unhandled HLSPlugin :: " + msgType);
                break;
        }
    }
    else {
         console.error && console.error("Received HLSPlugin callback for unknown media :: " + id);
    }

};

module.exports = HLSPlugin;

});
