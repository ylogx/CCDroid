package org.developfreedom.ccdroid.app.ui;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import org.assertj.core.api.Assertions;
import org.developfreedom.ccdroid.app.BuildConfig;
import org.developfreedom.ccdroid.app.Project;
import org.developfreedom.ccdroid.app.ProjectTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Shubham Chaudhary on 25/03/16
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class ProjectListFragmentRoboTest {

    private ProjectListFragment fragment;

    @Test public void updateProjects_FragmentNotCreated_DoNothing() throws Exception {
        fragment = ProjectListFragment.newInstance();

        try {
            fragment.updateProjects(getProjects());
        } catch (NullPointerException e) {
            Assertions.fail("Should handle destroyed/not started state gracefully");
        }
    }

    @Test public void updateProjects_SetsAdapter() throws Exception {
        fragment = ProjectListFragment.newInstance();
        SupportFragmentTestUtil.startFragment(fragment);

        fragment.updateProjects(getProjects());

        //assertThat(fragment.recyclerView).hasAdapter();
        RecyclerView.Adapter adapter = fragment.recyclerView.getAdapter();
        assertThat(adapter).isNotNull();
        assertThat(adapter.getItemCount()).isEqualTo(2);
    }

    @NonNull private ArrayList<Project> getProjects() {
        ArrayList<Project> projects = new ArrayList<>(1);
        projects.add(ProjectTest.getMockProject());
        projects.add(ProjectTest.getMockProject());
        return projects;
    }
}
