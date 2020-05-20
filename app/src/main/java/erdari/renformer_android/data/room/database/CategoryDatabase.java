package erdari.renformer_android.data.room.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import erdari.renformer_android.data.model.Category;
import erdari.renformer_android.data.room.dao.CategoryDao;

/**
 * Database holder class for the Category model.
 *
 * @author Ricard Pinilla Barnes
 */
@Database(entities = {Category.class}, version = 1, exportSchema = false)
public abstract class CategoryDatabase extends RoomDatabase {

    public abstract CategoryDao daoAccess();

}
