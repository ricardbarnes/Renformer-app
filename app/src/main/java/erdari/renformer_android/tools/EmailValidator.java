package erdari.renformer_android.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Simple email validator based on a REGEX pattern.
 *
 * @author Ricard Pinilla Barnes
 */
public class EmailValidator {

    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile(
                    "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
                    Pattern.CASE_INSENSITIVE
            );

    /**
     * Validates a email string.
     *
     * @param emailStr The email to validate.
     * @return True if validated, false if not.
     * @author Ricard Pinilla Barnes
     */
    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

}
