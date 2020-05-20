package erdari.renformer_android.network;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Retrieves the error message from an Api response object.
 *
 * @author Ricard Pinilla Barnes
 */
public class ApiErrorMessageRetriever {

    private static final String DEFAULT_MSG = "Some error happened.";

    /**
     * Retrieves the Api error message from a Retrofit2 response.
     *
     * @param response The Retrofit2 response.
     * @return The Api message, if any, or a default error message.
     */
    public static String retrieveErrorMessage(Response<?> response) {
        String message = DEFAULT_MSG;

        if (response != null) {
            ResponseBody errorBody = response.errorBody();

            if (errorBody != null) {
                try {
                    String errorBodyString = errorBody.string();
                    JSONObject jsonResponse = new JSONObject(errorBodyString);
                    message = jsonResponse.getString("message");
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        return message;
    }

}
