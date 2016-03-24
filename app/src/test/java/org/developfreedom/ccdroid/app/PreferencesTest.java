package org.developfreedom.ccdroid.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import org.developfreedom.ccdroid.app.utils.Preferences;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.shadows.ShadowPreferenceManager;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@org.robolectric.annotation.Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class PreferencesTest {

    private SharedPreferences sharedPreferences;
    private Preferences preferences;
    private String dummyUrl;

    @Before
    public void setUp() throws Exception {
        Application application = RuntimeEnvironment.application;
        Context context = application.getApplicationContext();
        sharedPreferences = ShadowPreferenceManager.getDefaultSharedPreferences(context);
        preferences = new Preferences(context);
        dummyUrl = "https://ccdroid.github.io";

    }

    @Test
    public void testShouldGetUrl() throws Exception {
        sharedPreferences.edit().putString(Preferences.KEY_URL, dummyUrl).commit();

        assertThat(preferences.getUrl(), is(dummyUrl));
    }

    @Test
    public void testSetUrl() throws Exception {
        preferences.setUrl(dummyUrl);

        assertThat(sharedPreferences.getString(Preferences.KEY_URL, dummyUrl), is(dummyUrl));
    }
}
