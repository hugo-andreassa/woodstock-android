package com.hyperdrive.woodstock.ui.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.hyperdrive.woodstock.R;
import com.hyperdrive.woodstock.adapters.MaterialAdapter;
import com.hyperdrive.woodstock.adapters.UserAdapter;
import com.hyperdrive.woodstock.models.MaterialModel;
import com.hyperdrive.woodstock.models.UserModel;
import com.hyperdrive.woodstock.persistence.SharedPreferencesUtil;
import com.hyperdrive.woodstock.viewmodel.MaterialViewModel;
import com.hyperdrive.woodstock.viewmodel.UserViewModel;

import java.util.ArrayList;
import java.util.List;

public class UserActivity extends AppCompatActivity {

    private static Long mCompanyId;
    private UserAdapter mAdapter;
    private static UserViewModel mUserViewModel;
    private static String auth;
    private static ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Bundle bundle = getIntent().getExtras();
        mCompanyId = bundle.getLong("companyId");

        SharedPreferencesUtil sharedPreferences = new SharedPreferencesUtil(getApplicationContext());
        auth = sharedPreferences.getAuthentication();

        setupRecyclerView(new ArrayList<>());

        progressBar = findViewById(R.id.progress_user);
        mUserViewModel = new UserViewModel();
        mUserViewModel.getMaterials(mCompanyId, auth, progressBar).observe(this, users -> {
            mAdapter.updateData(users);
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

    private void setupRecyclerView(List<UserModel> users) {
        RecyclerView mRecyclerView = findViewById(R.id.list_user);
        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new UserAdapter(users, mCompanyId);
        mRecyclerView.setAdapter(mAdapter);
    }

    public static void updateRecyclerView() {
        mUserViewModel.getMaterials(mCompanyId, auth, progressBar);
    }
}