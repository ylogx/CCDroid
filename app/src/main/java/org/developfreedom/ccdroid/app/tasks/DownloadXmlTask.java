package org.developfreedom.ccdroid.app.tasks;

import android.os.AsyncTask;
import org.developfreedom.ccdroid.app.OnDownloadTaskCompleted;
import org.developfreedom.ccdroid.app.ProjectParser;

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
        return projectParser.fetch(projectUrl);
    }

    @Override
    protected void onPostExecute(List result) {
        onDownloadTaskCompleted.updateListView(result);
    }
}
