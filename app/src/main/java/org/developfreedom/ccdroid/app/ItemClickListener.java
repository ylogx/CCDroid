package org.developfreedom.ccdroid.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Map;

public class ItemClickListener implements AdapterView.OnItemClickListener {
    static String TAG = ItemClickListener.class.getSimpleName();
    ListView projectsListView;
    Context context;
    Activity mainActivity;

    public ItemClickListener(ListView projectsListView, Context applicationContext, Activity mainActivity) {
        this.projectsListView = projectsListView;
        this.context = applicationContext;
        this.mainActivity = mainActivity;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        Log.v(TAG, "Listview item clicked");
        ListAdapter adapter = projectsListView.getAdapter();
        final Map<String, String> clickedItem = (Map<String, String>) adapter.getItem(position);
        AlertDialog.Builder alert = new AlertDialog.Builder(mainActivity);
        alert.setTitle("Details");
        alert.setMessage(getDetails(clickedItem));
        alert.setIcon(R.mipmap.ic_launcher);
        alert.setPositiveButton("Open", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.v(TAG, "Positive button clicked");
                String s = projectsListView.getItemAtPosition(position).toString();
                Toast.makeText(context, s, Toast.LENGTH_LONG).show();
                Toast.makeText(context, clickedItem.get("url"), Toast.LENGTH_LONG).show();
            }
        });
        alert.show();
    }

    private Spanned getDetails(Map<String, String> clickedItem) {
        String details = "";
        for (String key: clickedItem.keySet()) {
            details += "<b>";
            details += key.substring(0, 1).toUpperCase() + key.substring(1);
            details += ": ";
            details += "</b>";
            details += clickedItem.get(key);
            details += "<br/>";
        }
        Log.v(TAG, "Details: " + Html.fromHtml(details));
        return Html.fromHtml(details);
    }
}
