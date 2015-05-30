package org.developfreedom.ccdroid.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.*;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import org.developfreedom.ccdroid.app.listeners.ListViewItemClickListener;
import org.developfreedom.ccdroid.app.tasks.DownloadXmlTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity
        extends ActionBarActivity
        implements
            NavigationDrawerFragment.NavigationDrawerCallbacks,
            OnDownloadTaskCompleted {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private ListView projectsListView;
    private static String TAG = MainActivity.class.getSimpleName();
    private Config config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        config = new Config(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        refresh();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_drawer_item1);
                break;
            case 2:
                mTitle = getString(R.string.title_drawer_item2);
                break;
            case 3:
                mTitle = getString(R.string.title_drawer_item0);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
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
            Toast.makeText(this, getString(R.string.toast_refreshing), Toast.LENGTH_SHORT).show();
            refresh();
            return true;
        }

        if (item.getItemId() == R.id.action_add_url) {
            show_add_url_dialog();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

    public void refresh() {
        Log.v(TAG, "Refreshing");
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            // fetch data
            String projectUrl = config.getUrl();
            DownloadXmlTask downloadXmlTask = new DownloadXmlTask(this, new ProjectParser());
            downloadXmlTask.execute(projectUrl);
        } else {
            Log.v(TAG, "refresh: No Network");
            Toast.makeText(this, getString(R.string.toast_network_unavailable), Toast.LENGTH_SHORT).show();
        }
    }

    private void show_add_url_dialog() {
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage(R.string.dialog_message_add_url)
                .setTitle(R.string.dialog_title_add_url);

        final EditText input = new EditText(this);
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


    public void updateListView(List<Project> projects) {
        if (projects == null) {
            Toast.makeText(this, getString(R.string.toast_unable_to_fetch_project_list), Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Error: project list came empty");
            return;
        }
        Log.v(TAG, "Starting listview update");
        SimpleAdapter adapter = getAdapterFor(projects);

        projectsListView = (ListView) findViewById(R.id.fragment_listview_projects);

        projectsListView.setAdapter(adapter);
        Log.v(TAG, "Adapter set to projects listview has " + adapter.getCount() + " items");

        projectsListView.setOnItemClickListener(
                new ListViewItemClickListener(
                        projectsListView,
                        getApplicationContext(),
                        MainActivity.this
                )
        );
    }

    private SimpleAdapter getAdapterFor(List<Project> projects) {
        List<HashMap<String,String>> dataList = new ArrayList<HashMap<String,String>>();

        for (Project project : projects) {
            HashMap<String, String> hashMap = new HashMap<String,String>();
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
                "flag",
                "name",
        };
        int[] valuesIdInListviewLayout = {
                R.id.lw_status_flag,
                R.id.lw_project_name,
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
}
