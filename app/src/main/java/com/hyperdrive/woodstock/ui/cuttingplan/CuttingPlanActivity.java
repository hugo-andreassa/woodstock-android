package com.hyperdrive.woodstock.ui.cuttingplan;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hyperdrive.woodstock.R;
import com.hyperdrive.woodstock.adapters.CuttingPlanAdapter;
import com.hyperdrive.woodstock.models.CuttingPlanModel;

import java.util.ArrayList;
import java.util.List;

public class CuttingPlanActivity extends AppCompatActivity {

    private static Long budgetItemId;
    private CuttingPlanAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cutting_plan);

        List<CuttingPlanModel> cuttingPlans = new ArrayList<>();

        // Altura - Largura
        cuttingPlans.add(new CuttingPlanModel(1L, 80.0, 64.0,
                3, "", null));
        cuttingPlans.add(new CuttingPlanModel(2L, 45.0, 150.0,
                3, "", null));
        cuttingPlans.add(new CuttingPlanModel(3L, 80.0, 42.0,
                3, "", null));
        cuttingPlans.add(new CuttingPlanModel(4L, 50.0, 100.0,
                3, "", null));
        cuttingPlans.add(new CuttingPlanModel(4L, 183.0, 275.0,
                3, "", null));

        setupFloatingActionButton();
        setupRecyclerView(cuttingPlans);
    }

    private void setupFloatingActionButton() {
        FloatingActionButton fab = findViewById(R.id.fab_cuttingplan);
        fab.setOnClickListener(v -> {
            /* BudgetItemActionFragment budgetItemActionFragment = BudgetItemActionFragment.newInstance(budgetItemId, null);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.budgetitem_fragment_layout, budgetItemActionFragment);
            transaction.addToBackStack(null);
            transaction.commit(); */
        });
    }

    private void setupRecyclerView(List<CuttingPlanModel> cuttingPlans) {
        RecyclerView mRecyclerView = findViewById(R.id.list_cuttingplan);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new CuttingPlanAdapter(cuttingPlans, budgetItemId, this);
        mRecyclerView.setAdapter(mAdapter);
    }
}