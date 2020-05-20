package erdari.renformer_android.data.room.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import erdari.renformer_android.data.model.ReformType;
import erdari.renformer_android.data.room.dao.ReformTypeDao;

/**
 * Database holder class for the ReformType model.
 *
 * @author Ricard Pinilla Barnes
 */
@Database(entities = {ReformType.class}, version = 2, exportSchema = false)
public abstract class ReformTypeDatabase extends RoomDatabase {

    public abstract ReformTypeDao daoAccess();

}
