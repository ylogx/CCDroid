package org.developfreedom.ccdroid.app.ui;

import android.os.Build;
import android.widget.FrameLayout;
import org.developfreedom.ccdroid.app.BuildConfig;
import org.developfreedom.ccdroid.app.R;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.assertj.android.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.robolectric.RuntimeEnvironment.application;

/**
 * @author Shubham Chaudhary on 25/03/16
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class ProjectAdapterRoboTest {
    private ProjectAdapter adapter;

    @Before public void setUp() throws Exception {
        adapter = new ProjectAdapter(null, null);
    }

    @Test public void onCreateViewHolder_CorrectIdsMapped() throws Exception {
        ProjectAdapter.ViewHolder holder = adapter.createViewHolder(new FrameLayout(application), adapter.getItemViewType(0));

        assertThat(holder).isNotNull();
        assertThat(holder.name).hasId(R.id.lw_project_name);
        assertThat(holder.time).hasId(R.id.lw_project_time);
        assertThat(holder.activity).hasId(R.id.lw_project_activity);
        assertThat(holder.status).hasId(R.id.lw_status_flag);
        assertThat(holder.clickContainer).hasId(R.id.card_view);
    }
}
