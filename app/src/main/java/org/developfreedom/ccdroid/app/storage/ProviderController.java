/*
 * StorageController.java
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

package org.developfreedom.ccdroid.app.storage;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import org.developfreedom.ccdroid.app.Project;
import org.developfreedom.ccdroid.app.controllers.ProjectStorageController;

import java.util.ArrayList;
import java.util.List;

import static org.developfreedom.ccdroid.app.utils.LogUtils.*;

public class ProviderController implements ProjectStorageController {
    private static final String TAG = ProviderController.class.getSimpleName();
    private ContentResolver mResolver;
    public static final String[] PROJECT_PROJECTIONS = new String[]{
            ProjectContract.ProjectColumns._ID,
            ProjectContract.ProjectColumns.KEY_PROJECT_NAME,
            ProjectContract.ProjectColumns.KEY_PROJECT_ACTIVITY,
            ProjectContract.ProjectColumns.KEY_PROJECT_LABEL,
            ProjectContract.ProjectColumns.KEY_PROJECT_STATUS,
            ProjectContract.ProjectColumns.KEY_PROJECT_TIME,
            ProjectContract.ProjectColumns.KEY_PROJECT_URL,
    };
    public static final int PROJECTION_ID_INDEX = 0;
    public static final int PROJECTION_NAME_INDEX = 1;
    public static final int PROJECTION_ACTIVITY_INDEX = 2;
    public static final int PROJECTION_LABEL_INDEX = 3;
    public static final int PROJECTION_STATUS_INDEX = 4;
    public static final int PROJECTION_TIME_INDEX = 5;
    public static final int PROJECTION_URL_INDEX = 6;

    public ProviderController(ContentResolver resolver) {
        this.mResolver = resolver;
    }

    @Override
    public void add(List<Project> projects) {
        for (Project project: projects) {
            ContentValues values = new ContentValues();
            values.put(ProjectContract.ProjectColumns.KEY_PROJECT_NAME, project.getName());
            values.put(ProjectContract.ProjectColumns.KEY_PROJECT_ACTIVITY, project.getActivity());
            values.put(ProjectContract.ProjectColumns.KEY_PROJECT_LABEL, project.getLastBuildLabel());
            values.put(ProjectContract.ProjectColumns.KEY_PROJECT_STATUS, project.getLastBuildStatus());
            values.put(ProjectContract.ProjectColumns.KEY_PROJECT_TIME, project.getLastBuildTime());
            values.put(ProjectContract.ProjectColumns.KEY_PROJECT_URL, project.getWebUrl());
            mResolver.insert(ProjectContract.ProjectColumns.CONTENT_URI, values);
        }
    }

    @Override
    public List<Project> get() {
        Cursor c = mResolver.query(ProjectContract.ProjectColumns.CONTENT_URI,
                PROJECT_PROJECTIONS, null, null, null);
        if (c == null) LOGW(TAG, "Cursor is null!");
        List<Project> projects = new ArrayList<>();
        while (c != null && c.moveToNext()) {
            String name = c.getString(PROJECTION_NAME_INDEX);
            String activity = c.getString(PROJECTION_ACTIVITY_INDEX);
            String label = c.getString(PROJECTION_LABEL_INDEX);
            String status = c.getString(PROJECTION_STATUS_INDEX);
            String time = c.getString(PROJECTION_TIME_INDEX);
            String url = c.getString(PROJECTION_URL_INDEX);
            Project project = new Project(name, activity, label, status, time, url);
            projects.add(project);
            LOGV(TAG, "Project Queried: " + project.toString());
        }
        return projects;
    }

    @Override
    public void clear() {
        LOGD(TAG, "Clearing database");
        int delete = mResolver.delete(ProjectContract.ProjectColumns.CONTENT_URI, null, null);
        LOGI(TAG, "Deleted " + delete + " values");
    }
}
