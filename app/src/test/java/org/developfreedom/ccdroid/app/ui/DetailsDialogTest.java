package org.developfreedom.ccdroid.app.ui;

import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import org.developfreedom.ccdroid.app.BuildConfig;
import org.developfreedom.ccdroid.app.Project;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import static org.assertj.android.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Shubham Chaudhary on 25/03/16
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class DetailsDialogTest {
    private static final String NAME = "Named";
    private static final String ACTIVITY = "activities";
    private static final String LABEL = "labels";
    private static final String STATUS = "statuses";
    private static final String TIME = "times";
    private static final String WEB_URL = "http://shubham.chaudhary.xyz/blog";
    private Project project;
    private DetailsDialog dialog;

    @Before public void setUp() throws Exception {
        project = mock(Project.class);
        when(project.getName()).thenReturn(NAME);
        when(project.getActivity()).thenReturn(ACTIVITY);
        when(project.getLastBuildLabel()).thenReturn(LABEL);
        when(project.getLastBuildStatus()).thenReturn(STATUS);
        when(project.getLastBuildTime()).thenReturn(TIME);
        when(project.getWebUrl()).thenReturn(WEB_URL);
        dialog = DetailsDialog.newInstance(project);
    }

    @Test public void onCreateView_SetFieldsCorrectly() throws Exception {
        View rootView = dialog.onCreateView(LayoutInflater.from(RuntimeEnvironment.application), null, null);
        assertThat(rootView).isVisible();
        assertThat(dialog.name).hasText(NAME);
        assertThat(dialog.activity).hasText(ACTIVITY);
        assertThat(dialog.label).hasText(LABEL);
        assertThat(dialog.time).hasText(TIME);
        assertThat(dialog.url).hasText(WEB_URL);
    }

    @Test public void onCreateView_OpenClicked_StartsWebBrowser() throws Exception {
        dialog.onCreateView(LayoutInflater.from(RuntimeEnvironment.application), null, null);

        dialog.open.performClick();

        Intent next = ShadowApplication.getInstance().getNextStartedActivity();
        assertThat(next).hasAction(Intent.ACTION_VIEW).hasData(WEB_URL);
    }
}
