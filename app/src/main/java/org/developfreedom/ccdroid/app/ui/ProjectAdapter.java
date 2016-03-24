package org.developfreedom.ccdroid.app.ui;

import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import org.developfreedom.ccdroid.app.Project;
import org.developfreedom.ccdroid.app.R;

import java.util.List;

/**
 * @author Shubham Chaudhary on 8/9/15.
 */
public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ViewHolder> {
    public static final String STATUS_SUCCESS = "Success";
    public static final String STATUS_FAILURE = "Failure";
    public static final String STATUS_UNKNOWN = "Unknown";
    public static final String ACTIVITY_BUILDING = "Building";
    private static final int NORMAL_VIEW_TYPE = 0;
    private final List<Project> projects;
    private final FragmentManager fragmentManager;

    public ProjectAdapter(List<Project> projects, FragmentManager fragmentManager) {
        this.projects = projects;
        this.fragmentManager = fragmentManager;
    }

    @Override public int getItemViewType(int position) {
        return NORMAL_VIEW_TYPE;
    }

    @Nullable @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.list_row_layout_project, parent, false);
        return (viewType == NORMAL_VIEW_TYPE) ? new ViewHolder(v) : null;
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        final Project project = projects.get(position);
        holder.activity.setText(project.getActivity());
        holder.status.setTag(getDrawableId(project.getLastBuildStatus(), project.getActivity()));
        holder.name.setText(project.getName());
        holder.time.setText(project.getLastBuildTime());
        holder.clickContainer.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                DetailsDialog.newInstance(project)
                        .show(fragmentManager, DetailsDialog.KEY_TAG);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override public int getItemCount() {
        return projects.size();
    }

    private int getDrawableId(String lastBuildStatus, String activity) {
        int drawableId;
        switch (lastBuildStatus) {
            case STATUS_SUCCESS:
                drawableId = R.drawable.button_green;
                break;
            case STATUS_FAILURE:
                drawableId = R.drawable.button_red;
                break;
            case STATUS_UNKNOWN:
                if (activity.equals(ACTIVITY_BUILDING)) {
                    drawableId = R.drawable.button_refresh;
                } else {
                    drawableId = R.drawable.button_yellow;
                }
                break;
            default:
                drawableId = R.drawable.button_grey;
                break;
        }
        return drawableId;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.card_view) View clickContainer;
        @Bind(R.id.lw_project_name) TextView name;
        @Bind(R.id.lw_project_activity) TextView activity;
        @Bind(R.id.lw_project_time) TextView time;
        @Bind(R.id.lw_status_flag) ImageView status;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
