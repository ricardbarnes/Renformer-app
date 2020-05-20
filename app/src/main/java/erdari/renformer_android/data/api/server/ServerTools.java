package erdari.renformer_android.data.api.server;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.jetbrains.annotations.NotNull;

import erdari.renformer_android.data.api.service.ServerService;
import erdari.renformer_android.network.ApiConfig;
import erdari.renformer_android.network.RetrofitRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Tools for the Api server.
 *
 * @author Ricard Pinilla Barnes
 */
public class ServerTools {

    private static final String DEFAULT_MSG = "Some error happened.";
    private final ServerService serverService;
    private final Context context;

    /**
     * Class constructor.
     *
     * @param context The app context that will hold the outputs.
     */
    public ServerTools(Context context) {
        serverService = RetrofitRequest.getRetrofitInstance().create(ServerService.class);
        this.context = context;
    }

    /**
     * Checks the server for a response.
     *
     * @return True if receiving a server response.
     */
    public LiveData<Boolean> checkServer() {
        final MutableLiveData<Boolean> data = new MutableLiveData<>();

        serverService.checkServer()
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(
                            @NotNull Call<Void> call,
                            @NotNull Response<Void> response
                    ) {
                        int responseCode = response.code();
                        if (responseCode == 200) {
                            data.setValue(true);
                        } else {
                            String message;

                            switch (responseCode) {
                                case 404:
                                    message = "No response.";
                                    break;
                                case 500:
                                    message = "Server error.";
                                    break;
                                default:
                                    message = DEFAULT_MSG;
                                    break;
                            }

                            Toast.makeText(
                                    context,
                                    message,
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }

                    @Override
                    public void onFailure(
                            @NotNull Call<Void> call,
                            @NotNull Throwable t
                    ) {
                        Toast.makeText(
                                context,
                                ApiConfig.NO_CONNECTION_MSG,
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });

        return data;
    }
}
