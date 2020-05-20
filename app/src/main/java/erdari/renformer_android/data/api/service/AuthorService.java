package erdari.renformer_android.data.api.service;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Author service interface used by Retrofit2 requests concerning the project authors.
 *
 * @author Ricard Pinilla Barnes
 */
public interface AuthorService {

    /**
     * Finds the project authors.
     *
     * @return The project authors info.
     * @author Ricard Pinilla Barnes
     */
    @GET("authors")
    Call<Map> findAuthors();

    /**
     * Finds the project about info.
     *
     * @return The project about info.
     * @author Ricard Pinilla Barnes
     */
    @GET("authors/about")
    Call<Map> findAbout();

}
