package org.developfreedom.ccdroid.app;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(RobolectricTestRunner.class)
public class DownloadXmlTaskTest {
    private OnDownloadTaskCompleted onDownloadTaskCompleted;
    private DownloadXmlTask downloadXmlTask;
    private ProjectParser parser;

    @Before
    public void setUp() throws Exception {
        onDownloadTaskCompleted = mock(OnDownloadTaskCompleted.class);
        parser = mock(ProjectParser.class);
        downloadXmlTask = new DownloadXmlTask(onDownloadTaskCompleted, parser);
    }

    @Test
    public void testThatProjectListIsFetchedInBackgroundThread() throws Exception {
        String url = "https://snap-ci.com/hwEMz49fQYcu2gA_wLEMTE3lF53Xx5BMrxyCTm0heEk/cctray.xml";
        List<Project> expectedProjects = asList(new Project(), new Project());
        when(parser.fetch(url)).thenReturn(expectedProjects);

        List<Project> projects = downloadXmlTask.doInBackground(url);

        assertThat(projects, is(expectedProjects));
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
