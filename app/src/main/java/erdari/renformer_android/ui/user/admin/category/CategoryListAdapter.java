package erdari.renformer_android.ui.user.admin.category;

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
import erdari.renformer_android.data.model.Category;
import erdari.renformer_android.data.model.ReformType;
import erdari.renformer_android.data.repository.CategoryRepository;

/**
 * Required adapter for the category recycler view.
 *
 * @author Ricard Pinilla Barnes
 */
public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.UserListViewHolder> {

    private Context mContext;
    private ReformType mReformType;
    private List<Category> mCategories;
    private CategoryRepository categoryRepo;

    /**
     * Tha adapter view holder class.
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
     * @param categories The category list that will feed the views.
     */
    public CategoryListAdapter(Context context, ReformType reformType, List<Category> categories) {
        mContext = context;
        mReformType = reformType;
        mCategories = categories;
        categoryRepo = new CategoryRepository(context);
    }

    @NotNull
    @Override
    public UserListViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category, parent, false);
        return new UserListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NotNull UserListViewHolder holder, int position) {
        Category category = mCategories.get(position);
        String title = category.getName();
        holder.name.setText(title);
        holder.view.setOnClickListener(v -> {
            categoryRepo.insertLocalCategory(category)
                    .observe((LifecycleOwner) mContext, categoryId -> {
                        if (categoryId != 0) {
                            Intent intent = new Intent(mContext, CategoryEditActivity.class);
                            intent.putExtra(Key.REFORM_TYPE_ID, mReformType.getId());
                            intent.putExtra(Key.CATEGORY_ID, category.getId());
                            mContext.startActivity(intent);
                            ((Activity) mContext).finish();
                        } else {
                            Toast.makeText(
                                    mContext,
                                    "Category not found in room",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    });


        });
    }

    @Override
    public int getItemCount() {
        return mCategories.size();
    }

}