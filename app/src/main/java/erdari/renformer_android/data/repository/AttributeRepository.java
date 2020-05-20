package erdari.renformer_android.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import erdari.renformer_android.data.api.AttributeApi;
import erdari.renformer_android.data.model.relational.RelationalAttribute;
import erdari.renformer_android.data.model.Attribute;
import erdari.renformer_android.data.room.AttributeRoom;

/**
 * Attribute repository.
 *
 * @author Ricard Pinilla Barnes
 */
public class AttributeRepository {

    private AttributeApi attributeApi;
    private AttributeRoom attributeRoom;

    private Context mContext;

    /**
     * Class constructor.
     *
     * @param context The app context that will handle the outputs.
     * @author Ricard Pinilla Barnes
     */
    public AttributeRepository(Context context) {
        attributeApi = new AttributeApi(context);
        attributeRoom = new AttributeRoom(context);
        mContext = context;
    }

    /**
     * Finds all the attributes into a specific reform type.
     *
     * @param reformTypeId The parent attribute id.
     * @return A list with the required attributes.
     * @author Ricard Pinilla Barnes
     */
    public LiveData<List<Attribute>> findAllAttributesByReformTypeId(long reformTypeId) {
        return attributeApi.findAllAttributesByReformTypeId(reformTypeId);
    }

    /**
     * Creates a new attribute inside the corresponding reform type.
     *
     * @param attribute The attribute to be created.
     * @return True if the creation has been successful, false if not.
     * @author Ricard Pinilla Barnes
     */
    public LiveData<Attribute> createAttribute(RelationalAttribute attribute) {
        return attributeApi.createAttribute(attribute);
    }

    /**
     * Finds an attribute by its id.
     *
     * @param id The attribute id.
     * @return The requested attribute, if found.
     * @author Ricard Pinilla Barnes
     */
    public LiveData<Attribute> findAttributeById(long id) {
        return attributeApi.findAttributeById(id);
    }

    /**
     * Updates an attribute.
     *
     * @param attribute The attribute to be patched.
     * @return True if the patching is done, false if not.
     * @author Ricard Pinilla Barnes
     */
    public LiveData<Boolean> patchAttribute(RelationalAttribute attribute) {
        return attributeApi.patchAttribute(attribute);
    }

    /**
     * Deletes an attribute.
     *
     * @param attribute The attribute to be deleted.
     * @return True if deleted, false if not.
     * @author Ricard Pinilla Barnes
     */
    public LiveData<Boolean> deleteAttribute(@NotNull Attribute attribute) {
        return attributeApi.deleteAttribute(attribute);
    }

    /**
     * Persists an attribute on the local room.
     *
     * @param attribute The attribute to be persisted.
     * @return The assigned id (room).
     * @author Ricard Pinilla Barnes
     */
    public LiveData<Long> insertLocalAttribute(Attribute attribute) {
        return attributeRoom.insertAttribute(attribute);
    }

    /**
     * Finds an attribute on the local room.
     *
     * @param id The attribute id.
     * @return The found attribute or null if not found.
     * @author Ricard Pinilla Barnes
     */
    public LiveData<Attribute> findLocalAttributeById(long id) {
        return attributeRoom.findAttributeById(id);
    }

    /**
     * Deletes an attribute from the local attribute room.
     *
     * @param attribute The attribute to be deleted.
     * @author Ricard Pinilla Barnes
     */
    public void deleteLocalAttribute(Attribute attribute) {
        attributeRoom.deleteAttribute(attribute);
    }

}
