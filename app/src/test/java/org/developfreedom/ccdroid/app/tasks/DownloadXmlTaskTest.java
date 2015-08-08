package org.developfreedom.ccdroid.app.tasks;

import android.os.Build;
import org.developfreedom.ccdroid.app.BuildConfig;
import org.developfreedom.ccdroid.app.Project;
import org.developfreedom.ccdroid.app.ProjectParser;
import org.developfreedom.ccdroid.app.controllers.ListViewController;
import org.developfreedom.ccdroid.app.controllers.ProjectStorageController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.net.URL;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class DownloadXmlTaskTest {
    private ListViewController listViewController;
    private DownloadXmlTask downloadXmlTask;
    private ProjectParser parser;
    private ProjectStorageController projectStorageController;

    @Before
    public void setUp() throws Exception {
        listViewController = mock(ListViewController.class);
        projectStorageController = mock(ProjectStorageController.class);
        parser = mock(ProjectParser.class);
        downloadXmlTask = new DownloadXmlTask(parser, listViewController, projectStorageController);
    }

    @Test
    public void testThatProjectListIsFetchedInBackgroundThread() throws Exception {
        String url = "https://snap-ci.com/hwEMz49fQYcu2gA_wLEMTE3lF53Xx5BMrxyCTm0heEk/cctray.xml";
        List<Project> expectedProjects = asList(new Project(), new Project());
        when(parser.fetch(new URL(url))).thenReturn(expectedProjects);

        List<Project> projects = downloadXmlTask.doInBackground(url);

        assertThat(projects, is(expectedProjects));
    }

    @Test
    public void testThatProjectListIsStoredInLocalStorageInBackgroundThread() throws Exception {
        String url = "https://snap-ci.com/hwEMz49fQYcu2gA_wLEMTE3lF53Xx5BMrxyCTm0heEk/cctray.xml";
        List<Project> expectedProjects = asList(new Project(), new Project());
        when(parser.fetch(new URL(url))).thenReturn(expectedProjects);

        downloadXmlTask.doInBackground(url);

        verify(projectStorageController).add(expectedProjects);
    }

    @Test
    public void testThatListUpdateIsDonePostExecution() throws Exception {
        Project project1 = new Project();
        Project project2 = new Project();
        List<Project> projects = asList(project1, project2);

        downloadXmlTask.onPostExecute(projects);

        verify(listViewController).updateListView(projects);
    }
}
