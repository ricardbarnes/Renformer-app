package erdari.renformer_android.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;

import erdari.renformer_android.data.api.ReformBudgetApi;
import erdari.renformer_android.data.model.ReformBudget;

/**
 * Reform budget repository.
 *
 * @author Ricard Pinilla Barnes
 */
public class ReformBudgetRepository {

    private ReformBudgetApi reformBudgetApi;
//    private ReformBudgetRoom optionRoom;

    private Context mContext;

    /**
     * Class constructor.
     *
     * @param context The app context that will handle the outputs.
     * @author Ricard Pinilla Barnes
     */
    public ReformBudgetRepository(Context context) {
        reformBudgetApi = new ReformBudgetApi(context);
//        budgetRoom = new ReformBudgetRoom(context);
        mContext = context;
    }

    /**
     * Finds all the reform budgets.
     *
     * @return A list with all the reform budgets.
     * @author Ricard Pinilla Barnes
     */
    public LiveData<List<ReformBudget>> findAllBudgets() {
        return reformBudgetApi.findAllBudgets();
    }

    /**
     * Finds all the reform budgets from a specific user.
     *
     * @return A list with all the reform budgets.
     * @author Ricard Pinilla Barnes
     */
    public LiveData<List<ReformBudget>> findAllUserBudgetsByUserId(long userId) {
        return reformBudgetApi.findAllUserBudgetsByUserId(userId);
    }

    /**
     * Creates a new reform budget.
     *
     * @param reformBudget The reformBudget to be created.
     * @return True if the creation has been successful, false if no.
     * @author Ricard Pinilla Barnes
     */
    public LiveData<ReformBudget> createBudget(ReformBudget reformBudget) {
        return reformBudgetApi.createBudget(reformBudget);
    }

    /**
     * Deletes a reform budget.
     *
     * @param reformBudget The reform budget to be deleted.
     * @return True if deleted, false if not.
     * @author Ricard Pinilla Barnes
     */
    public LiveData<Boolean> deleteReformBudget(ReformBudget reformBudget) {
        return reformBudgetApi.deleteReformBudget(reformBudget);
    }

}
