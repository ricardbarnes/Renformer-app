package erdari.renformer_android.data.api.service;

import java.util.List;

import erdari.renformer_android.data.model.relational.RelationalCategory;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Category service interface used by Retrofit requests concerning ReformType <b>Categories<b/>
 *
 * @author Ricard Pinilla Barnes
 */
public interface CategoryService {

    /**
     * Finds all the categories.
     *
     * @return The complete category list.
     * @author Ricard Pinilla Barnes
     */
    @GET("categories")
    Call<List<RelationalCategory>> findAllCategories();

    /**
     * Finds all the categories, using pagination.
     *
     * @param start The staging initial index.
     * @param limit The staging final index.
     * @return The categories list staggered by the passed limit.
     * @author Ricard Pinilla Barnes
     */
    @GET("categories/start/{start}/limit/{limit}")
    Call<List<RelationalCategory>> findAllCategoriesPaginated(
            @Path("start") int start,
            @Path("limit") int limit
    );

    /**
     * Creates a new category.
     *
     * @param category The new category.
     * @return Void (but the whole api response implicitly).
     * @author Ricard Pinilla Barnes
     */
    @POST("categories")
    Call<Void> createCategory(@Body RelationalCategory category);

    /**
     * Finds a category by id.
     *
     * @param id The category id.
     * @return The requested category.
     * @author Ricard Pinilla Barnes
     */
    @GET("categories/{id}")
    Call<RelationalCategory> findCategoryById(@Path("id") long id);

    /**
     * Patches a category.
     *
     * @param id       The category id.
     * @param category The category to patch.
     * @return Void (but the whole api response implicitly).
     * @author Ricard Pinilla Barnes
     */
    @PATCH("categories/{id}")
    Call<Void> patchCategoryById(
            @Path("id") long id,
            @Body RelationalCategory category
    );

    /**
     * Deletes a category.
     *
     * @param id The category id.
     * @return Void (but the whole api response implicitly).
     * @author Ricard Pinilla Barnes
     */
    @DELETE("categories/{id}")
    Call<Void> deleteUserById(@Path("id") long id);

}
