package erdari.renformer_android.data.api.helper;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import erdari.renformer_android.data.api.service.UserService;
import erdari.renformer_android.data.model.User;
import erdari.renformer_android.network.ApiConfig;
import erdari.renformer_android.network.ApiErrorMessageRetriever;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Helper class that holds the recursive methods needed to achieve a proper pagination for the data.
 *
 * @author Ricard Pinilla Barnes
 */
public class FindUsersByRolePaginated {

    private static final int OBJECT_LIMIT = 10;

    private final MutableLiveData<List<User>> data;

    private UserService userService;
    private List<User> accumulatedUsers;
    private Context appContext;
    private int startPosition;

    /**
     * Helper class constructor.
     *
     * @param context The current application context.
     * @param service The user service for the Retrofit requests.
     * @author Ricard Pinilla Barnes
     */
    public FindUsersByRolePaginated(Context context, UserService service) {
        accumulatedUsers = new ArrayList<>();
        data = new MutableLiveData<>();
        appContext = context;
        userService = service;
    }

    /**
     * Finds all the users that have a specific role.
     *
     * @param role The requested user role.
     * @return A list with the users with the requested role.
     * @author Ricard Pinilla Barnes
     */
    public MutableLiveData<List<User>> findUsersByRoleWithPagination(int role) {

        userService.findUsersByRolePaginated(role, startPosition, OBJECT_LIMIT)
                .enqueue(new Callback<List<User>>() {
                    @Override
                    public void onResponse(
                            @NotNull Call<List<User>> call,
                            @NotNull Response<List<User>> response
                    ) {
                        int responseCode = response.code();
                        if (responseCode == 200) {
                            List<User> users = response.body();
                            if (users != null) {
                                if (!users.isEmpty()) {
                                    startPosition += OBJECT_LIMIT;
                                    accumulatedUsers.addAll(users);
                                    findUsersByRoleWithPagination(role);
                                } else {
                                    finishCall();
                                }
                            }
                        } else if (responseCode == 204) {
                            finishCall();
                        } else {
                            data.setValue(null);

                            String message = ApiErrorMessageRetriever.retrieveErrorMessage(response);
                            Toast.makeText(
                                    appContext,
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
                        Toast.makeText(
                                appContext,
                                ApiConfig.NO_CONNECTION_MSG,
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });

        return data;
    }

    /**
     * Handles the end post-call issues.
     *
     * @author Ricard Pinilla Barnes
     */
    private void finishCall() {
        sort();
        data.setValue(accumulatedUsers);
    }

    /**
     * Sorts the retrieved users alphabetically.
     *
     * @author Ricard Pinilla Barnes
     */
    private void sort() {
        Collections.sort(accumulatedUsers, (o1, o2) -> // Sorts alphabetically by name (upwards)
                o1.getEmail().compareToIgnoreCase(o2.getEmail()));
    }

}
