package com.hyperdrive.woodstock.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.hyperdrive.woodstock.R;
import com.hyperdrive.woodstock.holders.UserHolder;
import com.hyperdrive.woodstock.models.UserModel;
import com.hyperdrive.woodstock.ui.user.UserAdminFragment;

import java.util.List;
import java.util.Locale;

public class UserAdapter extends RecyclerView.Adapter<UserHolder> {

    private List<UserModel> mUsers;
    private Long mCompany;

    public UserAdapter(List<UserModel> mUsers, Long mCompany) {
        this.mUsers = mUsers;
        this.mCompany = mCompany;
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_user, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
        UserModel user = mUsers.get(position);

        holder.name.setText(user.getName());
        String status = user.getStatus().substring(0, 1).toUpperCase(Locale.forLanguageTag("BR"))
                + user.getStatus().substring(1).toLowerCase(Locale.forLanguageTag("BR"));
        holder.status.setText(status);

        String type = user.getType().substring(0, 1).toUpperCase(Locale.forLanguageTag("BR"))
                + user.getType().substring(1).toLowerCase(Locale.forLanguageTag("BR"));
        holder.type.setText(type);

        holder.moreButton.setOnClickListener(v -> {
            callFragment(v, user);
        });
    }

    @Override
    public int getItemCount() {
        return mUsers != null ? mUsers.size() : 0;
    }

    private void callFragment(View v, UserModel user) {
        AppCompatActivity activity = (AppCompatActivity) v.getContext();

        UserAdminFragment userAdminFragment = UserAdminFragment.newInstance(user);

        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.user_fragment_layout, userAdminFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void updateData(List<UserModel> users) {
        this.mUsers.clear();
        this.mUsers.addAll(users);
        notifyDataSetChanged();
    }
}
