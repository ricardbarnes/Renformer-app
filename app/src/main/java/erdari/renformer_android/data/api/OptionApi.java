package erdari.renformer_android.data.api;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import erdari.renformer_android.data.api.helper.FindOptionsPaginated;
import erdari.renformer_android.data.model.relational.RelationalOption;
import erdari.renformer_android.data.api.service.OptionService;
import erdari.renformer_android.data.model.Option;
import erdari.renformer_android.network.ApiConfig;
import erdari.renformer_android.network.ApiErrorMessageRetriever;
import erdari.renformer_android.network.RetrofitRequest;
import erdari.renformer_android.security.Session;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Option api management.
 *
 * @author Ricard Pinilla Barnes
 */
public class OptionApi {

    private final OptionService optionService;
    private final Context context;

    /**
     * Class constructor.
     *
     * @param context The app context that will handle the outputs.
     * @author Ricard Pinilla Barnes
     */
    public OptionApi(Context context) {
        Session session = Session.getInstance(context);
        this.context = context;
        this.optionService = RetrofitRequest.getUnsafeRetrofitInstanceWithToken(
                session.getSessionToken()
        ).create(OptionService.class);
    }

    /**
     * Finds all the options into a specific attribute.
     *
     * @param attributeId The parent attribute id.
     * @return A list with the required options.
     * @author Ricard Pinilla Barnes
     */
    public MutableLiveData<List<Option>> findAllOptionsByAttributeId(long attributeId) {
        FindOptionsPaginated helper = new FindOptionsPaginated(context, optionService);
        return helper.findOptionsByAttributeIdPaginated(attributeId);
    }

    /**
     * Creates a new option inside the corresponding attribute.
     *
     * @param option The option to be created.
     * @return True if the creation has been successful, false if no.
     * @author Ricard Pinilla Barnes
     */
    public MutableLiveData<Boolean> createOption(RelationalOption option) {
        final MutableLiveData<Boolean> data = new MutableLiveData<>();

        optionService.createOption(option)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(
                            @NotNull Call<Void> call,
                            @NotNull Response<Void> response
                    ) {
                        int responseCode = response.code();

                        if (responseCode == 201) {
                            data.setValue(true);
                        } else {
                            data.setValue(false);

                            String message = ApiErrorMessageRetriever.retrieveErrorMessage(response);
                            Toast.makeText(
                                    context,
                                    message,
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }

                    @Override
                    public void onFailure(
                            @NotNull Call<Void> call,
                            @NotNull Throwable t
                    ) {
                        ApiConfig.showStandardFailureToast(context);
                    }
                });

        return data;
    }

    /**
     * Finds an option by its id.
     *
     * @param id The Option id.
     * @return The requested option, if found.
     * @author Ricard Pinilla Barnes
     */
    public MutableLiveData<Option> findOptionById(long id) {
        final MutableLiveData<Option> data = new MutableLiveData<>();

        optionService.findOptionById(id)
                .enqueue(new Callback<RelationalOption>() {
                    @Override
                    public void onResponse(
                            @NotNull Call<RelationalOption> call,
                            @NotNull Response<RelationalOption> response
                    ) {
                        int responseCode = response.code();

                        if (responseCode == 200) {
                            data.setValue(response.body());
                        } else {
                            data.setValue(null);

                            String message = ApiErrorMessageRetriever.retrieveErrorMessage(response);
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(
                            @NotNull Call<RelationalOption> call,
                            @NotNull Throwable t
                    ) {
                        ApiConfig.showStandardFailureToast(context);
                    }
                });

        return data;
    }

    /**
     * Updates an option.
     *
     * @param option The option to be patched.
     * @return True if the patching is done, false if not.
     * @author Ricard Pinilla Barnes
     */
    public MutableLiveData<Boolean> patchOption(RelationalOption option) {
        final MutableLiveData<Boolean> data = new MutableLiveData<>();

        optionService.patchOptionById(option.getId(), option)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(
                            @NotNull Call<Void> call,
                            @NotNull Response<Void> response
                    ) {

                        if (response.isSuccessful()) {
                            data.setValue(true);
                        } else {
                            data.setValue(false);

                            String message = ApiErrorMessageRetriever.retrieveErrorMessage(response);
                            Toast.makeText(
                                    context,
                                    message,
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }

                    @Override
                    public void onFailure(
                            @NotNull Call<Void> call,
                            @NotNull Throwable t
                    ) {
                        ApiConfig.showStandardFailureToast(context);
                    }
                });

        return data;
    }

    /**
     * Deletes an option.
     *
     * @param option The option to be deleted.
     * @return True if deleted, false if not.
     * @author Ricard Pinilla Barnes
     */
    public MutableLiveData<Boolean> deleteOption(@NotNull Option option) {
        final MutableLiveData<Boolean> data = new MutableLiveData<>();

        optionService.deleteOptionById(option.getId())
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(
                            @NotNull Call<Void> call,
                            @NotNull Response<Void> response
                    ) {
                        int responseCode = response.code();
                        if (responseCode == 200) {
                            data.setValue(true);
                        } else {
                            data.setValue(false);

                            String message = ApiErrorMessageRetriever.retrieveErrorMessage(response);
                            Toast.makeText(
                                    context,
                                    message,
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }

                    @Override
                    public void onFailure(
                            @NotNull Call<Void> call,
                            @NotNull Throwable t
                    ) {
                        ApiConfig.showStandardFailureToast(context);
                    }
                });

        return data;
    }
}
