package erdari.renformer_android.data.account;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.jetbrains.annotations.NotNull;

import erdari.renformer_android.network.ApiConfig;
import erdari.renformer_android.network.AuthLogin;
import erdari.renformer_android.network.RetrofitRequest;
import erdari.renformer_android.security.Session;
import erdari.renformer_android.data.model.User;
import erdari.renformer_android.data.api.service.AccountService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Handles the account relative issues data flow with the Api server.
 *
 * @author Ricard Pinilla Barnes
 */
public class AccountHandler {

    private static final String DEFAULT_MSG = "Some error happened.";
    private final AccountService accountService;
    private final Context context;

    /**
     * Class constructor.
     *
     * @param context Application context that will receive the UI outputs.
     * @author Ricard Pinilla Barnes
     */
    public AccountHandler(Context context) {
        accountService = RetrofitRequest.getRetrofitInstance().create(AccountService.class);
        this.context = context;
    }

    /**
     * Requests a user login to the Api.
     *
     * @param user The logging in user.
     * @return The logged in user.
     * @author Ricard Pinilla Barnes
     */
    public LiveData<User> login(User user) {
        final MutableLiveData<User> data = new MutableLiveData<>();

        accountService.login(user).enqueue(new Callback<AuthLogin>() {
            @Override
            public void onResponse(
                    @NotNull Call<AuthLogin> call,
                    @NotNull Response<AuthLogin> response
            ) {
                int responseCode = response.code();
                if (responseCode == 200) {
                    AuthLogin authLogin = response.body();

                    User user;
                    if (authLogin != null) {
                        user = authLogin.getUser();

                        Session session = Session.getInstance(context);
                        session.setSessionUser(user);
                        session.setSessionToken(authLogin.getToken());
                        session.setSessionExpiration(authLogin.getExpiration());

                        data.setValue(user);

                        Log.e("TOKEN", authLogin.getToken());
                    }
                } else {
                    String message;

                    switch (responseCode) {
                        case 400:
                            message = "Empty data.";
                            break;
                        case 409:
                            message = "Data conflict.";
                            break;
                        case 500:
                            message = "Server error.";
                            break;
                        default:
                            message = "Bad credentials.";
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
                    @NotNull Call<AuthLogin> call,
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

    /**
     * Registers a new user account.
     *
     * @param user The register candidate user.
     * @return The registered user.
     * @author Ricard Pinilla Barnes
     */
    public LiveData<Boolean> registerAccount(User user) {
        final MutableLiveData<Boolean> data = new MutableLiveData<>();

        accountService.registerAccount(user).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(
                    @NotNull Call<Void> call,
                    @NotNull Response<Void> response
            ) {
                int responseCode = response.code();
                if (responseCode == 201) {
                    data.setValue(true);
                } else {
                    String message;

                    switch (responseCode) {
                        case 400:
                            message = "Empty data.";
                            break;
                        case 409:
                            message = "User already exists.";
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
