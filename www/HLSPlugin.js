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

// "static" function to return existing objs.
HLSPlugin.get = function(id) {
    return mediaObjects[id];
};


/**
 * Start or resume playing audio file.
 */
HLSPlugin.prototype.play = function(options) {
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
 * Adjust the volume.
 */
HLSPlugin.prototype.setVolume = function(volume) {
    exec(null, null, "HLSPlugin", "setVolume", [this.id, volume]);
};