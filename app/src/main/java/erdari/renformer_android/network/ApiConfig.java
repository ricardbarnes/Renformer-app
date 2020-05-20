package erdari.renformer_android.network;

import android.content.Context;
import android.widget.Toast;

/**
 * Constants for the API REST server interaction.
 * <p>
 * If the path string finishes with "/" means that route needs a PARAMETER.
 *
 * @author Ricard Pinilla Barnes
 */
public class ApiConfig {

    public static final String NO_CONNECTION_MSG = "No response from server.";
    public static final String TOKEN_START = "Bearer ";

    // URL settings
    private static final String PROTOCOL = "https";
    private static final String ADDRESS = "vps682126.ovh.net";
    private static final int PORT = 5001;
    private static final String NAME = "api";
    private static final String VERSION = "1";

    /**
     * Builds and return the complete API URL
     *
     * @return API BASE URL
     * @author Ricard Pinilla Barnes
     */
    static String getApiUrl() {
        return PROTOCOL +
                "://" +
                ADDRESS +
                ":" +
                PORT +
                "/" +
                NAME +
                "/v" +
                VERSION +
                "/";
    }

    /**
     * Prints the standard Api failure message into the current context.
     *
     * @param context The current context.
     */
    public static void showStandardFailureToast(Context context) {
        Toast.makeText(
                context,
                ApiConfig.NO_CONNECTION_MSG,
                Toast.LENGTH_SHORT
        ).show();
    }

}
