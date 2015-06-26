/*
 * ProjectOpenHelper.java
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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static org.developfreedom.ccdroid.app.storage.ProjectContract.ProjectColumns.*;
import static org.developfreedom.ccdroid.app.utils.LogUtils.LOGI;
import static org.developfreedom.ccdroid.app.utils.LogUtils.LOGW;

public class ProjectOpenHelper extends SQLiteOpenHelper {
    public static final String CCDROID_DATABASE_NAME = "ccdroid";
    public static final int DATABASE_VERSION = 1;
    private static final String TAG = ProjectOpenHelper.class.getSimpleName();
    private static final String TYPE_TEXT = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String COMMAND_DROP_PROJECT_TABLE = "DROP TABLE IF EXISTS " + TABLE_PROJECTS;
    private static final String COMMAND_CREATE_PROJECTS_TABLE = "CREATE TABLE " + TABLE_PROJECTS + " (" +
            _ID + " INTEGER PRIMARY KEY," +
            KEY_PROJECT_NAME + TYPE_TEXT + COMMA_SEP +
            KEY_PROJECT_ACTIVITY + TYPE_TEXT + COMMA_SEP +
            KEY_PROJECT_LABEL + TYPE_TEXT + COMMA_SEP +
            KEY_PROJECT_STATUS + TYPE_TEXT + COMMA_SEP +
            KEY_PROJECT_TIME + TYPE_TEXT + COMMA_SEP +
            KEY_PROJECT_URL + TYPE_TEXT + ")";


    public ProjectOpenHelper(Context context) {
        super(context, CCDROID_DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        LOGI(TAG, "Creating projects table: " + COMMAND_CREATE_PROJECTS_TABLE);
        db.execSQL(COMMAND_CREATE_PROJECTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        LOGW(TAG, "Dropping projects table: " + COMMAND_CREATE_PROJECTS_TABLE);
        db.execSQL(COMMAND_DROP_PROJECT_TABLE);
        db.execSQL(COMMAND_CREATE_PROJECTS_TABLE);
    }
}
