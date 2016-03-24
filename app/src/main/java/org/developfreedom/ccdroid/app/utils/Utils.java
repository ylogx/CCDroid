/*
 * Utils.java
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

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;

public class Utils {
    public static void openUrl(String url, Context context) {
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }
        Intent browserIntent = new Intent(
                Intent.ACTION_VIEW,
                Uri.parse(url)
        );
        context.startActivity(browserIntent);
    }


    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return (cm.getActiveNetworkInfo() != null) && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}
