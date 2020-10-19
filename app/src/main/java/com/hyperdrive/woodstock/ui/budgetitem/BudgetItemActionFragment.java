package com.hyperdrive.woodstock.ui.budgetitem;

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
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.hyperdrive.woodstock.R;
import com.hyperdrive.woodstock.api.config.RetrofitConfig;
import com.hyperdrive.woodstock.api.services.BudgetItemService;
import com.hyperdrive.woodstock.api.services.BudgetService;
import com.hyperdrive.woodstock.models.BudgetItemModel;
import com.hyperdrive.woodstock.persistence.Preferences;
import com.hyperdrive.woodstock.ui.budget.BudgetActivity;
import com.hyperdrive.woodstock.utils.Mask;
import com.hyperdrive.woodstock.utils.SnackbarUtil;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BudgetItemActionFragment extends Fragment {

    private static final String TAG = "BDGT_ITM_ACTN_FRAG";

    private final String SERVER_ERROR = "Erro na comunicação com o servidor";

    private final String BAD_REQUEST_UPDATE = "Erro ao atualizar os dados do Item do Orçamento";
    private final String OK_REQUEST_UPDATE = "Item do Orçamento atualizado com sucesso";
    private final String BAD_REQUEST_INSERT = "Erro ao inserir os dados do Item do Orçamento";
    private final String OK_REQUEST_INSERT = "Item do Orçamento inserido com sucesso";

    private static final String ARG_PARAM1 = "budgetId";
    private static final String ARG_PARAM2 = "budgetItem";

    private Long mBudgetId;
    private BudgetItemModel mBudgetItem;

    private TextInputEditText description;
    private TextInputEditText price;
    private TextInputEditText environment;
    private Spinner spinnerStatus;

    private Integer quantity = 1;

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

        sharedPreferences = new Preferences(getContext());
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Carregando...");

        setupSpinnerDropdownStatus(view);
        setupEditTexts(view);
        setupIncrementButtons(view);
        setupSaveButton(view);
        if(mBudgetItem != null) {
            loadFieldsInformation(view);
            setupDeleteButton(view);
        }

        return view;
    }

    private void setupSpinnerDropdownStatus(View v) {
        spinnerStatus = v.findViewById(R.id.budgetitem_status);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(v.getContext(),
                R.array.budgetitem_status_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerStatus.setAdapter(adapter);
    }

    private void setupEditTexts(View v) {
        description = v.findViewById(R.id.budgetitem_description);
        price = v.findViewById(R.id.budgetitem_price);
        price.addTextChangedListener(Mask.moneyMask(price));
        environment = v.findViewById(R.id.budgetitem_environment);
    }

    private void setupIncrementButtons(View v) {
        Button increment = v.findViewById(R.id.budgetitem_quantity_more_button);
        Button decrement = v.findViewById(R.id.budgetitem_quantity_less_button);

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

    private void setupDeleteButton(View v) {
        Button button = v.findViewById(R.id.budgetitem_delete_button);
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(view -> {
            deleteBudgetItem(v);
        });
    }

    private void displayQuantity(int number, View view) {
        TextView quantityTextView = view.findViewById(R.id.budgetitem_quantity);
        quantityTextView.setText(String.valueOf(number));
    }

    private void setupSaveButton(View v) {
        Button saveButton = v.findViewById(R.id.budgetitem_save_button);
        saveButton.setOnClickListener(view -> {
            BudgetItemModel budgetItem = getValuesFromFields();

            if(budgetItem != null) {
                if (mBudgetItem != null) {
                    progressDialog.show();

                    budgetItem.setId(mBudgetItem.getId());
                    updateBudgetItemInApi(budgetItem, v);
                } else {
                    progressDialog.show();
                    insertBudgetItemInAPi(budgetItem, v);
                }
            }
        });
    }

    private boolean validateFields() {
        if (Objects.equals(price.getText().toString(), null)) {
            return false;
        } else {
            String desc = description.getText().toString();
            Double prc = Mask.unmaskMoney(price.getText().toString());

            if(desc.isEmpty() || prc < 1) {
                String error = getActivity().getResources().getString(R.string.campo_obrigatorio);
                description.setError(error);
                price.setError(error + ", o valor digitado não pode ser menor do que 1");

                return false;
            }
            return true;
        }
    }

    private void loadFieldsInformation(View v) {
        description.setText(mBudgetItem.getDescription());
        price.setText(String.valueOf(mBudgetItem.getPrice() * 10));
        environment.setText(mBudgetItem.getEnvironment());

        quantity = mBudgetItem.getQuantity();
        displayQuantity(mBudgetItem.getQuantity(), v);
    }

    private BudgetItemModel getValuesFromFields() {
        if(validateFields()) {
            BudgetItemModel budgetItem = new BudgetItemModel();

            budgetItem.setBudgetId(mBudgetId);
            budgetItem.setDescription(description.getText().toString());
            budgetItem.setPrice(Mask.unmaskMoney(price.getText().toString()));
            Log.e(TAG, Mask.unmaskMoney(price.getText().toString()).toString());
            budgetItem.setQuantity(quantity);
            budgetItem.setEnvironment(environment.getText().toString());
            budgetItem.setStatus(spinnerStatus.getSelectedItem().toString());

            return budgetItem;
        }

        return null;
    }

    private void clearFields(View v) {
        description.setText("");
        price.setText("");
        environment.setText("");

        quantity = 1;
        displayQuantity(1, v);
    }

    private void insertBudgetItemInAPi(BudgetItemModel budgetItem, View v) {
        String auth = sharedPreferences.getAuthentication();

        BudgetItemService budgetItemService = RetrofitConfig.getRetrofitInstance().create(BudgetItemService.class);
        Call<Void> call = budgetItemService.insert(budgetItem, auth);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                progressDialog.dismiss();
                if(response.isSuccessful()) {
                    BudgetItemActivity.updateRecyclerView();
                    SnackbarUtil.showSuccess(getActivity(), OK_REQUEST_INSERT);
                    clearFields(v);
                } else {
                    SnackbarUtil.showError(getActivity(), BAD_REQUEST_INSERT);
                    Log.e(TAG, response.toString());
                    Log.e(TAG, budgetItem.toString());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                progressDialog.dismiss();
                SnackbarUtil.showError(getActivity(), SERVER_ERROR);
                Log.e(TAG, budgetItem.toString());
            }
        });
    }


    private void updateBudgetItemInApi(BudgetItemModel budgetItem, View v) {
        String auth = sharedPreferences.getAuthentication();

        BudgetItemService budgetItemService = RetrofitConfig.getRetrofitInstance().create(BudgetItemService.class);
        Call<Void> call = budgetItemService.update(budgetItem.getId(), budgetItem, auth);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                progressDialog.dismiss();
                if(response.isSuccessful()) {
                    BudgetItemActivity.updateRecyclerView();
                    SnackbarUtil.showSuccess(getActivity(), OK_REQUEST_UPDATE);
                } else {
                    SnackbarUtil.showError(getActivity(), BAD_REQUEST_UPDATE);
                    Log.e(TAG, response.toString());
                    Log.e(TAG, budgetItem.toString());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                progressDialog.dismiss();
                SnackbarUtil.showError(getActivity(), SERVER_ERROR);
                Log.e(TAG, budgetItem.toString());
            }
        });
    }

    private void deleteBudgetItem(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setMessage("Deseja mesmo excluir este Item do Orçamento?")
                .setPositiveButton("Sim", (dialog, id) -> {
                    deleteBudgetItemFromApi(mBudgetItem.getId(), v);
                })
                .setNegativeButton("Cancelar", (dialog, id) -> {
                    dialog.dismiss();
                });

        Dialog dialog = builder.create();
        dialog.show();
    }

    private void deleteBudgetItemFromApi(Long id, View v) {
        String auth = sharedPreferences.getAuthentication();

        BudgetItemService budgetItemService = RetrofitConfig.getRetrofitInstance().create(BudgetItemService.class);
        Call<Void> call = budgetItemService.delete(id, auth);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                progressDialog.dismiss();
                if(response.isSuccessful()) {
                    BudgetItemActivity.updateRecyclerView();
                    getActivity().getSupportFragmentManager().popBackStackImmediate();
                } else {
                    SnackbarUtil.showError(getActivity(), BAD_REQUEST_UPDATE);
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