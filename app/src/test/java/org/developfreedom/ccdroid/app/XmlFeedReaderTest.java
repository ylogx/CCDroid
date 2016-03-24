package org.developfreedom.ccdroid.app;

import android.os.Build;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Shubham Chaudhary on 25/03/16
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class XmlFeedReaderTest {
    private static final String PROJECT_XML_2 = "<Projects>\n" +
            "  <Project name=\"DevelopFreedom/logmein-android (master) :: AndroidInstall\" activity=\"Sleeping\" lastBuildLabel=\"7\" lastBuildStatus=\"Success\" lastBuildTime=\"2015-07-27T21:08:18Z\" webUrl=\"https://snap-ci.com/DevelopFreedom/logmein-android/branch/master/logs/defaultPipeline/7/AndroidInstall\"/>\n" +
            "  <Project name=\"DevelopFreedom/logmein-android (master) :: TestRunnar\" activity=\"Sleeping\" lastBuildLabel=\"7\" lastBuildStatus=\"Failure\" lastBuildTime=\"2015-07-27T21:08:23Z\" webUrl=\"https://snap-ci.com/DevelopFreedom/logmein-android/branch/master/logs/defaultPipeline/7/TestRunnar\"/>\n" +
            "</Projects>\n";
    @Test public void parse() throws Exception {
        InputStream stream = new ByteArrayInputStream(PROJECT_XML_2.getBytes());

        List projects = new XmlFeedReader().parse(stream);

        assertThat(projects).hasSize(2).hasOnlyElementsOfType(Project.class);
        Project project = (Project) projects.get(0);
        assertThat(project).isNotNull();
        assertThat(project.getName()).isEqualTo("DevelopFreedom/logmein-android (master) :: AndroidInstall");
        assertThat(project.getActivity()).isEqualTo("Sleeping");
        assertThat(project.getLastBuildLabel()).isEqualTo("7");
        assertThat(project.getLastBuildStatus()).isEqualTo("Success");
        assertThat(project.getLastBuildTime()).isEqualTo("2015-07-27T21:08:18Z");
        assertThat(project.getWebUrl()).isEqualTo("https://snap-ci.com/DevelopFreedom/logmein-android/branch/master/logs/defaultPipeline/7/AndroidInstall");
    }
}
