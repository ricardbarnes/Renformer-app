package erdari.renformer_android.data.api.service;

import retrofit2.Call;
import retrofit2.http.HEAD;

/**
 * Server service interface used by Retrofit requests concerning server.
 * Actually just the server activity checking.
 *
 * @author Ricard Pinilla Barnes
 */
public interface ServerService {

    /**
     * Checks the server connectivity.
     *
     * @return Void (but the whole api response implicitly).
     * @author Ricard Pinilla Barnes
     */
    @HEAD("accounts")
    Call<Void> checkServer();

}
