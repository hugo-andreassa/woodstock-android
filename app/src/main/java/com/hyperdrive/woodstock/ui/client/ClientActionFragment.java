package com.hyperdrive.woodstock.ui.client;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

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

    private static final String ARG_PARAM = "CLIENT_MODEL";

    private final String SERVER_ERROR = "Erro na comunicação com o servidor";
    private final String BAD_REQUEST_UPDATE = "Erro ao atualizar os dados do Cliente";
    private final String OK_REQUEST_UPDATE = "Cliente atualiza com sucesso";
    private final String BAD_REQUEST_INSERT = "Erro ao inserir os dados do Cliente";
    private final String OK_REQUEST_INSERT = "Cliente inserido com sucesso";

    private ClientModel client;
    private ProgressDialog progressDialog;
    private Preferences sharedPreferences;

    private EditText name;
    private EditText cpfOrCpnj;
    private EditText phone;
    private EditText email;
    private EditText cep;
    private EditText street;
    private EditText city;
    // EditText state = findViewById(R.id.client_state);
    private EditText number;
    private EditText comp;

    public ClientActionFragment() {

    }

    public static ClientActionFragment newInstance(ClientModel client) {
        ClientActionFragment fragment = new ClientActionFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM, client);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            client = (ClientModel) getArguments().getSerializable(ARG_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_action_client, container, false);

        setupEditTexts(v);
        setupSpinnerDropdown(v);
        if(client != null) {
            loadFieldsInformation();
        }
        setupButton(v);

        return v;
    }

    private void setupEditTexts(View v) {

        cpfOrCpnj = v.findViewById(R.id.client_cpf_cnpj);
        cpfOrCpnj.addTextChangedListener(Mask.mask(cpfOrCpnj, Mask.CPF));

        phone = v.findViewById(R.id.client_phone);
        phone.addTextChangedListener(Mask.mask(phone, Mask.PHONE));

        cep = v.findViewById(R.id.client_cep);
        cep.addTextChangedListener(Mask.mask(cep, Mask.CEP));

        name = v.findViewById(R.id.client_name);
        email = v.findViewById(R.id.client_email);
        street = v.findViewById(R.id.client_street);
        city = v.findViewById(R.id.client_city);
        // EditText state = findViewById(R.id.client_state);
        number = v.findViewById(R.id.client_number);
        comp = v.findViewById(R.id.client_comp);
    }

    private void setupSpinnerDropdown(View v) {
        Spinner spinner = v.findViewById(R.id.client_state);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(v.getContext(),
                R.array.state_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
    }


    private void loadFieldsInformation() {
        cpfOrCpnj.setText(client.getCpfOrCnpj());
        phone.setText(client.getPhone());
        name.setText(client.getName());
        email.setText(client.getEmail());

        cep.setText(client.getAddress().getCep());
        street.setText(client.getAddress().getStreet());
        city.setText(client.getAddress().getCity());
        number.setText(client.getAddress().getNumber());
        comp.setText(client.getAddress().getComp());

        // EditText state = findViewById(R.id.client_state);
    }

    private void setupButton(View v) {
        progressDialog = new ProgressDialog(v.getContext());
        progressDialog.setMessage("Carregando....");

        sharedPreferences = new Preferences(v.getContext());

        Button salvarButton = v.findViewById(R.id.client_salvar_button);
        salvarButton.setOnClickListener(view -> {

            ClientModel clientModel = getValuesFromFields();

            if(client != null) {
                if(clientModel != null) {
                    progressDialog.show();
                    updateClientInApi(clientModel, v);
                }
            } else {
                if(clientModel != null) {
                    progressDialog.show();
                    insertClientInAPi(clientModel, v);
                }
            }
        });
    }

    private ClientModel getValuesFromFields() {
        String cpfAux = cpfOrCpnj.getText().toString();
        String phoneAux = phone.getText().toString();
        String nameAux = name.getText().toString();

        if(cpfAux.isEmpty() || phoneAux.isEmpty() || nameAux.isEmpty()) {
            cpfOrCpnj.setError("Esse campo é obrigatório");
            phone.setError("Esse campo é obrigatório");
            name.setError("Esse campo é obrigatório");

            return null;
        } else {
            ClientModel clientModel = new ClientModel();
            clientModel.setId(client.getId());

            clientModel.setCpfOrCnpj(cpfAux);
            clientModel.setPhone(phoneAux);
            clientModel.setName(nameAux);
            clientModel.setEmail(email.getText().toString());
            clientModel.setCompanyId(1L);

            AddressModel addressModel = new AddressModel();
            addressModel.setCep(cep.getText().toString());
            addressModel.setCep(street.getText().toString());
            addressModel.setCep(number.getText().toString());
            addressModel.setCep(comp.getText().toString());
            addressModel.setCep(city.getText().toString());

            clientModel.setAddress(addressModel);

            return clientModel;
        }
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
                    SnackbarUtil.showSuccess(getActivity(), OK_REQUEST_UPDATE);
                } else {
                    SnackbarUtil.showError(getActivity(), BAD_REQUEST_UPDATE);
                    Log.e("CLIENT_ACTION", response.toString());
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
                    SnackbarUtil.showSuccess(getActivity(), OK_REQUEST_INSERT);
                } else {
                    SnackbarUtil.showError(getActivity(), BAD_REQUEST_INSERT);
                    Log.e("CLIENT_ACTION", response.toString());
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