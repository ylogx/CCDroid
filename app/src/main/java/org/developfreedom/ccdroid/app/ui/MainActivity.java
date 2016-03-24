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
package org.developfreedom.ccdroid.app.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import io.fabric.sdk.android.Fabric;
import org.developfreedom.ccdroid.app.BuildConfig;
import org.developfreedom.ccdroid.app.Project;
import org.developfreedom.ccdroid.app.ProjectParser;
import org.developfreedom.ccdroid.app.R;
import org.developfreedom.ccdroid.app.controllers.ListViewController;
import org.developfreedom.ccdroid.app.controllers.ProjectStorageController;
import org.developfreedom.ccdroid.app.storage.ProviderController;
import org.developfreedom.ccdroid.app.tasks.DownloadXmlTask;
import org.developfreedom.ccdroid.app.utils.Preferences;
import org.developfreedom.ccdroid.app.utils.Utils;

import java.util.List;

import static butterknife.ButterKnife.findById;
import static org.developfreedom.ccdroid.app.utils.LogUtils.*;


public class MainActivity extends AppCompatActivity implements ListViewController {
    private static final String TAG = makeLogTag(MainActivity.class);
    private Preferences preferences;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProjectStorageController projectStorageController;
    private ProjectListFragment listFragment;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!BuildConfig.DEBUG) {
            Fabric.with(this, new Crashlytics(), new Answers());
        }
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            listFragment = ProjectListFragment.newInstance();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, listFragment)
                    .commit();
        }

        preferences = new Preferences(PreferenceManager.getDefaultSharedPreferences(this));
        projectStorageController = new ProviderController(getContentResolver());
    }

    @Override protected void onStart() {
        super.onStart();
        updateListView();
        swipeRefreshLayout = findById(this, R.id.main_swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                refresh();
            }
        });
        refresh();
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (R.id.action_refresh == id) {
            refresh();
            return true;
        } else if (R.id.action_add_url == id) {
            showAddUrlDialog();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public void refresh() {
        swipeRefreshLayout.setRefreshing(true);
        LOGD(TAG, "Refreshing");
        if (Utils.isOnline(this)) {
            // fetch data
            String projectUrl = preferences.getUrl();
            new DownloadXmlTask(new ProjectParser(), this, projectStorageController)
                    .execute(projectUrl);
        } else {
            LOGI(TAG, "refresh: No Network");
            Toast.makeText(this, getString(R.string.toast_network_unavailable), Toast.LENGTH_SHORT).show();
        }
    }

    private void showAddUrlDialog() {
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_TEXT_VARIATION_URI);
        input.setText(preferences.getUrl());

        new AlertDialog.Builder(this)
                .setMessage(R.string.dialog_message_add_url)
                .setTitle(R.string.dialog_title_add_url)
                .setView(input)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int id) {
                        //TODO: Check input text to be a url
                        preferences.setUrl(input.getText().toString());
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create()
                .show();
    }

    @Override public void updateListView() {
        updateListView(projectStorageController.get());
    }

    @Override public void updateListView(List<Project> projects) {
        if (projects == null) {
            Toast.makeText(this, getString(R.string.toast_unable_to_fetch_project_list), Toast.LENGTH_SHORT).show();
            LOGE(TAG, "Error: project list came empty");
            return;
        }
        listFragment.updateProjects(projects);
        if ((swipeRefreshLayout != null) && swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}
