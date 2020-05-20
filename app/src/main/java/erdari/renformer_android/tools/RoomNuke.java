package erdari.renformer_android.tools;

import android.content.Context;

import erdari.renformer_android.data.room.AttributeRoom;
import erdari.renformer_android.data.room.CategoryRoom;
import erdari.renformer_android.data.room.OptionRoom;
import erdari.renformer_android.data.room.ReformTypeRoom;
import erdari.renformer_android.data.room.UserRoom;

/**
 * Holds the nuke for the rooms method.
 *
 * @author Ricard Pinilla Barnes
 */
public class RoomNuke {

    /**
     * Deletes all the rows of every app's room database.
     * <p>
     * Intended to free up some space in the device.
     *
     * @param context The app current context.
     * @author Ricard Pinilla Barnes
     */
    public static void nukeAll(Context context) {
        AttributeRoom attributeRoom = new AttributeRoom(context);
        CategoryRoom categoryRoom = new CategoryRoom(context);
        OptionRoom optionRoom = new OptionRoom(context);
        ReformTypeRoom reformTypeRoom = new ReformTypeRoom(context);
        UserRoom userRoom = new UserRoom(context);

        attributeRoom.nukeAttributes();
        categoryRoom.nukeCategories();
        optionRoom.nukeOptions();
        reformTypeRoom.nukeReformTypes();
        userRoom.nukeUsers();
    }

}
