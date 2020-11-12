package com.hyperdrive.woodstock.ui.material;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.hyperdrive.woodstock.R;
import com.hyperdrive.woodstock.api.config.RetrofitConfig;
import com.hyperdrive.woodstock.api.services.CuttingPlanService;
import com.hyperdrive.woodstock.api.services.MaterialService;
import com.hyperdrive.woodstock.models.MaterialModel;
import com.hyperdrive.woodstock.persistence.Preferences;
import com.hyperdrive.woodstock.ui.cuttingplan.CuttingPlanActivity;
import com.hyperdrive.woodstock.utils.SnackbarUtil;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MaterialActionFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private String TAG = "MATERIAL_ACTION_FRAG";

    private static final String ARG_PARAM1 = "companyId";
    private static final String ARG_PARAM2 = "material";

    private final String SERVER_ERROR = "Erro na comunicação com o servidor";
    private final String BAD_REQUEST_UPDATE = "Erro ao atualizar os dados do Material";
    private final String OK_REQUEST_UPDATE = "Material atualizada com sucesso";
    private final String BAD_REQUEST_INSERT = "Erro ao inserir os dados do Material";
    private final String OK_REQUEST_INSERT = "Material inserido com sucesso";

    private Long mCompanyId;
    private MaterialModel mMaterial;

    private TextInputEditText name;
    private TextInputEditText description;
    private String unit;
    private Integer stock = 1;
    private Integer minimumStock = 1;

    private Preferences sharedPreferences;
    private ProgressDialog progressDialog;

    public MaterialActionFragment() {

    }

    public static MaterialActionFragment newInstance(Long mCompanyId, MaterialModel mMaterial) {
        MaterialActionFragment fragment = new MaterialActionFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_PARAM1, mCompanyId);
        args.putSerializable(ARG_PARAM2, mMaterial);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCompanyId = getArguments().getLong(ARG_PARAM1);
            mMaterial = (MaterialModel) getArguments().getSerializable(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_material_action, container, false);

        sharedPreferences = new Preferences(getContext());
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Carregando...");

        setupSpinnerDropDownUnit(view);
        setupIncrementButtons(view);
        setupEditTexts(view);
        setupSaveButton(view);
        if(mMaterial != null) {
            loadFieldsInformation(view);
            setupDeleteButton(view);
        }

        return view;
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        this.unit = parent.getItemAtPosition(pos).toString();
    }

    public void onNothingSelected(AdapterView<?> parent) {
        return;
    }

    private void setupSpinnerDropDownUnit(View v) {
        Spinner spinnerUnit = v.findViewById(R.id.material_unit);
        spinnerUnit.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(v.getContext(),
                R.array.material_unit_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUnit.setAdapter(adapter);

        if(mMaterial != null) {
            List<String> list = Arrays.asList(
                    v.getContext().getResources().getStringArray(R.array.material_unit_array));
            for (int i=0; i<=list.size(); i++) {
                String str = list.get(i);
                if(str.substring(0, 2).trim().equals(mMaterial.getUnit())) {
                    spinnerUnit.setSelection(i);
                    break;
                }
            }
        }
    }

    private void setupDeleteButton(View view) {
        Button deleteButton = view.findViewById(R.id.material_delete_button);
        deleteButton.setVisibility(View.VISIBLE);

        deleteButton.setOnClickListener(v -> {
            deleteMaterial(view);
        });
    }

    private void setupSaveButton(View view) {
        Button saveButton = view.findViewById(R.id.material_save_button);

        saveButton.setOnClickListener(v -> {
            MaterialModel material = getValuesFromFields();
            if(material == null) {
                return;
            }

            progressDialog.show();
            if(mMaterial != null) {
                material.setId(mMaterial.getId());
                updateMaterialInApi(material, view);
            } else {
                insertMaterialInApi(material, view);
            }
        });
    }

    private MaterialModel getValuesFromFields() {

        if(validateFields()) {
            MaterialModel material = new MaterialModel();
            material.setCompanyId(mCompanyId);

            material.setName(name.getText().toString());
            material.setDescription(description.getText().toString());
            material.setLastUpdate(null);
            material.setStock(stock);
            material.setMinimumStock(minimumStock);
            material.setUnit(unit.substring(0, 2).trim());

            return material;
        }

        return null;
    }

    private boolean validateFields() {
        if(name.getText().toString().isEmpty()) {
            String error = getActivity().getResources().getString(R.string.campo_obrigatorio);
            name.setError(error);

            return false;
        }

        return true;
    }

    private void loadFieldsInformation(View view) {
        name.setText(mMaterial.getName());
        description.setText(mMaterial.getDescription());
        stock = mMaterial.getStock();
        minimumStock = mMaterial.getMinimumStock();

        displayStock(stock, view);
        displayMinimumStock(minimumStock, view);
    }

    private void setupEditTexts(View v) {
        name = v.findViewById(R.id.material_name);
        description = v.findViewById(R.id.material_description);
    }

    private void setupIncrementButtons(View v) {
        Button incrementStock = v.findViewById(R.id.material_stock_more_button);
        Button decrementStock = v.findViewById(R.id.material_stock_less_button);

        displayStock(stock, v);
        displayMinimumStock(minimumStock, v);

        incrementStock.setOnClickListener((view) -> {
            Log.e(TAG, stock.toString());
            Log.e(TAG, minimumStock.toString());

            if(stock < 500) {
                stock = stock + 1;
                displayStock(stock, v);
            }
        });

        decrementStock.setOnClickListener((view) -> {
            if(stock > 0) {
                stock = stock - 1;
                displayStock(stock, v);
            }
        });

        Button incrementMinimumStock = v.findViewById(R.id.material_stock_minimum_more_button);
        Button decrementMinimumStock = v.findViewById(R.id.material_stock_minimum_less_button);
        incrementMinimumStock.setOnClickListener((view) -> {
            if(minimumStock < 500) {
                minimumStock = minimumStock + 1;
                displayMinimumStock(minimumStock, v);
            }
        });

        decrementMinimumStock.setOnClickListener((view) -> {
            if(minimumStock > 0) {
                minimumStock = minimumStock - 1;
                displayMinimumStock(minimumStock, v);
            }
        });
    }

    private void displayStock(int number, View view) {
        TextView materialTextView = view.findViewById(R.id.material_stock);
        materialTextView.setText(String.valueOf(number));
    }

    private void displayMinimumStock(int number, View view) {
        TextView materialTextView = view.findViewById(R.id.material_stock_minimum);
        materialTextView.setText(String.valueOf(number));
    }

    private void clearFields(View view) {
        name.setText("");
        description.setText("");
        stock = 0;
        minimumStock = 0;

        displayStock(stock, view);
        displayMinimumStock(minimumStock, view);
    }

    private void insertMaterialInApi(MaterialModel material, View view) {
        String auth = sharedPreferences.getAuthentication();

        MaterialService materialService = RetrofitConfig
                .getRetrofitInstance().create(MaterialService.class);
        Call<Void> call = materialService.insert(material, auth);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                progressDialog.dismiss();
                if(response.isSuccessful()) {
                    MaterialActivity.updateRecyclerView();
                    SnackbarUtil.showSuccess(getActivity(), OK_REQUEST_INSERT);
                    clearFields(view);
                } else {
                    SnackbarUtil.showError(getActivity(), BAD_REQUEST_INSERT);
                    Log.e(TAG, material.toString());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                progressDialog.dismiss();
                SnackbarUtil.showError(getActivity(), SERVER_ERROR);
                Log.e(TAG, t.getMessage());
            }
        });
    }

    private void updateMaterialInApi(MaterialModel material, View view) {
        String auth = sharedPreferences.getAuthentication();

        MaterialService materialService = RetrofitConfig
                .getRetrofitInstance().create(MaterialService.class);
        Call<Void> call = materialService.update(material.getId(), material, auth);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                progressDialog.dismiss();
                if(response.isSuccessful()) {
                    MaterialActivity.updateRecyclerView();
                    SnackbarUtil.showSuccess(getActivity(), OK_REQUEST_UPDATE);
                } else {
                    SnackbarUtil.showError(getActivity(), BAD_REQUEST_UPDATE);
                    Log.e(TAG, material.toString());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                progressDialog.dismiss();
                SnackbarUtil.showError(getActivity(), SERVER_ERROR);
                Log.e(TAG, t.getMessage());
            }
        });
    }

    private void deleteMaterial(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setMessage("Deseja mesmo excluir este Material?")
                .setPositiveButton("Sim", (dialog, id) -> {
                    deleteBudgetItemFromApi(mMaterial.getId());
                })
                .setNegativeButton("Cancelar", (dialog, id) -> {
                    dialog.dismiss();
                });

        Dialog dialog = builder.create();
        dialog.show();
    }

    private void deleteBudgetItemFromApi(Long id) {
        String auth = sharedPreferences.getAuthentication();

        MaterialService materialService = RetrofitConfig.getRetrofitInstance().create(MaterialService.class);
        Call<Void> call = materialService.delete(id, auth);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                progressDialog.dismiss();
                if(response.isSuccessful()) {
                    MaterialActivity.updateRecyclerView();
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