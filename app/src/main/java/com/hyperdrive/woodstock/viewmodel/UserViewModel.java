package com.hyperdrive.woodstock.viewmodel;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hyperdrive.woodstock.api.config.RetrofitConfig;
import com.hyperdrive.woodstock.api.services.MaterialService;
import com.hyperdrive.woodstock.api.services.UserService;
import com.hyperdrive.woodstock.models.MaterialModel;
import com.hyperdrive.woodstock.models.UserModel;
import com.hyperdrive.woodstock.persistence.SharedPreferencesUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserViewModel extends ViewModel {
    private String TAG = "MATERIAL_VIEW_MODEL";

    private MutableLiveData<List<UserModel>> mUsers;
    private ProgressBar progressBar;

    public LiveData<List<UserModel>> getMaterials(Long id, String auth, ProgressBar progressBar) {
        this.progressBar = progressBar;

        if (mUsers == null) {
            mUsers = new MutableLiveData<>();
        }

        progressBar.setVisibility(View.VISIBLE);
        loadUsersFromApi(id, auth);

        return mUsers;
    }

    private void loadUsersFromApi(Long id, String auth) {
        UserService userService = RetrofitConfig.getRetrofitInstance().create(UserService.class);
        Call<List<UserModel>> call = userService.findAll(id, auth);

        call.enqueue(new Callback<List<UserModel>>() {
            @Override
            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()) {
                    mUsers.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<UserModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "onFailure findAll Users");
            }
        });

    }
}
