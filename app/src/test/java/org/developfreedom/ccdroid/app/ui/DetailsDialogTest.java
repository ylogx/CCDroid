package org.developfreedom.ccdroid.app.ui;

import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import org.developfreedom.ccdroid.app.BuildConfig;
import org.developfreedom.ccdroid.app.Project;
import org.developfreedom.ccdroid.app.ProjectTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import static org.assertj.android.api.Assertions.assertThat;

/**
 * @author Shubham Chaudhary on 25/03/16
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class DetailsDialogTest {
    private Project project;
    private DetailsDialog dialog;

    @Before public void setUp() throws Exception {
        project = ProjectTest.getMockProject();
        dialog = DetailsDialog.newInstance(project);
    }

    @Test public void onCreateView_SetFieldsCorrectly() throws Exception {
        View rootView = dialog.onCreateView(LayoutInflater.from(RuntimeEnvironment.application), null, null);
        assertThat(rootView).isVisible();
        assertThat(dialog.name).hasText(ProjectTest.NAME);
        assertThat(dialog.activity).hasText(ProjectTest.ACTIVITY);
        assertThat(dialog.label).hasText(ProjectTest.LABEL);
        assertThat(dialog.time).hasText(ProjectTest.TIME);
        assertThat(dialog.url).hasText(ProjectTest.WEB_URL);
    }

    @Test public void onCreateView_OpenClicked_StartsWebBrowser() throws Exception {
        dialog.onCreateView(LayoutInflater.from(RuntimeEnvironment.application), null, null);

        dialog.open.performClick();

        Intent next = ShadowApplication.getInstance().getNextStartedActivity();
        assertThat(next).hasAction(Intent.ACTION_VIEW).hasData(ProjectTest.WEB_URL);
    }
}
