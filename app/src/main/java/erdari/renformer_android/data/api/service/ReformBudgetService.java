package erdari.renformer_android.data.api.service;

import java.util.List;

import erdari.renformer_android.data.api.model.ApiReformBudget;
import erdari.renformer_android.data.model.ReformBudget;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Reform budget service interface used by Retrofit requests concerning the
 * <b>ReformBudget<b/> model.
 *
 * @author Ricard Pinilla Barnes
 */
public interface ReformBudgetService {

    /**
     * Finds all the reform budgets.
     *
     * @param start The staging initial index.
     * @param limit The staging final index.
     * @return The budget list staggered by the passed limit.
     * @author Ricard Pinilla Barnes
     */
    @GET("ReformBudgets/start/{start}/limit/{limit}")
    Call<List<ApiReformBudget>> findAllBudgetsPaginated(
            @Path("start") int start,
            @Path("limit") int limit
    );

    /**
     * Finds all the reform budgets from a specific user.
     *
     * @param userId The user id.
     * @param start  The staging initial index.
     * @param limit  The staging final index.
     * @return The budget list staggered by the passed limit.
     * @author Ricard Pinilla Barnes
     */
    @GET("ReformBudgets/user/{id}/start/{start}/limit/{limit}")
    Call<List<ApiReformBudget>> findAllUserBudgetsByUserIdPaginated(
            @Path("id") long userId,
            @Path("start") int start,
            @Path("limit") int limit
    );

    /**
     * Finds all reform budgets by reform type id.
     *
     * @param userId The user id.
     * @param start  The staging initial index.
     * @param limit  The staging final index.
     * @return The budget list staggered by the passed limit.
     * @author Ricard Pinilla Barnes
     */
    @GET("/api/v{version}/ReformBudgets/ReformType/{id}/start/{start}/limit/{limit}")
    Call<ApiReformBudget> findBudgetByReformTypeId(
            @Path("id") long userId,
            @Path("start") int start,
            @Path("limit") int limit
    );

    /**
     * Creates a new reform budget for a user.
     *
     * @param userId       The user id.
     * @param reformBudget The new budget.
     * @return Void (but the whole api response implicitly).
     * @author Ricard Pinilla Barnes
     */
    @POST("ReformBudgets/user/{id}")
    Call<ReformBudget> createBudget(
            @Path("id") long userId,
            @Body ReformBudget reformBudget
    );

    /**
     * Deletes a reform budget.
     *
     * @param id The reform budget id.
     * @return Void (but the whole api response implicitly).
     * @author Ricard Pinilla Barnes
     */
    @DELETE("ReformBudgets/{id}")
    Call<Void> deleteBudgetById(@Path("id") long id);

}
