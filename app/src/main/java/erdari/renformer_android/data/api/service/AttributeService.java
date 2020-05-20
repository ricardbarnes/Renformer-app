package erdari.renformer_android.data.api.service;

import java.util.List;

import erdari.renformer_android.data.model.relational.RelationalAttribute;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Attribute service interface used by Retrofit requests concerning ReformType <b>attributes<b/>.
 * <p>
 * Important: the called attributes will come with their inner options, if the have any.
 *
 * @author Ricard Pinilla Barnes
 */
public interface AttributeService {

    /**
     * Finds all the attributes.
     *
     * @return The complete attribute list.
     * @author Ricard Pinilla Barnes
     */
    @GET("attributes")
    Call<List<RelationalAttribute>> findAllAttributes();

    /**
     * Finds all the attributes, using pagination.
     *
     * @param start The staging initial index.
     * @param limit The staging final index.
     * @return The attribute list staggered by the passed limit.
     * @author Ricard Pinilla Barnes
     */
    @GET("attributes/start/{start}/limit/{limit}")
    Call<List<RelationalAttribute>> findAllAttributesPaginated(
            @Path("start") int start,
            @Path("limit") int limit
    );

    /**
     * Creates a new attribute.
     *
     * @param attribute The new attribute.
     * @return Void (but the whole api response implicitly).
     * @author Ricard Pinilla Barnes
     */
    @POST("attributes")
    Call<RelationalAttribute> createAttribute(@Body RelationalAttribute attribute);

    /**
     * Finds an attribute by id.
     *
     * @param id The attribute id.
     * @return The requested attribute.
     * @author Ricard Pinilla Barnes
     */
    @GET("attributes/{id}")
    Call<RelationalAttribute> findAttributeById(
            @Path("id") long id
    );

    /**
     * Patches an attribute.
     *
     * @param id        The attribute id.
     * @param attribute The attribute to patch.
     * @return Void (but the whole api response implicitly).
     * @author Ricard Pinilla Barnes
     */
    @PATCH("attributes/{id}")
    Call<Void> patchAttributeById(
            @Path("id") long id,
            @Body RelationalAttribute attribute
    );

    /**
     * Deletes an attribute.
     *
     * @param id The attribute id.
     * @return Void (but the whole api response implicitly).
     * @author Ricard Pinilla Barness
     */
    @DELETE("attributes/{id}")
    Call<Void> deleteUserById(
            @Path("id") long id
    );
}
