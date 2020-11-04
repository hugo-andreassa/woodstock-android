package com.hyperdrive.woodstock.ui.material;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hyperdrive.woodstock.R;
import com.hyperdrive.woodstock.adapters.MaterialAdapter;
import com.hyperdrive.woodstock.models.MaterialModel;

import java.util.ArrayList;
import java.util.List;

public class MaterialActivity extends AppCompatActivity {

    private String TAG = "MATERIAL_ACTIVITY";

    private Long mCompanyId;
    private MaterialAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        mCompanyId = bundle.getLong("companyId");

        List<MaterialModel> materials = new ArrayList<>();
        materials.add(new MaterialModel(null, "Corrediça 45mm Larga",
                "", 10, 15, "2020-11-13T23:30:52Z", "PR", 1L));
        materials.add(new MaterialModel(null, "Corrediça 40mm Larga",
                "", 10, 5, "2020-11-13T23:30:52Z", "PR", 1L));
        materials.add(new MaterialModel(null, "Corrediça 35mm Larga",
                "", 10, 0, "2020-11-13T23:30:52Z", "PR", 1L));
        materials.add(new MaterialModel(null, "Corrediça 30mm Larga",
                "", 10, 0, "2020-11-13T23:30:52Z", "PR", 1L));
        materials.add(new MaterialModel(null, "Corrediça 25mm Estreita",
                "", 10, 0, "2020-11-13T23:30:52Z", "PR", 1L));

        setupFloatingActionButton();
        setupRecyclerView(materials);
    }

    private void setupFloatingActionButton() {
        FloatingActionButton fab = findViewById(R.id.fab_material);
        fab.setOnClickListener(v -> {
            /* OperatingExpenseActionFragment operatingExpenseActionFragment =
                    OperatingExpenseActionFragment.newInstance(mCompanyId, null);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.operatingexpense_fragment_layout, operatingExpenseActionFragment);
            transaction.addToBackStack(null);
            transaction.commit(); */
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
    }
}