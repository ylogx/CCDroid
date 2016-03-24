package org.developfreedom.ccdroid.app.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.developfreedom.ccdroid.app.Project;
import org.developfreedom.ccdroid.app.R;

import java.util.List;

/**
 * @author Shubham Chaudhary on 8/9/15.
 */
public class ProjectListFragment extends Fragment {
    private static final String TAG = ProjectListFragment.class.getSimpleName();
    private RecyclerView recyclerView;
    private ProjectAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public static ProjectListFragment newInstance() {
        return new ProjectListFragment();
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.fragment_listview_projects);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        return rootView;
    }

    public void updateProjects(List<Project> projects) {
        adapter = new ProjectAdapter(projects, getFragmentManager());
        recyclerView.setAdapter(adapter);
    }
}
