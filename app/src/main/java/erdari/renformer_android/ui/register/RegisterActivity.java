package erdari.renformer_android.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import erdari.renformer_android.R;
import erdari.renformer_android.config.Role;
import erdari.renformer_android.data.account.AccountHandler;
import erdari.renformer_android.data.model.User;
import erdari.renformer_android.security.Md5Hash;
import erdari.renformer_android.tools.EmailValidator;
import erdari.renformer_android.tools.PasswordComparator;
import erdari.renformer_android.ui.user.admin.AdminActivity;
import erdari.renformer_android.ui.user.customer.CustomerActivity;

/**
 * Holds the user registry GUI.
 *
 * @author Ricard Pinilla Barnes
 */
public class RegisterActivity extends AppCompatActivity {

    private EditText mNameBox;
    private EditText mSurnameBox;
    private EditText mEmailBox;
    private EditText mPasswordBox;
    private EditText mPassConfirmBox;
    private ProgressBar mProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mProgressBar = findViewById(R.id.categoriesProgressBar);
        mNameBox = findViewById(R.id.registerNameBox);
        mSurnameBox = findViewById(R.id.registerSurnameBox);
        mEmailBox = findViewById(R.id.registerEmailBox);
        mPasswordBox = findViewById(R.id.registerPassBox);
        mPassConfirmBox = findViewById(R.id.registerConfirmPassBox);

        mProgressBar.setVisibility(View.GONE);
    }

    /**
     * Retrieves all the user data and triggers the registration chain.
     *
     * @param view The triggered view.
     * @author Ricard Pinilla Barnes
     */
    public void registerUser(View view) {
        mProgressBar.setVisibility(View.VISIBLE);

        String name = mNameBox.getText().toString().trim();
        String surname = mSurnameBox.getText().toString().trim();
        String email = mEmailBox.getText().toString().trim();
        String password = mPasswordBox.getText().toString().trim();
        String passConfirm = mPassConfirmBox.getText().toString().trim();

        if (!name.isEmpty()
                && !surname.isEmpty()
                && !email.isEmpty()
                && !password.isEmpty()
                && !passConfirm.isEmpty()) {
            if (EmailValidator.validate(email)) {
                if (PasswordComparator.arePasswordsEqual(password, passConfirm)) {
                    User user = new User();
                    user.setName(name);
                    user.setSurname(surname);
                    user.setEmail(email);
                    user.setPassword(Md5Hash.encode(password));
                    user.setRole(3);

                    AccountHandler accountHandler = new AccountHandler(this);
                    accountHandler.registerAccount(user).observe(this, isRegistered -> {
                        if (isRegistered) {
                            accountHandler.login(user).observe(this, retrievedUser -> {
                                if (retrievedUser != null) {
                                    Intent intent;
                                    if (retrievedUser.getRole() == Role.ADMIN) {
                                        intent = new Intent(this, AdminActivity.class);
                                    } else {
                                        intent = new Intent(this, CustomerActivity.class);
                                    }
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }
                    });

                } else {
                    mPasswordBox.requestFocus();
                    Toast.makeText(getApplicationContext(), "Passwords do not match.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Must be a valid email.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "All fields must be filled.", Toast.LENGTH_LONG).show();
        }
        mProgressBar.setVisibility(View.GONE);
    }
}
