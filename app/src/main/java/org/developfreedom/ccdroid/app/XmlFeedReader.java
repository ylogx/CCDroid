package org.developfreedom.ccdroid.app;

import android.util.Xml;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class XmlFeedReader {
    private static final String ns = null;

    public List parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    private List readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List projects = new ArrayList();

//        parser.require(XmlPullParser.START_TAG, ns, "projects");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("Project")) {
                projects.add(readProject(parser));
            } else {
                skip(parser);
            }
        }
        return projects;
    }

    private Project readProject(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "Project");
        String name = parser.getAttributeValue(null, "name");
        String activity = parser.getAttributeValue(null, "activity");
        String lastBuildLabel = parser.getAttributeValue(null, "lastBuildLabel");
        String lastBuildStatus = parser.getAttributeValue(null, "lastBuildStatus");
        String lastBuildTime = parser.getAttributeValue(null, "lastBuildTime");
        String webUrl = parser.getAttributeValue(null, "webUrl");
        parser.nextTag();
        parser.require(XmlPullParser.END_TAG, ns, "Project");
        return new Project(name, activity, lastBuildLabel, lastBuildStatus, lastBuildTime, webUrl);
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
