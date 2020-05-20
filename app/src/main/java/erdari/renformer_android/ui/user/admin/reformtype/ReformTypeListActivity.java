package erdari.renformer_android.ui.user.admin.reformtype;

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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import erdari.renformer_android.R;
import erdari.renformer_android.config.Key;
import erdari.renformer_android.data.model.ReformType;
import erdari.renformer_android.data.repository.ReformTypeRepository;
import erdari.renformer_android.data.room.ReformTypeRoom;

/**
 * An activity representing a list of ReformTypes. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ReformTypeDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ReformTypeListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private ReformTypeRepository mReformTypeRepo;
    private List<ReformType> mReformTypes;
    private SimpleItemRecyclerViewAdapter mAdapter;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reformtype_list);

        setTitle(getString(R.string.reform_types_title));

        mProgressBar = findViewById(R.id.categoriesProgressBar);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(this, ReformTypeCreateActivity.class);
            startActivity(intent);
            finish();
        });

        if (findViewById(R.id.reformtype_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        mReformTypes = new ArrayList<>();

        mReformTypeRepo = new ReformTypeRepository(this);
        mReformTypeRepo.findAllReformTypes().observe(this, reformTypes -> {
            if (!reformTypes.isEmpty()) {
                mReformTypes.addAll(reformTypes);
                mAdapter.notifyDataSetChanged();
                mProgressBar.setVisibility(View.GONE);
            }
        });

        View recyclerView = findViewById(R.id.reformtype_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        mAdapter = new SimpleItemRecyclerViewAdapter(this, mReformTypes);
        recyclerView.setAdapter(mAdapter);
    }

    public void goBack(View view) {
        ReformTypeRoom reformTypeRoom = new ReformTypeRoom(this);
        reformTypeRoom.nukeReformTypes(); // Frees device's space
        finish();
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final ReformTypeListActivity mParentActivity;
        private final List<ReformType> mValues;
        private final View.OnClickListener mOnClickListener;

        SimpleItemRecyclerViewAdapter(ReformTypeListActivity parent,
                                      List<ReformType> items) {
            mValues = items;
            mParentActivity = parent;

            mOnClickListener = view -> {
                ReformType reformSummary = (ReformType) view.getTag();

                Intent intent = new Intent(mParentActivity, ReformTypeDetailActivity.class);
                intent.putExtra(Key.REFORM_TYPE_ID, reformSummary.getId());
                mParentActivity.startActivity(intent);
                mParentActivity.finish();
            };
        }

        @NotNull
        @Override
        public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.reformtype_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NotNull final ViewHolder holder, int position) {
            holder.mIdView.setText(String.valueOf(mValues.get(position).getId()));
            holder.mContentView.setText(mValues.get(position).getName());

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
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
