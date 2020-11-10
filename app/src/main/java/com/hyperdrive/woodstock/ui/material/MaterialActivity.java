package com.hyperdrive.woodstock.ui.material;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hyperdrive.woodstock.R;
import com.hyperdrive.woodstock.adapters.MaterialAdapter;
import com.hyperdrive.woodstock.models.MaterialModel;
import com.hyperdrive.woodstock.viewmodel.MaterialViewModel;

import java.util.ArrayList;
import java.util.List;

public class MaterialActivity extends AppCompatActivity {

    private String TAG = "MATERIAL_ACTIVITY";

    private static Long mCompanyId;
    private MaterialAdapter mAdapter;
    private static MaterialViewModel mMaterialViewModel;
    private static ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        mCompanyId = bundle.getLong("companyId");

        setupFloatingActionButton();
        setupRecyclerView(new ArrayList<>());

        progressBar = findViewById(R.id.progress_material);
        mMaterialViewModel = new MaterialViewModel();
        mMaterialViewModel.getMaterials(mCompanyId, progressBar).observe(this, materials -> {
            mAdapter.updateData(materials);
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
        FloatingActionButton fab = findViewById(R.id.fab_material);
        fab.setOnClickListener(v -> {
            MaterialActionFragment materialActionFragment =
                    MaterialActionFragment.newInstance(mCompanyId, null);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.material_fragment_layout, materialActionFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });
    }

    private void setupRecyclerView(List<MaterialModel> materials) {
        RecyclerView mRecyclerView = findViewById(R.id.list_material);
        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new MaterialAdapter(getApplicationContext(), materials, mCompanyId);
        mRecyclerView.setAdapter(mAdapter);
    }

    public static void updateRecyclerView() {
        mMaterialViewModel.getMaterials(mCompanyId, progressBar);
    }
}