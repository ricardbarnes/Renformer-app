package erdari.renformer_android.ui.user.admin.user.list.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import erdari.renformer_android.R;
import erdari.renformer_android.config.Key;
import erdari.renformer_android.data.model.User;
import erdari.renformer_android.data.repository.UserRepository;
import erdari.renformer_android.security.Session;
import erdari.renformer_android.ui.user.admin.user.UserEditRoleActivity;

/**
 * Required adapter for the user list recycler view.
 *
 * @author Ricard Pinilla Barnes
 */
public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserListViewHolder> {

    private Context mContext;
    private List<User> mUserList;
    private UserRepository userRepo;

    static class UserListViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView fullName;
        TextView email;

        UserListViewHolder(View card) {
            super(card);
            view = card;
            fullName = card.findViewById(R.id.userName);
            email = card.findViewById(R.id.userEmail);
        }
    }

    public UserListAdapter(Context context, List<User> users) {
        mContext = context;
        mUserList = users;
        userRepo = new UserRepository(context);
    }

    @NotNull
    @Override
    public UserListViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user, parent, false);
        return new UserListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NotNull UserListViewHolder holder, int position) {
        User user = mUserList.get(position);
        String title = user.getName() + " " + user.getSurname();
        holder.fullName.setText(title);
        holder.email.setText(user.getEmail());

        if (user.getId() == Session.getInstance(mContext).getSessionUser().getId()) {
            int color = ContextCompat.getColor(mContext, R.color.colorPrimary);
            holder.fullName.setTextColor(color);
            holder.email.setTextColor(color);
        }

        holder.view.setOnClickListener(v -> userRepo.insertLocalUser(user)
                .observe((LifecycleOwner) mContext, userId -> {
                    if (userId != 0) {
                        Intent intent = new Intent(mContext, UserEditRoleActivity.class);
                        intent.putExtra(Key.USER_ID, user.getId());
                        mContext.startActivity(intent);
                        ((Activity) mContext).finish();
                    } else {
                        Toast.makeText(
                                mContext,
                                "User not found in room",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }));
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }

}