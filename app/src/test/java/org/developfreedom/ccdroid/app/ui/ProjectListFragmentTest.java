package org.developfreedom.ccdroid.app.ui;

import android.support.v7.widget.RecyclerView;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * @author Shubham Chaudhary on 25/03/16
 */
public class ProjectListFragmentTest {
    @Test public void onDestroyView_ClearBindings() throws Exception {
        ProjectListFragment fragment = ProjectListFragment.newInstance();
        fragment.recyclerView = mock(RecyclerView.class);
        assertThat(fragment.recyclerView).isNotNull();

        fragment.onDestroyView();

        assertThat(fragment.recyclerView).isNull();
    }
}
