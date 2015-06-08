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

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import org.developfreedom.ccdroid.app.R;
import org.developfreedom.ccdroid.app.RobolectricGradleTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowAlertDialog;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.robolectric.Robolectric.application;

@RunWith(RobolectricGradleTestRunner.class)
@Config(emulateSdk = 18)
public class ListViewItemClickListenerTest {
//    @Mock
    ListView listView;
    @Mock
    private Context context;
    private ListViewItemClickListener listener;

    @Before
    public void setUp() throws Exception {
        listView = mock(ListView.class);
        context = application.getApplicationContext();
        listener = new ListViewItemClickListener(listView, context);
    }

    @Test
    public void testShouldShowAlertDialog() throws Exception {
        int position = 1;
        Map<String, String> itemMap = new HashMap<>();
        String dummyUrl = "https://ccdroid.github.io";
        itemMap.put("url", dummyUrl);
        itemMap.put("activity", "sleeping");
        ListAdapter adapter = mock(ListAdapter.class);
        when(listView.getAdapter()).thenReturn(adapter);
        when(adapter.getItem(position)).thenReturn(itemMap);

        listener.onItemClick(mock(AdapterView.class), mock(View.class), position, 1);

        AlertDialog alertDialog = ShadowAlertDialog.getLatestAlertDialog();
        ShadowAlertDialog shadowAlertDialog = Robolectric.shadowOf(alertDialog);
        assertThat(shadowAlertDialog.getTitle().toString(), is(context.getString(R.string.alert_title_details)));
        assertThat(shadowAlertDialog.getMessage(), is(CharSequence.class));
        assertThat(shadowAlertDialog.getMessage().toString(), is("Activity: sleeping\nUrl: https://ccdroid.github.io\n"));
    }


}
