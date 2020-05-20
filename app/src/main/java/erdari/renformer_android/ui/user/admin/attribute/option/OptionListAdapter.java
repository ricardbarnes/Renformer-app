package erdari.renformer_android.ui.user.admin.attribute.option;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import erdari.renformer_android.R;
import erdari.renformer_android.config.Key;
import erdari.renformer_android.data.auxiliary.ReformIdObjectBundle;
import erdari.renformer_android.data.model.Option;
import erdari.renformer_android.data.repository.OptionRepository;

/**
 * Required adapter for the category recycler view.
 *
 * @author Ricard Pinilla Barnes
 */
public class OptionListAdapter extends RecyclerView.Adapter<OptionListAdapter.UserListViewHolder> {

    private Context mContext;
    private List<Option> mOptions;

    private ReformIdObjectBundle mBundle;
    private OptionRepository mOptionRepo;

    /**
     * The adapter view holder class.
     */
    static class UserListViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView name;

        /**
         * View holder constructor.
         *
         * @param card A single view.
         */
        UserListViewHolder(View card) {
            super(card);
            view = card;
            name = card.findViewById(R.id.optionName);
        }
    }

    /**
     * Adapter constructor.
     *
     * @param context The app context that will hold the views.
     * @param options The option list that will feed the views.
     * @param bundle  Id bundle to manage any further request.
     */
    public OptionListAdapter(Context context, List<Option> options, ReformIdObjectBundle bundle) {
        mContext = context;
        mOptions = options;
        mBundle = bundle;
        mOptionRepo = new OptionRepository(context);
    }

    @NotNull
    @Override
    public UserListViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.option, parent, false);
        return new UserListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NotNull UserListViewHolder holder, int position) {
        Option option = mOptions.get(position);
        String title = option.getName();
        holder.name.setText(title);

        holder.view.setOnClickListener(v -> {
            mOptionRepo.insertLocalOption(option).observe((LifecycleOwner) mContext, optionId -> {
                if (optionId != 0) {
                    Intent intent = new Intent(mContext, OptionEditActivity.class);
                    intent.putExtra(Key.REFORM_TYPE_ID, mBundle.getReformTypeId());
                    intent.putExtra(Key.ATTRIBUTE_ID, mBundle.getAttributeId());
                    intent.putExtra(Key.OPTION_ID, option.getId());
                    mContext.startActivity(intent);
                    ((Activity) mContext).finish();
                } else {
                    Toast.makeText(
                            mContext,
                            "Option not found in room",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return mOptions.size();
    }

}