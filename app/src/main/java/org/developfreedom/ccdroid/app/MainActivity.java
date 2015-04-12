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
import android.text.Html;
import android.util.Log;
import android.view.*;
import android.widget.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
        if (id == R.id.action_settings) {
            return true;
        }

        if (item.getItemId() == R.id.action_refresh) {
            Toast.makeText(this, "Refreshing", Toast.LENGTH_SHORT).show();
            refresh();
            return true;
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
            String projectUrl = "https://snap-ci.com/hwEMz49fQYcu2gA_wLEMTE3lF53Xx5BMrxyCTm0heEk/cctray.xml";
            DownloadXmlTask downloadXmlTask = new DownloadXmlTask(this, new ProjectParser());
            downloadXmlTask.execute(projectUrl);
        } else {
            Log.v(TAG, "refresh: No Network");
        }
    }

    public void updateListView(List<Project> projects) {
        Log.v(TAG, "Starting listview update");
        SimpleAdapter adapter = getAdapterFor(projects);

        projectsListView = (ListView) findViewById(R.id.fragment_listview_projects);

        projectsListView.setAdapter(adapter);
        Log.v(TAG, "Adapter set to projects listview has " + adapter.getCount() + " items");

        projectsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Log.v(TAG, "Listview item clicked");
                ListAdapter adapter = projectsListView.getAdapter();
                final Map<String, String> clickedItem = (Map<String, String>) adapter.getItem(position);
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("Details");
                String details = "";
                for (String key: clickedItem.keySet()) {
                    details += "<b>";
                    details += key.substring(0, 1).toUpperCase() + key.substring(1);
                    details += ": ";
                    details += "</b>";
                    details += clickedItem.get(key);
                    details += "<br/>";
                }
                Log.v(TAG, "Details: " + Html.fromHtml(details));
                alert.setMessage(Html.fromHtml(details));
                alert.setIcon(R.mipmap.ic_launcher);
                alert.setPositiveButton("Open", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.v(TAG, "Positive button clicked");
                        String s = projectsListView.getItemAtPosition(position).toString();
                        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(), clickedItem.get("url"), Toast.LENGTH_LONG).show();
                    }
                });
                alert.show();
            }
        });
    }

    private SimpleAdapter getAdapterFor(List<Project> projects) {
        List<HashMap<String,String>> dataList = new ArrayList<HashMap<String,String>>();

        for (Project project : projects) {
            HashMap<String, String> hashMap = new HashMap<String,String>();
            String lastBuildStatus = project.getLastBuildStatus();
            if (lastBuildStatus.equals("Success")) {
                hashMap.put("flag", Integer.toString(R.drawable.button_green));
            } else if (lastBuildStatus.equals("Failure")) {
                hashMap.put("flag", Integer.toString(R.drawable.button_red));
            } else if (lastBuildStatus.equals("Unknown")) {
                hashMap.put("flag", Integer.toString(R.drawable.button_yellow));
            } else {
                hashMap.put("flag", Integer.toString(R.drawable.button_grey));
            }
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
                "activity",
                "time"
        };
        int[] valuesIdInListviewLayout = {
                R.id.lw_status_flag,
                R.id.lw_project_name,
                R.id.lw_project_activity,
                R.id.lw_project_time
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
}
