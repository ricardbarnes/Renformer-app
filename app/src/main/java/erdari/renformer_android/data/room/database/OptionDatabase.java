package erdari.renformer_android.data.room.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import erdari.renformer_android.data.model.Option;
import erdari.renformer_android.data.room.dao.OptionDao;

/**
 * Database holder class for the Option model.
 *
 * @author Ricard Pinilla Barnes
 */
@Database(entities = {Option.class}, version = 1, exportSchema = false)
public abstract class OptionDatabase extends RoomDatabase {

    public abstract OptionDao daoAccess();

}
