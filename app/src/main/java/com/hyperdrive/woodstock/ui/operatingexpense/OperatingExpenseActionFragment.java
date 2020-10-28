package com.hyperdrive.woodstock.ui.operatingexpense;

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
import com.hyperdrive.woodstock.ui.cuttingplan.CuttingPlanActivity;
import com.hyperdrive.woodstock.utils.Mask;
import com.hyperdrive.woodstock.utils.SnackbarUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OperatingExpenseActionFragment extends Fragment {

    private String TAG = "EXPENSES_ACTION_FRAG";

    private final String SERVER_ERROR = "Erro na comunicação com o servidor";
    private final String BAD_REQUEST_UPDATE = "Erro ao atualizar os dados da Despesa";
    private final String OK_REQUEST_UPDATE = "Despesa atualizada com sucesso";
    private final String BAD_REQUEST_INSERT = "Erro ao inserir os dados da Despesa";
    private final String OK_REQUEST_INSERT = "Despesa inserido com sucesso";

    private static final String ARG_PARAM1 = "companyId";
    private static final String ARG_PARAM2 = "operatingExpense";

    private Long mCompanyId;
    private OperatingExpenseModel mOperatingExpense;
    private Preferences sharedPreferences;
    private ProgressDialog progressDialog;

    private TextInputEditText name;
    private TextInputEditText value;
    private TextInputEditText description;
    private Spinner spinnerType;

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

        setupSpinnerDropdownType(view);
        setupEditTexts(view);
        setupSaveButton(view);

        return view;
    }

    private void setupSaveButton(View view) {
        Button saveButton = view.findViewById(R.id.operating_salvar_button);
        saveButton.setOnClickListener(v -> {
            OperatingExpenseModel operatingExpense = getValuesFromFields();
            if(operatingExpense == null) {
                return;
            }

            if(mOperatingExpense != null) {
                operatingExpense.setId(mOperatingExpense.getId());

                // Atualiza
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
            operatingExpense.setType(
                    convertExpenseType(
                            spinnerType.getSelectedItem().toString()));

            return operatingExpense;
        }
        return null;
    }

    private boolean validateFields() {
        if(name.getText().toString().isEmpty() ||
            value.getText().toString().isEmpty()) {

            String error = getContext().getResources().getString(R.string.campo_obrigatorio);

            name.setError(error);
            value.setError(error);

            return false;
        }
        return true;
    }

    private void setupEditTexts(View view) {
        name = view.findViewById(R.id.operating_name);
        description = view.findViewById(R.id.operating_description);

        value = view.findViewById(R.id.operating_value);
        value.addTextChangedListener(Mask.moneyMask(value));
    }

    private void setupSpinnerDropdownType(View v) {
        spinnerType = v.findViewById(R.id.operating_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(v.getContext(),
                R.array.operating_type_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerType.setAdapter(adapter);
    }

    private void clearFields(View v) {
        name.setText("");
        value.setText("");
        description.setText("");
    }

    private String convertExpenseType(String type) {
        String aux = "";

        switch (type) {
            case "DESPESAS COM VEÍCULO":
                aux = "DESPESAS_ESCRITORIO";
                break;
            case "MANUTENÇÃO":
                aux =  "MANUTENCAO";
                break;
            case "SUPRIMENTOS":
                aux =  "SUPRIMENTOS";
                break;
            case "CONTAS":
                aux =  "CONTAS";
                break;
            case "DESPESAS COM ESCRITÓRIO":
                aux =  "DESPESAS_ESCRITORIO";
                break;
            case "ALUGUEL":
                aux =  "ALUGUEL";
                break;
            case "SALARIO":
                aux =  "SALARIO";
                break;
            default:
                aux = "";
                break;
        };

        return aux;
    }

    private void insertOperatingExpenseInApi(OperatingExpenseModel operatingExpense, View v) {
        String auth = sharedPreferences.getAuthentication();

        OperatingExpenseService operatingExpenseService =
                RetrofitConfig.getRetrofitInstance().create(OperatingExpenseService.class);
        Call<Void> call = operatingExpenseService.insert(operatingExpense, auth);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    OperatingExpenseActivity.updateRecyclerView();
                    SnackbarUtil.showSuccess(getActivity(), OK_REQUEST_INSERT);
                    clearFields(v);
                } else {
                    SnackbarUtil.showError(getActivity(), BAD_REQUEST_INSERT);
                    Log.e(TAG, operatingExpense.toString());
                    Log.e(TAG, response.toString());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                SnackbarUtil.showSuccess(getActivity(), SERVER_ERROR);
            }
        });
    }
}