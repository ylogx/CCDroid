package org.developfreedom.ccdroid.app;

import android.content.Context;
import android.util.Log;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class ProjectParser extends XmlFeedReader {
    private static String TAG = ProjectParser.class.getSimpleName();
    private Context context;
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

    // Given a URL, establishes an HttpUrlConnection and retrieves
    // the web page content as a InputStream, which it returns as
    // a string.
    private List downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        List contentAsString = null;

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
            contentAsString = parse(is);

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return contentAsString;
    }
}
