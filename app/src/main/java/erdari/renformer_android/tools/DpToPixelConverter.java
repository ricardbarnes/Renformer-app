package erdari.renformer_android.tools;

import android.content.Context;

/**
 * Density points to pixel converter class.
 *
 * @author Ricard Pinilla Barnes
 */
public class DpToPixelConverter {

    /**
     * Converts the desired DP into pixels.
     *
     * @param context  The app context where the conversion will be applied.
     * @param sizeInDp The desired DP.
     * @return The pixel equivalence of the passed DPs.
     */
    public static int parseDpToPixels(Context context, int sizeInDp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (sizeInDp * scale + 0.5f);
    }

}
