/*
 * UtilsTest.java
 *
 * Copyright (c) 2015 Shubham Chaudhary <me@shubhamchaudhary.in>
 *
 * CCDroid is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CCDroid is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CCDroid.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.developfreedom.ccdroid.app.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UtilsTest {
    Context context;

    @Before
    public void setUp() throws Exception {
        context = mock(Context.class);
    }

    @Test
    public void testShouldOpenUrl() throws Exception {
        //Activity activity = Robolectric.buildActivity(MainActivity.class).create().get();
        String url = "https://ccdroid.github.io";

        Utils.openUrl(url, context);

        ArgumentCaptor<Intent> argument = ArgumentCaptor.forClass(Intent.class);
        verify(context).startActivity(argument.capture());
/*
        //TODO: This test needs to use robolectric shadows
        ShadowActivity shadowActivity = Robolectric.shadowOf(activity);
        Intent startedIntent = shadowActivity.getNextStartedActivity();
        ShadowIntent shadowIntent = shadowOf(startedIntent);
        assertThat(shadowIntent.getAction(), is(Intent.ACTION_VIEW));
        assertThat(shadowIntent.getData(), is(Uri.parse(url)));
*/
    }

    @Test
    public void testShouldReturnFalseIfNetworkDisconnected() throws Exception {
        ConnectivityManager connectivityManager = mock(ConnectivityManager.class);
        when(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManager);
        when(connectivityManager.getActiveNetworkInfo()).thenReturn(null);

        boolean online = Utils.isOnline(context);

        assertThat(online, is(false));
    }

    @Test
    public void testShouldReturnTrueIfNetworkConnected() throws Exception {
        ConnectivityManager connectivityManager = mock(ConnectivityManager.class);
        when(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManager);
        NetworkInfo networkInfo = mock(NetworkInfo.class);
        when(connectivityManager.getActiveNetworkInfo()).thenReturn(networkInfo);
        when(networkInfo.isConnectedOrConnecting()).thenReturn(true);

        boolean online = Utils.isOnline(context);

        assertThat(online, is(true));
    }
}
