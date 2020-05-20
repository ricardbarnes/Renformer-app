package erdari.renformer_android.ui.user.admin.attribute;

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
import erdari.renformer_android.data.model.Attribute;
import erdari.renformer_android.data.model.ReformType;
import erdari.renformer_android.data.repository.AttributeRepository;

/**
 * Required adapter for the attribute recycler view.
 *
 * @author Ricard Pinilla Barnes
 */
public class AttributeListAdapter extends RecyclerView.Adapter<AttributeListAdapter.UserListViewHolder> {

    private Context mContext;
    private ReformType mReformType;
    private List<Attribute> mAttributes;
    private AttributeRepository attributeRepo;

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
            name = card.findViewById(R.id.categoryName);
        }
    }

    /**
     * Adapter constructor.
     *
     * @param context    The app context that will hold the views.
     * @param reformType The parent reform type.
     * @param attributes The attribute list that will feed the views.
     */
    public AttributeListAdapter(Context context, ReformType reformType, List<Attribute> attributes) {
        mContext = context;
        mReformType = reformType;
        mAttributes = attributes;
        attributeRepo = new AttributeRepository(context);
    }

    @NotNull
    @Override
    public UserListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category, parent, false);
        return new UserListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(UserListViewHolder holder, int position) {
        Attribute attribute = mAttributes.get(position);
        String title = attribute.getName();
        holder.name.setText(title);

        holder.view.setOnClickListener(v -> {
            attributeRepo.insertLocalAttribute(attribute)
                    .observe((LifecycleOwner) mContext, attributeId -> {
                        if (attributeId != 0) {
                            Intent intent = new Intent(mContext, AttributeEditActivity.class);
                            intent.putExtra(Key.REFORM_TYPE_ID, mReformType.getId());
                            intent.putExtra(Key.ATTRIBUTE_ID, attribute.getId());
                            mContext.startActivity(intent);
                            ((Activity) mContext).finish();
                        } else {
                            Toast.makeText(
                                    mContext,
                                    "Attribute not found in room",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    });
        });
    }

    @Override
    public int getItemCount() {
        return mAttributes.size();
    }

}