/*
 * SyncAdapter.java
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

package org.developfreedom.ccdroid.app.sync;

import android.accounts.Account;
import android.annotation.TargetApi;
import android.content.*;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import org.developfreedom.ccdroid.app.Config;
import org.developfreedom.ccdroid.app.Project;
import org.developfreedom.ccdroid.app.ProjectParser;
import org.developfreedom.ccdroid.app.controllers.ListViewController;
import org.developfreedom.ccdroid.app.storage.ProjectContract;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.List;

import static org.developfreedom.ccdroid.app.utils.LogUtils.LOGD;
import static org.developfreedom.ccdroid.app.utils.LogUtils.LOGI;

/**
 * Define a sync adapter for the app.
 * <p/>
 * <p>This class is instantiated in {@link SyncService}, which also binds SyncAdapter to the system.
 * SyncAdapter should only be initialized in SyncService, never anywhere else.
 * <p/>
 * <p>The system calls onPerformSync() via an RPC call through the IBinder object supplied by
 * SyncService.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {
    // Constants representing column positions from PROJECTION.
    public static final int COLUMN_ID = 0;
    public static final int COLUMN_PROJECT_ID = 1;
    public static final int COLUMN_NAME = 2;
    public static final int COLUMN_ACTIVITY = 3;
    public static final int COLUMN_LABEL = 4;
    public static final int COLUMN_STATUS = 5;
    public static final int COLUMN_TIME = 6;
    public static final int COLUMN_URL = 7;
    private static final String TAG = SyncAdapter.class.getSimpleName();
    /**
     * URL to fetch content from during a sync.
     * <p/>
     * <p>This points to the Android Developers Blog. (Side note: We highly recommend reading the
     * Android Developer Blog to stay up to date on the latest Android platform developments!)
     */
    private static final String FEED_URL = "http://android-developers.blogspot.com/atom.xml";
    /**
     * Network connection timeout, in milliseconds.
     */
    private static final int NET_CONNECT_TIMEOUT_MILLIS = 15000;  // 15 seconds
    /**
     * Network read timeout, in milliseconds.
     */
    private static final int NET_READ_TIMEOUT_MILLIS = 10000;  // 10 seconds
    /**
     * Project used when querying content provider. Returns all known fields.
     */
    private static final String[] PROJECTION = new String[]{
            ProjectContract.ProjectColumns._ID,
            ProjectContract.ProjectColumns.KEY_PROJECT_NAME,
            ProjectContract.ProjectColumns.KEY_PROJECT_ACTIVITY,
            ProjectContract.ProjectColumns.KEY_PROJECT_LABEL,
            ProjectContract.ProjectColumns.KEY_PROJECT_STATUS,
            ProjectContract.ProjectColumns.KEY_PROJECT_TIME,
            ProjectContract.ProjectColumns.KEY_PROJECT_URL};
    /**
     * Content resolver, for performing database operations.
     */
    private final ContentResolver mContentResolver;
    private ListViewController mController;

    /**
     * Constructor. Obtains handle to content resolver for later use.
     */
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContentResolver = context.getContentResolver();
    }

    public SyncAdapter(Context context, boolean autoInitialize, ListViewController controller) {
        super(context, autoInitialize);
        this.mController = controller;
        mContentResolver = context.getContentResolver();
    }

    /**
     * Constructor. Obtains handle to content resolver for later use.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        mContentResolver = context.getContentResolver();
    }

    /**
     * Called by the Android system in response to a request to run the sync adapter. The work
     * required to read data from the network, parse it, and store it in the content provider is
     * done here. Extending AbstractThreadedSyncAdapter ensures that all methods within SyncAdapter
     * run on a background thread. For this reason, blocking I/O and other long-running tasks can be
     * run <em>in situ</em>, and you don't have to set up a separate thread for them.
     * .
     * <p/>
     * <p>This is where we actually perform any work required to perform a sync.
     * {@link AbstractThreadedSyncAdapter} guarantees that this will be called on a non-UI thread,
     * so it is safe to peform blocking I/O here.
     * <p/>
     * <p>The syncResult argument allows you to pass information back to the method that triggered
     * the sync.
     */
    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
                              ContentProviderClient provider, SyncResult syncResult) {
        LOGI(TAG, "Beginning network synchronization");
        try {
            List<Project> projects = new ProjectParser().fetch(new URL(new Config(getContext()).getUrl()));
            mController.updateListView(projects);
        } catch (MalformedURLException e) {
            Log.wtf(TAG, "Feed URL is malformed", e);
            syncResult.stats.numParseExceptions++;
            return;
        } catch (IOException e) {
            Log.e(TAG, "Error reading from network: " + e.toString());
            syncResult.stats.numIoExceptions++;
            return;
        }
        LOGI(TAG, "Network synchronization complete");
    }

    /**
     * Read XML from an input stream, storing it into the content provider.
     * <p/>
     * <p>This is where incoming data is persisted, committing the results of a sync. In order to
     * minimize (expensive) disk operations, we compare incoming data with what's already in our
     * database, and compute a merge. Only changes (insert/update/delete) will result in a database
     * write.
     * <p/>
     * <p>As an additional optimization, we use a batch operation to perform all database writes at
     * once.
     * <p/>
     * <p>Merge strategy:
     * 1. Get cursor to all items in feed<br/>
     * 2. For each item, check if it's in the incoming data.<br/>
     * a. YES: Remove from "incoming" list. Check if data has mutated, if so, perform
     * database UPDATE.<br/>
     * b. NO: Schedule DELETE from database.<br/>
     * (At this point, incoming database only contains missing items.)<br/>
     * 3. For any items remaining in incoming list, ADD to database.
     */
    public void updateLocalFeedData(final InputStream stream, final SyncResult syncResult)
            throws IOException, XmlPullParserException, RemoteException,
            OperationApplicationException, ParseException {
        LOGD(TAG, "Update local feed called");
        /*
        final FeedParser feedParser = new FeedParser();
        final ContentResolver contentResolver = getContext().getContentResolver();

        Log.i(TAG, "Parsing stream as Atom feed");
        final List<FeedParser.Entry> entries = feedParser.parse(stream);
        Log.i(TAG, "Parsing complete. Found " + entries.size() + " entries");


        ArrayList<ContentProviderOperation> batch = new ArrayList<ContentProviderOperation>();

        // Build hash table of incoming entries
        HashMap<String, FeedParser.Entry> entryMap = new HashMap<String, FeedParser.Entry>();
        for (FeedParser.Entry e : entries) {
            entryMap.put(e.id, e);
        }

        // Get list of all items
        Log.i(TAG, "Fetching local entries for merge");
        Uri uri = ProjectContract.Entry.CONTENT_URI; // Get all entries
        Cursor c = contentResolver.query(uri, PROJECTION, null, null, null);
        assert c != null;
        Log.i(TAG, "Found " + c.getCount() + " local entries. Computing merge solution...");

        // Find stale data
        int id;
        String entryId;
        String title;
        String link;
        long published;
        while (c.moveToNext()) {
            syncResult.stats.numEntries++;
            id = c.getInt(COLUMN_ID);
            entryId = c.getString(COLUMN_ENTRY_ID);
            title = c.getString(COLUMN_TITLE);
            link = c.getString(COLUMN_LINK);
            published = c.getLong(COLUMN_PUBLISHED);
            FeedParser.Entry match = entryMap.get(entryId);
            if (match != null) {
                // Entry exists. Remove from entry map to prevent insert later.
                entryMap.remove(entryId);
                // Check to see if the entry needs to be updated
                Uri existingUri = ProjectContract.Entry.CONTENT_URI.buildUpon()
                        .appendPath(Integer.toString(id)).build();
                if ((match.title != null && !match.title.equals(title)) ||
                        (match.link != null && !match.link.equals(link)) ||
                        (match.published != published)) {
                    // Update existing record
                    Log.i(TAG, "Scheduling update: " + existingUri);
                    batch.add(ContentProviderOperation.newUpdate(existingUri)
                            .withValue(ProjectContract.Entry.COLUMN_NAME_TITLE, title)
                            .withValue(ProjectContract.Entry.COLUMN_NAME_LINK, link)
                            .withValue(ProjectContract.Entry.COLUMN_NAME_PUBLISHED, published)
                            .build());
                    syncResult.stats.numUpdates++;
                } else {
                    Log.i(TAG, "No action: " + existingUri);
                }
            } else {
                // Entry doesn't exist. Remove it from the database.
                Uri deleteUri = ProjectContract.Entry.CONTENT_URI.buildUpon()
                        .appendPath(Integer.toString(id)).build();
                Log.i(TAG, "Scheduling delete: " + deleteUri);
                batch.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
            }
        }
        c.close();

        // Add new items
        for (FeedParser.Entry e : entryMap.values()) {
            Log.i(TAG, "Scheduling insert: entry_id=" + e.id);
            batch.add(ContentProviderOperation.newInsert(ProjectContract.Entry.CONTENT_URI)
                    .withValue(ProjectContract.Entry.COLUMN_NAME_ENTRY_ID, e.id)
                    .withValue(ProjectContract.Entry.COLUMN_NAME_TITLE, e.title)
                    .withValue(ProjectContract.Entry.COLUMN_NAME_LINK, e.link)
                    .withValue(ProjectContract.Entry.COLUMN_NAME_PUBLISHED, e.published)
                    .build());
            syncResult.stats.numInserts++;
        }
        Log.i(TAG, "Merge solution ready. Applying batch update");
        mContentResolver.applyBatch(ProjectContract.CONTENT_AUTHORITY, batch);
        mContentResolver.notifyChange(
                ProjectContract.ProjectColumns.CONTENT_URI, // URI where data was modified
                null,                           // No local observer
                false);                         // IMPORTANT: Do not sync to network
        // This sample doesn't support uploads, but if *your* code does, make sure you set
        // syncToNetwork=false in the line above to prevent duplicate syncs.
        */
    }

}
