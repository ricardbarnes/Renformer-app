package erdari.renformer_android.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit request building class.
 *
 * @author Ricard Pinilla Barnes
 */
public class RetrofitRequest {

    /**
     * Builds and returns a complete Retrofit instance.
     *
     * @return A complete Retrofit instance.
     * @author Ricard Pinilla Barnes
     */
    public static Retrofit getRetrofitInstance() {
        return new Retrofit.Builder()
                .baseUrl(ApiConfig.getApiUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .client(UnsafeNetworkProvider.getUnsafeOkHttpClient())
                .build();
    }

    /**
     * Builds and returns a complete Retrofit instance with the session token included.
     *
     * @return A complete Retrofit instance.
     * @author Ricard Pinilla Barnes
     */
    public static Retrofit getUnsafeRetrofitInstanceWithToken(String token) {
        return new Retrofit.Builder()
                .baseUrl(ApiConfig.getApiUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .client(HttpClientProvider.getUnsafeClientWithToken(token))
                .build();
    }

}
