package com.hyperdrive.woodstock.ui.project;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hyperdrive.woodstock.R;
import com.hyperdrive.woodstock.adapters.ProjectAdapter;
import com.hyperdrive.woodstock.models.ProjectModel;
import com.hyperdrive.woodstock.viewmodel.ProjectViewModel;

import java.util.ArrayList;
import java.util.List;

public class ProjectActivity extends AppCompatActivity {

    private final String TAG = "PROJECT_ACTIVITY";

    private static final int CAMERA_REQUEST = 100;

    private static Long mBudgetItemId;
    private ProjectAdapter mAdapter;
    private static ProjectViewModel mProjectViewModel;
    private static ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        askForPermission(Manifest.permission.CAMERA, CAMERA_REQUEST);

        Bundle bundle = getIntent().getExtras();
        mBudgetItemId = bundle.getLong("budgetItemId");

        setupFloatingActionButton();
        setupRecyclerView(new ArrayList<>());

        progressBar = findViewById(R.id.progress_project);
        mProjectViewModel = new ProjectViewModel();
        mProjectViewModel.getProjects(mBudgetItemId, progressBar).observe(this, projects -> {
            mAdapter.updateData(projects);
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id==android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupFloatingActionButton() {
        FloatingActionButton fab = findViewById(R.id.fab_project);
        fab.setOnClickListener(v -> {
            ProjectActionFragment cuttingPlanActionFragment =
                    ProjectActionFragment.newInstance(mBudgetItemId, null);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.project_fragment_layout, cuttingPlanActionFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });
    }

    private void setupRecyclerView(List<ProjectModel> projects) {
        RecyclerView mRecyclerView = findViewById(R.id.list_project);
        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new ProjectAdapter(projects, mBudgetItemId);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
            }
        } else if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {
            Toast.makeText(getApplicationContext(), "Permission was denied", Toast.LENGTH_SHORT).show();
        }
    }

    public static void updateRecyclerView() {
        mProjectViewModel.getProjects(mBudgetItemId, progressBar);
    }
}