package erdari.renformer_android.ui.user.admin;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

import erdari.renformer_android.R;
import erdari.renformer_android.config.AppConstants;
import erdari.renformer_android.config.Key;
import erdari.renformer_android.security.Session;
import erdari.renformer_android.tools.RoomNuke;
import erdari.renformer_android.ui.login.LoginActivity;
import erdari.renformer_android.ui.user.ReformTypeViewListActivity;
import erdari.renformer_android.ui.user.UserEditActivity;
import erdari.renformer_android.ui.user.admin.reformtype.ReformTypeListActivity;
import erdari.renformer_android.ui.user.admin.user.UserCreateActivity;
import erdari.renformer_android.ui.user.admin.user.UserManagementActivity;

/**
 * Holds the Admin main user interface.
 *
 * @author Ricard Pinilla Barnes
 */
public class AdminActivity extends AppCompatActivity {

    boolean doubleBackToExitPressedOnce;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.admin_panel));
        setContentView(R.layout.activity_admin);

        doubleBackToExitPressedOnce = false;

        mProgressBar = findViewById(R.id.categoriesProgressBar);
        mProgressBar.setVisibility(View.VISIBLE);

        Intent intent = getIntent();
        int userDeleted = intent.getIntExtra(Key.USER_DELETED, 0);

        if (userDeleted == 1) {
            Toast.makeText(getApplicationContext(), "User deleted.", Toast.LENGTH_SHORT).show();
        }

        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        RoomNuke.nukeAll(this);  // Cleans the room and frees some space
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_nav_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        if (item.getItemId() == R.id.account_settings) {
            Intent intent = new Intent(getApplicationContext(), UserEditActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            Session.clearSession(this);

            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, R.string.back_to_logout, Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, AppConstants.BACK_PRESS_DELAY_MILLIS);
    }

    /**
     * Starts the user creation user interface.
     *
     * @param view The triggered view.
     * @author Ricard Pinilla Barnes
     */
    public void createUser(View view) {
        Intent intent = new Intent(getApplicationContext(), UserCreateActivity.class);
        startActivity(intent);
    }

    /**
     * Starts the user management activity.
     *
     * @param view The triggered view.
     * @author Ricard Pinilla Barnes
     */
    public void startUserManagement(View view) {
        Intent intent = new Intent(this, UserManagementActivity.class);
        startActivity(intent);
    }

    /**
     * Starts the content management activity.
     *
     * @param view The triggered view.
     * @author Ricard Pinilla Barnes
     */
    public void manageContent(View view) {
        Intent intent = new Intent(this, ReformTypeListActivity.class);
        startActivity(intent);
    }

    /**
     * Starts the content viewer activity.
     *
     * @param view The triggered view.
     * @author Ricard Pinilla Barnes
     */
    public void viewContent(View view) {
        Intent intent = new Intent(this, ReformTypeViewListActivity.class);
        startActivity(intent);
    }

}
