package com.hyperdrive.woodstock.ui.budget;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.StrictMode;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;

import com.google.android.material.textfield.TextInputEditText;
import com.hyperdrive.woodstock.R;
import com.hyperdrive.woodstock.api.config.RetrofitConfig;
import com.hyperdrive.woodstock.api.services.BudgetService;
import com.hyperdrive.woodstock.models.AddressModel;
import com.hyperdrive.woodstock.models.BudgetModel;
import com.hyperdrive.woodstock.persistence.Preferences;
import com.hyperdrive.woodstock.utils.DateUtil;
import com.hyperdrive.woodstock.utils.Mask;
import com.hyperdrive.woodstock.utils.SnackbarUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BudgetActionFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "BUDGET_ACTION_FRAGMENT";

    private final String SERVER_ERROR = "Erro na comunicação com o servidor";
    private final String SERVER_ERROR_DELETE = "Erro ao deletar Orçamento...";
    private final String BAD_REQUEST_UPDATE = "Erro ao atualizar os dados do Orçamento";
    private final String OK_REQUEST_UPDATE = "Orçamento atualizado com sucesso";
    private final String BAD_REQUEST_INSERT = "Erro ao inserir os dados do Orçamento";
    private final String OK_REQUEST_INSERT = "Orçamento inserido com sucesso";

    private static final String ARG_PARAM1 = "clientId";
    private static final String ARG_PARAM2 = "budget";

    private BudgetModel mBudget;
    private Long mClientId;

    private TextInputEditText deadline;
    private TextInputEditText deliveryDay;
    private TextInputEditText paymentMethod;
    private String status;
    private TextInputEditText cep;
    private TextInputEditText street;
    private TextInputEditText city;
    private Spinner spinnerEstados;
    private TextInputEditText number;
    private TextInputEditText comp;

    private ProgressDialog progressDialog;
    private Preferences sharedPreferences;
    private DatePickerDialog datepicker;

    public BudgetActionFragment() {
    }

    public static BudgetActionFragment newInstance(BudgetModel budget, Long clientId) {
        BudgetActionFragment fragment = new BudgetActionFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_PARAM1, clientId);
        args.putSerializable(ARG_PARAM2, budget);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mClientId = getArguments().getLong(ARG_PARAM1);
            mBudget = (BudgetModel) getArguments().getSerializable(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_budget_action, container, false);

        progressDialog = new ProgressDialog(v.getContext());
        progressDialog.setMessage("Carregando....");
        sharedPreferences = new Preferences(v.getContext());

        setupSpinnerDropdownStatus(v);
        setupSpinnerDropdownEstados(v);

        setupEditTexts(v);
        if(mBudget != null) {
            loadFieldsInformation();
            setupTotalField(v);
            setupDeleteButton(v);
            setupPdfButton(v);
        }

        setupSaveButton(v);

        return v;
    }

    private void setupTotalField(View v) {
        if(mBudget.getTotal() > 0) {
            View linear = v.findViewById(R.id.budget_total_linear_layout);
            linear.setVisibility(View.VISIBLE);

            TextInputEditText total = v.findViewById(R.id.budget_total);
            total.addTextChangedListener(Mask.moneyMask(total));
            total.setText(String.valueOf(mBudget.getTotal() * 10));
        }
    }

    private void setupPdfButton(View v) {
        Button pdfButton = v.findViewById(R.id.budget_pdf_button);
        pdfButton.setVisibility(View.VISIBLE);
        pdfButton.setOnClickListener(view -> {
            downloadPdfFromApi(v);
        });
    }

    private void setupDeleteButton(View v) {
        Button button = v.findViewById(R.id.budget_delete_button);
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(view -> {
            deleteBudget(v);
        });
    }

    private void setupSaveButton(View v) {
        Button saveButton = v.findViewById(R.id.budget_save_button);
        saveButton.setOnClickListener(view -> {
            BudgetModel budgetModel = getValuesFromFields();

            if(budgetModel != null) {
                if (mBudget != null) {
                    budgetModel.setId(mBudget.getId());

                    progressDialog.show();
                    updateBudgetInApi(budgetModel, v);
                } else {
                    progressDialog.show();
                    insertBudgetInAPi(budgetModel, v);
                }
            }
        });
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        this.status = parent.getItemAtPosition(pos).toString();
    }

    public void onNothingSelected(AdapterView<?> parent) {
        return;
    }

    private void setupSpinnerDropdownStatus(View v) {
        Spinner spinnerStatus = v.findViewById(R.id.budget_status);
        spinnerStatus.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(v.getContext(),
                R.array.budget_status_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(adapter);

        if(mBudget != null) {
            List<String> list = Arrays.asList(
                    v.getContext().getResources().getStringArray(R.array.budget_status_array));
            for (int i=0; i<=list.size(); i++) {
                String str = list.get(i);
                if(str.equals(mBudget.getStatus())) {
                    spinnerStatus.setSelection(i);
                    break;
                }
            }
        }
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
        deadline.setText(String.valueOf(mBudget.getDeadline()));

        String date = DateUtil.formatDateFromServer(
                mBudget.getDeliveryDay(),
                DateUtil.DD_MM_YYYY);
        deliveryDay.setText(date);
        paymentMethod.setText(mBudget.getPaymentMethod());

        cep.setText(mBudget.getAddress().getCep());
        street.setText(mBudget.getAddress().getStreet());
        city.setText(mBudget.getAddress().getCity());
        number.setText(mBudget.getAddress().getNumber());
        comp.setText(mBudget.getAddress().getComp());
    }

    private void clearFields() {
        deadline.setText("");
        deliveryDay.setText("");
        paymentMethod.setText("");

        cep.setText("");
        street.setText("");
        city.setText("");
        number.setText("");
        comp.setText("");
    }

    private BudgetModel getValuesFromFields() {

        if(validateFields()) {
            BudgetModel budget = new BudgetModel();

            budget.setClientId(mClientId);
            budget.setDeadline(Integer.parseInt(deadline.getText().toString()));
            String date = DateUtil.formatDateToServer(
                    deliveryDay.getText().toString(),
                    DateUtil.DD_MM_YYYY);
            budget.setDeliveryDay(date);
            budget.setPaymentMethod(paymentMethod.getText().toString());
            budget.setStatus(status);

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
            String erro = getActivity().getResources().getString(R.string.campo_obrigatorio);

            deadline.setError(erro);
            paymentMethod.setError(erro);

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
                    BudgetActivity.updateRecyclerView();
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
                    BudgetActivity.updateRecyclerView();
                    SnackbarUtil.showSuccess(getActivity(), OK_REQUEST_INSERT);
                    clearFields();
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

    private void deleteBudget(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setMessage("Deseja mesmo excluir este orçamento?")
                .setPositiveButton("Sim", (dialog, id) -> {
                    deleteBudgetFromApi(mBudget.getId(), v);
                })
                .setNegativeButton("Cancelar", (dialog, id) -> {
                    dialog.dismiss();
                });

        Dialog dialog = builder.create();
        dialog.show();
    }

    private void deleteBudgetFromApi(Long id, View v) {
        String auth = sharedPreferences.getAuthentication();

        BudgetService budgetService = RetrofitConfig.getRetrofitInstance().create(BudgetService.class);
        Call<Void> call = budgetService.delete(id, auth);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()) {
                    BudgetActivity.updateRecyclerView();
                    getActivity().getSupportFragmentManager().popBackStackImmediate();
                } else {
                    SnackbarUtil.showError((AppCompatActivity) v.getContext(), SERVER_ERROR_DELETE);
                    Log.e(TAG, response.toString());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                SnackbarUtil.showError((AppCompatActivity) v.getContext(), SERVER_ERROR_DELETE);
                Log.e(TAG, t.getMessage());
            }
        });
    }

    private void downloadPdfFromApi(View v) {
        String auth = sharedPreferences.getAuthentication();

        Log.e(TAG, mClientId + " - " + mBudget.getId() + " - "  + auth);

        BudgetService budgetService = RetrofitConfig.getRetrofitInstance().create(BudgetService.class);
        Call<ResponseBody> call = budgetService.downloadPdf(1l, mClientId, mBudget.getId(), auth);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    allowStrictMode();
                    SnackbarUtil.showSuccess((AppCompatActivity) v.getContext(), "Download em andamento...");
                    boolean write = writeResponseBodyToDisk(response.body());
                } else {
                    SnackbarUtil.showError((AppCompatActivity) v.getContext(), SERVER_ERROR);
                    Log.e(TAG, response.toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                SnackbarUtil.showError((AppCompatActivity) v.getContext(), SERVER_ERROR);
                Log.e(TAG, t.getMessage());
            }
        });

    }

    private boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            String clientName = getActivity().getTitle().toString().substring(11);

            File destinationFile = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    "Orçamento " + clientName + ".pdf");
            Log.e(TAG, "OK");

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;
                Log.e(TAG, "OK");

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(destinationFile);

                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
                    Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                }
                outputStream.flush();
                Log.e(TAG, "OK");

                return true;
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            return false;
        }
    }

    private void allowStrictMode() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
}