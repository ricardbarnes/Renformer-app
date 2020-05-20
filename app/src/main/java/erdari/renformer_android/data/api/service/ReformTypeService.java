package erdari.renformer_android.data.api.service;

import java.util.List;

import erdari.renformer_android.data.model.ReformType;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * ReformType service interface used by Retrofit requests concerning reform types.
 *
 * @author Ricard Pinilla Barnes
 */
public interface ReformTypeService {

    /**
     * Finds all the reform types.
     *
     * @return Void (but the whole api response implicitly).
     * @author Ricard Pinilla Barnes
     */
    @GET("reformTypes")
    Call<List<ReformType>> findAllReformTypes();

    /**
     * Creates a new reform type.
     *
     * @param reformType The new ReformType object.
     * @return Void (but the whole api response implicitly).
     * @author Ricard Pinilla Barnes
     */
    @POST("reformTypes")
    Call<Void> createReformType(@Body ReformType reformType);

    /**
     * Finds a reform type by id.
     *
     * @param id The reform type id.
     * @return The found ReformType object.
     * @author Ricard Pinilla Barnes
     */
    @GET("reformTypes/{id}")
    Call<ReformType> findReformTypeById(@Path("id") long id);

    /**
     * Patches a reform type.
     *
     * @param id         The reform type id.
     * @param reformType The reform type to patch.
     * @return Void (but the whole api response implicitly).
     * @author Ricard Pinilla Barnes
     */
    @PATCH("reformTypes/{id}")
    Call<Void> patchReformTypeById(
            @Path("id") long id,
            @Body ReformType reformType
    );

    /**
     * Deletes a reform type.
     *
     * @param id The reform type id.
     * @return Void (but the whole api response implicitly).
     * @author Ricard Pinilla Barnes
     */
    @DELETE("reformTypes/{id}")
    Call<Void> deleteReformTypeById(@Path("id") long id);

}
