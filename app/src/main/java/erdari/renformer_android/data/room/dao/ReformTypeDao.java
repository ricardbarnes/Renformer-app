package erdari.renformer_android.data.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import erdari.renformer_android.data.model.ReformType;

/**
 * Required interface for the ReformType data access object model.
 *
 * @author Ricard Pinilla Barnes
 */
@Dao
public interface ReformTypeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertReformType(ReformType reformType);

    @Update
    void updateReformType(ReformType reformType);

    @Delete
    void deleteReformType(ReformType reformType);

    @Query("SELECT * FROM reform_type WHERE id = :reformTypeId")
    LiveData<ReformType> findReformTypeBy(long reformTypeId);

    @Query("DELETE FROM reform_type")
    void nukeReformTypes();

}
