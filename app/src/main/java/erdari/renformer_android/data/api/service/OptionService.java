package erdari.renformer_android.data.api.service;

import java.util.List;

import erdari.renformer_android.data.model.relational.RelationalOption;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Option service interface used by Retrofit requests concerning ReformType <b>Options<b/>
 *
 * @author Ricard Pinilla Barnes
 */
public interface OptionService {

    /**
     * Finds all the options.
     *
     * @return The complete option list.
     * @author Ricard Pinilla Barnes
     */
    @GET("options")
    Call<List<RelationalOption>> findAllOptions();

    /**
     * Finds all the options, using pagination.
     *
     * @param start The staging initial index.
     * @param limit The staging final index.
     * @return The option list staggered by the passed limit.
     * @author Ricard Pinilla Barnes
     */
    @GET("options/start/{start}/limit/{limit}")
    Call<List<RelationalOption>> findAllOptionsPaginated(
            @Path("start") int start,
            @Path("limit") int limit
    );

    /**
     * Creates a new option.
     *
     * @param option The new option.
     * @return Void (but the whole api response implicitly).
     * @author Ricard Pinilla Barnes
     */
    @POST("options")
    Call<Void> createOption(@Body RelationalOption option);

    /**
     * Finds a option by id.
     *
     * @param id The option id.
     * @return The requested option.
     * @author Ricard Pinilla Barnes
     */
    @GET("options/{id}")
    Call<RelationalOption> findOptionById(@Path("id") long id);

    /**
     * Patches an option.
     *
     * @param id     The option id.
     * @param option The option to patch.
     * @return Void (but the whole api response implicitly).
     * @author Ricard Pinilla Barnes
     */
    @PATCH("options/{id}")
    Call<Void> patchOptionById(
            @Path("id") long id,
            @Body RelationalOption option
    );

    /**
     * Deletes a option.
     *
     * @param id The option id.
     * @return Void (but the whole api response implicitly).
     * @author Ricard Pinilla Barnes
     */
    @DELETE("options/{id}")
    Call<Void> deleteOptionById(@Path("id") long id);

}
