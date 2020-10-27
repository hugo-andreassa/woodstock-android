package com.hyperdrive.woodstock.ui.operatingexpense;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hyperdrive.woodstock.R;
import com.hyperdrive.woodstock.models.OperatingExpenseModel;

public class OperatingExpenseActionFragment extends Fragment {

    private static final String ARG_PARAM1 = "companyId";
    private static final String ARG_PARAM2 = "operatingExpense";

    private Long mCompanyId;
    private OperatingExpenseModel mOperatingExpense;

    public OperatingExpenseActionFragment() {

    }

    public static OperatingExpenseActionFragment newInstance(Long mCompanyId, OperatingExpenseModel mOperatingExpense) {
        OperatingExpenseActionFragment fragment = new OperatingExpenseActionFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_PARAM1, mCompanyId);
        args.putSerializable(ARG_PARAM2, mOperatingExpense);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCompanyId = getArguments().getLong(ARG_PARAM1);
            mOperatingExpense = (OperatingExpenseModel) getArguments().getSerializable(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_operating_expense, container, false);

        return view;
    }
}