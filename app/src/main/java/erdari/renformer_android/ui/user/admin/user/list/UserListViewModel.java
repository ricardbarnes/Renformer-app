package erdari.renformer_android.ui.user.admin.user.list;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

/**
 * View model for the user list.
 *
 * @author Ricard Pinilla Barnes.
 */
public class UserListViewModel extends ViewModel {

    private MutableLiveData<Integer> mIndex = new MutableLiveData<>();
    private LiveData<String> mText = Transformations.map(mIndex, input -> {
        String userType;
        if (input == 1) {
            userType = "Admins";
        } else {
            userType = "Customers";
        }
        return userType + " are sorted by email.";
    });

    public void setIndex(int index) {
        mIndex.setValue(index);
    }

    public LiveData<String> getText() {
        return mText;
    }
}