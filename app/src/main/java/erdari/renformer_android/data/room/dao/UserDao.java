package erdari.renformer_android.data.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import erdari.renformer_android.data.model.User;

/**
 * Required interface for the User data access object model.
 *
 * @author Ricard Pinilla Barnes
 */
@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertUser(User user);

    @Update
    void updateUser(User user);

    @Delete
    void deleteUser(User user);

    @Query("SELECT * FROM user WHERE id = :userId")
    LiveData<User> findUserById(long userId);

    @Query("DELETE FROM user")
    void nukeUsers();

}
