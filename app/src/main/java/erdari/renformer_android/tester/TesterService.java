package erdari.renformer_android.tester;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

/**
 * Tester interface for Retrofit2 calls. Just for Api testing purposes.
 *
 * @author Ricard Pinilla Barnes
 */
public interface TesterService {

    @GET("categories")
    Call<ResponseBody> getCategories(
            @Header("Authorization") String token
    );

    @GET("reformtypes")
    Call<ResponseBody> getReformTypes(
            @Header("Authorization") String token
    );

}
