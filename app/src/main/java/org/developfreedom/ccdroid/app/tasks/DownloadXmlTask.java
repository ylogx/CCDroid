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

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import org.developfreedom.ccdroid.app.Project;
import org.developfreedom.ccdroid.app.ProjectParser;
import org.developfreedom.ccdroid.app.controllers.ListViewController;
import org.developfreedom.ccdroid.app.storage.ProjectContract.ProjectColumns;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DownloadXmlTask extends AsyncTask<String, Void, List<Project>> {
    private ListViewController listViewController;
    private ProjectParser projectParser;
    private Context mContext;

    public DownloadXmlTask(ListViewController listViewController, ProjectParser projectParser, Context context) {
        this.listViewController = listViewController;
        this.projectParser = projectParser;
        mContext = context;
    }

    @Override
    protected List<Project> doInBackground(String... urls) {
        ArrayList<Project> projects = new ArrayList<>();
        try {
            for (String url: urls) {
                List<Project> projectList = projectParser.fetch(new URL(url));
                projects.addAll(projectList);
                addToDatabase(projectList);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return projects;
    }

    private void addToDatabase(List<Project> projectList) {
        for (Project project: projectList) {
            ContentResolver resolver = mContext.getContentResolver();
            ContentValues values = new ContentValues();
            values.put(ProjectColumns.KEY_PROJECT_NAME, project.getName());
            values.put(ProjectColumns.KEY_PROJECT_ACTIVITY, project.getActivity());
            values.put(ProjectColumns.KEY_PROJECT_LABEL, project.getLastBuildLabel());
            values.put(ProjectColumns.KEY_PROJECT_STATUS, project.getLastBuildStatus());
            values.put(ProjectColumns.KEY_PROJECT_TIME, project.getLastBuildTime());
            values.put(ProjectColumns.KEY_PROJECT_URL, project.getWebUrl());
            resolver.insert(ProjectColumns.CONTENT_URI, values);
        }
    }

    @Override
    protected void onPostExecute(List<Project> result) {
        listViewController.updateListView(result);
    }
}
