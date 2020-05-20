package erdari.renformer_android.data.api.helper;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import erdari.renformer_android.data.api.service.AttributeService;
import erdari.renformer_android.data.model.relational.RelationalAttribute;
import erdari.renformer_android.data.model.Attribute;
import erdari.renformer_android.network.ApiConfig;
import erdari.renformer_android.network.ApiErrorMessageRetriever;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Helper class that holds the recursive methods needed to achieve a proper pagination for the data.
 *
 * @author Ricard Pinilla Barnes
 */
public class FindAttributesPaginated {

    private static final int OBJECT_LIMIT = 10;

    private final MutableLiveData<List<Attribute>> data;

    private AttributeService attributeService;
    private List<Attribute> accumulatedAttributes;
    private Context appContext;
    private int startPosition;

    /**
     * Helper class constructor.
     *
     * @param context The current application context.
     * @param service The user service for the Retrofit requests.
     * @author Ricard Pinilla Barnes
     */
    public FindAttributesPaginated(Context context, AttributeService service) {
        accumulatedAttributes = new ArrayList<>();
        data = new MutableLiveData<>();
        appContext = context;
        attributeService = service;
    }

    /**
     * Finds all the attributes into a specific reform type.
     *
     * @param reformTypeId The parent reform type id.
     * @return A list with the users with the requested role.
     * @author Ricard Pinilla Barnes
     */
    public MutableLiveData<List<Attribute>> findAttributesByReformTypeId(long reformTypeId) {

        attributeService.findAllAttributesPaginated(startPosition, OBJECT_LIMIT)
                .enqueue(new Callback<List<RelationalAttribute>>() {
                    @Override
                    public void onResponse(
                            @NotNull Call<List<RelationalAttribute>> call,
                            @NotNull Response<List<RelationalAttribute>> response
                    ) {
                        int responseCode = response.code();
                        if (responseCode == 200) {
                            List<RelationalAttribute> attributes = response.body();

                            if (attributes != null) {
                                if (!attributes.isEmpty()) {
                                    startPosition += OBJECT_LIMIT;

                                    /* Non-OOP/semi-OOP request main adaption Beta block BEGIN */
                                    for (RelationalAttribute attribute : attributes) {
                                        if (attribute.getReformTypeId() == reformTypeId) {
                                            accumulatedAttributes.add(attribute);
                                        }
                                    }
                                    /* Non-OOP/semi-OOP request main adaption Beta block END */

                                    findAttributesByReformTypeId(reformTypeId);
                                } else {
                                    finishCall();
                                }
                            }
                        } else if (responseCode == 204) {
                            finishCall();
                        } else {
                            data.setValue(null);

                            String message = ApiErrorMessageRetriever.retrieveErrorMessage(response);
                            Toast.makeText(
                                    appContext,
                                    message,
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }

                    @Override
                    public void onFailure(
                            @NotNull Call<List<RelationalAttribute>> call,
                            @NotNull Throwable t
                    ) {
                        Toast.makeText(
                                appContext,
                                ApiConfig.NO_CONNECTION_MSG,
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });

        return data;
    }

    /**
     * Handles the end post-call issues.
     *
     * @author Ricard Pinilla Barnes
     */
    private void finishCall() {
        sort();
        data.setValue(accumulatedAttributes);
    }

    /**
     * Sorts the retrieved attributes alphabetically.
     *
     * @author Ricard Pinilla Barnes
     */
    private void sort() {
        Collections.sort(accumulatedAttributes, (o1, o2) -> // Sorts alphabetically by name (upwards)
                o1.getName().compareToIgnoreCase(o2.getName()));
    }

}
