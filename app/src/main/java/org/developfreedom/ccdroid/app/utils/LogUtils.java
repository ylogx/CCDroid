/*
 * LogUtils.java
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

package org.developfreedom.ccdroid.app.utils;

import android.util.Log;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

public class LogUtils {
    private static final String LOG_PREFIX = "cc_";
    private static final int LOG_PREFIX_LENGTH = LOG_PREFIX.length();
    private static final int MAX_LOG_TAG_LENGTH = 23;

    public static String makeLogTag(String str) {
        if (str.length() > MAX_LOG_TAG_LENGTH - LOG_PREFIX_LENGTH) {
            return LOG_PREFIX + str.substring(0, MAX_LOG_TAG_LENGTH - LOG_PREFIX_LENGTH - 1);
        }

        return LOG_PREFIX + str;
    }

    /**
     * Don't use this when obfuscating class names!
     */
    public static String makeLogTag(Class cls) {
        return makeLogTag(cls.getSimpleName());
    }

    public static void LOGD(final String tag, String message) {
        if (Fabric.isInitialized())
            Crashlytics.getInstance().core.log(Log.DEBUG, tag, message);
        else
            Log.d(tag, message);
    }

    public static void LOGD(final String tag, String message, Throwable cause) {
        if (Fabric.isInitialized())
            Crashlytics.getInstance().core.log(message);
        Log.d(tag, message, cause);
    }

    public static void LOGV(final String tag, String message) {
        if (Fabric.isInitialized())
            Crashlytics.getInstance().core.log(Log.VERBOSE, tag, message);
        else
            Log.v(tag, message);
    }

    public static void LOGV(final String tag, String message, Throwable cause) {
        if (Fabric.isInitialized())
            Crashlytics.getInstance().core.log(message);
        Log.v(tag, message, cause);
    }

    public static void LOGI(final String tag, String message) {
        if (Fabric.isInitialized())
            Crashlytics.getInstance().core.log(Log.INFO, tag, message);
        else
            Log.i(tag, message);
    }

    public static void LOGI(final String tag, String message, Throwable cause) {
        if (Fabric.isInitialized())
            Crashlytics.getInstance().core.log(message);
        Log.i(tag, message, cause);
    }

    public static void LOGW(final String tag, String message) {
        if (Fabric.isInitialized())
            Crashlytics.getInstance().core.log(Log.WARN, tag, message);
        else
            Log.w(tag, message);
    }

    public static void LOGW(final String tag, String message, Throwable cause) {
        if (Fabric.isInitialized())
            Crashlytics.getInstance().core.log(message);
        Log.w(tag, message, cause);
    }

    public static void LOGE(final String tag, String message) {
        if (Fabric.isInitialized())
            Crashlytics.getInstance().core.log(Log.ERROR, tag, message);
        else
            Log.e(tag, message);
    }

    public static void LOGE(final String tag, String message, Throwable cause) {
        if (Fabric.isInitialized())
            Crashlytics.getInstance().core.log(message);
        Log.e(tag, message, cause);
    }

    private LogUtils() {
    }
}
