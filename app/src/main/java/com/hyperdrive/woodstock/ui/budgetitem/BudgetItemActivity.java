package com.hyperdrive.woodstock.ui.budgetitem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hyperdrive.woodstock.R;
import com.hyperdrive.woodstock.adapters.BudgetAdapter;
import com.hyperdrive.woodstock.adapters.BudgetItemAdapter;
import com.hyperdrive.woodstock.models.BudgetItemModel;
import com.hyperdrive.woodstock.models.BudgetModel;
import com.hyperdrive.woodstock.ui.budget.BudgetActionFragment;
import com.hyperdrive.woodstock.viewmodel.BudgetItemViewModel;
import com.hyperdrive.woodstock.viewmodel.BudgetViewModel;

import java.util.ArrayList;
import java.util.List;

public class BudgetItemActivity extends AppCompatActivity {

    private static Long budgetId;
    private BudgetItemAdapter mAdapter;
    private static BudgetItemViewModel mBudgetItemViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_item);

        Bundle bundle = getIntent().getExtras();
        budgetId = bundle.getLong("budgetId");

        setupFloatingActionButton();
        setupRecyclerView(new ArrayList<>());

        mBudgetItemViewModel = new BudgetItemViewModel();
        mBudgetItemViewModel.getBudgetItems(budgetId).observe(this, budgetItems -> {
            mAdapter.updateData(budgetItems);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        mBudgetItemViewModel.getBudgetItems(budgetId);
    }

    private void setupFloatingActionButton() {
        FloatingActionButton fab = findViewById(R.id.fab_budgetitem);
        fab.setOnClickListener(v -> {
            BudgetItemActionFragment budgetItemActionFragment = BudgetItemActionFragment.newInstance(budgetId, null);

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

        mAdapter = new BudgetItemAdapter(budgetItems, budgetId);
        mRecyclerView.setAdapter(mAdapter);
    }

    public static void updateRecyclerView() {
        mBudgetItemViewModel.getBudgetItems(budgetId);
    }
}