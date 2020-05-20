package erdari.renformer_android.ui.user.admin.user.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import erdari.renformer_android.ui.user.admin.user.list.adapter.UserListAdapter;
import erdari.renformer_android.R;
import erdari.renformer_android.data.model.User;
import erdari.renformer_android.data.repository.UserRepository;


/**
 * A placeholder fragment containing a simple view.
 */
public class UserListFragment extends Fragment implements FragmentManager.OnBackStackChangedListener {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private int mUserRole;
    private UserListViewModel userListViewModel;
    private RecyclerView mRecyclerView;
    private UserListAdapter mAdapter;
    private List<User> mUsers;
    private ProgressBar mProgressBar;
    private FloatingActionButton mFab;
    private UserRepository mUserRepo;

    public static UserListFragment newInstance(int index) {
        UserListFragment fragment = new UserListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userListViewModel = new UserListViewModel();
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        userListViewModel.setIndex(index);

        if (index == 1) {
            mUserRole = 1;
        } else {
            mUserRole = 3;
        }

        mUsers = new ArrayList<>();
        mUserRepo = new UserRepository(this.getContext());
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_user_management_activity, container, false);
        final TextView textView = root.findViewById(R.id.section_label);
        userListViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        mProgressBar = root.findViewById(R.id.categoriesProgressBar);
        mRecyclerView = root.findViewById(R.id.userListView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mAdapter = new UserListAdapter(this.getContext(), mUsers);
        mRecyclerView.setAdapter(mAdapter);

        mUserRepo.findUsersByRole(mUserRole).observe(getViewLifecycleOwner(), users -> {
            if (users != null) {
                mUsers.addAll(users);
                mAdapter.notifyDataSetChanged();
                mProgressBar.setVisibility(View.GONE);
            } else {
                Toast.makeText(this.getContext(), R.string.no_users_found, Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }

    @Override
    public void onBackStackChanged() {

    }
}