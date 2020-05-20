package erdari.renformer_android.security;

import android.content.Context;
import android.content.SharedPreferences;

import erdari.renformer_android.data.model.User;

/**
 * Holds the user session during the whole app execution via Shared Preferences.
 *
 * @author Ricard Pinilla Barnes
 */
public class Session {

    private static final String SESSION_PREFS = "session"; // Shared Preferences key

    private static final String SESSION_TOKEN = "sessionToken";
    private static final String SESSION_EXPIRE = "sessionExpire";

    /**
     * User fields keys.
     */
    private static final String PREF_USER_ID = "userId"; // long
    private static final String PREF_USER_ROLE = "userRole"; // int
    private static final String PREF_USER_NAME = "userName"; // String
    private static final String PREF_USER_SURNAME = "userSurname"; // String
    private static final String PREF_USER_EMAIL = "userEmail"; // String
    private static final String PREF_USER_PASSWORD = "userPassword"; // String

    /**
     * Default values for the session user.
     */
    private static final int DEFAULT_ID = 0;
    private static final int DEFAULT_ROLE = 3;
    private static final String DEFAULT_NAME = "Unknown";
    private static final String DEFAULT_SURNAME = "Unknown";
    private static final String DEFAULT_EMAIL = "unknown@unknown.com";
    private static final String DEFAULT_PASSWORD = "";

    private static Session session;

    private SharedPreferences prefs;

    /**
     * Getter for the singleton Session instance.
     *
     * @param context The app context.
     * @return The open session or a new session if there was not any open.
     */
    public static Session getInstance(Context context) {
        if (session == null) {
            session = new Session(context);
        }
        return session;
    }

    /**
     * Session object constructor.
     *
     * @param context The actual context.
     */
    private Session(Context context) {
        prefs = context.getSharedPreferences(SESSION_PREFS, Context.MODE_PRIVATE);
    }

    /**
     * Registers a user in the session.
     *
     * @param user A user object.
     * @author Ricard Pinilla Barnes
     */
    public void setSessionUser(User user) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(PREF_USER_ID, user.getId()).apply();
        editor.putInt(PREF_USER_ROLE, user.getRole()).apply();
        editor.putString(PREF_USER_NAME, user.getName()).apply();
        editor.putString(PREF_USER_SURNAME, user.getSurname()).apply();
        editor.putString(PREF_USER_EMAIL, user.getEmail()).apply();
        editor.putString(PREF_USER_PASSWORD, user.getPassword()).apply();
    }

    /**
     * Retrieves the previously registered user, if there is one.
     *
     * @return The current registered user object.
     * @author Ricard Pinilla Barnes
     */
    public User getSessionUser() {
        User user = new User();
        user.setId(prefs.getLong(PREF_USER_ID, DEFAULT_ID));
        user.setRole(prefs.getInt(PREF_USER_ROLE, DEFAULT_ROLE));
        user.setName(prefs.getString(PREF_USER_NAME, DEFAULT_NAME));
        user.setSurname(prefs.getString(PREF_USER_SURNAME, DEFAULT_SURNAME));
        user.setEmail(prefs.getString(PREF_USER_EMAIL, DEFAULT_EMAIL));
        user.setPassword(prefs.getString(PREF_USER_PASSWORD, DEFAULT_PASSWORD));
        return user;
    }

    /**
     * Setter for the session token.
     *
     * @param token The session token.
     * @author Ricard Pinilla Barnes
     */
    public void setSessionToken(String token) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SESSION_TOKEN, token).apply();
    }

    /**
     * Setter for the session expiration.
     *
     * @param expiration The session expiration date.
     * @author Ricard Pinilla Barnes
     */
    public void setSessionExpiration(String expiration) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SESSION_EXPIRE, expiration).apply();
    }

    public String getSessionToken() {
        return prefs.getString(SESSION_TOKEN, "null");
    }

    /**
     * Clears the current session.
     *
     * @author Ricard Pinilla Barnes
     */
    public static void clearSession(Context context) {
        context.getSharedPreferences(Session.SESSION_PREFS, 0).edit().clear().apply();
    }
}
