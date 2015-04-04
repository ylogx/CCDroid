package org.developfreedom.ccdroid.app;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class DownloadXmlTaskTest {
    private OnDownloadTaskCompleted onDownloadTaskCompleted;
    private DownloadXmlTask downloadXmlTask;

    @Before
    public void setUp() throws Exception {
        onDownloadTaskCompleted = mock(OnDownloadTaskCompleted.class);
        downloadXmlTask = new DownloadXmlTask(onDownloadTaskCompleted);
    }

    @Test
    public void testThatListUpdateIsDonePostExecution() throws Exception {
        Project project1 = new Project();
        Project project2 = new Project();
        List<Project> projects = asList(project1, project2);

        downloadXmlTask.onPostExecute(projects);

        verify(onDownloadTaskCompleted).updateListView(projects);
    }
}
