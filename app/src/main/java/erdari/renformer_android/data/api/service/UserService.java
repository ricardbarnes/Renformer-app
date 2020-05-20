package erdari.renformer_android.data.api.service;

import java.util.List;

import erdari.renformer_android.data.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * User service interface used by Retrofit requests concerning users.
 *
 * @author Ricard Pinilla Barnes
 */
public interface UserService {

    /**
     * Creates a new user.
     *
     * @param user The user to be created.
     * @return Void (but the whole api response implicitly).
     * @author Ricard Pinilla Barnes
     */
    @POST("users")
    Call<Void> createUser(
            @Body User user
    );

    /**
     * Deletes a user using its id.
     *
     * @param id The user id.
     * @return Void (but the whole api response implicitly).
     * @author Ricard Pinilla Barnes
     */
    @DELETE("users/{id}")
    Call<Void> deleteUserById(
            @Path("id") long id
    );

    /**
     * Patches the user finding it via id.
     *
     * @param id   The user id.
     * @param user The user to be patched.
     * @return Void (but the whole api response implicitly).
     * @author Ricard Pinilla Barnes
     */
    @PATCH("users/{id}")
    Call<Void> patchUserById(
            @Path("id") long id,
            @Body User user
    );

    /**
     * Updates the user finding it via id
     *
     * @param id   The user id.
     * @param user The user to be updated.
     * @return Void (but the whole api response implicitly).
     * @author Ricard Pinilla Barnes
     */
    @PUT("users/{id}")
    Call<Void> updateUserById(
            @Path("id") long id,
            @Body User user
    );

    /**
     * Finds all the users.
     *
     * @param start The starting position.
     * @param limit The maximum number of objects.
     * @return The complete user list.
     * @author Ricard Pinilla Barnes
     */
    @GET("users/start/{start}/limit/{limit}")
    Call<List<User>> findAllUsersPaginated(
            @Path("start") int start,
            @Path("limit") int limit
    );

    /**
     * Finds a user by its email.
     *
     * @param email The user email.
     * @return A ResponseBody object.
     * @author Ricard Pinilla Barnes
     */
    @GET("users/email/{email}")
    Call<User> findUserByEmail(
            @Path("email") String email
    );

    /**
     * Find all the users corresponding to a concrete role.
     *
     * @param role The requested user role.
     * @return The user list with the requested role.
     * @author Ricard Pinilla Barnes
     */
    @Deprecated
    @GET("users/role/{role}")
    Call<List<User>> findUsersByRole(
            @Path("role") int role
    );

    /**
     * Find all the users corresponding to a concrete role (paginated).
     *
     * @param role  The requested user role.
     * @param start The starting position.
     * @param limit The maximum number of objects.
     * @return The user list with the requested role.
     * @author Ricard Pinilla Barnes
     */
    @GET("users/role/{role}/start/{start}/limit/{limit}")
    Call<List<User>> findUsersByRolePaginated(
            @Path("role") int role,
            @Path("start") int start,
            @Path("limit") int limit
    );

}
