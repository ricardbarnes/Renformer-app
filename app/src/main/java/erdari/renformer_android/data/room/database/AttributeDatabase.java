package erdari.renformer_android.data.room.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import erdari.renformer_android.data.model.Attribute;
import erdari.renformer_android.data.room.dao.AttributeDao;

/**
 * Database holder class for the Attribute model.
 *
 * @author Ricard Pinilla Barnes
 */
@Database(entities = {Attribute.class}, version = 1, exportSchema = false)
public abstract class AttributeDatabase extends RoomDatabase {

    public abstract AttributeDao daoAccess();

}
