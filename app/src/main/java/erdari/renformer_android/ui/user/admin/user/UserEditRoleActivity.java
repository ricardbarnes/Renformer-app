package erdari.renformer_android.ui.user.admin.user;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import erdari.renformer_android.R;
import erdari.renformer_android.config.Key;
import erdari.renformer_android.config.Role;
import erdari.renformer_android.data.model.User;
import erdari.renformer_android.data.repository.ReformBudgetRepository;
import erdari.renformer_android.data.repository.UserRepository;
import erdari.renformer_android.security.Session;

/**
 * Holds the user role edition user interface.
 *
 * @author Ricard Pinilla Barnes
 */
public class UserEditRoleActivity extends AppCompatActivity {

    private boolean mIsEditMode;
    private int mInitialRole;
    private int mSelectedRole;
    private User mUser;
    private UserRepository mUserRepo;

    private TextView mTitleText;
    private TextView mIdBox;
    private TextView mNameBox;
    private TextView mSurnameBox;
    private TextView mEmailBox;
    private TextView mDeleteText;

    private RadioButton mAdminRadioButton;
    //    private RadioButton mManagerRadioButton;
    private RadioButton mCustomerRadioButton;

    private Button mRoleButton;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_role);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        mProgressBar = findViewById(R.id.categoriesProgressBar);
        mTitleText = findViewById(R.id.title);
        mIdBox = findViewById(R.id.userIdText);
        mNameBox = findViewById(R.id.nameBox);
        mSurnameBox = findViewById(R.id.factorBox);
        mEmailBox = findViewById(R.id.idBox);
        mDeleteText = findViewById(R.id.deleteOptionLabel);

        mAdminRadioButton = findViewById(R.id.radioButtonAdmin);
//        mManagerRadioButton = findViewById(R.id.radioButtonManager);
        mCustomerRadioButton = findViewById(R.id.radioButtonCustomer);

        mRoleButton = findViewById(R.id.saveButton);

        long userId = getIntent().getLongExtra(Key.USER_ID, 0);

        mUserRepo = new UserRepository(getApplicationContext());
        mUserRepo.findLocalUserById(userId).observe(this, user -> {
            if (user != null) {
                mUser = user;
                setUpViews();
            } else {
                Toast.makeText(this, "Some error happened", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    @Override
    public void onDestroy() {
        mUserRepo.deleteLocalUser(mUser); // Cleans the room and free some space
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_role_menu, menu);
        return true;
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
            Intent intent = new Intent(this, UserManagementActivity.class);
            intent.putExtra(Key.USER_ROLE, mUser.getRole());
            startActivity(intent);
            finish();
        } else if (id == R.id.budget_menu) {
            ReformBudgetRepository reformBudgetRepo = new ReformBudgetRepository(this);
            reformBudgetRepo.findAllUserBudgetsByUserId(mUser.getId())
                    .observe(this, reformBudgets -> {
                        if (reformBudgets != null && !reformBudgets.isEmpty()) {
                            //
                        } else {
                            Toast.makeText(
                                    this,
                                    "This user has no budgets yet...",
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    });
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, UserManagementActivity.class);
        intent.putExtra(Key.USER_ROLE, mUser.getRole());
        startActivity(intent);
        finish();
    }

    /**
     * Toggles the edit/save button.
     *
     * @param view The triggered view.
     */
    public void toggleChangeRole(View view) {
        mAdminRadioButton.setEnabled(!mIsEditMode);
//        mManagerRadioButton.setEnabled(!mIsEditMode);
        mCustomerRadioButton.setEnabled(!mIsEditMode);
        if (!mIsEditMode) {
            mRoleButton.setText(R.string.save_role_button);
        } else {
            mRoleButton.setText(R.string.change_user_role);
            if (mUser.getRole() != mSelectedRole) {
                applyChanges();
            }
        }
        mIsEditMode = !mIsEditMode;
    }

    /**
     * Starts the user change application event chain.
     */
    private void applyChanges() {
        mProgressBar.setVisibility(View.VISIBLE);
        mUser.setRole(mSelectedRole);

        mUserRepo.patchUser(mUser).observe(this, isPatched -> {
            if (isPatched) {
                Toast.makeText(this, "User successfully updated.", Toast.LENGTH_SHORT).show();
            }
        });
        mProgressBar.setVisibility(View.GONE);
    }

    /**
     * Handles the edited user deletion.
     *
     * @param view The triggered view.
     */
    public void deleteAccount(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Delete account")
                .setMessage("Are you sure that you want to proceed?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    mProgressBar.setVisibility(View.VISIBLE);

                    mUserRepo.deleteUser(mUser).observe(this, isDeleted -> {
                        Toast.makeText(this, "Account successfully deleted.", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(this, UserManagementActivity.class);
                        intent.putExtra(Key.USER_ROLE, mUser.getRole());
                        startActivity(intent);

                        mUserRepo.deleteUser(mUser);

                        finish();
                    });

                    mProgressBar.setVisibility(View.GONE);
                }).setNegativeButton(android.R.string.no, null).show();
    }

    private void setUpViews() {
        mInitialRole = mUser.getRole();
        mSelectedRole = mInitialRole;

        if (Session.getInstance(this).getSessionUser().getId() == mUser.getId()) {
            mTitleText.setText(R.string.my_info);
            mDeleteText.setVisibility(View.INVISIBLE);
        }

        mIdBox.setText(String.valueOf(mUser.getId()));
        mNameBox.setText(mUser.getName());
        mSurnameBox.setText(mUser.getSurname());
        mEmailBox.setText(mUser.getEmail());

        switch (mUser.getRole()) {
            case Role.ADMIN:
                mAdminRadioButton.setChecked(true);
                break;
//            case Role.MANAGER:
//                mManagerRadioButton.setChecked(true);
//                break;
            default:
                mCustomerRadioButton.setChecked(true);
                break;
        }

        mAdminRadioButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                mSelectedRole = Role.ADMIN;
            }
        });

        mCustomerRadioButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                mSelectedRole = Role.CUSTOMER;
            }
        });

        mProgressBar.setVisibility(View.GONE);
    }

}
