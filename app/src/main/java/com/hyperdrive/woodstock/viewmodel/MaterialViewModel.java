package com.hyperdrive.woodstock.viewmodel;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hyperdrive.woodstock.api.config.RetrofitConfig;
import com.hyperdrive.woodstock.api.services.CuttingPlanService;
import com.hyperdrive.woodstock.api.services.MaterialService;
import com.hyperdrive.woodstock.models.CuttingPlanModel;
import com.hyperdrive.woodstock.models.MaterialModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MaterialViewModel extends ViewModel {

    private String TAG = "MATERIAL_VIEW_MODEL";

    private MutableLiveData<List<MaterialModel>> materials;
    private ProgressBar progressBar;

    public LiveData<List<MaterialModel>> getMaterials(Long id, ProgressBar progressBar) {
        this.progressBar = progressBar;

        if (materials == null) {
            materials = new MutableLiveData<>();
        }

        progressBar.setVisibility(View.VISIBLE);
        loadMaterialsFromApi(id);

        return materials;
    }

    private void loadMaterialsFromApi(Long id) {
        MaterialService materialService = RetrofitConfig.getRetrofitInstance().create(MaterialService.class);
        Call<List<MaterialModel>> call = materialService.findAll(id);

        call.enqueue(new Callback<List<MaterialModel>>() {
            @Override
            public void onResponse(Call<List<MaterialModel>> call, Response<List<MaterialModel>> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()) {
                    materials.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<MaterialModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "onFailure findAll Cutting Plan");
            }
        });

    }
}
