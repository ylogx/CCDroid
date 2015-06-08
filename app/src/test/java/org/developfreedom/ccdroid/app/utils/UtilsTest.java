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
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

//@RunWith(RobolectricGradleTestRunner.class)
//@Config(emulateSdk = 18)
public class UtilsTest {
    @Test
    public void testShouldOpenUrl() throws Exception {
        //Activity activity = Robolectric.buildActivity(MainActivity.class).create().get();
        Context context = mock(Context.class);
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
}
