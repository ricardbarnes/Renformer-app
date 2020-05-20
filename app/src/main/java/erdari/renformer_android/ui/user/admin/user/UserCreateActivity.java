package erdari.renformer_android.ui.user.admin.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import erdari.renformer_android.R;
import erdari.renformer_android.config.Key;
import erdari.renformer_android.config.Password;
import erdari.renformer_android.config.Role;
import erdari.renformer_android.data.model.User;
import erdari.renformer_android.data.repository.UserRepository;
import erdari.renformer_android.security.Md5Hash;
import erdari.renformer_android.tools.PasswordComparator;

/**
 * Holds the user creation user interface.
 *
 * @author Ricard Pinilla Barnes
 */
public class UserCreateActivity extends AppCompatActivity {

    private int mSelectedRole;
    private EditText mNameBox;
    private EditText mSurnameBox;
    private EditText mEmailBox;
    private EditText mPasswordBox;
    private EditText mConfirmPassBox;
    private RadioButton mAdminRadioBtn;
    // private RadioButton mManagerRadioBtn;
    private RadioButton mCustomerRadioBtn;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        mSelectedRole = getIntent().getIntExtra(Key.USER_ROLE, 3);

        mProgressBar = findViewById(R.id.categoriesProgressBar);
        mNameBox = findViewById(R.id.nameBox);
        mSurnameBox = findViewById(R.id.factorBox);
        mEmailBox = findViewById(R.id.idBox);
        mPasswordBox = findViewById(R.id.passwordBox);
        mConfirmPassBox = findViewById(R.id.confirmPassBox);
        mAdminRadioBtn = findViewById(R.id.radioButtonAdmin);
//        mManagerRadioBtn = findViewById(R.id.radioButtonManager);
        mCustomerRadioBtn = findViewById(R.id.radioButtonCustomer);

        mCustomerRadioBtn.setChecked(true);

        if (mSelectedRole == Role.ADMIN) {
            mAdminRadioBtn.setChecked(true);
        } else {
            mCustomerRadioBtn.setChecked(true);
        }

        mAdminRadioBtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                mSelectedRole = Role.ADMIN;
            }
        });

//        mManagerRadioBtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            if (isChecked) {
//                mSelectedRole = Role.MANAGER;
//            }
//        });

        mCustomerRadioBtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                mSelectedRole = Role.CUSTOMER;
            }
        });

        mProgressBar.setVisibility(View.GONE);
    }

    /**
     * Stands for the home button action. Returns to the user (by role) list instead of the
     * AdminActivity or whatever static context.
     *
     * @param item A menu item.
     * @return always true.
     */
    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return true;
    }

    /**
     * Starts the user creation event chain.
     *
     * @param view The triggered view.
     * @author Ricard Pinilla Barnes
     */
    public void createUser(View view) {
        mProgressBar.setVisibility(View.GONE);

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
                    User user = new User();
                    user.setName(name);
                    user.setSurname(surname);
                    user.setEmail(email);
                    user.setPassword(Md5Hash.encode(password));
                    user.setRole(mSelectedRole);

                    UserRepository userRepo = new UserRepository(this);
                    userRepo.createUser(user).observe(this, isCreated -> {
                        if (isCreated) {
                            Toast.makeText(
                                    this,
                                    "User successfully created.",
                                    Toast.LENGTH_SHORT
                            ).show();

                            Intent intent = new Intent(this, UserManagementActivity.class);
                            intent.putExtra(Key.USER_ROLE, mSelectedRole);
                            startActivity(intent);

                            finish();
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
}
