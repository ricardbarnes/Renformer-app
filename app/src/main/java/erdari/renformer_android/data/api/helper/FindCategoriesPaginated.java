package erdari.renformer_android.data.api.helper;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import erdari.renformer_android.data.model.relational.RelationalCategory;
import erdari.renformer_android.data.api.service.CategoryService;
import erdari.renformer_android.data.model.Category;
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
public class FindCategoriesPaginated {

    private static final int OBJECT_LIMIT = 10;

    private final MutableLiveData<List<Category>> data;

    private CategoryService categoryService;
    private List<Category> accumulatedCategories;
    private Context appContext;
    private int startPosition;

    /**
     * Helper class constructor.
     *
     * @param context The current application context.
     * @param service The user service for the Retrofit requests.
     * @author Ricard Pinilla Barnes
     */
    public FindCategoriesPaginated(Context context, CategoryService service) {
        accumulatedCategories = new ArrayList<>();
        data = new MutableLiveData<>();
        appContext = context;
        categoryService = service;
    }

    /**
     * Finds all the categories into a specific reform type.
     *
     * @param reformTypeId The requested reform type id.
     * @return A list with the users with the requested role.
     * @author Ricard Pinilla Barnes
     */
    public MutableLiveData<List<Category>> findCategoriesByAttributeIdPaginated(long reformTypeId) {

        categoryService.findAllCategoriesPaginated(startPosition, OBJECT_LIMIT)
                .enqueue(new Callback<List<RelationalCategory>>() {
                    @Override
                    public void onResponse(
                            @NotNull Call<List<RelationalCategory>> call,
                            @NotNull Response<List<RelationalCategory>> response
                    ) {
                        int responseCode = response.code();
                        if (responseCode == 200) {
                            List<RelationalCategory> categories = response.body();

                            if (categories != null) {
                                if (!categories.isEmpty()) {
                                    startPosition += OBJECT_LIMIT;

                                    /* Non-OOP/semi-OOP request main adaption Beta block BEGIN */
                                    for (RelationalCategory category : categories) {
                                        if (category.getReformTypeId() == reformTypeId) {
                                            accumulatedCategories.add(category);
                                        }
                                    }
                                    /* Non-OOP/semi-OOP request main adaption Beta block END */

                                    findCategoriesByAttributeIdPaginated(reformTypeId);
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
                            @NotNull Call<List<RelationalCategory>> call,
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
        data.setValue(accumulatedCategories);
    }

    /**
     * Sorts the retrieved categories alphabetically upwards.
     *
     * @author Ricard Pinilla Barnes
     */
    private void sort() {
        Collections.sort(accumulatedCategories, (o1, o2) ->
                o1.getName().compareToIgnoreCase(o2.getName()));
    }

}
