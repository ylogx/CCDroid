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
    private static final String PROJECT_XML_1_WITH_SKIP = "<Projects>\n" +
            "  <SkipableTag foo=\"bar\" baz=\"booze\"/>\n" +
            "  <Project name=\"DevelopFreedom/logmein-android (master) :: AndroidInstall\" activity=\"Sleeping\" lastBuildLabel=\"7\" lastBuildStatus=\"Success\" lastBuildTime=\"2015-07-27T21:08:18Z\" webUrl=\"https://snap-ci.com/DevelopFreedom/logmein-android/branch/master/logs/defaultPipeline/7/AndroidInstall\"/>\n" +
            "</Projects>\n";

    @Test public void parse_HasTwoProject_ConvertToPOJO() throws Exception {
        InputStream stream = new ByteArrayInputStream(PROJECT_XML_2.getBytes());

        List projects = new XmlFeedReader().parse(stream);

        assertThat(projects).hasSize(2).hasOnlyElementsOfType(Project.class);
        assertThatProjectParsedCorrectly((Project) projects.get(0));
    }

    @Test public void parse_HasSkipableTag_SkipOver() throws Exception {
        InputStream stream = new ByteArrayInputStream(PROJECT_XML_1_WITH_SKIP.getBytes());

        List projects = new XmlFeedReader().parse(stream);

        assertThat(projects).hasSize(1).hasOnlyElementsOfType(Project.class);
        assertThatProjectParsedCorrectly((Project) projects.get(0));
    }

    private void assertThatProjectParsedCorrectly(Project project) {
        assertThat(project).isNotNull();
        assertThat(project.getName()).isEqualTo("DevelopFreedom/logmein-android (master) :: AndroidInstall");
        assertThat(project.getActivity()).isEqualTo("Sleeping");
        assertThat(project.getLastBuildLabel()).isEqualTo("7");
        assertThat(project.getLastBuildStatus()).isEqualTo("Success");
        assertThat(project.getLastBuildTime()).isEqualTo("2015-07-27T21:08:18Z");
        assertThat(project.getWebUrl()).isEqualTo("https://snap-ci.com/DevelopFreedom/logmein-android/branch/master/logs/defaultPipeline/7/AndroidInstall");
    }
}
