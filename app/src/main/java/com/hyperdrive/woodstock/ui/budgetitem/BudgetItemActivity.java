package com.hyperdrive.woodstock.ui.budgetitem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hyperdrive.woodstock.R;
import com.hyperdrive.woodstock.adapters.BudgetItemAdapter;
import com.hyperdrive.woodstock.models.BudgetItemModel;
import com.hyperdrive.woodstock.viewmodel.BudgetItemViewModel;

import java.util.ArrayList;
import java.util.List;

public class BudgetItemActivity extends AppCompatActivity {

    private static Long mBudgetId;
    private BudgetItemAdapter mAdapter;
    private static BudgetItemViewModel mBudgetItemViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_item);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        mBudgetId = bundle.getLong("budgetId");

        setupFloatingActionButton();
        setupRecyclerView(new ArrayList<>());

        mBudgetItemViewModel = new BudgetItemViewModel();
        mBudgetItemViewModel.getBudgetItems(mBudgetId).observe(this, budgetItems -> {
            mAdapter.updateData(budgetItems);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        mBudgetItemViewModel.getBudgetItems(mBudgetId);
    }

    private void setupFloatingActionButton() {
        FloatingActionButton fab = findViewById(R.id.fab_budgetitem);
        fab.setOnClickListener(v -> {
            BudgetItemActionFragment budgetItemActionFragment = BudgetItemActionFragment.newInstance(mBudgetId, null);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.budgetitem_fragment_layout, budgetItemActionFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });
    }

    private void setupRecyclerView(List<BudgetItemModel> budgetItems) {
        RecyclerView mRecyclerView = findViewById(R.id.list_budgetitem);
        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new BudgetItemAdapter(budgetItems, mBudgetId);
        mRecyclerView.setAdapter(mAdapter);
    }

    public static void updateRecyclerView() {
        mBudgetItemViewModel.getBudgetItems(mBudgetId);
    }
}