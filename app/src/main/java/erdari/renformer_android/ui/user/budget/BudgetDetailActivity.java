package erdari.renformer_android.ui.user.budget;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import erdari.renformer_android.R;
import erdari.renformer_android.config.Key;
import erdari.renformer_android.data.model.ReformBudget;

public class BudgetDetailActivity extends AppCompatActivity {

    private ReformBudget mReformBudget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_detail);

        String budgetJsonStr = getIntent().getStringExtra(Key.JSON_BUDGET);
        mReformBudget = new Gson().fromJson(budgetJsonStr, ReformBudget.class);

        // TODO
    }
}
