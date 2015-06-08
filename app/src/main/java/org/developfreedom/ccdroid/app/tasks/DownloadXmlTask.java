package org.developfreedom.ccdroid.app.tasks;

import android.os.AsyncTask;
import org.developfreedom.ccdroid.app.OnDownloadTaskCompleted;
import org.developfreedom.ccdroid.app.ProjectParser;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class DownloadXmlTask extends AsyncTask<String, Void, List> {
    private OnDownloadTaskCompleted onDownloadTaskCompleted;
    private ProjectParser projectParser;

    public DownloadXmlTask(OnDownloadTaskCompleted onDownloadTaskCompleted, ProjectParser projectParser) {
        this.onDownloadTaskCompleted = onDownloadTaskCompleted;
        this.projectParser = projectParser;
    }

    @Override
    protected List doInBackground(String... urls) {
        String projectUrl = urls[0];
        try {
            return projectParser.fetch(new URL(projectUrl));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(List result) {
        onDownloadTaskCompleted.updateListView(result);
    }
}
