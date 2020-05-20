package erdari.renformer_android.tools;

/**
 * Simple password strings comparator.
 *
 * @author Ricard Pinilla Barnes
 */
public class PasswordComparator {

    /**
     * Compares two password strings to find if they are equal or not.
     *
     * @param password    One of the password strings.
     * @param passConfirm The other one of the password strings.
     * @return True if strings are equal, false if they are not.
     * @author Ricard Pinilla Barnes
     */
    public static boolean arePasswordsEqual(String password, String passConfirm) {
        return password.equals(passConfirm);
    }

}
