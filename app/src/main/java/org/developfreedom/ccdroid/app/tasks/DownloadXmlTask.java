package org.developfreedom.ccdroid.app.tasks;

import android.os.AsyncTask;
import org.developfreedom.ccdroid.app.OnDownloadTaskCompleted;
import org.developfreedom.ccdroid.app.Project;
import org.developfreedom.ccdroid.app.ProjectParser;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DownloadXmlTask extends AsyncTask<String, Void, List<Project>> {
    private OnDownloadTaskCompleted onDownloadTaskCompleted;
    private ProjectParser projectParser;

    public DownloadXmlTask(OnDownloadTaskCompleted onDownloadTaskCompleted, ProjectParser projectParser) {
        this.onDownloadTaskCompleted = onDownloadTaskCompleted;
        this.projectParser = projectParser;
    }

    @Override
    protected List<Project> doInBackground(String... urls) {
        ArrayList<Project> projects = new ArrayList<>();
        try {
            for (String url: urls) {
                List<Project> projectList = projectParser.fetch(new URL(url));
                projects.addAll(projectList);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return projects;
    }

    @Override
    protected void onPostExecute(List<Project> result) {
        onDownloadTaskCompleted.updateListView(result);
    }
}
