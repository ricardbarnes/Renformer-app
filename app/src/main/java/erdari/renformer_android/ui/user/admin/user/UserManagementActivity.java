package erdari.renformer_android.ui.user.admin.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import erdari.renformer_android.ui.user.admin.user.list.UserListPagerAdapter;
import erdari.renformer_android.R;
import erdari.renformer_android.config.Key;
import erdari.renformer_android.data.repository.UserRepository;
import erdari.renformer_android.tools.EmailValidator;

/**
 * Holds the main user management elements such as its list.
 *
 * @author Ricard Pinilla Barnes
 */
public class UserManagementActivity extends AppCompatActivity {

    private UserRepository mUserRepo;
    private SearchView mSearchBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_management);

        mUserRepo = new UserRepository(this);

        mSearchBox = findViewById(R.id.searchBox);
        mSearchBox.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String trimmedQuery = query.trim();

                if (EmailValidator.validate(trimmedQuery)) {
                    mUserRepo.findUserByEmail(trimmedQuery).observe(
                            UserManagementActivity.this, foundUser -> {
                                if (foundUser != null) {
                                    Intent intent = new Intent(getApplicationContext(), UserEditRoleActivity.class);
                                    intent.putExtra(Key.JSON_USER, new Gson().toJson(foundUser));
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(
                                            UserManagementActivity.this,
                                            "User not found",
                                            Toast.LENGTH_SHORT
                                    ).show();
                                }
                            });
                } else {
                    Toast.makeText(
                            UserManagementActivity.this,
                            "Must be a valid email address",
                            Toast.LENGTH_SHORT
                    ).show();
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        UserListPagerAdapter userListPagerAdapter = new UserListPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(userListPagerAdapter);

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(this, UserCreateActivity.class);
            startActivity(intent);
            finish();
        });
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
     * Turns the user back to the panel.
     *
     * @param view The triggered view.
     */
    public void goBack(View view) {
        finish();
    }
}