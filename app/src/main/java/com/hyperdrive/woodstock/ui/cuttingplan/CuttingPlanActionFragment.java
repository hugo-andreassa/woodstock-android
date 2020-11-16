package com.hyperdrive.woodstock.ui.cuttingplan;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.hyperdrive.woodstock.R;
import com.hyperdrive.woodstock.api.config.RetrofitConfig;
import com.hyperdrive.woodstock.api.services.CuttingPlanService;
import com.hyperdrive.woodstock.models.CuttingPlanModel;
import com.hyperdrive.woodstock.persistence.SharedPreferencesUtil;
import com.hyperdrive.woodstock.utils.SnackbarUtil;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CuttingPlanActionFragment extends Fragment {

    private String TAG = "CUT_PLAN_ACTION_FRAG";

    private final String SERVER_ERROR = "Erro na comunicação com o servidor";

    private final String BAD_REQUEST_UPDATE = "Erro ao atualizar os dados do Plano de Corte";
    private final String OK_REQUEST_UPDATE = "Plano de Corte atualizado com sucesso";
    private final String BAD_REQUEST_INSERT = "Erro ao inserir os dados do Plano de Corte";
    private final String OK_REQUEST_INSERT = "Plano de Corte inserido com sucesso";

    private static final String ARG_PARAM1 = "budgetItemId";
    private static final String ARG_PARAM2 = "cuttingPlan";

    private Long mBudgetItemId;
    private CuttingPlanModel mCuttingPlan;
    private SharedPreferencesUtil sharedPreferences;
    private ProgressDialog progressDialog;

    private TextInputEditText height;
    private TextInputEditText width;
    private TextInputEditText comment;
    private int quantity = 1;

    public CuttingPlanActionFragment() {

    }

    public static CuttingPlanActionFragment newInstance(Long mBudgetItemId, CuttingPlanModel mCuttingPlan) {
        CuttingPlanActionFragment fragment = new CuttingPlanActionFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_PARAM1, mBudgetItemId);
        args.putSerializable(ARG_PARAM2, mCuttingPlan);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mBudgetItemId = getArguments().getLong(ARG_PARAM1);
            mCuttingPlan = (CuttingPlanModel) getArguments().getSerializable(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cutting_plan_action, container, false);

        sharedPreferences = new SharedPreferencesUtil(getContext());
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Carregando...");

        setupEditTexts(view);
        setupIncrementButtons(view);
        setupSaveButton(view);

        if(mCuttingPlan != null) {
            loadFieldsInformation(view);
            setupDeleteButton(view);
        }

        return view;
    }

    private void setupDeleteButton(View view) {
        Button button = view.findViewById(R.id.cuttingplan_delete_button);
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(v -> {
            deleteCuttingPlan(view);
        });
    }

    private void setupSaveButton(View view) {
        Button saveButton = view.findViewById(R.id.cuttingplan_save_button);

        saveButton.setOnClickListener((v) -> {
            CuttingPlanModel cuttingPlan = getValuesFromFields();

            if (cuttingPlan != null) {
                if(mCuttingPlan != null) {
                    cuttingPlan.setId(mCuttingPlan.getId());

                    progressDialog.show();
                    updateCuttingPlanInApi(cuttingPlan);
                } else {
                    progressDialog.show();
                    insertCuttingPlanInApi(cuttingPlan, view);
                }
            }
        });
    }

    private void loadFieldsInformation(View view) {
        height.setText(String.format(Locale.getDefault(), "%.0f", mCuttingPlan.getHeight()));
        width.setText(String.format(Locale.getDefault(), "%.0f", mCuttingPlan.getWidth()));
        comment.setText(mCuttingPlan.getComment());

        quantity = mCuttingPlan.getQuantity();
        displayQuantity(mCuttingPlan.getQuantity(), view);
    }

    private CuttingPlanModel getValuesFromFields() {

        if(validateFields()) {
            CuttingPlanModel cuttingPlan = new CuttingPlanModel();
            cuttingPlan.setBudgetItemId(mBudgetItemId);
            cuttingPlan.setComment(comment.getText().toString());
            cuttingPlan.setHeight(Double.parseDouble(height.getText().toString()));
            cuttingPlan.setWidth(Double.parseDouble(width.getText().toString()));
            cuttingPlan.setQuantity(quantity);

            return cuttingPlan;
        }

        return null;
    }

    private boolean validateFields() {
        if(height.getText().toString().isEmpty() || width.getText().toString().isEmpty()) {
            String error = getActivity().getResources().getString(R.string.campo_obrigatorio);
            height.setError(error);
            width.setError(error);

            return false;
        }

        return true;
    }

    private void setupEditTexts(View v) {
        height = v.findViewById(R.id.cuttingplan_height);
        width = v.findViewById(R.id.cuttingplan_width);
        comment = v.findViewById(R.id.cuttingplan_comment);
    }

    private void setupIncrementButtons(View v) {
        Button increment = v.findViewById(R.id.cuttingplan_quantity_more_button);
        Button decrement = v.findViewById(R.id.cuttingplan_quantity_less_button);

        increment.setOnClickListener((view) -> {
            if(quantity < 100) {
                quantity = quantity + 1;
                displayQuantity(quantity, v);
            }
        });

        decrement.setOnClickListener((view) -> {
            if(quantity > 1) {
                quantity = quantity - 1;
                displayQuantity(quantity, v);
            }
        });
    }

    private void displayQuantity(int number, View view) {
        TextView quantityTextView = view.findViewById(R.id.cuttingplan_quantity);
        quantityTextView.setText(String.valueOf(number));
    }

    private void clearFields(View v) {
        comment.setText("");
        width.setText("");
        height.setText("");

        quantity = 1;
        displayQuantity(1, v);
    }

    private void insertCuttingPlanInApi(CuttingPlanModel cuttingPlan, View v) {
        String auth = sharedPreferences.getAuthentication();

        CuttingPlanService cuttingPlanService = RetrofitConfig.getRetrofitInstance().create(CuttingPlanService.class);
        Call<Void> call = cuttingPlanService.insert(cuttingPlan, auth);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                progressDialog.dismiss();
                if(response.isSuccessful()) {
                    CuttingPlanActivity.updateRecyclerView();
                    SnackbarUtil.showSuccess(getActivity(), OK_REQUEST_INSERT);
                    Log.e(TAG, response.toString());
                    Log.e(TAG, cuttingPlan.toString());
                    clearFields(v);
                } else {
                    SnackbarUtil.showError(getActivity(), BAD_REQUEST_INSERT);
                    Log.e(TAG, response.toString());
                    Log.e(TAG, cuttingPlan.toString());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                progressDialog.dismiss();
                SnackbarUtil.showError(getActivity(), SERVER_ERROR);
                Log.e(TAG, cuttingPlan.toString());
            }
        });
    }


    private void updateCuttingPlanInApi(CuttingPlanModel cuttingPlan) {
        String auth = sharedPreferences.getAuthentication();

        CuttingPlanService cuttingPlanService = RetrofitConfig.getRetrofitInstance().create(CuttingPlanService.class);
        Call<Void> call = cuttingPlanService.update(cuttingPlan.getId(), cuttingPlan, auth);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                progressDialog.dismiss();
                if(response.isSuccessful()) {
                    CuttingPlanActivity.updateRecyclerView();
                    SnackbarUtil.showSuccess(getActivity(), OK_REQUEST_UPDATE);
                } else {
                    SnackbarUtil.showError(getActivity(), BAD_REQUEST_UPDATE);
                    Log.e(TAG, response.toString());
                    Log.e(TAG, cuttingPlan.toString());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                progressDialog.dismiss();
                SnackbarUtil.showError(getActivity(), SERVER_ERROR);
            }
        });
    }

    private void deleteCuttingPlan(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setMessage("Deseja mesmo excluir este Plano de Corte?")
                .setPositiveButton("Sim", (dialog, id) -> {
                    deleteBudgetItemFromApi(mCuttingPlan.getId());
                })
                .setNegativeButton("Cancelar", (dialog, id) -> {
                    dialog.dismiss();
                });

        Dialog dialog = builder.create();
        dialog.show();
    }

    private void deleteBudgetItemFromApi(Long id) {
        String auth = sharedPreferences.getAuthentication();

        CuttingPlanService cuttingPlanService = RetrofitConfig.getRetrofitInstance().create(CuttingPlanService.class);
        Call<Void> call = cuttingPlanService.delete(id, auth);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                progressDialog.dismiss();
                if(response.isSuccessful()) {
                    CuttingPlanActivity.updateRecyclerView();
                    getActivity().getSupportFragmentManager().popBackStackImmediate();
                } else {
                    SnackbarUtil.showError(getActivity(), SERVER_ERROR);
                    Log.e(TAG, response.toString());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                progressDialog.dismiss();
                SnackbarUtil.showError(getActivity(), SERVER_ERROR);
                Log.e(TAG, id.toString());
            }
        });
    }
}