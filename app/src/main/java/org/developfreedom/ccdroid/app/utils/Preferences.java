package org.developfreedom.ccdroid.app.utils;

import android.content.SharedPreferences;

public class Preferences {
    public static final String DEFAULT_KEY_URL = "https://api.travis-ci.org/repos/shubhamchaudhary/CCDroid/cc.xml";
    public static final String KEY_URL = "key_url";
    private SharedPreferences preferences;

    public Preferences(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    public String getUrl() {
        return preferences.getString(KEY_URL, DEFAULT_KEY_URL);
    }

    public void setUrl(String url) {
        preferences.edit().putString(KEY_URL, url).apply();
    }
}
