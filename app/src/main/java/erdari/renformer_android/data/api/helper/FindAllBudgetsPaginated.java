package erdari.renformer_android.data.api.helper;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import erdari.renformer_android.data.api.model.ApiReformBudget;
import erdari.renformer_android.data.api.service.ReformBudgetService;
import erdari.renformer_android.data.model.ReformBudget;
import erdari.renformer_android.network.ApiConfig;
import erdari.renformer_android.network.ApiErrorMessageRetriever;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Helper class that holds the recursive methods needed to achieve a proper pagination for the data.
 *
 * @author Ricard Pinilla Barnes
 */
public class FindAllBudgetsPaginated {

    private static final int OBJECT_LIMIT = 10;

    private final MutableLiveData<List<ReformBudget>> data;

    private ReformBudgetService budgetService;
    private List<ReformBudget> accumulatedBudgets;
    private Context appContext;
    private int startPosition;

    /**
     * Helper class constructor.
     *
     * @param context The current application context.
     * @param service The user service for the Retrofit requests.
     * @author Ricard Pinilla Barnes
     */
    public FindAllBudgetsPaginated(Context context, ReformBudgetService service) {
        accumulatedBudgets = new ArrayList<>();
        data = new MutableLiveData<>();
        appContext = context;
        budgetService = service;
    }

    /**
     * Finds all the reform budgets.
     *
     * @return The full budget list.
     * @author Ricard Pinilla Barnes
     */
    public MutableLiveData<List<ReformBudget>> findAllBudgetsPaginated() {

        budgetService.findAllBudgetsPaginated(startPosition, OBJECT_LIMIT)
                .enqueue(new Callback<List<ApiReformBudget>>() {
                    @Override
                    public void onResponse(
                            @NotNull Call<List<ApiReformBudget>> call,
                            @NotNull Response<List<ApiReformBudget>> response
                    ) {
                        int responseCode = response.code();
                        if (responseCode == 200) {
                            List<ApiReformBudget> reformBudgets = response.body();

                            if (reformBudgets != null) {
                                if (!reformBudgets.isEmpty()) {
                                    startPosition += OBJECT_LIMIT;

                                    for (ApiReformBudget adaptedBudget : reformBudgets) {
                                        accumulatedBudgets.add(
                                                ApiReformBudget.convertToPOJO(adaptedBudget)
                                        );
                                    }

                                    findAllBudgetsPaginated();
                                } else {
                                    finishCall();
                                }
                            }
                        } else if (responseCode == 204) {
                            finishCall();
                        } else {
                            data.setValue(null);

                            String message = ApiErrorMessageRetriever.retrieveErrorMessage(response);
                            Toast.makeText(
                                    appContext,
                                    message,
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }

                    @Override
                    public void onFailure(
                            @NotNull Call<List<ApiReformBudget>> call,
                            @NotNull Throwable t
                    ) {
                        Toast.makeText(
                                appContext,
                                ApiConfig.NO_CONNECTION_MSG,
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });

        return data;
    }

    /**
     * Handles the end post-call issues.
     *
     * @author Ricard Pinilla Barnes
     */
    private void finishCall() {
        sort();
        data.setValue(accumulatedBudgets);
    }

    /**
     * Sorts the retrieved budgets by date upwards.
     *
     * @author Ricard Pinilla Barnes
     */
    private void sort() {
        Collections.sort(accumulatedBudgets, (o1, o2) ->
                o1.getDate().compareTo(o2.getDate()));
    }

}
