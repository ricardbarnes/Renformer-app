package erdari.renformer_android.network;

import org.jetbrains.annotations.NotNull;

import erdari.renformer_android.data.model.User;

/**
 * Login handler class. Used to simplify the login Api response, by parsing it from JSON to this
 * equivalent model.
 *
 * @author Ricard Pinilla Barnes
 */
public class AuthLogin {

    private String token;
    private String expiration;
    private User user;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @NotNull
    @Override
    public String toString() {
        return "AuthLogin{" +
                "token='" + token + '\'' +
                ", expiration='" + expiration + '\'' +
                ", user=" + user +
                '}';
    }
}
