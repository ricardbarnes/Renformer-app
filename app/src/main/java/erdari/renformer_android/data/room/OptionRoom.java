package erdari.renformer_android.data.room;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import org.jetbrains.annotations.NotNull;

import erdari.renformer_android.data.model.Option;
import erdari.renformer_android.data.room.database.OptionDatabase;

/**
 * Room manager class for categories.
 *
 * @author Ricard Pinilla Barnes
 */
public class OptionRoom {

    private static final String DB_NAME = "db_option";

    private OptionDatabase optionDatabase;

    /**
     * Class constructor.
     *
     * @param context Tha current app context.
     * @author Ricard Pinilla Barnes
     */
    public OptionRoom(Context context) {
        optionDatabase = Room.databaseBuilder(context, OptionDatabase.class, DB_NAME).build();
    }

    /**
     * Inserts an option into the room database.
     *
     * @param option The option to be inserted.
     * @return The auto assigned id.
     * @author Ricard Pinilla Barnes
     */
    public LiveData<Long> insertOption(final Option option) {
        return Static.insertOption(optionDatabase, option);
    }

    /**
     * Updates an option already persisted in the room database.
     *
     * @param option The option to update.
     * @author Ricard Pinilla Barnes
     */
    public void updateOption(final Option option) {
        Static.updateOption(optionDatabase, option);
    }

    /**
     * Deletes an option from the room database.
     *
     * @param option The option to be deleted.
     * @author Ricard Pinilla Barnes
     */
    public void deleteOption(final Option option) {
        Static.deleteOption(optionDatabase, option);
    }

    /**
     * Deletes all the options from the room database.
     *
     * @author Ricard Pinilla Barnes
     */
    public void nukeOptions() {
        Static.nukeOptions(optionDatabase);
    }

    /**
     * Finds an options from the room database.
     *
     * @param id The id of the searched options.
     * @return The searched options if found.
     * @author Ricard Pinilla Barnes
     */
    public LiveData<Option> findOptionById(long id) {
        return optionDatabase.daoAccess().findOptionById(id);
    }

    /**
     * Static methods for the OptionsRoom class.
     * Those are needed to avoid memory leaks, as the methods run in parallel child threads.
     * Descriptions are the same as in the caller methods.
     *
     * @author Ricard Pinilla Barnes
     */
    private static class Static {

        /**
         * Description in the caller method.
         *
         * @param optionDatabase An option database object.
         * @param option         An option object.
         * @return An autogenerated id.
         * @author Ricard Pinilla Barnes
         */
        @NotNull
        private static LiveData<Long> insertOption(
                final OptionDatabase optionDatabase,
                final Option option
        ) {
            final MutableLiveData<Long> id = new MutableLiveData<>();

            new AsyncTask<Void, Void, Long>() {
                @Override
                protected Long doInBackground(Void... voids) {
                    return optionDatabase.daoAccess().insertOption(option);
                }

                @Override
                protected void onPostExecute(Long aLong) {
                    id.postValue(aLong);
                }
            }.execute();

            return id;
        }

        /**
         * Description in the caller method.
         *
         * @param optionDatabase An option database object.
         * @param option         An option object.
         * @author Ricard Pinilla Barnes
         */
        private static void updateOption(
                final OptionDatabase optionDatabase,
                final Option option
        ) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    optionDatabase.daoAccess().updateOption(option);
                    return null;
                }
            }.execute();
        }

        /**
         * Description in the caller method.
         *
         * @param optionDatabase An option database object.
         * @param option         An option object.
         * @author Ricard Pinilla Barnes
         */
        private static void deleteOption(
                final OptionDatabase optionDatabase,
                final Option option
        ) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    optionDatabase.daoAccess().deleteOption(option);
                    return null;
                }
            }.execute();
        }

        /**
         * Description in the caller method.
         *
         * @param optionDatabase An option database object.
         * @author Ricard Pinilla Barnes
         */
        private static void nukeOptions(final OptionDatabase optionDatabase) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    optionDatabase.daoAccess().nukeOptions();
                    return null;
                }
            }.execute();
        }

    }

}
