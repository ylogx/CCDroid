package org.developfreedom.ccdroid.app;

import android.os.AsyncTask;

import java.util.List;

public class DownloadXmlTask extends AsyncTask<String, Void, List> {
    private OnDownloadTaskCompleted onDownloadTaskCompleted;

    public DownloadXmlTask(OnDownloadTaskCompleted onDownloadTaskCompleted) {
        this.onDownloadTaskCompleted = onDownloadTaskCompleted;
    }

    @Override
    protected List doInBackground(String... urls) {
        String projectUrl = urls[0];
        ProjectParser projectParser = new ProjectParser(projectUrl);
        return projectParser.fetch();
    }

    @Override
    protected void onPostExecute(List result) {
        onDownloadTaskCompleted.updateListView(result);
    }
}
