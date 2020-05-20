package erdari.renformer_android.data.api;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import erdari.renformer_android.data.api.helper.FindAllBudgetsPaginated;
import erdari.renformer_android.data.api.helper.FindBudgetsByUserIdPaginated;
import erdari.renformer_android.data.api.service.ReformBudgetService;
import erdari.renformer_android.data.model.ReformBudget;
import erdari.renformer_android.network.ApiConfig;
import erdari.renformer_android.network.ApiErrorMessageRetriever;
import erdari.renformer_android.network.RetrofitRequest;
import erdari.renformer_android.security.Session;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Reform budget api management.
 *
 * @author Ricard Pinilla Barnes
 */
public class ReformBudgetApi {

    private final ReformBudgetService budgetService;
    private final Context context;
    private final long userId;

    /**
     * Class constructor.
     *
     * @param context The app context that will handle the outputs.
     * @author Ricard Pinilla Barnes
     */
    public ReformBudgetApi(Context context) {
        Session session = Session.getInstance(context);
        this.userId = session.getSessionUser().getId();
        this.context = context;
        this.budgetService = RetrofitRequest.getUnsafeRetrofitInstanceWithToken(
                session.getSessionToken()
        ).create(ReformBudgetService.class);
    }

    /**
     * Finds all the reform budgets.
     *
     * @return A list with all the reform budgets.
     * @author Ricard Pinilla Barnes
     */
    public MutableLiveData<List<ReformBudget>> findAllBudgets() {
        FindAllBudgetsPaginated helper = new FindAllBudgetsPaginated(context, budgetService);
        return helper.findAllBudgetsPaginated();
    }

    /**
     * Finds all the reform budgets from a specific user.
     *
     * @return A list with all the reform budgets.
     * @author Ricard Pinilla Barnes
     */
    public MutableLiveData<List<ReformBudget>> findAllUserBudgetsByUserId(long userId) {
        FindBudgetsByUserIdPaginated helper = new FindBudgetsByUserIdPaginated(context, budgetService);
        return helper.findAllBudgetsByUserIdPaginated(userId);
    }

    /**
     * Creates a new reform budget.
     *
     * @param reformBudget The reformBudget to be created.
     * @return True if the creation has been successful, false if no.
     * @author Ricard Pinilla Barnes
     */
    public MutableLiveData<ReformBudget> createBudget(ReformBudget reformBudget) {
        final MutableLiveData<ReformBudget> data = new MutableLiveData<>();

        budgetService.createBudget(userId, reformBudget)
                .enqueue(new Callback<ReformBudget>() {
                    @Override
                    public void onResponse(
                            @NotNull Call<ReformBudget> call,
                            @NotNull Response<ReformBudget> response
                    ) {
                        int responseCode = response.code();

                        if (responseCode == 201) {
                            data.setValue(response.body());
                        } else {
                            data.setValue(null);

                            String message = ApiErrorMessageRetriever.retrieveErrorMessage(response);
                            Toast.makeText(
                                    context,
                                    message,
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }

                    @Override
                    public void onFailure(
                            @NotNull Call<ReformBudget> call,
                            @NotNull Throwable t
                    ) {
                        ApiConfig.showStandardFailureToast(context);
                    }
                });

        return data;
    }

    /**
     * Deletes a reform budget.
     *
     * @param reformBudget The reform budget to be deleted.
     * @return True if deleted, false if not.
     * @author Ricard Pinilla Barnes
     */
    public MutableLiveData<Boolean> deleteReformBudget(@NotNull ReformBudget reformBudget) {
        final MutableLiveData<Boolean> data = new MutableLiveData<>();

        budgetService.deleteBudgetById(reformBudget.getId())
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(
                            @NotNull Call<Void> call,
                            @NotNull Response<Void> response
                    ) {
                        int responseCode = response.code();
                        if (responseCode == 200) {
                            data.setValue(true);
                        } else {
                            data.setValue(false);

                            String message = ApiErrorMessageRetriever.retrieveErrorMessage(response);
                            Toast.makeText(
                                    context,
                                    message,
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }

                    @Override
                    public void onFailure(
                            @NotNull Call<Void> call,
                            @NotNull Throwable t
                    ) {
                        ApiConfig.showStandardFailureToast(context);
                    }
                });

        return data;
    }
}
