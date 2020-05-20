package erdari.renformer_android.ui.user.budget.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import erdari.renformer_android.ui.user.budget.BudgetDetailActivity;
import erdari.renformer_android.R;
import erdari.renformer_android.config.Key;
import erdari.renformer_android.data.model.ReformBudget;
import erdari.renformer_android.data.repository.ReformBudgetRepository;
import erdari.renformer_android.tools.DateFormatter;

/**
 * Required adapter for the budget list recycler view.
 *
 * @author Ricard Pinilla Barnes
 */
public class BudgetListAdapter extends RecyclerView.Adapter<BudgetListAdapter.BudgetListViewHolder> {

    private Context mContext;
    private List<ReformBudget> mBudgetList;
    private ReformBudgetRepository budgetRepo;

    static class BudgetListViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView budgetId;
        TextView budgetDate;

        BudgetListViewHolder(View card) {
            super(card);
            view = card;
            budgetId = card.findViewById(R.id.budgetId);
            budgetDate = card.findViewById(R.id.budgetDate);
        }
    }

    public BudgetListAdapter(Context context, List<ReformBudget> budgets) {
        mContext = context;
        mBudgetList = budgets;
        budgetRepo = new ReformBudgetRepository(context);
    }

    @NotNull
    @Override
    public BudgetListViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.budget, parent, false);
        return new BudgetListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NotNull BudgetListViewHolder holder, int position) {
        ReformBudget budget = mBudgetList.get(position);
        String title = String.valueOf(budget.getId());
        holder.budgetId.setText(title);
        holder.budgetDate.setText(DateFormatter.dateToString(budget.getDate()));
        holder.view.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, BudgetDetailActivity.class);
            intent.putExtra(Key.JSON_BUDGET, new Gson().toJson(budget));
            mContext.startActivity(intent);
            ((Activity) mContext).finish();
        });
    }

    @Override
    public int getItemCount() {
        return mBudgetList.size();
    }

}