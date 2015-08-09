/*
 * MainActivity.java
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
package org.developfreedom.ccdroid.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.view.*;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import org.developfreedom.ccdroid.app.controllers.ListViewController;
import org.developfreedom.ccdroid.app.controllers.ProjectStorageController;
import org.developfreedom.ccdroid.app.listeners.ListViewItemClickListener;
import org.developfreedom.ccdroid.app.storage.ProviderController;
import org.developfreedom.ccdroid.app.tasks.DownloadXmlTask;
import org.developfreedom.ccdroid.app.utils.LogUtils;
import org.developfreedom.ccdroid.app.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.developfreedom.ccdroid.app.utils.LogUtils.*;


public class MainActivity
        extends ActionBarActivity
        implements
        ListViewController {

    private static String TAG = LogUtils.makeLogTag(MainActivity.class);
    private ListView projectsListView;
    private Config config;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProjectStorageController mProjectStorageController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance())
                .commit();

        config = new Config(this);
        mProjectStorageController = new ProviderController(getContentResolver());
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateListView();
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.main_swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        refresh();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (item.getItemId() == R.id.action_refresh) {
            refresh();
            return true;
        }

        if (item.getItemId() == R.id.action_add_url) {
            showAddUrlDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    public void refresh() {
        swipeRefreshLayout.setRefreshing(true);
        LOGD(TAG, "Refreshing");
        if (Utils.isOnline(this)) {
            // fetch data
            String projectUrl = config.getUrl();
            DownloadXmlTask downloadXmlTask = new DownloadXmlTask(new ProjectParser(), this, mProjectStorageController);
            downloadXmlTask.execute(projectUrl);
        } else {
            LOGI(TAG, "refresh: No Network");
            Toast.makeText(this, getString(R.string.toast_network_unavailable), Toast.LENGTH_SHORT).show();
        }
    }

    private void showAddUrlDialog() {
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage(R.string.dialog_message_add_url)
                .setTitle(R.string.dialog_title_add_url);

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_TEXT_VARIATION_URI);
        input.setText(config.getUrl());
        builder.setView(input);

        // Add the buttons
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                //TODO: Check input text to be a url
                config.setUrl(input.getText().toString());
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        // 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void updateListView() {
        updateListView(mProjectStorageController.get());
    }

    @Override
    public void updateListView(List<Project> projects) {
        if (projects == null) {
            Toast.makeText(this, getString(R.string.toast_unable_to_fetch_project_list), Toast.LENGTH_SHORT).show();
            LOGE(TAG, "Error: project list came empty");
            return;
        }
        LOGD(TAG, "Starting listview update");
        SimpleAdapter adapter = getAdapterFor(projects);

        projectsListView = (ListView) findViewById(R.id.fragment_listview_projects);

        projectsListView.setAdapter(adapter);
        LOGD(TAG, "Adapter set to projects listview has " + adapter.getCount() + " items");

        projectsListView.setOnItemClickListener(
                new ListViewItemClickListener(
                        projectsListView,
                        this
                )
        );
        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private SimpleAdapter getAdapterFor(List<Project> projects) {
        List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();

        for (Project project : projects) {
            HashMap<String, String> hashMap = new HashMap<String, String>();
            int drawableId = getDrawableId(
                    project.getLastBuildStatus(),
                    project.getActivity()
            );

            hashMap.put("flag", Integer.toString(drawableId));
            hashMap.put("name", project.getName());
            hashMap.put("activity", project.getActivity());
            hashMap.put("time", project.getLastBuildTime());
            hashMap.put("label", project.getLastBuildLabel());
            hashMap.put("url", project.getWebUrl());

            dataList.add(hashMap);
        }

        String[] keysInDataHashmap = {
                "activity",
                "flag",
                "name",
                "time",
        };
        int[] valuesIdInListviewLayout = {
                R.id.lw_project_activity,
                R.id.lw_status_flag,
                R.id.lw_project_name,
                R.id.lw_project_time,
        };

        SimpleAdapter adapter = new SimpleAdapter(
                getBaseContext(),
                dataList,
                R.layout.list_row_layout_project, //this layout defines the layout of each item
                keysInDataHashmap,
                valuesIdInListviewLayout
        );
        return adapter;
    }

    private int getDrawableId(String lastBuildStatus, String activity) {
        int drawableId;
        switch (lastBuildStatus) {
            case "Success":
                drawableId = R.drawable.button_green;
                break;
            case "Failure":
                drawableId = R.drawable.button_red;
                break;
            case "Unknown":
                if (activity.equals("Building")) {
                    drawableId = R.drawable.button_refresh;
                } else {
                    drawableId = R.drawable.button_yellow;
                }
                break;
            default:
                drawableId = R.drawable.button_grey;
                break;
        }
        return drawableId;
    }

    public static class PlaceholderFragment extends Fragment {
        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance() {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }
}
