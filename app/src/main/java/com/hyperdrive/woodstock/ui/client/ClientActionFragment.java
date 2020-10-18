package com.hyperdrive.woodstock.ui.client;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.hyperdrive.woodstock.R;
import com.hyperdrive.woodstock.api.config.RetrofitConfig;
import com.hyperdrive.woodstock.api.services.ClientService;
import com.hyperdrive.woodstock.models.AddressModel;
import com.hyperdrive.woodstock.models.ClientModel;
import com.hyperdrive.woodstock.persistence.Preferences;
import com.hyperdrive.woodstock.utils.Mask;
import com.hyperdrive.woodstock.utils.SnackbarUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ClientActionFragment extends Fragment {

    private static final String ARG_PARAM1 = "client";
    private static final String TAG = "CLIENT_ACTION_FRAGMENT";

    private final String SERVER_ERROR = "Erro na comunicação com o servidor";
    private final String SERVER_ERROR_DELETE = "Erro ao deletar Cliente...";
    private final String BAD_REQUEST_UPDATE = "Erro ao atualizar os dados do Cliente";
    private final String OK_REQUEST_UPDATE = "Cliente atualizado com sucesso";
    private final String BAD_REQUEST_INSERT = "Erro ao inserir os dados do Cliente";
    private final String OK_REQUEST_INSERT = "Cliente inserido com sucesso";

    private ClientModel client;
    private ProgressDialog progressDialog;
    private Preferences sharedPreferences;

    private EditText name;
    private EditText cpfOrCpnj;
    private EditText phone;
    private EditText email;

    public ClientActionFragment() {

    }

    public static ClientActionFragment newInstance(ClientModel client) {
        ClientActionFragment fragment = new ClientActionFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, client);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            client = (ClientModel) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_action_client, container, false);

        sharedPreferences = new Preferences(getContext());
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Carregando...");

        setupEditTexts(v);
        if(client != null) {
            loadFieldsInformation();
            setupDeleteButton(v);
        }

        setupSaveButton(v);

        return v;
    }

    private void setupDeleteButton(View v) {
        Button button = v.findViewById(R.id.client_deletar_button);
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(view -> {
            deleteClient(v);
        });
    }

    private void setupEditTexts(View v) {
        name = v.findViewById(R.id.client_name);
        email = v.findViewById(R.id.client_email);

        cpfOrCpnj = v.findViewById(R.id.client_cpf_cnpj);
        cpfOrCpnj.addTextChangedListener(Mask.mask(cpfOrCpnj, Mask.CPF));

        phone = v.findViewById(R.id.client_phone);
        phone.addTextChangedListener(Mask.mask(phone, Mask.PHONE));
    }

    private void loadFieldsInformation() {
        cpfOrCpnj.setText(client.getCpfOrCnpj());
        phone.setText(client.getPhone());
        name.setText(client.getName());
        email.setText(client.getEmail());
    }

    private void setupSaveButton(View v) {
        Button salvarButton = v.findViewById(R.id.client_salvar_button);
        salvarButton.setOnClickListener(view -> {
            ClientModel clientModel = getValuesFromFields();

            if(clientModel != null) {
                if (client != null) {

                    clientModel.setId(client.getId());

                    progressDialog.show();
                    updateClientInApi(clientModel, v);
                } else {
                    progressDialog.show();
                    insertClientInAPi(clientModel, v);
                }
            }
        });
    }

    private ClientModel getValuesFromFields() {
        if(validateFields()) {
            ClientModel clientModel = new ClientModel();

            clientModel.setCpfOrCnpj(Mask.unmask(cpfOrCpnj.getText().toString()));
            clientModel.setPhone(Mask.unmask(phone.getText().toString()));
            clientModel.setName(name.getText().toString());
            clientModel.setEmail(email.getText().toString());
            clientModel.setCompanyId(1L);

            AddressModel addressModel = new AddressModel();
            clientModel.setAddress(addressModel);

            return clientModel;
        }

        return null;
    }

    private boolean validateFields() {
        String cpfAux = cpfOrCpnj.getText().toString();
        String phoneAux = phone.getText().toString();
        String nameAux = name.getText().toString();

        if(cpfAux.isEmpty() || phoneAux.isEmpty() || nameAux.isEmpty()) {
            cpfOrCpnj.setError("Esse campo é obrigatório");
            phone.setError("Esse campo é obrigatório");
            name.setError("Esse campo é obrigatório");

            return false;
        }

        return true;
    }

    private void updateClientInApi(ClientModel client, View v) {
        String auth = sharedPreferences.getAuthentication();

        ClientService clientService = RetrofitConfig.getRetrofitInstance().create(ClientService.class);
        Call<Void> call = clientService.update(client.getId(), client, auth);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                progressDialog.dismiss();

                if(response.isSuccessful()) {
                    ClientActivity.updateRecyclerView();
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

    private void insertClientInAPi(ClientModel client, View v) {
        String auth = sharedPreferences.getAuthentication();

        ClientService clientService = RetrofitConfig.getRetrofitInstance().create(ClientService.class);
        Call<Void> call = clientService.insert(client, auth);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                progressDialog.dismiss();

                if(response.isSuccessful()) {
                    ClientActivity.updateRecyclerView();
                    SnackbarUtil.showSuccess(getActivity(), OK_REQUEST_INSERT);
                } else {
                    SnackbarUtil.showError(getActivity(), BAD_REQUEST_INSERT);
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

    private void deleteClient(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setMessage("Deseja mesmo excluir este cadastro?")
                .setPositiveButton("Sim", (dialog, id) -> {
                    deleteClientFromApi(client.getId(), v);
                })
                .setNegativeButton("Cancelar", (dialog, id) -> {
                    dialog.dismiss();
                });

        Dialog dialog = builder.create();
        dialog.show();
    }

    private void deleteClientFromApi(Long id, View v) {
        String auth = sharedPreferences.getAuthentication();

        ClientService clientService = RetrofitConfig.getRetrofitInstance().create(ClientService.class);
        Call<Void> call = clientService.delete(id, auth);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()) {
                    ClientActivity.updateRecyclerView();
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
}