/*
 * ProjectProvider.java
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

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import org.developfreedom.ccdroid.app.utils.SelectionBuilder;

import static org.developfreedom.ccdroid.app.utils.LogUtils.LOGV;

public class ProjectProvider extends ContentProvider {
    public static final String TAG = ProjectProvider.class.getSimpleName();
    /**
     * URI ID for route: /projects
     */
    public static final int ROUTE_PROJECTS = 1;
    /**
     * URI ID for route: /projects/{ID}
     */
    public static final int ROUTE_PROJECTS_ID = 2;

    // The constants below represent individual URI routes, as IDs. Every URI pattern recognized by
    // this ContentProvider is defined using sUriMatcher.addURI(), and associated with one of these
    // IDs.
    //
    // When a incoming URI is run through sUriMatcher, it will be tested against the defined
    // URI patterns, and the corresponding route ID will be returned.
    /**
     * Content authority for this provider.
     */
    private static final String AUTHORITY = ProjectContract.CONTENT_AUTHORITY;
    /**
     * UriMatcher, used to decode incoming URIs.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(AUTHORITY, "projects", ROUTE_PROJECTS);
        sUriMatcher.addURI(AUTHORITY, "projects/*", ROUTE_PROJECTS_ID);
    }

    ProjectOpenHelper mDatabaseHelper;

    @Override
    public boolean onCreate() {
        mDatabaseHelper = new ProjectOpenHelper(getContext());
        return true;
    }

    /**
     * Determine the mime type for entries returned by a given URI.
     */
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ROUTE_PROJECTS:
                return ProjectContract.ProjectColumns.CONTENT_TYPE;
            case ROUTE_PROJECTS_ID:
                return ProjectContract.ProjectColumns.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    /**
     * Perform a database query by URI.
     * <p/>
     * <p>Currently supports returning all entries (/entries) and individual entries by ID
     * (/entries/{ID}).
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        LOGV(TAG, "Query: " + selection + selectionArgs);
        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        SelectionBuilder builder = new SelectionBuilder();
        int uriMatch = sUriMatcher.match(uri);
        switch (uriMatch) {
            case ROUTE_PROJECTS_ID:
                // Return a single entry, by ID.
                String id = uri.getLastPathSegment();
                builder.where(ProjectContract.ProjectColumns._ID + "=?", id);
            case ROUTE_PROJECTS:
                // Return all known entries.
                builder.table(ProjectContract.ProjectColumns.TABLE_PROJECTS)
                        .where(selection, selectionArgs);
                Cursor c = builder.query(db, projection, sortOrder);
                // Note: Notification URI must be manually set here for loaders to correctly
                // register ContentObservers.
                Context ctx = getContext();
                assert ctx != null;
                c.setNotificationUri(ctx.getContentResolver(), uri);
                return c;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    /**
     * Insert a new entry into the database.
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        LOGV(TAG, "Insert: " + ((values == null) ? 0 : values.size()) + " values into the db");
        LOGV(TAG, "Insert: " + values.toString());
        final SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        assert db != null;
        final int match = sUriMatcher.match(uri);
        Uri result;
        switch (match) {
            case ROUTE_PROJECTS:
                long id = db.insertOrThrow(ProjectContract.ProjectColumns.TABLE_PROJECTS, null, values);
                result = Uri.parse(ProjectContract.ProjectColumns.CONTENT_URI + "/" + id);
                break;
            case ROUTE_PROJECTS_ID:
                throw new UnsupportedOperationException("Insert not supported on URI: " + uri);
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Send broadcast to registered ContentObservers, to refresh UI.
        Context ctx = getContext();
        assert ctx != null;
        ctx.getContentResolver().notifyChange(uri, null, false);
        return result;
    }

    /**
     * Delete an entry by database by URI.
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        LOGV(TAG, "Delete: " + selection + selectionArgs);
        SelectionBuilder builder = new SelectionBuilder();
        final SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int count;
        switch (match) {
            case ROUTE_PROJECTS:
                count = builder.table(ProjectContract.ProjectColumns.TABLE_PROJECTS)
                        .where(selection, selectionArgs)
                        .delete(db);
                break;
            case ROUTE_PROJECTS_ID:
                String id = uri.getLastPathSegment();
                count = builder.table(ProjectContract.ProjectColumns.TABLE_PROJECTS)
                        .where(ProjectContract.ProjectColumns._ID + "=?", id)
                        .where(selection, selectionArgs)
                        .delete(db);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Send broadcast to registered ContentObservers, to refresh UI.
        Context ctx = getContext();
        assert ctx != null;
        ctx.getContentResolver().notifyChange(uri, null, false);
        return count;
    }

    /**
     * Update an etry in the database by URI.
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        LOGV(TAG, "Update: " + selection + selectionArgs.toString());
        SelectionBuilder builder = new SelectionBuilder();
        final SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int count;
        switch (match) {
            case ROUTE_PROJECTS:
                count = builder.table(ProjectContract.ProjectColumns.TABLE_PROJECTS)
                        .where(selection, selectionArgs)
                        .update(db, values);
                break;
            case ROUTE_PROJECTS_ID:
                String id = uri.getLastPathSegment();
                count = builder.table(ProjectContract.ProjectColumns.TABLE_PROJECTS)
                        .where(ProjectContract.ProjectColumns._ID + "=?", id)
                        .where(selection, selectionArgs)
                        .update(db, values);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        Context ctx = getContext();
        assert ctx != null;
        ctx.getContentResolver().notifyChange(uri, null, false);
        return count;
    }

}
