package com.hyperdrive.woodstock.ui.budget;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.hyperdrive.woodstock.R;
import com.hyperdrive.woodstock.api.config.RetrofitConfig;
import com.hyperdrive.woodstock.api.services.BudgetService;
import com.hyperdrive.woodstock.api.services.ClientService;
import com.hyperdrive.woodstock.models.AddressModel;
import com.hyperdrive.woodstock.models.BudgetModel;
import com.hyperdrive.woodstock.models.ClientModel;
import com.hyperdrive.woodstock.persistence.Preferences;
import com.hyperdrive.woodstock.utils.DateUtil;
import com.hyperdrive.woodstock.utils.Mask;
import com.hyperdrive.woodstock.utils.SnackbarUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BudgetActionFragment extends Fragment {

    private final String SERVER_ERROR = "Erro na comunicação com o servidor";
    private final String BAD_REQUEST_UPDATE = "Erro ao atualizar os dados do Orçamento";
    private final String OK_REQUEST_UPDATE = "Orçamento atualizado com sucesso";
    private final String BAD_REQUEST_INSERT = "Erro ao inserir os dados do Orçamento";
    private final String OK_REQUEST_INSERT = "Orçamento inserido com sucesso";

    private static final String TAG = "BUDGET_ACTION_FRAGMENT";
    private static final String ARG1 = "clientId";
    private static final String ARG2 = "budget";

    private BudgetModel budget;
    private Long clientId;

    private EditText deadline;
    private EditText deliveryDay;
    private EditText paymentMethod;
    private Spinner spinnerStatus;
    private EditText cep;
    private EditText street;
    private EditText city;
    private Spinner spinnerEstados;
    private EditText number;
    private EditText comp;

    private ProgressDialog progressDialog;
    private Preferences sharedPreferences;
    private DatePickerDialog datepicker;

    public BudgetActionFragment() {
    }

    public static BudgetActionFragment newInstance(BudgetModel budget, Long clientId) {
        BudgetActionFragment fragment = new BudgetActionFragment();
        Bundle args = new Bundle();
        args.putLong(ARG1, clientId);
        args.putSerializable(ARG2, budget);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            clientId = getArguments().getLong(ARG1);
            budget = (BudgetModel) getArguments().getSerializable(ARG2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_budget_action, container, false);

        setupSpinnerDropdownStatus(v);
        setupSpinnerDropdownEstados(v);

        setupEditTexts(v);
        if(budget != null) {
            loadFieldsInformation();
        }
        setupSaveButton(v);

        return v;
    }

    private void setupSaveButton(View v) {
        progressDialog = new ProgressDialog(v.getContext());
        progressDialog.setMessage("Carregando....");

        sharedPreferences = new Preferences(v.getContext());

        Button saveButton = v.findViewById(R.id.budget_save_button);
        saveButton.setOnClickListener(view -> {
            BudgetModel budgetModel = getValuesFromFields();

            if(budgetModel != null) {
                if (budget != null) {
                    budgetModel.setId(budget.getId());

                    progressDialog.show();
                    updateBudgetInApi(budgetModel, v);
                } else {
                    progressDialog.show();
                    insertBudgetInAPi(budgetModel, v);
                }
            }
        });
    }

    private void setupSpinnerDropdownStatus(View v) {
        spinnerStatus = v.findViewById(R.id.budget_status);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(v.getContext(),
                R.array.status_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerStatus.setAdapter(adapter);
    }

    private void setupSpinnerDropdownEstados(View v) {
        spinnerEstados = v.findViewById(R.id.budget_state);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(v.getContext(),
                R.array.state_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerEstados.setAdapter(adapter);
    }

    private void setupEditTexts(View v) {
        deadline = v.findViewById(R.id.budget_deadline);
        deliveryDay = v.findViewById(R.id.budget_delivery_day);
        deliveryDay.setInputType(InputType.TYPE_NULL);
        deliveryDay.setOnClickListener(view -> {
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);

            datepicker = new DatePickerDialog(getActivity(),
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            deliveryDay.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                        }
                    }, year, month, day);
            datepicker.show();
        });

        paymentMethod = v.findViewById(R.id.budget_payment_method);
        paymentMethod.setText("Entrada de 30% e restante em até 10x no cartão.");

        cep = v.findViewById(R.id.budget_cep);
        cep.addTextChangedListener(Mask.mask(cep, Mask.CEP));

        street = v.findViewById(R.id.budget_street);
        city = v.findViewById(R.id.budget_city);
        number = v.findViewById(R.id.budget_number);
        comp = v.findViewById(R.id.budget_comp);
    }

    private void loadFieldsInformation() {
        deadline.setText(String.valueOf(budget.getDeadline()));

        String date = DateUtil.formatDateFromServer(
                budget.getDeliveryDay(),
                DateUtil.DD_MM_YYYY);
        deliveryDay.setText(date);
        paymentMethod.setText(budget.getPaymentMethod());

        cep.setText(budget.getAddress().getCep());
        street.setText(budget.getAddress().getStreet());
        city.setText(budget.getAddress().getCity());
        number.setText(budget.getAddress().getNumber());
        comp.setText(budget.getAddress().getComp());
    }

    private BudgetModel getValuesFromFields() {

        if(validateFields()) {
            BudgetModel budget = new BudgetModel();

            budget.setClientId(clientId);
            budget.setDeadline(Integer.parseInt(deadline.getText().toString()));
            String date = DateUtil.formatDateToServer(
                    deliveryDay.getText().toString(),
                    DateUtil.DD_MM_YYYY);
            budget.setDeliveryDay(date);
            budget.setPaymentMethod(paymentMethod.getText().toString());
            budget.setStatus(spinnerStatus.getSelectedItem().toString());

            AddressModel addressModel = new AddressModel();
            addressModel.setCep(Mask.unmask(cep.getText().toString()));
            addressModel.setStreet(street.getText().toString());
            addressModel.setCity(city.getText().toString());

            addressModel.setState(spinnerEstados.getSelectedItem().toString());

            addressModel.setNumber(number.getText().toString());
            addressModel.setComp(comp.getText().toString());

            budget.setAddress(addressModel);

            return budget;
        }

        return null;
    }

    private boolean validateFields() {
        String deadlineAux = deadline.getText().toString();
        String paymentMethodAux = paymentMethod.getText().toString();

        if(deadlineAux.isEmpty() || paymentMethodAux.isEmpty()) {
            deadline.setError("Esse campo é obrigatório");
            paymentMethod.setError("Esse campo é obrigatório");

            return false;
        }

        return true;
    }

    private void updateBudgetInApi(BudgetModel budget, View v) {
        String auth = sharedPreferences.getAuthentication();

        BudgetService budgetService = RetrofitConfig.getRetrofitInstance().create(BudgetService.class);
        Call<Void> call = budgetService.update(budget.getId(), budget, auth);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                progressDialog.dismiss();
                if(response.isSuccessful()) {
                    SnackbarUtil.showSuccess(getActivity(), OK_REQUEST_UPDATE);
                } else {
                    SnackbarUtil.showError(getActivity(), BAD_REQUEST_UPDATE);
                    Log.e(TAG, response.toString());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                progressDialog.dismiss();
                SnackbarUtil.showError(getActivity(), SERVER_ERROR);
            }
        });
    }

    private void insertBudgetInAPi(BudgetModel budget, View v) {
        String auth = sharedPreferences.getAuthentication();

        BudgetService budgetService = RetrofitConfig.getRetrofitInstance().create(BudgetService.class);
        Call<Void> call = budgetService.insert(budget, auth);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                progressDialog.dismiss();
                if(response.isSuccessful()) {
                    SnackbarUtil.showSuccess(getActivity(), OK_REQUEST_INSERT);
                } else {
                    SnackbarUtil.showError(getActivity(), BAD_REQUEST_INSERT);
                    Log.e(TAG, response.toString());
                    Log.e(TAG, budget.toString());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                progressDialog.dismiss();
                SnackbarUtil.showError(getActivity(), SERVER_ERROR);
            }
        });
    }
}