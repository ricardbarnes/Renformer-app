package erdari.renformer_android.ui.user.customer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import erdari.renformer_android.R;
import erdari.renformer_android.config.AppConstants;
import erdari.renformer_android.security.Session;
import erdari.renformer_android.tools.RoomNuke;
import erdari.renformer_android.ui.login.LoginActivity;
import erdari.renformer_android.ui.user.ReformTypeViewListActivity;
import erdari.renformer_android.ui.user.UserEditActivity;

/**
 * Holds the customer user interface.
 *
 * @author Ricard Pinilla Barnes
 */
public class CustomerActivity extends AppCompatActivity {

    private boolean doubleBackToExitPressedOnce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
    }

    @Override
    public void onDestroy() {
        RoomNuke.nukeAll(this); // Cleans the room and free some space
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.customer_nav_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
        Toast.makeText(this, R.string.back_to_logout, Toast.LENGTH_SHORT)
                .show();

        new Handler().postDelayed(() ->
                doubleBackToExitPressedOnce = false, AppConstants.BACK_PRESS_DELAY_MILLIS);
    }

    /**
     * Starts the content view activity.
     *
     * @param view the Triggered view.
     * @author Ricard Pinilla Barnes
     */
    public void viewContent(View view) {
        Intent intent = new Intent(this, ReformTypeViewListActivity.class);
        startActivity(intent);
    }

}
