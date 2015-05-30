package org.developfreedom.ccdroid.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Config {
    private static final String DEFAULT_KEY_URL = "https://api.travis-ci.org/repos/shubhamchaudhary/CCDroid/cc.xml";
    private static final String KEY_URL = "key_url";
    private Context context;

    public Config(Context context) {
        this.context = context;
    }

    public String getUrl() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.context);
        return preferences.getString(KEY_URL, DEFAULT_KEY_URL);
    }

    public void setUrl(String url) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_URL, url);
        editor.apply();
    }
}
