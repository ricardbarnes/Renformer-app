package erdari.renformer_android.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import erdari.renformer_android.data.api.CategoryApi;
import erdari.renformer_android.data.model.relational.RelationalCategory;
import erdari.renformer_android.data.model.Category;
import erdari.renformer_android.data.room.CategoryRoom;

/**
 * Category repository.
 *
 * @author Ricard Pinilla Barnes
 */
public class CategoryRepository {

    private CategoryApi categoryApi;
    private CategoryRoom categoryRoom;

    /**
     * Class constructor.
     *
     * @param context The app context that will handle the outputs.
     * @author Ricard Pinilla Barnes
     */
    public CategoryRepository(Context context) {
        categoryApi = new CategoryApi(context);
        categoryRoom = new CategoryRoom(context);
    }

    /**
     * Finds all the categories into a specific reform type.
     *
     * @param reformTypeId The parent reform type id.
     * @return A list with the required categories.
     * @author Ricard Pinilla Barnes
     */
    public LiveData<List<Category>> findAllCategoriesByReformTypeId(long reformTypeId) {
        return categoryApi.findAllCategoriesByReformTypeId(reformTypeId);
    }

    /**
     * Creates a new category inside the corresponding reform type.
     *
     * @param category The category to be created.
     * @return True if the creation has been successful, false if not.
     * @author Ricard Pinilla Barnes
     */
    public LiveData<Boolean> createCategory(RelationalCategory category) {
        return categoryApi.createCategory(category);
    }

    /**
     * Finds a category by its id.
     *
     * @param id The Category id.
     * @return The requested category, if found.
     * @author Ricard Pinilla Barnes
     */
    public LiveData<Category> findCategoryById(long id) {
        return categoryApi.findCategoryById(id);
    }

    /**
     * Updates category.
     *
     * @param category The category to be patched.
     * @return True if the patching is done, false if not.
     * @author Ricard Pinilla Barnes
     */
    public LiveData<Boolean> patchCategory(RelationalCategory category) {
        return categoryApi.patchCategory(category);
    }

    /**
     * Deletes a category.
     *
     * @param category The category to be deleted.
     * @return True if deleted, false if not.
     * @author Ricard Pinilla Barnes
     */
    public LiveData<Boolean> deleteCategory(@NotNull Category category) {
        return categoryApi.deleteCategory(category);
    }

    /**
     * Persists a category on the local room.
     *
     * @param category The category to be persisted.
     * @return The assigned id (room).
     * @author Ricard Pinilla Barnes
     */
    public LiveData<Long> insertLocalCategory(Category category) {
        return categoryRoom.insertCategory(category);
    }

    /**
     * Finds a category on the local room.
     *
     * @param id The category id.
     * @return The found category or null if not found.
     * @author Ricard Pinilla Barnes
     */
    public LiveData<Category> findLocalCategoryById(long id) {
        return categoryRoom.findCategoryById(id);
    }

    /**
     * Deletes a category from the local attribute room.
     *
     * @param category The category to be deleted.
     * @author Ricard Pinilla Barnes
     */
    public void deleteLocalCategory(Category category) {
        categoryRoom.deleteCategory(category);
    }

}
