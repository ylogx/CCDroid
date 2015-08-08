package org.developfreedom.ccdroid.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
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
public class ConfigTest {

    private SharedPreferences sharedPreferences;
    private Config config;
    private String dummyUrl;

    @Before
    public void setUp() throws Exception {
        Application application = RuntimeEnvironment.application;
        Context context = application.getApplicationContext();
        sharedPreferences = ShadowPreferenceManager.getDefaultSharedPreferences(context);
        config = new Config(context);
        dummyUrl = "https://ccdroid.github.io";

    }

    @Test
    public void testShouldGetUrl() throws Exception {
        sharedPreferences.edit().putString(Config.KEY_URL, dummyUrl).commit();

        assertThat(config.getUrl(), is(dummyUrl));
    }

    @Test
    public void testSetUrl() throws Exception {
        config.setUrl(dummyUrl);

        assertThat(sharedPreferences.getString(Config.KEY_URL, dummyUrl), is(dummyUrl));
    }
}
