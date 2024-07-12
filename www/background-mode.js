/*
    Copyright 2013-2017 appPlant GmbH

    Licensed to the Apache Software Foundation (ASF) under one
    or more contributor license agreements.  See the NOTICE file
    distributed with this work for additional information
    regarding copyright ownership.  The ASF licenses this file
    to you under the Apache License, Version 2.0 (the
    "License"); you may not use this file except in compliance
    with the License.  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing,
    software distributed under the License is distributed on an
    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
    KIND, either express or implied.  See the License for the
    specific language governing permissions and limitations
    under the License.
*/

cordova.define("cordova-plugin-background-mode.BackgroundMode", function(require, exports, module) {
var exec    = require('cordova/exec'),
    channel = require('cordova/channel');


exports._isDebugMode = false;
exports.isDebugMode = function (){
    return this._isDebugMode;
}

/**
 * 在指定时间后发送通知
 * @param args {Object} The arguments to send to the native side. text,content
 */
exports.sendNotificationForTime = function (args){
    cordova.exec(null, null, 'BackgroundMode','sendNotificationForTime', [args]);
};

/**
 * 重置预约的通知
 */
exports.resetNotificationForTime = function () {
    cordova.exec(null, null, 'BackgroundMode', 'resetNotificationForTime', []);
};

exports.isDebugable = function () {
    var that = this;
    function successCallback(result) {
        that._isDebugMode = result == 'OK';
    }
    function errorCallback(error) {
        that._isDebugMode = false;
        console.error('Error getting debuggable state:'+ error);
    }
    cordova.exec(successCallback, errorCallback, 'BackgroundMode', 'isDebuggable', []);
};

exports.GotoAutoStartManagerPage = function() {
    var fn = function() {
        //alert('GotoAutoStartManagerPage-success');
            //exports._isEnabled = true;
            //exports.fireEvent('enable');
        };

    cordova.exec(null, null, 'BackgroundMode', 'GotoAutoStartManagerPage', []);
};

exports.StartJobServer = function() {
    var fn = function() {
        //alert('StartJobServer-success');
            //exports._isEnabled = true;
            //exports.fireEvent('enable');
        };

    cordova.exec(null, null, 'BackgroundMode', 'StartJobServer', []);
};

exports.StartOnPixelActivityWhenScreenOff = function() {
    var fn = function() {
        alert('StartOnPixelActivityWhenScreenOff-success');
            //exports._isEnabled = true;
            //exports.fireEvent('enable');
        };

    cordova.exec(null, null, 'BackgroundMode', 'StartOnPixelActivityWhenScreenOff', []);
};

exports.StartVVSerivce = function() {
    var fn = function() {
        alert('StartVVSerivce-success');
            //exports._isEnabled = true;
            //exports.fireEvent('enable');
        };

    cordova.exec(null, null, 'BackgroundMode', 'StartVVSerivce', []);
};

exports.BringToFront = function() {
    var fn = function() {
        alert('BringToFront-success');
            //exports._isEnabled = true;
            //exports.fireEvent('enable');
        };

    cordova.exec(null, null, 'BackgroundMode', 'BringToFront', []);
};

exports.BringToFrontBySetTime = function(time) {
    var fn = function() {
        alert('BringToFrontBySetTime-success');
        };
    
    var fn1 = function() {
        alert('BringToFrontBySetTime-failed');
        };
    cordova.exec(null, null, 'BackgroundMode', 'BringToFrontBySetTime', [time]);
};

exports.moveTaskToBack = function() {
    cordova.exec(null, null, 'BackgroundMode', 'moveTaskToBack', []);
};

exports.StartIPC = function() {
    cordova.exec(null, null, 'BackgroundMode', 'StartIPC', []);
};

exports.GetLog = function(success) {
    cordova.exec(success, null, 'BackgroundMode', 'GetLog', []);
};

exports.ClearLog = function() {
    cordova.exec(null, null, 'BackgroundMode', 'ClearLog', []);
};

exports.GetToken = function(success) {
    cordova.exec(success, null, 'BackgroundMode', 'GetToken', []);
};

exports.TestBugly = function() {
    cordova.exec(null, null, 'BackgroundMode', 'TestBugly', []);
};

exports.sendNotification = function(arg0,arg1,agr2) {
    cordova.exec(null, null, 'BackgroundMode', 'sendNotification', [arg0,arg1,agr2]);
};

exports.setNotificationText = function(arg0,arg1) {
    cordova.exec(null, null, 'BackgroundMode', 'setNotificationText', [arg0,arg1]);
};

exports.sendBigPicNotification = function(arg0,arg1) {
    cordova.exec(null, null, 'BackgroundMode', 'sendBigPicNotification', [arg0,arg1]);
};


exports.setNotificationButtonClickIntent = function() {
    cordova.exec(null, null, 'BackgroundMode', 'setNotificationButtonClickIntent', []);
};

//电池优化
exports.ignoreBatteryOption = function() {
    cordova.exec(null, null, 'BackgroundMode', 'ignoreBatteryOption', []);
};

//获取忽略电池优化 是否开启
exports.GetignoreBatteryOptionState = function(success) {
    cordova.exec(success, null, 'BackgroundMode', 'GetignoreBatteryOptionState', []);
};

//跳转到应用商店详情界面
exports.launchAppMarketDetail = function() {
    cordova.exec(null, null, 'BackgroundMode', 'launchAppMarketDetail', []);
};

//跳转到应用商店搜索界面
exports.launchAppMarketSearch = function() {
    cordova.exec(null, null, 'BackgroundMode', 'launchAppMarketSearch', []);
};

//加QQ
exports.joinQQChatPage = function(arg0) {
    cordova.exec(null, null, 'BackgroundMode', 'joinQQChatPage', [arg0]);
};

//加QQ群
exports.joinQQGroupPage = function(arg0) {
    cordova.exec(null, null, 'BackgroundMode', 'joinQQGroupPage', [arg0]);
};

exports.goToJoinQqGroup  = function() {
    cordova.exec(null, null, 'BackgroundMode', 'goToJoinQqGroup_IOS', []);
};

//获取手机的信息
exports.getMobileInfo  = function(success) {
    cordova.exec(success, null, 'BackgroundMode', 'getMobileInfo', []);
};


/*************
 * INTERFACE *
 *************/

/**
 * Activates the background mode. When activated the application
 * will be prevented from going to sleep while in background
 * for the next time.
 *
 * @return [ Void ]
 */
exports.enable = function() {
    if (this.isEnabled())
        return;

    var fn = function() {
            exports._isEnabled = true;
            exports.fireEvent('enable');
        };

    cordova.exec(fn, null, 'BackgroundMode', 'enable', []);
};

/**
 * Deactivates the background mode. When deactivated the application
 * will not stay awake while in background.
 *
 * @return [ Void ]
 */
exports.disable = function() {
    if (!this.isEnabled())
        return;

    var fn = function() {
            exports._isEnabled = false;
            exports.fireEvent('disable');
        };

    cordova.exec(fn, null, 'BackgroundMode', 'disable', []);
};

/**
 * Enable or disable the background mode.
 *
 * @param [ Bool ] enable The status to set for.
 *
 * @return [ Void ]
 */
exports.setEnabled = function (enable) {
    if (enable) {
        this.enable();
    } else {
        this.disable();
    }
};

/**
 * List of all available options with their default value.
 *
 * @return [ Object ]
 */
exports.getDefaults = function() {
    return this._defaults;
};

/**
 * The actual applied settings.
 *
 * @return [ Object ]
 */
exports.getSettings = function() {
    return this._settings || {};
};

/**
 * Overwrite the default settings.
 *
 * @param [ Object ] overrides Dict of options to be overridden.
 *
 * @return [ Void ]
 */
exports.setDefaults = function (overrides) {
    var defaults = this.getDefaults();

    for (var key in defaults) {
        if (overrides.hasOwnProperty(key)) {
            defaults[key] = overrides[key];
        }
    }

    if (this._isAndroid) {
        cordova.exec(null, null, 'BackgroundMode', 'configure', [defaults, false]);
    }
};

/**
 * Configures the notification settings for Android.
 * Will be merged with the defaults.
 *
 * @param [ Object ] options Dict of options to be overridden.
 *
 * @return [ Void ]
 */
exports.configure = function (options) {
    var settings = this.getSettings(),
        defaults = this.getDefaults();

    if (!this._isAndroid)
        return;

    if (!this._isActive) {
        console.log('BackgroundMode is not active, skipped...');
        return;
    }

    this._mergeObjects(options, settings);
    this._mergeObjects(options, defaults);
    this._settings = options;

    cordova.exec(null, null, 'BackgroundMode', 'configure', [options, true]);
};

/**
 * Enable GPS-tracking in background (Android).
 *
 * @return [ Void ]
 */
exports.disableWebViewOptimizations = function() {
    if (this._isAndroid) {
        cordova.exec(null, null, 'BackgroundMode', 'optimizations', []);
    }
};

/**
 * Move app to background (Android only).
 *
 * @return [ Void ]
 */
exports.moveToBackground = function() {
    if (this._isAndroid) {
        cordova.exec(null, null, 'BackgroundMode', 'background', []);
    }
};

/**
 * Move app to foreground when in background (Android only).
 *
 * @return [ Void ]
 */
exports.moveToForeground = function() {
    if (this.isActive() && this._isAndroid) {
        cordova.exec(null, null, 'BackgroundMode', 'foreground', []);
    }
};

/**
 * Exclude the app from the recent tasks list (Android only).
 *
 * @return [ Void ]
 */
exports.excludeFromTaskList = function() {
    if (this._isAndroid) {
        cordova.exec(null, null, 'BackgroundMode', 'tasklist', []);
    }
};

/**
 * Override the back button on Android to go to background
 * instead of closing the app.
 *
 * @return [ Void ]
 */
exports.overrideBackButton = function() {
    document.addEventListener('backbutton', function() {
        exports.moveToBackground();
    }, false);
};

/**
 * If the screen is off.
 *
 * @param [ Function ] fn Callback function to invoke with boolean arg.
 *
 * @return [ Void ]
 */
exports.isScreenOff = function (fn) {
    if (this._isAndroid) {
        cordova.exec(fn, null, 'BackgroundMode', 'dimmed', []);
    } else {
        fn(undefined);
    }
};

/**
 * Wake up the device.
 *
 * @return [ Void ]
 */
exports.wakeUp = function() {
    if (this._isAndroid) {
        cordova.exec(null, null, 'BackgroundMode', 'wakeup', []);
    }
};

/**
 * Wake up and unlock the device.
 *
 * @return [ Void ]
 */
exports.unlock = function() {
    if (this._isAndroid) {
        cordova.exec(null, null, 'BackgroundMode', 'unlock', []);
    }
};

/**
 * If the mode is enabled or disabled.
 *
 * @return [ Boolean ]
 */
exports.isEnabled = function() {
    return this._isEnabled !== false;
};

/**
 * If the mode is active.
 *
 * @return [ Boolean ]
 */
exports.isActive = function() {
    return this._isActive !== false;
};


/**********
 * EVENTS *
 **********/

exports._listener = {};

/**
 * Fire event with given arguments.
 *
 * @param [ String ] event The event's name.
 * @param [ Array<Object> ] The callback's arguments.
 *
 * @return [ Void ]
 */
exports.fireEvent = function (event) {
    var args     = Array.apply(null, arguments).slice(1),
        listener = this._listener[event];

    if (!listener)
        return;

    for (var i = 0; i < listener.length; i++) {
        var fn    = listener[i][0],
            scope = listener[i][1];

        fn.apply(scope, args);
    }
};

/**
 * Register callback for given event.
 *
 * @param [ String ] event The event's name.
 * @param [ Function ] callback The function to be exec as callback.
 * @param [ Object ] scope The callback function's scope.
 *
 * @return [ Void ]
 */
exports.on = function (event, callback, scope) {

    if (typeof callback !== "function")
        return;

    if (!this._listener[event]) {
        this._listener[event] = [];
    }

    var item = [callback, scope || window];

    this._listener[event].push(item);
};

/**
 * Unregister callback for given event.
 *
 * @param [ String ] event The event's name.
 * @param [ Function ] callback The function to be exec as callback.
 *
 * @return [ Void ]
 */
exports.un = function (event, callback) {
    var listener = this._listener[event];

    if (!listener)
        return;

    for (var i = 0; i < listener.length; i++) {
        var fn = listener[i][0];

        if (fn == callback) {
            listener.splice(i, 1);
            break;
        }
    }
};

/**
 * @deprecated
 *
 * Called when the background mode has been activated.
 */
exports.onactivate = function() {};

/**
 * @deprecated
 *
 * Called when the background mode has been deaktivated.
 */
exports.ondeactivate = function() {};

/**
 * @deprecated
 *
 * Called when the background mode could not been activated.
 *
 * @param {Integer} errorCode
 *      Error code which describes the error
 */
exports.onfailure = function() {};


/***********
 * PRIVATE *
 ***********/

/**
 * @private
 *
 * Flag indicates if the mode is enabled.
 */
exports._isEnabled = false;

/**
 * @private
 *
 * Flag indicates if the mode is active.
 */
exports._isActive = false;

/**
 * @private
 *
 * Default values of all available options.
 */
exports._defaults = {
    title:   'App is running in background',
    text:    'Doing heavy tasks.',
    bigText: false,
    resume:  true,
    silent:  false,
    hidden:  true,
    color:   undefined,
    icon:    'icon'
};

/**
 * @private
 *
 * Merge settings with default values.
 *
 * @param [ Object ] options The custom options.
 * @param [ Object ] toMergeIn The options to merge in.
 *
 * @return [ Object ] Default values merged with custom values.
 */
exports._mergeObjects = function (options, toMergeIn) {
    for (var key in toMergeIn) {
        if (!options.hasOwnProperty(key)) {
            options[key] = toMergeIn[key];
            continue;
        }
    }

    return options;
};

/**
 * @private
 *
 * Setter for the isActive flag. Resets the
 * settings if the mode isnt active anymore.
 *
 * @param [ Boolean] value The new value for the flag.
 *
 * @return [ Void ]
 */
exports._setActive = function(value) {
    if (this._isActive == value)
        return;

    this._isActive = value;
    this._settings = value ? this._mergeObjects({}, this._defaults) : {};
};

/**
 * @private
 *
 * Initialize the plugin.
 *
 * Method should be called after the 'deviceready' event
 * but before the event listeners will be called.
 *
 * @return [ Void ]
 */
exports._pluginInitialize = function() {
    this._isAndroid = device.platform.match(/^android|amazon/i) !== null;
    this.setDefaults({});

    if (device.platform == 'browser') {
        this.enable();
        this._isEnabled = true;
    }

    this._isActive  = this._isActive || device.platform == 'browser';
};

// Called before 'deviceready' listener will be called
channel.onCordovaReady.subscribe(function() {
    channel.onCordovaInfoReady.subscribe(function() {
        exports._pluginInitialize();
    });
});

// Called after 'deviceready' event
channel.deviceready.subscribe(function() {
    if (exports.isEnabled()) {
        exports.fireEvent('enable');
    }

    if (exports.isActive()) {
        exports.fireEvent('activate');
    }
    exports.isDebugable();
});

});
