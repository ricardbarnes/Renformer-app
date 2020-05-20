package erdari.renformer_android.data.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import erdari.renformer_android.data.model.Category;

/**
 * Required interface for the Category data access object model.
 *
 * @author Ricard Pinilla Barnes
 */
@Dao
public interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertCategory(Category category);

    @Update
    void updateCategory(Category category);

    @Delete
    void deleteCategory(Category category);

    @Query("SELECT * FROM category WHERE id =:categoryId")
    LiveData<Category> findCategoryById(long categoryId);

    @Query("DELETE FROM category")
    void nukeCategories();

}
