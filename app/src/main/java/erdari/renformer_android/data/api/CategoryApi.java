package erdari.renformer_android.data.api;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import erdari.renformer_android.data.api.helper.FindCategoriesPaginated;
import erdari.renformer_android.data.model.relational.RelationalCategory;
import erdari.renformer_android.data.api.service.CategoryService;
import erdari.renformer_android.data.model.Category;
import erdari.renformer_android.network.ApiConfig;
import erdari.renformer_android.network.ApiErrorMessageRetriever;
import erdari.renformer_android.network.RetrofitRequest;
import erdari.renformer_android.security.Session;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Category api management.
 *
 * @author Ricard Pinilla Barnes
 */
public class CategoryApi {

    private final CategoryService categoryService;
    private final Context context;

    /**
     * Class constructor.
     *
     * @param context The app context that will handle the outputs.
     * @author Ricard Pinilla Barnes
     */
    public CategoryApi(Context context) {
        Session session = Session.getInstance(context);
        this.context = context;
        this.categoryService = RetrofitRequest.getUnsafeRetrofitInstanceWithToken(
                session.getSessionToken()
        ).create(CategoryService.class);
    }

    /**
     * Finds all the categories into a specific reform type.
     *
     * @param reformTypeId The parent reform type id.
     * @return A list with the required categories.
     * @author Ricard Pinilla Barnes
     */
    public MutableLiveData<List<Category>> findAllCategoriesByReformTypeId(long reformTypeId) {
        FindCategoriesPaginated helper = new FindCategoriesPaginated(context, categoryService);
        return helper.findCategoriesByAttributeIdPaginated(reformTypeId);
    }

    /**
     * Creates a new category inside the corresponding reform type.
     *
     * @param category The category to be created.
     * @return True if the creation has been successful, false if not.
     * @author Ricard Pinilla Barnes
     */
    public MutableLiveData<Boolean> createCategory(RelationalCategory category) {
        final MutableLiveData<Boolean> data = new MutableLiveData<>();

        categoryService.createCategory(category)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(
                            @NotNull Call<Void> call,
                            @NotNull Response<Void> response
                    ) {
                        int responseCode = response.code();

                        if (responseCode == 201) {
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

    /**
     * Finds a category by its id.
     *
     * @param id The Category id.
     * @return The requested category, if found.
     * @author Ricard Pinilla Barnes
     */
    public MutableLiveData<Category> findCategoryById(long id) {
        final MutableLiveData<Category> data = new MutableLiveData<>();

        categoryService.findCategoryById(id)
                .enqueue(new Callback<RelationalCategory>() {
                    @Override
                    public void onResponse(
                            @NotNull Call<RelationalCategory> call,
                            @NotNull Response<RelationalCategory> response
                    ) {
                        int responseCode = response.code();

                        if (responseCode == 200) {
                            data.setValue(response.body());
                        } else {
                            data.setValue(null);

                            String message = ApiErrorMessageRetriever.retrieveErrorMessage(response);
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(
                            @NotNull Call<RelationalCategory> call,
                            @NotNull Throwable t
                    ) {
                        ApiConfig.showStandardFailureToast(context);
                    }
                });

        return data;
    }

    /**
     * Updates category.
     *
     * @param category The category to be patched.
     * @return True if the patching is done, false if not.
     * @author Ricard Pinilla Barnes
     */
    public MutableLiveData<Boolean> patchCategory(RelationalCategory category) {
        final MutableLiveData<Boolean> data = new MutableLiveData<>();

        categoryService.patchCategoryById(category.getId(), category)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(
                            @NotNull Call<Void> call,
                            @NotNull Response<Void> response
                    ) {

                        if (response.isSuccessful()) {
                            data.setValue(true);
                        } else {
                            data.setValue(false);

                            String message = ApiErrorMessageRetriever.retrieveErrorMessage(response);
                            Toast.makeText(
                                    context,
                                    message,
                                    Toast.LENGTH_SHORT
                            ).show();

                            data.setValue(false);
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

    /**
     * Deletes a category.
     *
     * @param category The category to be deleted.
     * @return True if deleted, false if not.
     * @author Ricard Pinilla Barnes
     */
    public MutableLiveData<Boolean> deleteCategory(@NotNull Category category) {
        final MutableLiveData<Boolean> data = new MutableLiveData<>();

        categoryService.deleteUserById(category.getId())
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
