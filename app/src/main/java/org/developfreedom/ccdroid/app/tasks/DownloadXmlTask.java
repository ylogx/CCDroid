/*
 * DownloadXmlTask.java
 *
 * Copyright (c) 2015 Shubham Chaudhary <me@shubhamchaudhary.in>
 *
 * CCDroid is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CCDroid is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CCDroid.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.developfreedom.ccdroid.app.tasks;

import android.os.AsyncTask;
import org.developfreedom.ccdroid.app.Project;
import org.developfreedom.ccdroid.app.ProjectParser;
import org.developfreedom.ccdroid.app.controllers.ListViewController;
import org.developfreedom.ccdroid.app.controllers.ProjectStorageController;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DownloadXmlTask extends AsyncTask<String, Void, List<Project>> {
    private ListViewController listViewController;
    private ProjectParser projectParser;
    private ProjectStorageController mProjectStorageController;

    public DownloadXmlTask(ProjectParser projectParser, ListViewController listViewController, ProjectStorageController
            projectStorageController) {
        this.listViewController = listViewController;
        this.projectParser = projectParser;
        this.mProjectStorageController = projectStorageController;
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
        mProjectStorageController.clear();
        mProjectStorageController.add(projects);
        return projects;
    }

    @Override
    protected void onPostExecute(List<Project> result) {
        listViewController.updateListView(result);
    }
}
