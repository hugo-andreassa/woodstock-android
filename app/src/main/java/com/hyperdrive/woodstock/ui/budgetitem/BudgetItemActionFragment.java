package com.hyperdrive.woodstock.ui.budgetitem;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hyperdrive.woodstock.R;
import com.hyperdrive.woodstock.models.BudgetItemModel;
import com.hyperdrive.woodstock.persistence.Preferences;

public class BudgetItemActionFragment extends Fragment {

    private static final String ARG_PARAM1 = "budgetId";
    private static final String ARG_PARAM2 = "budgetItem";

    private Long mBudgetId;
    private BudgetItemModel mBudgetItem;

    private ProgressDialog progressDialog;
    private Preferences sharedPreferences;

    public BudgetItemActionFragment() {

    }

    public static BudgetItemActionFragment newInstance(Long mBudgetId, BudgetItemModel mBudgetItem) {
        BudgetItemActionFragment fragment = new BudgetItemActionFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_PARAM1, mBudgetId);
        args.putSerializable(ARG_PARAM2, mBudgetItem);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mBudgetId = getArguments().getLong(ARG_PARAM1);
            mBudgetItem = (BudgetItemModel) getArguments().getSerializable(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_budget_item_action, container, false);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Carregando...");
        sharedPreferences = new Preferences(getContext());


        return view;
    }
}