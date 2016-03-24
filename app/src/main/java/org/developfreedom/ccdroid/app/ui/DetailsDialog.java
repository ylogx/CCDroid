package org.developfreedom.ccdroid.app.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import org.developfreedom.ccdroid.app.Project;
import org.developfreedom.ccdroid.app.R;
import org.developfreedom.ccdroid.app.utils.Utils;
import org.developfreedom.ccdroid.app.views.BaseDialogFragment;

/**
 * @author Shubham Chaudhary on 25/03/16
 */
public class DetailsDialog extends BaseDialogFragment {
    private static final String KEY_PROJECT = "project_for_dialog";
    public static final String KEY_TAG = "DetailsDialog";
    @Bind(R.id.details_project_open) View open;
    @Bind(R.id.details_project_name) TextView name;
    @Bind(R.id.details_project_time) TextView time;
    @Bind(R.id.details_project_url) TextView url;
    @Bind(R.id.details_project_label) TextView label;
    @Bind(R.id.details_project_activity) TextView activity;
    private Project project;

    public static DetailsDialog newInstance(Project project) {
        if (project == null) throw new NullPointerException("Project can't be null");
        Bundle args = new Bundle(1);
        DetailsDialog fragment = new DetailsDialog();
        args.putSerializable(KEY_PROJECT, project);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.details_dialog, container, false);
        extractBundle(getArguments());
        ButterKnife.bind(this, rootView);
        name.setText(project.getName());
        time.setText(project.getLastBuildTime());
        url.setText(project.getWebUrl());
        label.setText(project.getLastBuildLabel());
        activity.setText(project.getActivity());
        open.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Utils.openUrl(project.getWebUrl(), v.getContext());
            }
        });
        return rootView;
    }

    private void extractBundle(Bundle arguments) {
        if ((arguments == null) || !arguments.containsKey(KEY_PROJECT)) {
            throw new IllegalArgumentException("Must pass project in bundle. Use newInstance method.");
        }
        project = (Project) arguments.getSerializable(KEY_PROJECT);
    }
}
