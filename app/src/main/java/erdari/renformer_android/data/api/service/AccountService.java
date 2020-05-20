package erdari.renformer_android.data.api.service;

import erdari.renformer_android.data.model.User;
import erdari.renformer_android.network.AuthLogin;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Account service interface used by Retrofit requests concerning accounts.
 *
 * @author Ricard Pinilla Barnes
 */
public interface AccountService {

    /**
     * Registers a new user account.
     *
     * @param user The user to be registered.
     * @return Void (but the whole api response implicitly).
     * @author Ricard Pinilla Barnes
     */
    @POST("accounts")
    Call<Void> registerAccount(@Body User user);

    /**
     * Logs in a user.
     *
     * @param user The user to be logged in.
     * @return The whole response body. Should contain the token, the expiration date and
     * the logged user object.
     * @author Ricard Pinilla Barnes
     */
    @POST("accounts/auth")
    Call<AuthLogin> login(@Body User user);

    /**
     * Requests the account password restoration.
     *
     * @param email The user email.
     * @return Void (but the whole api response implicitly).
     * @author Ricard Pinilla Barnes
     */
    @GET("accounts/restore/{email}")
    Call<Void> restoreAccountPassword(@Path("email") String email);

}
