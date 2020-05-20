package erdari.renformer_android.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;

import erdari.renformer_android.data.api.ReformTypeApi;
import erdari.renformer_android.data.model.ReformType;
import erdari.renformer_android.data.room.ReformTypeRoom;

/**
 * Reform types repository.
 *
 * @author Ricard Pinilla Barnes
 */
public class ReformTypeRepository {

    private ReformTypeApi reformTypeApi;
    private ReformTypeRoom reformTypeRoom;

    /**
     * Class constructor.
     *
     * @param context The app context that will handle the outputs.
     * @author Ricard Pinilla Barnes
     */
    public ReformTypeRepository(Context context) {
        reformTypeApi = new ReformTypeApi(context);
        reformTypeRoom = new ReformTypeRoom(context);
    }

    /**
     * Finds all the reform types.
     *
     * @return All the reform types, if any.
     * @author Ricard Pinilla Barnes
     */
    public LiveData<List<ReformType>> findAllReformTypes() {
        return reformTypeApi.findAllReformTypes();
    }

    /**
     * Creates a new reform type.
     *
     * @param reformType The reform type to be created.
     * @return True if creation has been successful, false if not.
     */
    public LiveData<Boolean> createReformType(ReformType reformType) {
        return reformTypeApi.createReformType(reformType);
    }

    /**
     * Finds a reform type by its id.
     *
     * @param id The reform type id.
     * @return The requested reform type, if found.
     * @author Ricard Pinlla Barnes
     */
    public LiveData<ReformType> findReformTypeById(long id) {
        return reformTypeApi.findReformTypeById(id);
    }

    /**
     * Updates a reform type.
     *
     * @param reformType The reform type to be patched.
     * @return True if the patching is done, false if not.
     * @author Ricard Pinilla Barnes
     */
    public LiveData<Boolean> patchReformType(ReformType reformType) {
        return reformTypeApi.patchReformType(reformType);
    }

    /**
     * Deletes a reform type.
     *
     * @param reformType The reform type to be deleted.
     * @return True if deleted, false if not.
     * @author Ricard Pinilla Barnes
     */
    public LiveData<Boolean> deleteReformType(ReformType reformType) {
        return reformTypeApi.deleteReformType(reformType);
    }

    /**
     * Persists a reform type on the local room.
     *
     * @param reformType The attribute to be persisted.
     * @return The assigned id (room).
     * @author Ricard Pinilla Barnes
     */
    public LiveData<Long> insertLocalReformType(ReformType reformType) {
        return reformTypeRoom.insertReformType(reformType);
    }

    /**
     * Finds a reform type on the local room.
     *
     * @param id The reform type id.
     * @return The found reform type or null if not found.
     * @author Ricard Pinilla Barnes
     */
    public LiveData<ReformType> findLocalReformTypeById(long id) {
        return reformTypeRoom.getReformTypeById(id);
    }

    /**
     * Deletes a reform type from the local attribute room.
     *
     * @param reformType The attribute to be deleted.
     * @author Ricard Pinilla Barnes
     */
    public void deleteLocalReformType(ReformType reformType) {
        reformTypeRoom.deleteReformType(reformType);
    }

}
