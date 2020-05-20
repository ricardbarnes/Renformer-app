package erdari.renformer_android.ui.user;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import erdari.renformer_android.data.api.UserApi;
import erdari.renformer_android.ui.login.LoginActivity;
import erdari.renformer_android.R;
import erdari.renformer_android.config.Password;
import erdari.renformer_android.data.model.User;
import erdari.renformer_android.data.repository.UserRepository;
import erdari.renformer_android.security.Md5Hash;
import erdari.renformer_android.security.Session;
import erdari.renformer_android.tools.PasswordComparator;

/**
 * Holds the user edition user interface.
 *
 * @author Ricard Pinilla Barnes
 */
public class UserEditActivity extends AppCompatActivity {

    private boolean mIsEditMode;
    private User mUser;
    private EditText mNameBox;
    private EditText mSurnameBox;
    private EditText mEmailBox;
    private EditText mPasswordBox;
    private EditText mConfirmPassBox;
    private Button mEditSaveButton;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
        setTitle(getString(R.string.account_settings));

        mProgressBar = findViewById(R.id.categoriesProgressBar);

        Session mSession = Session.getInstance(this);
        TextView mIdText = findViewById(R.id.userIdText);

        mNameBox = findViewById(R.id.nameBox);
        mSurnameBox = findViewById(R.id.factorBox);
        mEmailBox = findViewById(R.id.idBox);
        mPasswordBox = findViewById(R.id.passwordBox);
        mConfirmPassBox = findViewById(R.id.passConfirmBox);

        mUser = mSession.getSessionUser();

        mIdText.setText(String.valueOf(mUser.getId()));
        mNameBox.setText(mUser.getName());
        mSurnameBox.setText(mUser.getSurname());
        mEmailBox.setText(mUser.getEmail());

        mEditSaveButton = findViewById(R.id.saveButton);

        mProgressBar.setVisibility(View.GONE);
    }

    /**
     * Toggles the edit/save button.
     *
     * @param view The triggered view.
     * @author Ricard Pinilla Barnes
     */
    public void toggleChangeRoleButton(View view) {
        mNameBox.setEnabled(!mIsEditMode);
        mSurnameBox.setEnabled(!mIsEditMode);
        mEmailBox.setEnabled(!mIsEditMode);
        mPasswordBox.setEnabled(!mIsEditMode);
        mConfirmPassBox.setEnabled(!mIsEditMode);
        if (!mIsEditMode) {
            mEditSaveButton.setText(R.string.save);
        } else {
            mEditSaveButton.setText(R.string.edit);
            saveChanges();
        }
        mIsEditMode = !mIsEditMode;
    }

    /**
     * Triggers the user saving event chain.
     *
     * @author Ricard Pinilla Barnes
     */
    private void saveChanges() {
        mProgressBar.setVisibility(View.VISIBLE);

        String name = mNameBox.getText().toString().trim();
        String surname = mSurnameBox.getText().toString().trim();
        String email = mEmailBox.getText().toString().trim();
        String password = mPasswordBox.getText().toString().trim();
        String passConfirm = mConfirmPassBox.getText().toString().trim();

        if (!name.isEmpty()
                && !surname.isEmpty()
                && !email.isEmpty()
                && !password.isEmpty()
                && !passConfirm.isEmpty()) {

            if (password.length() >= Password.MIN_PASS_LENGTH) {
                if (PasswordComparator.arePasswordsEqual(password, passConfirm)) {
                    User user = mUser;
                    user.setId(mUser.getId());
                    user.setName(name);
                    user.setSurname(surname);
                    user.setEmail(email);
                    user.setPassword(Md5Hash.encode(password));
                    user.setRole(mUser.getRole());

                    UserApi userApi = new UserApi(this);
                    userApi.patchUser(user).observe(this, isUpdated -> {
                        if (isUpdated) {
                            Session.getInstance(this).setSessionUser(user);
                            Toast.makeText(
                                    this,
                                    "Successfully updated.",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    });
                } else {
                    mPasswordBox.requestFocus();
                    Toast.makeText(
                            getApplicationContext(),
                            "Passwords do not match.",
                            Toast.LENGTH_SHORT
                    ).show();
                }

            } else {
                mPasswordBox.requestFocus();
                Toast.makeText(
                        getApplicationContext(),
                        "Password min length is " + Password.MIN_PASS_LENGTH + " characters.",
                        Toast.LENGTH_SHORT
                ).show();
            }

        } else {
            Toast.makeText(
                    getApplicationContext(),
                    "All fields must be filled.",
                    Toast.LENGTH_LONG
            ).show();
        }
        mProgressBar.setVisibility(View.GONE);
    }

    /**
     * Starts the account deletion event chain.
     *
     * @param view The triggered view.
     * @author Ricard Pinilla Barnes
     */
    public void deleteAccount(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Delete account")
                .setMessage("Are you sure that you want to proceed?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    mProgressBar.setVisibility(View.VISIBLE);

                    UserRepository userRepo = new UserRepository(this);
                    userRepo.deleteUser(Session.getInstance(this).getSessionUser()).observe(this, isDeleted -> {
                        Toast.makeText(this, "Account successfully deleted.", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        getApplicationContext().startActivity(intent);
                    });

                    mProgressBar.setVisibility(View.GONE);
                }).setNegativeButton(android.R.string.no, null).show();
    }
}
