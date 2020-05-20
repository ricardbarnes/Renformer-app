package erdari.renformer_android.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import erdari.renformer_android.data.api.server.ServerTools;
import erdari.renformer_android.ui.register.RegisterActivity;
import erdari.renformer_android.ui.user.customer.CustomerActivity;
import erdari.renformer_android.R;
import erdari.renformer_android.config.Role;
import erdari.renformer_android.data.account.AccountHandler;
import erdari.renformer_android.data.model.User;
import erdari.renformer_android.security.Md5Hash;
import erdari.renformer_android.tools.EmailValidator;
import erdari.renformer_android.tools.RoomNuke;
import erdari.renformer_android.ui.user.admin.AdminActivity;

/**
 * Holds the login user GUI.
 *
 * @author Ricard Pinilla Barnes
 */
public class LoginActivity extends AppCompatActivity {

    private EditText mEmail;
    private EditText mPassword;
    private ProgressBar mProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        RoomNuke.nukeAll(this); // Empties the whole local DB

        mEmail = findViewById(R.id.emailBox);
        mPassword = findViewById(R.id.passwordBox);
        mProgressBar = findViewById(R.id.categoriesProgressBar);

        mProgressBar.setVisibility(View.GONE);
    }

    /**
     * Triggers the user sign in chain.
     *
     * @param view The triggered view.
     * @author Ricard Pinilla Barnes
     */
    public void signIn(View view) {
        mProgressBar.setVisibility(View.VISIBLE);

        if (!isSomeFieldEmpty()) {
            String email = mEmail.getText().toString().trim();
            String password = mPassword.getText().toString().trim();

            if (EmailValidator.validate(email)) {
                User user = new User();
                user.setEmail(email);
                user.setPassword(Md5Hash.encode(password));

                ServerTools serverTools = new ServerTools(this);
                serverTools.checkServer().observe(this, isServerOk -> {
                    if (isServerOk) {
                        AccountHandler accountHandler = new AccountHandler(this);
                        accountHandler.login(user).observe(this, retrievedUser -> {
                            if (retrievedUser != null) {

                                Intent intent;
                                if (retrievedUser.getRole() == Role.ADMIN) {
                                    intent = new Intent(this, AdminActivity.class);
                                } else {
                                    intent = new Intent(this, CustomerActivity.class);
                                }

                                mProgressBar.setVisibility(View.GONE);

                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                });

            } else {
                Toast.makeText(
                        this,
                        "Must be a valid email address.",
                        Toast.LENGTH_SHORT
                ).show();
            }
        } else {
            Toast.makeText(this, "Please, fill the fields.", Toast.LENGTH_SHORT).show();
        }
        mProgressBar.setVisibility(View.GONE);
    }

    /**
     * Triggers the user register interface.
     *
     * @param view The triggered view.
     * @author Ricard Pinilla Barnes
     */
    public void register(View view) {
        ServerTools serverTools = new ServerTools(this);
        serverTools.checkServer().observe(this, isServerOk -> {
            if (isServerOk) {
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Checks if some field is empty.
     *
     * @return True if there is some empty field, false if not.
     * @author Ricard Pinilla Barnes
     */
    private boolean isSomeFieldEmpty() {
        return TextUtils.isEmpty(mEmail.getText()) || TextUtils.isEmpty(mPassword.getText());
    }

    /**
     * Only for debugging purposes. Erase it in a production environment.
     *
     * @param view The triggered view.
     * @author Ricard Pinilla Barnes
     */
    public void adminPenetration(View view) {
        User user = new User();
        user.setEmail("ricard@renformer.com");
//        user.setEmail("usador@gmail.com");
        user.setPassword(Md5Hash.encode("ricardricard"));
//        user.setPassword(Md5Hash.encode("usadorusador"));

        ServerTools serverTools = new ServerTools(this);
        serverTools.checkServer().observe(this, isServerOk -> {
            if (isServerOk) {
                AccountHandler accountHandler = new AccountHandler(this);
                accountHandler.login(user).observe(this, retrievedUser -> {
                    if (retrievedUser != null) {

                        Intent intent;
                        if (retrievedUser.getRole() == Role.ADMIN) {
                            intent = new Intent(this, AdminActivity.class);
                        } else {
                            intent = new Intent(this, CustomerActivity.class);
                        }

                        mProgressBar.setVisibility(View.GONE);

                        startActivity(intent);
                        finish();
                    }
                });
            }
        });
    }

}
