package erdari.renformer_android.tester;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import erdari.renformer_android.network.ApiConfig;
import erdari.renformer_android.network.RetrofitRequest;
import erdari.renformer_android.security.Session;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Tester tool. Just for testing purposes.
 *
 * @author Ricard Pinilla Barnes
 */
public class TesterTool {

    private static final String DEFAULT_MSG = "Some error happened.";
    private final TesterService testerService;
    private final String sessionToken;
    private final Context context;

    public TesterTool(Context context) {
        Session session = Session.getInstance(context);
        this.context = context;
        this.sessionToken = ApiConfig.TOKEN_START + session.getSessionToken();
        testerService = RetrofitRequest.getRetrofitInstance().create(TesterService.class);
    }

    public LiveData<Boolean> getCategories() {
        final MutableLiveData<Boolean> data = new MutableLiveData<>();

        testerService.getCategories(sessionToken)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                        Log.i("RESPONSE", "Categories");
                        Log.i("RESPONSE", "Code: " + response.code());
                        try {
                            Log.i("RESPONSE", "Body: " + response.body().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Log.i("RESPONSE", "------------");
                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                        Toast.makeText(
                                context,
                                ApiConfig.NO_CONNECTION_MSG,
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });

        return data;
    }

    public LiveData<Boolean> getReformTypes() {
        final MutableLiveData<Boolean> data = new MutableLiveData<>();

        testerService.getReformTypes(sessionToken)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                        Log.i("RESPONSE", "ReformTypes");
                        Log.i("RESPONSE", "Code: " + response.code());
                        try {
                            Log.i("RESPONSE", "Body: " + response.body().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Log.i("RESPONSE", "------------");
                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
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
