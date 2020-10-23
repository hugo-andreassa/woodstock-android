package com.hyperdrive.woodstock.ui.cuttingplan;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hyperdrive.woodstock.R;
import com.hyperdrive.woodstock.adapters.CuttingPlanAdapter;
import com.hyperdrive.woodstock.models.CuttingPlanModel;
import com.hyperdrive.woodstock.viewmodel.CuttingPlanViewModel;

import java.util.ArrayList;
import java.util.List;

public class CuttingPlanActivity extends AppCompatActivity {

    private String TAG = "CUT_PLAN_ACTIVITY";

    private static Long mBudgetItemId;
    private CuttingPlanAdapter mAdapter;
    private static CuttingPlanViewModel mCuttingPlanViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cutting_plan);

        Bundle bundle = getIntent().getExtras();
        mBudgetItemId = bundle.getLong("budgetItemId");
        Log.e(TAG, mBudgetItemId.toString());

        setupFloatingActionButton();
        setupRecyclerView(new ArrayList<>());

        mCuttingPlanViewModel = new CuttingPlanViewModel();
        mCuttingPlanViewModel.getCuttingPlans(mBudgetItemId).observe(this, cuttingPlans -> {
            mAdapter.updateData(cuttingPlans);
        });
    }

    private void setupFloatingActionButton() {
        FloatingActionButton fab = findViewById(R.id.fab_cuttingplan);
        fab.setOnClickListener(v -> {
            CuttingPlanActionFragment cuttingPlanActionFragment =
                    CuttingPlanActionFragment.newInstance(mBudgetItemId, null);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.cuttingplan_fragment_layout, cuttingPlanActionFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });
    }

    private void setupRecyclerView(List<CuttingPlanModel> cuttingPlans) {
        RecyclerView mRecyclerView = findViewById(R.id.list_cuttingplan);
        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new CuttingPlanAdapter(cuttingPlans, mBudgetItemId, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    public static void updateRecyclerView() {
        mCuttingPlanViewModel.getCuttingPlans(mBudgetItemId);
    }
}