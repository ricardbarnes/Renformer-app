package erdari.renformer_android.ui.user;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import erdari.renformer_android.R;
import erdari.renformer_android.config.Key;
import erdari.renformer_android.data.model.ReformType;
import erdari.renformer_android.data.repository.ReformTypeRepository;

/**
 * The reform type forms list activity.
 *
 * @author Ricard Pinilla Barnes
 */
public class ReformTypeViewListActivity extends AppCompatActivity {

    private ReformTypeRepository mReformTypeRepo;
    private List<ReformType> mReformTypes;
    private SimpleItemRecyclerViewAdapter mAdapter;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reformtypeview_list);

        setTitle(getString(R.string.reforms_menu_title));

        mProgressBar = findViewById(R.id.categoriesProgressBar);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        View recyclerView = findViewById(R.id.reformtypeview_list);

        mReformTypeRepo = new ReformTypeRepository(this);
        mReformTypeRepo.findAllReformTypes().observe(this, reformTypes -> {
            if (!reformTypes.isEmpty()) {
                mReformTypes = reformTypes;
                setupRecyclerView((RecyclerView) recyclerView);
                mProgressBar.setVisibility(View.GONE);
            }
        });
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        mAdapter = new SimpleItemRecyclerViewAdapter(this, mReformTypes);
        recyclerView.setAdapter(mAdapter);
    }

    public void goBack(View view) {
        finish();
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final ReformTypeViewListActivity mParentActivity;
        private final List<ReformType> mValues;
        private final View.OnClickListener mOnClickListener;

        SimpleItemRecyclerViewAdapter(ReformTypeViewListActivity parent,
                                      List<ReformType> items) {
            mValues = items;
            mParentActivity = parent;

            mOnClickListener = view -> {
                ReformType reformType = (ReformType) view.getTag();
                Intent intent = new Intent(mParentActivity, ReformTypeViewDetailActivity.class);
                intent.putExtra(Key.REFORM_TYPE_NAME, reformType.getName());
                intent.putExtra(Key.REFORM_TYPE_ID, reformType.getId());
                mParentActivity.startActivity(intent);
            };
        }

        @NotNull
        @Override
        public SimpleItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.reformtypeview_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NotNull final SimpleItemRecyclerViewAdapter.ViewHolder holder, int position) {
            holder.mContentView.setText(mValues.get(position).getName());

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                mIdView = view.findViewById(R.id.id_text);
                mContentView = view.findViewById(R.id.content);
            }
        }
    }
}

