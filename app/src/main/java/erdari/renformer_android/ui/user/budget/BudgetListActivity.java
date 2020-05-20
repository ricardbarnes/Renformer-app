package erdari.renformer_android.ui.user.budget;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import erdari.renformer_android.ui.user.budget.adapter.BudgetListAdapter;
import erdari.renformer_android.R;
import erdari.renformer_android.data.model.ReformBudget;
import erdari.renformer_android.data.repository.ReformBudgetRepository;
import erdari.renformer_android.security.Session;

public class BudgetListActivity extends AppCompatActivity {

    private long mUserId;

    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;
    private BudgetListAdapter mAdapter;

    private List<ReformBudget> mBudgets;
    private ReformBudgetRepository mBudgetRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_list);
        setTitle(getString(R.string.my_budgets));

        mProgressBar = findViewById(R.id.progressBar);
        mRecyclerView = findViewById(R.id.budgetRecycler);

        mBudgets = new ArrayList<>();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new BudgetListAdapter(this, mBudgets);
        mRecyclerView.setAdapter(mAdapter);

        mUserId = Session.getInstance(this).getSessionUser().getId();

        mBudgetRepo = new ReformBudgetRepository(this);
        mBudgetRepo.findAllUserBudgetsByUserId(mUserId).observe(this, budgets -> {
            if (budgets != null && !budgets.isEmpty()) {
                mBudgets.addAll(budgets);
                mAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "No budgets yet", Toast.LENGTH_SHORT).show();
            }
            mProgressBar.setVisibility(View.GONE);
        });
    }


}
