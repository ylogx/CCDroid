package org.developfreedom.ccdroid.app;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.util.ActivityController;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@Ignore
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class MainActivityTest //extends FragmentTestCase<NavigationDrawerFragment>
{

    private final ActivityController<MainActivity> controller = Robolectric.buildActivity(MainActivity.class);
    private final Context context = RuntimeEnvironment.application.getApplicationContext();
    protected MainActivity mainActivity;

    @Before
    public void createMainActivity() {
        shadowApplicationContext();
        mainActivity = controller.create().get();
        //mainActivity = controller.attach().create().get();
        //mainActivity.setContentView(R.layout.activity_main);

        //startFragment(new NavigationDrawerFragment());
        //Fragment fragment = new NavigationDrawerFragment();
        //FragmentManager fragmentManager = mainActivity.getSupportFragmentManager();
        //FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //fragmentTransaction.add( fragment, null );
        //fragmentTransaction.commit();
    }

    private void shadowApplicationContext() {
        Shadows.shadowOf(context);
    }

    @Test
    public void shouldEnsureTestsAreRunningFine() throws Exception {
        assertTrue(true);
    }

    @Test
    public void shouldUpdateListViewWithProperData() throws Exception {
        String name = "shubhamchaudhary/wordpowermadeeasy";
        String activity = "Sleeping";
        String lastBuildLabel = "31";
        String lastBuildStatus = "Success";
        String lastBuildTime = "2015-03-22T11:32:14.000+0000";
        String webUrl = "https://travis-ci.org/shubhamchaudhary/wordpowermadeeasy";
        Project project1 = new Project(
                name,
                activity,
                lastBuildLabel,
                lastBuildStatus,
                lastBuildTime,
                webUrl
        );
        List<Project> projects = Collections.singletonList(project1);
        mainActivity.updateListView(projects);


        ListView projectsListView = (ListView) getViewById(R.id.fragment_listview_projects);
        View view = getViewByPosition(projectsListView.getFirstVisiblePosition(), projectsListView);
        TextView tw_name = (TextView) view.findViewById(R.id.lw_project_name);
        assertThat(tw_name.getText().toString(), is(name));
    }

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    private View getViewById(int viewId) {
        View rootView = mainActivity.getWindow().getDecorView().getRootView();

        return rootView.findViewById(viewId);
    }
}
