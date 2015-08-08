/*
 * ListViewItemClickListenerTest.java
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

package org.developfreedom.ccdroid.app.listeners;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import org.developfreedom.ccdroid.app.BuildConfig;
import org.developfreedom.ccdroid.app.MainActivity;
import org.developfreedom.ccdroid.app.R;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowAlertDialog;
import org.robolectric.shadows.ShadowIntent;
import org.robolectric.shadows.ShadowView;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class ListViewItemClickListenerTest {
//    @Mock
    ListView listView;
    @Mock
    private Context context;
    private ListViewItemClickListener listener;
    private int position;
    private String url;

    @Before
    public void setUp() throws Exception {
        listView = mock(ListView.class);
        context = RuntimeEnvironment.application.getApplicationContext();
        listener = new ListViewItemClickListener(listView, context);

        position = 1;
        Map<String, String> itemMap = new HashMap<>();
        url = "https://ccdroid.github.io";
        itemMap.put("url", url);
        itemMap.put("activity", "sleeping");
        ListAdapter adapter = mock(ListAdapter.class);
        when(listView.getAdapter()).thenReturn(adapter);
        when(adapter.getItem(position)).thenReturn(itemMap);
    }

    @Test
    public void testShouldShowAlertDialog() throws Exception {
        listener.onItemClick(mock(AdapterView.class), mock(View.class), position, 1);

        AlertDialog alertDialog = ShadowAlertDialog.getLatestAlertDialog();
        ShadowAlertDialog shadowAlertDialog = Shadows.shadowOf(alertDialog);
        assertThat(shadowAlertDialog.getTitle().toString(), is(context.getString(R.string.alert_title_details)));
        assertThat(shadowAlertDialog.getMessage(), is(CharSequence.class));
        assertThat(shadowAlertDialog.getMessage().toString(), is("Activity: sleeping\nUrl: https://ccdroid.github.io\n"));
        Button openButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        assertThat(openButton.getText().toString(), is(context.getString(R.string.alert_button_open)));
    }

    @Ignore
    @Test
    public void testShouldOpenUrl() throws Exception {
        Activity activity = Robolectric.buildActivity(MainActivity.class).create().get();

        listener.onItemClick(mock(AdapterView.class), mock(View.class), position, 1);
        clickOnOpenButton();

        ShadowActivity shadowActivity = Shadows.shadowOf(activity);
        Intent startedIntent = shadowActivity.getNextStartedActivity();
        ShadowIntent shadowIntent = Shadows.shadowOf(startedIntent);
        assertThat(shadowIntent.getAction(), is(Intent.ACTION_VIEW));
        assertThat(shadowIntent.getData(), is(Uri.parse(url)));
//        ArgumentCaptor<Intent> argumentCaptor = ArgumentCaptor.forClass(Intent.class);
//        verify(context).startActivity(argumentCaptor.capture());
//        Intent intent = argumentCaptor.getValue();
    }

    private void clickOnOpenButton() {
        AlertDialog alertDialog = ShadowAlertDialog.getLatestAlertDialog();
        Button openButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        ShadowView.clickOn(openButton);
    }
}
