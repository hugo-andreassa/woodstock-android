package com.hyperdrive.woodstock.ui.operatingexpense;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.material.textfield.TextInputEditText;
import com.hyperdrive.woodstock.R;
import com.hyperdrive.woodstock.api.config.RetrofitConfig;
import com.hyperdrive.woodstock.api.services.OperatingExpenseService;
import com.hyperdrive.woodstock.models.OperatingExpenseModel;
import com.hyperdrive.woodstock.persistence.Preferences;
import com.hyperdrive.woodstock.utils.Mask;
import com.hyperdrive.woodstock.utils.SnackbarUtil;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OperatingExpenseActionFragment extends Fragment {

    private String TAG = "EXPENSES_ACTION_FRAG";

    private final String SERVER_ERROR = "Erro na comunicação com o servidor";
    private final String BAD_REQUEST_UPDATE = "Erro ao atualizar os dados da Despesa";
    private final String OK_REQUEST_UPDATE = "Despesa atualizada com sucesso";
    private final String BAD_REQUEST_INSERT = "Erro ao inserir os dados da Despesa";
    private final String OK_REQUEST_INSERT = "Despesa inserida com sucesso";

    private static final String ARG_PARAM1 = "companyId";
    private static final String ARG_PARAM2 = "operatingExpense";

    private Long mCompanyId;
    private OperatingExpenseModel mOperatingExpense;
    private Preferences sharedPreferences;
    private ProgressDialog progressDialog;

    private TextInputEditText name;
    private TextInputEditText value;
    private TextInputEditText description;

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

        sharedPreferences = new Preferences(getContext());
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Carregando...");

        setupEditTexts(view);
        setupSaveButton(view);

        if(mOperatingExpense != null) {
            loadFieldsInformation(view);
            setupDeleteButton(view);
        }

        return view;
    }

    private void setupDeleteButton(View view) {
        Button deleteButton = view.findViewById(R.id.operating_deletar_button);
        deleteButton.setVisibility(View.VISIBLE);

        deleteButton.setOnClickListener(v -> {
            deleteOperatingExpense(view);
        });
    }

    private void loadFieldsInformation(View view) {
        name.setText(mOperatingExpense.getName());
        value.setText(mOperatingExpense.getValue().toString());
        description.setText(mOperatingExpense.getDescription());
    }

    private void setupSaveButton(View view) {
        Button saveButton = view.findViewById(R.id.operating_salvar_button);
        saveButton.setOnClickListener(v -> {
            OperatingExpenseModel operatingExpense = getValuesFromFields();
            if(operatingExpense == null) {
                return;
            }

            progressDialog.show();
            if(mOperatingExpense != null) {
                operatingExpense.setId(mOperatingExpense.getId());
                updateOperatingExpenseInApi(operatingExpense, v);
            } else {
                insertOperatingExpenseInApi(operatingExpense, v);
            }
        });
    }

    private OperatingExpenseModel getValuesFromFields() {
        if(validateFields()) {
            OperatingExpenseModel operatingExpense = new OperatingExpenseModel();
            operatingExpense.setCompanyId(mCompanyId);
            operatingExpense.setName(name.getText().toString());
            operatingExpense.setValue(Mask.unmaskMoney(value.getText().toString()));
            operatingExpense.setDescription(description.getText().toString());

            return operatingExpense;
        }
        return null;
    }

    private boolean validateFields() {
        if (Objects.equals(value.getText().toString(), null)) {
            return false;
        } else {
            String name = this.name.getText().toString();
            Double prc = Mask.unmaskMoney(value.getText().toString());

            if(name.isEmpty() || prc < 1) {
                String error = getActivity().getResources().getString(R.string.campo_obrigatorio);
                this.name.setError(error);
                value.setError(error + ", o valor digitado não pode ser menor do que 1");

                return false;
            }
            return true;
        }
    }

    private void setupEditTexts(View view) {
        name = view.findViewById(R.id.operating_name);
        description = view.findViewById(R.id.operating_description);

        value = view.findViewById(R.id.operating_value);
        value.addTextChangedListener(Mask.moneyMask(value));
    }

    private void clearFields() {
        name.setText("");
        value.setText("");
        description.setText("");
    }

    private void insertOperatingExpenseInApi(OperatingExpenseModel operatingExpense, View v) {
        String auth = sharedPreferences.getAuthentication();

        OperatingExpenseService operatingExpenseService =
                RetrofitConfig.getRetrofitInstance().create(OperatingExpenseService.class);
        Call<Void> call = operatingExpenseService.insert(operatingExpense, auth);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    OperatingExpenseActivity.updateRecyclerView();
                    SnackbarUtil.showSuccess(getActivity(), OK_REQUEST_INSERT);
                    clearFields();
                } else {
                    SnackbarUtil.showError(getActivity(), BAD_REQUEST_INSERT);
                    Log.e(TAG, operatingExpense.toString());
                    Log.e(TAG, response.toString());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                progressDialog.dismiss();
                SnackbarUtil.showSuccess(getActivity(), SERVER_ERROR);
            }
        });
    }


    private void updateOperatingExpenseInApi(OperatingExpenseModel operatingExpense, View v) {
        String auth = sharedPreferences.getAuthentication();

        OperatingExpenseService operatingExpenseService =
                RetrofitConfig.getRetrofitInstance().create(OperatingExpenseService.class);
        Call<Void> call = operatingExpenseService.update(
                operatingExpense.getId(), operatingExpense, auth);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    OperatingExpenseActivity.updateRecyclerView();
                    SnackbarUtil.showSuccess(getActivity(), OK_REQUEST_UPDATE);
                } else {
                    SnackbarUtil.showError(getActivity(), BAD_REQUEST_UPDATE);
                    Log.e(TAG, operatingExpense.toString());
                    Log.e(TAG, response.toString());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                progressDialog.dismiss();
                SnackbarUtil.showSuccess(getActivity(), SERVER_ERROR);
            }
        });
    }

    private void deleteOperatingExpense(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setMessage("Deseja mesmo excluir esta Despesa?")
                .setPositiveButton("Sim", (dialog, id) -> {
                    progressDialog.show();
                    deleteOperatingExpenseFromApi(mOperatingExpense.getId());
                })
                .setNegativeButton("Cancelar", (dialog, id) -> {
                    dialog.dismiss();
                });

        Dialog dialog = builder.create();
        dialog.show();
    }

    private void deleteOperatingExpenseFromApi(Long id) {
        String auth = sharedPreferences.getAuthentication();

        OperatingExpenseService operatingExpenseService = RetrofitConfig.getRetrofitInstance().create(OperatingExpenseService.class);
        Call<Void> call = operatingExpenseService.delete(id, auth);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                progressDialog.dismiss();
                if(response.isSuccessful()) {
                    OperatingExpenseActivity.updateRecyclerView();
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