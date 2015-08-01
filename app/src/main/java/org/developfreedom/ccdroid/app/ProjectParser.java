package org.developfreedom.ccdroid.app;

import com.crashlytics.android.Crashlytics;
import org.developfreedom.ccdroid.app.utils.LogUtils;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import static org.developfreedom.ccdroid.app.utils.LogUtils.LOGD;
import static org.developfreedom.ccdroid.app.utils.LogUtils.LOGI;

public class ProjectParser {
    private static String TAG = LogUtils.makeLogTag(ProjectParser.class);
    private final XmlFeedReader xmlFeedReader;

    public ProjectParser() {
        this.xmlFeedReader = new XmlFeedReader();
    }

    public List<Project> fetch(URL url) throws IOException {
        InputStream is = null;
        List projectList = null;

        try {
            LOGI(TAG, "Parsing " + url.toString());
            HttpURLConnection conn = openHttpGetConnection(url);
            // Convert the InputStream into a string
            is = conn.getInputStream();
            LOGD(TAG, "InputStream has " + is.available() + " available bytes");
            projectList = xmlFeedReader.parse(is);
            conn.disconnect();
        } catch (XmlPullParserException e) {
            Crashlytics.logException(e);
            e.printStackTrace();
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return projectList;
    }

    private HttpURLConnection openHttpGetConnection(URL url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Starts the query
        conn.connect();
        int response = conn.getResponseCode();
        LOGD(TAG, "The response is: " + response);
        return conn;
    }

}
