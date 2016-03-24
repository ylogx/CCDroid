package org.developfreedom.ccdroid.app;

import android.os.Build;
import android.support.v7.widget.RecyclerView;
import org.developfreedom.ccdroid.app.ui.MainActivity;
import org.developfreedom.ccdroid.app.ui.ProjectAdapter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.Collections;

import static butterknife.ButterKnife.findById;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class MainActivityTest { /*extends FragmentTestCase<NavigationDrawerFragment>*/
    protected MainActivity mainActivity;

    @Before public void setUp() {
        mainActivity = Robolectric.buildActivity(MainActivity.class).setup().get();
    }

    @Test public void updateListView_AddItemsToList() throws Exception {
        mainActivity.updateListView(Collections.singletonList(ProjectTest.getMockProject()));

        RecyclerView recyclerView = findById(mainActivity, R.id.fragment_listview_projects);
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        assertThat(adapter).isInstanceOf(ProjectAdapter.class);
        assertThat(adapter.getItemCount()).isEqualTo(1);
    }
}
