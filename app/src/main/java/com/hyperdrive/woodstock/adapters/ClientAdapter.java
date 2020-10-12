package com.hyperdrive.woodstock.adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.hyperdrive.woodstock.R;
import com.hyperdrive.woodstock.api.config.RetrofitConfig;
import com.hyperdrive.woodstock.api.services.ClientService;
import com.hyperdrive.woodstock.holders.ClientHolder;
import com.hyperdrive.woodstock.models.ClientModel;
import com.hyperdrive.woodstock.ui.client.ClientActionFragment;
import com.hyperdrive.woodstock.ui.client.ClientActivity;
import com.hyperdrive.woodstock.utils.SnackbarUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientAdapter extends RecyclerView.Adapter<ClientHolder> {

    private final String SERVER_ERROR_DELETE = "Erro ao deletar Cliente...";
    private static final String TAG = "CLIENT_ACTIVITY";

    private final List<ClientModel> mClients;
    private final String auth;

    public ClientAdapter(List<ClientModel> mClients, String auth) {
        this.mClients = mClients;
        this.auth = auth;
    }

    @NonNull
    @Override
    public ClientHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ClientHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_client, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ClientHolder holder, int position) {
        ClientModel client = mClients.get(position);
        holder.title.setText(client.getName());

        holder.budgetButton.setOnClickListener((v -> {

        }));

        holder.deleteButton.setOnClickListener((v -> {
            deleteClient(v, position);
        }));

        holder.moreButton.setOnClickListener((v -> {
            callFragment(v, client);
        }));
    }

    private void callFragment(View v, ClientModel client) {
        AppCompatActivity activity = (AppCompatActivity) v.getContext();

        ClientActionFragment clientActionFragment = ClientActionFragment.newInstance(client);

        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.client_fragment_layout, clientActionFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void deleteClient(View v, int position) {
        ClientModel clientModel = mClients.get(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setMessage("Deseja mesmo excluir o cadastro do(a) " + clientModel.getName())
                .setPositiveButton("Sim", (dialog, id) -> {
                    deleteClientFromApi(clientModel.getId(), v, position);
                })
                .setNegativeButton("Cancelar", (dialog, id) -> {
                    dialog.dismiss();
                });

        Dialog dialog = builder.create();
        dialog.show();
    }

    private void deleteClientFromApi(Long id, View v, int position) {
        ClientService clientService = RetrofitConfig.getRetrofitInstance().create(ClientService.class);
        Call<Void> call = clientService.delete(id, auth);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()) {
                    updateRecyclerView(position);
                    SnackbarUtil.showSuccess((AppCompatActivity) v.getContext(), "Cliente deletado com sucesso");
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

    private void updateRecyclerView(int position) {
        mClients.remove(position);
        this.notifyItemRemoved(position);
        this.notifyItemRangeChanged(position, mClients.size());
    }

    @Override
    public int getItemCount() {
        return mClients != null ? mClients.size() : 0;
    }
}

