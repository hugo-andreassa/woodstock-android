package com.hyperdrive.woodstock.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.hyperdrive.woodstock.R;
import com.hyperdrive.woodstock.holders.ProjectHolder;
import com.hyperdrive.woodstock.models.ProjectModel;
import com.hyperdrive.woodstock.ui.project.ProjectActionFragment;
import com.hyperdrive.woodstock.utils.LoadImage;

import java.util.List;
import java.util.Locale;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectHolder> {

    private static final String TAG = "PROJECT_ACTIVITY";

    private final List<ProjectModel> mProjects;
    private final Long mBudgetItemId;

    public ProjectAdapter(List<ProjectModel> mProjects, Long mBudgetItemId) {
        this.mProjects = mProjects;
        this.mBudgetItemId = mBudgetItemId;
    }

    @NonNull
    @Override
    public ProjectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProjectHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_project, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectHolder holder, int position) {
        ProjectModel project = mProjects.get(position);

        LoadImage loadImage = new LoadImage(holder.image, null);
        loadImage.execute(project.getUrl());

        holder.tittle.setText(
                String.format(Locale.getDefault(),
                "Projeto %d", position + 1));
        holder.comment.setText(project.getComment());
        holder.moreButton.setOnClickListener(v -> {
          callFragment(v, project);
        });
    }

    @Override
    public int getItemCount() {
        return mProjects != null ? mProjects.size() : 0;
    }

    private void callFragment(View v, ProjectModel projectModel) {
        AppCompatActivity activity = (AppCompatActivity) v.getContext();

        ProjectActionFragment projectActionFragment =
                ProjectActionFragment.newInstance(mBudgetItemId, projectModel);

        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.project_fragment_layout, projectActionFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void updateData(List<ProjectModel> projects) {
        this.mProjects.clear();
        this.mProjects.addAll(projects);
        notifyDataSetChanged();
    }
}
