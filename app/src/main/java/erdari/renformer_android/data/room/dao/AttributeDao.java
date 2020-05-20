package erdari.renformer_android.data.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import erdari.renformer_android.data.model.Attribute;

/**
 * Required interface for the Attribute data access object model.
 *
 * @author Ricard Pinilla Barnes
 */
@Dao
public interface AttributeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertAttribute(Attribute attribute);

    @Update
    void updateAttribute(Attribute attribute);

    @Delete
    void deleteAttribute(Attribute attribute);

    @Query("SELECT * FROM attribute WHERE id = :attributeId")
    LiveData<Attribute> findAttributeById(long attributeId);

    @Query("DELETE FROM attribute")
    void nukeAttributes();

}
