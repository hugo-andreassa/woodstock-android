package com.hyperdrive.woodstock.ui.cuttingplan;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
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
    private static ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cutting_plan);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        mBudgetItemId = bundle.getLong("budgetItemId");
        Log.e(TAG, mBudgetItemId.toString());

        setupFloatingActionButton();
        setupRecyclerView(new ArrayList<>());

        progressBar = findViewById(R.id.progress_cuttingplan);
        mCuttingPlanViewModel = new CuttingPlanViewModel();
        mCuttingPlanViewModel.getCuttingPlans(mBudgetItemId, progressBar).observe(this, cuttingPlans -> {
            mAdapter.updateData(cuttingPlans);
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
        mCuttingPlanViewModel.getCuttingPlans(mBudgetItemId, progressBar);
    }
}