/*
 The MIT License (MIT)
 Copyright © 2014-2015 Andrew Stevens <andy@andxyz.com>, Modern Alchemits <office@modalog.at>

 Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 to permit persons to whom the Software is furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 SOFTWARE.
*/

package org.apache.cordova.plugin.cache;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;

import android.annotation.TargetApi;
import android.app.Activity;
import android.util.Log;

@TargetApi(19)
public class Cache extends CordovaPlugin {
	private static final String LOG_TAG = "Cache";
	private CallbackContext callbackContext;

	/**
	 * Constructor.
	 */
	public Cache() {

	}

	@Override
	public boolean execute (String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		/*try
		{
		*/
		if ( action.equals("clear") ) {
			Log.v(LOG_TAG, "Cordova Android Cache.clear() called.");
			this.callbackContext = callbackContext;

			final Cache self = this;
			cordova.getActivity().runOnUiThread( new Runnable() {
				public void run() {
					try {
						// clear the cache
						self.webView.clearCache(true);

						// clear the data
						self.clearApplicationData();

						// send success result to cordova
						PluginResult result = new PluginResult(PluginResult.Status.OK);
						result.setKeepCallback(false);
						self.callbackContext.sendPluginResult(result);
					} catch ( Exception e ) {
						String msg = "Error while clearing webview cache.";
						Log.e(LOG_TAG, msg );

						// return error answer to cordova
						PluginResult result = new PluginResult(PluginResult.Status.ERROR, msg);
						result.setKeepCallback(false);
						self.callbackContext.sendPluginResult(result);
					}
				}
			});
			return true;
		}
		return false;
		/*
		}
		catch (JSONException e)
		{
		// TODO: signal JSON problem to JS
		//callbackContext.error("Problem with JSON");
		return false;
		}
		*/
	}

	private void clearApplicationData() {
		File cache = this.cordova.getActivity().getCacheDir();
		File appDir = new File(cache.getPath());
		Log.i(LOG_TAG, "Absolute path: " + appDir.getAbsolutePath());
		if (appDir.exists() && appDir.isDirectory()) {
			String[] children = appDir.list();
			for(int i = 0; i < children.length; i++){
				new File(appDir, children[i]).delete();
			}
		}
	}

}
