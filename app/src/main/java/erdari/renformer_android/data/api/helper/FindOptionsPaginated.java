package erdari.renformer_android.data.api.helper;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import erdari.renformer_android.data.model.relational.RelationalOption;
import erdari.renformer_android.data.api.service.OptionService;
import erdari.renformer_android.data.model.Option;
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
public class FindOptionsPaginated {

    private static final int OBJECT_LIMIT = 10;

    private final MutableLiveData<List<Option>> data;

    private OptionService optionService;
    private List<Option> accumulatedOptions;
    private Context appContext;
    private int startPosition;

    /**
     * Helper class constructor.
     *
     * @param context The current application context.
     * @param service The user service for the Retrofit requests.
     * @author Ricard Pinilla Barnes
     */
    public FindOptionsPaginated(Context context, OptionService service) {
        accumulatedOptions = new ArrayList<>();
        data = new MutableLiveData<>();
        appContext = context;
        optionService = service;
    }

    /**
     * Finds all the options into a specific attribute.
     *
     * @param attributeId The requested attribute id.
     * @return A list with the users with the requested role.
     * @author Ricard Pinilla Barnes
     */
    public MutableLiveData<List<Option>> findOptionsByAttributeIdPaginated(long attributeId) {

        optionService.findAllOptionsPaginated(startPosition, OBJECT_LIMIT)
                .enqueue(new Callback<List<RelationalOption>>() {
                    @Override
                    public void onResponse(
                            @NotNull Call<List<RelationalOption>> call,
                            @NotNull Response<List<RelationalOption>> response
                    ) {
                        int responseCode = response.code();
                        if (responseCode == 200) {
                            List<RelationalOption> options = response.body();

                            if (options != null) {
                                if (!options.isEmpty()) {
                                    startPosition += OBJECT_LIMIT;

                                    /* Non-OOP/semi-OOP request main adaption Beta block BEGIN */
                                    for (RelationalOption option : options) {
                                        if (option.getAttributeId() == attributeId) {
                                            accumulatedOptions.add(option);
                                        }
                                    }
                                    /* Non-OOP/semi-OOP request main adaption Beta block END */

                                    findOptionsByAttributeIdPaginated(attributeId);
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
                            @NotNull Call<List<RelationalOption>> call,
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
        data.setValue(accumulatedOptions);
    }

    /**
     * Sorts the retrieved options alphabetically upwards.
     *
     * @author Ricard Pinilla Barnes
     */
    private void sort() {
        Collections.sort(accumulatedOptions, (o1, o2) ->
                o1.getName().compareToIgnoreCase(o2.getName()));
    }

}
