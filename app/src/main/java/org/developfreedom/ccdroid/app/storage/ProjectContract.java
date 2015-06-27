/*
 * ProjectContract.java
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
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by chaudhary on 6/26/15.
 */
public class ProjectContract {
    /**
     * Content provider authority.
     */
    public static final String CONTENT_AUTHORITY = "org.developfreedom.ccdroid.app";

    /**
     * Base URI. (content://com.example.android.network.sync.basicsyncadapter)
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Path component for "entry"-type resources..
     */
    private static final String PATH_PROJECTS = "projects";

    /**
     * Columns supported by "entries" records.
     */
    public static class ProjectColumns implements BaseColumns {
        /**
         * MIME type for lists of entries.
         */
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.projectsyncadapter.projects";
        /**
         * MIME type for individual entries.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.projectsyncadapter.project";

        /**
         * Fully qualified URI for "entry" resources.
         */
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PROJECTS).build();

        public static final String TABLE_PROJECTS = "projects";
        public static final String KEY_PROJECT_NAME = "name";
        public static final String KEY_PROJECT_ACTIVITY = "activity";
        public static final String KEY_PROJECT_LABEL = "lastBuildLabel";
        public static final String KEY_PROJECT_STATUS = "lastBuildStatus";
        public static final String KEY_PROJECT_TIME = "lastBuildTime";
        public static final String KEY_PROJECT_URL = "webUrl";
    }
}
