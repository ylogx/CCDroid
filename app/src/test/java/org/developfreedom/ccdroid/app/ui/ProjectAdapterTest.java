package org.developfreedom.ccdroid.app.ui;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import org.developfreedom.ccdroid.app.Project;
import org.developfreedom.ccdroid.app.ProjectTest;
import org.developfreedom.ccdroid.app.R;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * @author Shubham Chaudhary on 25/03/16
 */
public class ProjectAdapterTest {
    private Project project;
    private ProjectAdapter adapter;

    @NonNull private static ProjectAdapter.ViewHolder getMockedHolder() {
        ProjectAdapter.ViewHolder holder = mock(ProjectAdapter.ViewHolder.class);
        holder.name = mock(TextView.class);
        holder.activity = mock(TextView.class);
        holder.time = mock(TextView.class);
        holder.status = mock(ImageView.class);
        holder.clickContainer = mock(View.class);
        return holder;
    }

    @Before public void setUp() throws Exception {
        ArrayList<Project> projects = new ArrayList<>(1);
        project = ProjectTest.getMockProject();
        projects.add(project);
        adapter = new ProjectAdapter(projects, null);
    }

    @Test public void getItemCount() throws Exception {
        assertThat(adapter.getItemCount()).isEqualTo(1);
    }

    @Test public void onBindView_SetCorrectFields() throws Exception {
        ProjectAdapter.ViewHolder holder = getMockedHolder();

        adapter.onBindViewHolder(holder, 0);

        verify(holder.name, only()).setText(ProjectTest.NAME);
        verify(holder.activity, only()).setText(ProjectTest.ACTIVITY);
        verify(holder.time, only()).setText(ProjectTest.TIME);
        verify(holder.clickContainer, only()).setOnClickListener(any(View.OnClickListener.class));
        verify(holder.status, only()).setTag(anyInt());
        verifyZeroInteractions(holder);
    }

    @Test public void onBindView_StatusSuccess_ColorGreen() throws Exception {
        assertBuildColorWhenStatus(ProjectAdapter.STATUS_SUCCESS, R.drawable.button_green);
    }

    @Test public void onBindView_StatusFailure_ColorRed() throws Exception {
        assertBuildColorWhenStatus(ProjectAdapter.STATUS_FAILURE, R.drawable.button_red);
    }

    @Test public void onBindView_StatusUnknown_ColorYellow() throws Exception {
        assertBuildColorWhenStatus(ProjectAdapter.STATUS_UNKNOWN, R.drawable.button_yellow);
    }

    private void assertBuildColorWhenStatus(String status, int drawableId) {
        ProjectAdapter.ViewHolder holder = getMockedHolder();
        when(project.getLastBuildStatus()).thenReturn(status);

        adapter.onBindViewHolder(holder, 0);

        verify(holder.status, only()).setTag(drawableId);
    }
}
