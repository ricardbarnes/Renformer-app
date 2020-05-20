package erdari.renformer_android.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5 encoder.
 * <p>
 * (StackOverflow)
 */
public class Md5Hash {

    /**
     * Encodes a string using MD5 algorithm.
     *
     * @param plainText The string to encode.
     * @return The encoded string or null if could not encode.
     * @author Den Delimarsky
     */
    public static String encode(String plainText) {

        final String MD5 = "MD5";

        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(plainText.getBytes());
            byte[] messageDigest = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                StringBuilder h = new StringBuilder(Integer.toHexString(0xFF & aMessageDigest));
                while (h.length() < 2)
                    h.insert(0, "0");
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }
}
