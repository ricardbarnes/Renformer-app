package erdari.renformer_android.data.room.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import erdari.renformer_android.data.model.User;
import erdari.renformer_android.data.room.dao.UserDao;

/**
 * Database holder class for the User model.
 *
 * @author Ricard Pinilla Barnes
 */
@Database(entities = {User.class}, version = 1, exportSchema = false)
public abstract class UserDatabase extends RoomDatabase {

    public abstract UserDao daoAccess();

}
