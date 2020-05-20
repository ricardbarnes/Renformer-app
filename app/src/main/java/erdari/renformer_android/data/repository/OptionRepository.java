package erdari.renformer_android.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import erdari.renformer_android.data.api.OptionApi;
import erdari.renformer_android.data.model.relational.RelationalOption;
import erdari.renformer_android.data.model.Option;
import erdari.renformer_android.data.room.OptionRoom;

/**
 * Option repository.
 *
 * @author Ricard Pinilla Barnes
 */
public class OptionRepository {

    private OptionApi optionApi;
    private OptionRoom optionRoom;

    private Context mContext;

    /**
     * Class constructor.
     *
     * @param context The app context that will handle the outputs.
     * @author Ricard Pinilla Barnes
     */
    public OptionRepository(Context context) {
        optionApi = new OptionApi(context);
        optionRoom = new OptionRoom(context);
        mContext = context;
    }

    /**
     * Finds all the options into a specific attribute.
     *
     * @param attributeId The parent attribute id.
     * @return A list with the required options.
     * @author Ricard Pinilla Barnes
     */
    public LiveData<List<Option>> findAllOptionsByAttributeId(long attributeId) {
        return optionApi.findAllOptionsByAttributeId(attributeId);
    }

    /**
     * Creates a new option inside the corresponding attribute.
     *
     * @param option The option to be created.
     * @return True if the creation has been successful, false if no.
     * @author Ricard Pinilla Barnes
     */
    public LiveData<Boolean> createOption(RelationalOption option) {
        return optionApi.createOption(option);
    }

    /**
     * Finds an option by its id.
     *
     * @param id The Option id.
     * @return The requested option, if found.
     * @author Ricard Pinilla Barnes
     */
    public LiveData<Option> findOptionById(long id) {
        return optionApi.findOptionById(id);
    }

    /**
     * Updates an option.
     *
     * @param option The option to be patched.
     * @return True if the patching is done, false if not.
     * @author Ricard Pinilla Barnes
     */
    public LiveData<Boolean> patchOption(RelationalOption option) {
        return optionApi.patchOption(option);
    }

    /**
     * Deletes an option.
     *
     * @param option The option to be deleted.
     * @return True if deleted, false if not.
     * @author Ricard Pinilla Barnes
     */
    public LiveData<Boolean> deleteOption(@NotNull Option option) {
        return optionApi.deleteOption(option);
    }

    /**
     * Persists an option on the local room.
     *
     * @param option The attribute to be persisted.
     * @return The assigned id (room).
     * @author Ricard Pinilla Barnes
     */
    public LiveData<Long> insertLocalOption(Option option) {
        return optionRoom.insertOption(option);
    }

    /**
     * Finds an option on the local room.
     *
     * @param id The attribute id.
     * @return The found attribute or null if not found.
     * @author Ricard Pinilla Barnes
     */
    public LiveData<Option> findLocalOptionById(long id) {
        return optionRoom.findOptionById(id);
    }

    /**
     * Deletes an option from the local attribute room.
     *
     * @param option The attribute to be deleted.
     * @author Ricard Pinilla Barnes
     */
    public void deleteLocalOption(Option option) {
        optionRoom.deleteOption(option);
    }

}
