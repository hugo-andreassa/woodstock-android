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
import com.hyperdrive.woodstock.viewmodel.BudgetViewModel;

import java.util.ArrayList;
import java.util.List;

public class BudgetItemActivity extends AppCompatActivity {

    private static Long budgetId;
    private BudgetItemAdapter mAdapter;
    // private static BudgetItemViewModel mBudgetItemViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_item);

        Bundle bundle = getIntent().getExtras();
        budgetId = bundle.getLong("budgetId");

        setupFloatingActionButton();

        List<BudgetItemModel> budgetItems = new ArrayList<>();
        for (int i=1; i<=20; i++) {
            budgetItems.add(new BudgetItemModel(null, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam fringilla rhoncus mauris, sit amet ornare massa euismod ac. Nam sodales nisi sit amet tellus tempus faucibus. Donec hendrerit justo eu velit eleifend mattis. Donec rhoncus ipsum lorem. In quis augue erat. Sed neque tellus, vehicula id felis ac, tincidunt gravida ante.",
                    null, null, "Tittle", null,
                    null));
        }

        setupRecyclerView(budgetItems);
    }

    private void setupFloatingActionButton() {
        FloatingActionButton fab = findViewById(R.id.fab_budgetitem);
        fab.setOnClickListener(v -> {
            // BudgetItemActionFragment budgetItemActionFragment = BudgetActionFragment.newInstance(null, budgetId);

            /* FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.budgetitem_fragment_layout, budgetItemActionFragment);
            transaction.addToBackStack(null);
            transaction.commit(); */
        });
    }

    private void setupRecyclerView(List<BudgetItemModel> budgetItems) {
        RecyclerView mRecyclerView = findViewById(R.id.list_budgetitem);
        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        mAdapter = new BudgetItemAdapter(budgetItems, budgetId);
        mRecyclerView.setAdapter(mAdapter);
    }
}