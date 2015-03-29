package org.developfreedom.ccdroid.app;

import android.util.Log;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class ProjectParser extends XmlFeedReader {
    private static String TAG = ProjectParser.class.getSimpleName();
    private String url;

    public ProjectParser(String projectUrl) {
        this.url = projectUrl;
    }

    public List<Project> fetch() {
        try {
            return downloadUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        List projectList = null;

        try {
            Log.d(TAG, "Parsing " + myurl);
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d(TAG, "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            Log.d(TAG, "InputStream has " + is.available() + " available bytes");
            projectList = parse(is);
            conn.disconnect();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return projectList;
    }
}
