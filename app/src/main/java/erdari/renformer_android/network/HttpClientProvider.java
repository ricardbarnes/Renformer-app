package erdari.renformer_android.network;

import org.jetbrains.annotations.NotNull;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Http connection client provider.
 *
 * @author Ricard Pinilla Barnes
 */
class HttpClientProvider {

    /**
     * Builds the client with the session token.
     *
     * @param token The current session token.
     * @return The client with the session token.
     * @author Ricard Pinilla Barnes
     */
    @NotNull
    static OkHttpClient getUnsafeClientWithToken(String token) {

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = UnsafeNetworkProvider.getUnsafeOkHttpClient();

        OkHttpClient.Builder builder = client.newBuilder();
        builder.addInterceptor(chain -> {
            Request newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer " + token)
                    .build();
            return chain.proceed(newRequest);
        })
                .addInterceptor(loggingInterceptor); // A logging interceptor is added for testing and debugging purposes

        return builder.build();
    }

}
