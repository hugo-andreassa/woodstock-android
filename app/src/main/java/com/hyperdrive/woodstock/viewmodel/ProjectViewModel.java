package com.hyperdrive.woodstock.viewmodel;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hyperdrive.woodstock.api.config.RetrofitConfig;
import com.hyperdrive.woodstock.api.services.ProjectService;
import com.hyperdrive.woodstock.models.ProjectModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProjectViewModel extends ViewModel {

    private String TAG = "PROJECT_VIEW_MODEL";

    private MutableLiveData<List<ProjectModel>> projects;
    private ProgressBar progressBar;

    public LiveData<List<ProjectModel>> getProjects(Long id, ProgressBar progressBar) {
        this.progressBar = progressBar;
        if (projects == null) {
            projects = new MutableLiveData<>();
        }

        progressBar.setVisibility(View.VISIBLE);
        loadProjectsFromApi(id);

        return projects;
    }

    private void loadProjectsFromApi(Long id) {
        ProjectService projectsService = RetrofitConfig.getRetrofitInstance().create(ProjectService.class);
        Call<List<ProjectModel>> call = projectsService.findAll(id);

        call.enqueue(new Callback<List<ProjectModel>>() {
            @Override
            public void onResponse(Call<List<ProjectModel>> call, Response<List<ProjectModel>> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()) {
                    projects.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<ProjectModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "onFailure findAll projects");
            }
        });

    }
}
