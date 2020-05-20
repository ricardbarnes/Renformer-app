package erdari.renformer_android.data.api;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import erdari.renformer_android.data.api.helper.FindUsersByRolePaginated;
import erdari.renformer_android.data.api.service.UserService;
import erdari.renformer_android.data.model.User;
import erdari.renformer_android.network.ApiConfig;
import erdari.renformer_android.network.ApiErrorMessageRetriever;
import erdari.renformer_android.network.RetrofitRequest;
import erdari.renformer_android.security.Session;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * User api management.
 *
 * @author Ricard Pinilla Barnes
 */
public class UserApi {

    private final UserService userService;
    private final Context context;

    /**
     * Class constructor.
     *
     * @param context The app context that will handle the outputs.
     */
    public UserApi(Context context) {
        Session session = Session.getInstance(context);
        this.context = context;
        this.userService = RetrofitRequest.getUnsafeRetrofitInstanceWithToken(
                session.getSessionToken()
        ).create(UserService.class);
    }

    /**
     * Creates a new user inside the corresponding reform type.
     *
     * @param user The user to be created.
     * @return True if the creation has been successful, fals if not.
     * @author Ricard Pinilla Barnes
     */
    public MutableLiveData<Boolean> createUser(User user) {
        final MutableLiveData<Boolean> data = new MutableLiveData<>();

        userService.createUser(user)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(
                            @NotNull Call<Void> call,
                            @NotNull Response<Void> response
                    ) {
                        int responseCode = response.code();
                        if (responseCode == 201) {
                            data.setValue(true);
                        } else {
                            data.setValue(false);

                            String message = ApiErrorMessageRetriever.retrieveErrorMessage(response);
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
                        ApiConfig.showStandardFailureToast(context);
                    }
                });

        return data;
    }

    /**
     * Deletes a user.
     *
     * @param user The user to be deleted.
     * @return True if deleted, false if not.
     * @author Ricard Pinilla Barnes
     */
    public MutableLiveData<Boolean> deleteUser(@NotNull User user) {
        final MutableLiveData<Boolean> data = new MutableLiveData<>();

        userService.deleteUserById(user.getId())
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
                            data.setValue(false);

                            String message = ApiErrorMessageRetriever.retrieveErrorMessage(response);
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
                        ApiConfig.showStandardFailureToast(context);
                    }
                });

        return data;
    }

    /**
     * Updates a user.
     *
     * @param user The user to be patched.
     * @return True if patched, false if not
     * @author Ricard Pinilla Barnes
     */
    public MutableLiveData<Boolean> patchUser(User user) {
        final MutableLiveData<Boolean> data = new MutableLiveData<>();

        userService.patchUserById(user.getId(), user)
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
                            data.setValue(false);

                            String message = ApiErrorMessageRetriever.retrieveErrorMessage(response);
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
                        ApiConfig.showStandardFailureToast(context);
                    }
                });

        return data;
    }

    /**
     * Adds a user.
     *
     * @param user The user to be added.
     * @return True if added, false if not.
     * @author Ricard Pinilla Barnes
     */
    public MutableLiveData<Boolean> putUser(User user) {
        final MutableLiveData<Boolean> data = new MutableLiveData<>();

        userService.updateUserById(user.getId(), user)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(
                            @NotNull Call<Void> call,
                            @NotNull Response<Void> response
                    ) {
                        int responseCode = response.code();
                        if (responseCode == 200 || responseCode == 201) {
                            data.setValue(true);
                            if (responseCode == 201) {
                                Toast.makeText(context, "User has been created.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            data.setValue(false);

                            String message = ApiErrorMessageRetriever.retrieveErrorMessage(response);
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
                        ApiConfig.showStandardFailureToast(context);
                    }
                });

        return data;
    }

    /**
     * Finds all users. (Uses no pagination.)
     *
     * @return All the users.
     * @author Ricard Pinilla Barnes
     */
    @Deprecated
    public MutableLiveData<List<User>> findAllUsers() {
        final MutableLiveData<List<User>> data = new MutableLiveData<>();

        userService.findAllUsersPaginated(1, 10).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(
                    @NotNull Call<List<User>> call,
                    @NotNull Response<List<User>> response
            ) {
                int responseCode = response.code();
                if (responseCode == 200) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);

                    String message = ApiErrorMessageRetriever.retrieveErrorMessage(response);
                    Toast.makeText(
                            context,
                            message,
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onFailure(
                    @NotNull Call<List<User>> call,
                    @NotNull Throwable t
            ) {
                ApiConfig.showStandardFailureToast(context);
            }
        });

        return data;
    }

    /**
     * Finds user by email address.
     *
     * @param email The email to be searched.
     * @return The whole user object, if found.
     * @author Ricard Pinilla Barnes
     */
    public MutableLiveData<User> findUserByEmail(String email) {
        final MutableLiveData<User> data = new MutableLiveData<>();

        userService.findUserByEmail(email)
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(
                            @NotNull Call<User> call,
                            @NotNull Response<User> response
                    ) {
                        int responseCode = response.code();

                        if (responseCode == 200) {
                            data.setValue(response.body());
                        } else {
                            data.setValue(null);

                            String message = ApiErrorMessageRetriever.retrieveErrorMessage(response);
                            Toast.makeText(
                                    context,
                                    message,
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }

                    @Override
                    public void onFailure(
                            @NotNull Call<User> call,
                            @NotNull Throwable t
                    ) {
                        ApiConfig.showStandardFailureToast(context);
                    }
                });

        return data;
    }

    /**
     * Finds all the users corresponding to a concrete role.
     *
     * @param role The corresponding user role.
     * @return The requested role user list.
     * @author Ricard Pinilla Barnes
     */
    public MutableLiveData<List<User>> findUsersByRole(int role) {
        FindUsersByRolePaginated helper = new FindUsersByRolePaginated(context, userService);
        return helper.findUsersByRoleWithPagination(role);
    }

}
