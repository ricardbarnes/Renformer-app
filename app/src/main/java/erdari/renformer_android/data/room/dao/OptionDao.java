package erdari.renformer_android.data.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import erdari.renformer_android.data.model.Option;

/**
 * Required interface for the Option data access object model.
 *
 * @author Ricard Pinilla Barnes
 */
@Dao
public interface OptionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertOption(Option option);

    @Update
    void updateOption(Option option);

    @Delete
    void deleteOption(Option option);

    @Query("SELECT * FROM option WHERE id = :optionId")
    LiveData<Option> findOptionById(long optionId);

    @Query("DELETE FROM option")
    void nukeOptions();

}
