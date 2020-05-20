package erdari.renformer_android.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;

import erdari.renformer_android.data.api.UserApi;
import erdari.renformer_android.data.model.User;
import erdari.renformer_android.data.room.UserRoom;

/**
 * User repository.
 *
 * @author Ricard Pinilla Barnes
 */
public class UserRepository {

    private UserApi userApi;
    private UserRoom userRoom;

    /**
     * Class constructor.
     *
     * @param context The app context that will handle the outputs.
     */
    public UserRepository(Context context) {
        userApi = new UserApi(context);
        userRoom = new UserRoom(context);
    }

    /**
     * Creates a new user inside the corresponding reform type.
     *
     * @param user The user to be created.
     * @return True if the creation has been successful, fals if not.
     * @author Ricard Pinilla Barnes
     */
    public LiveData<Boolean> createUser(User user) {
        return userApi.createUser(user);
    }

    /**
     * Deletes a user.
     *
     * @param user The user to be deleted.
     * @return True if deleted, false if not.
     * @author Ricard Pinilla Barnes
     */
    public LiveData<Boolean> deleteUser(User user) {
        return userApi.deleteUser(user);
    }

    /**
     * Updates a user.
     *
     * @param user The user to be patched.
     * @return True if patched, false if not
     * @author Ricard Pinilla Barnes
     */
    public LiveData<Boolean> patchUser(User user) {
        return userApi.patchUser(user);
    }

    /**
     * Adds a user.
     *
     * @param user The user to be added.
     * @return True if added, false if not.
     * @author Ricard Pinilla Barnes
     */
    public LiveData<Boolean> putUser(User user) {
        return userApi.putUser(user);
    }

    /**
     * Finds user by email address.
     *
     * @param email The email to be searched.
     * @return The whole user object, if found.
     * @author Ricard Pinilla Barnes
     */
    public LiveData<User> findUserByEmail(String email) {
        return userApi.findUserByEmail(email);
    }

    /**
     * Finds all the users corresponding to a concrete role.
     *
     * @param role The corresponding user role.
     * @return The requested role user list.
     * @author Ricard Pinilla Barnes
     */
    public LiveData<List<User>> findUsersByRole(int role) {
        return userApi.findUsersByRole(role);
    }

    /**
     * Persists an user on the local room.
     *
     * @param user The user to be persisted.
     * @return The assigned id (room).
     * @author Ricard Pinilla Barnes
     */
    public LiveData<Long> insertLocalUser(User user) {
        return userRoom.insertUser(user);
    }

    /**
     * Finds an user on the local room.
     *
     * @param id The user id.
     * @return The found user or null if not found.
     * @author Ricard Pinilla Barnes
     */
    public LiveData<User> findLocalUserById(long id) {
        return userRoom.getUserById(id);
    }

    /**
     * Deletes an user from the local attribute room.
     *
     * @param user The user to be deleted.
     * @author Ricard Pinilla Barnes
     */
    public void deleteLocalUser(User user) {
        userRoom.deleteUser(user);
    }

}
