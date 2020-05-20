package erdari.renformer_android.data.api;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import erdari.renformer_android.data.api.helper.FindAttributesPaginated;
import erdari.renformer_android.data.api.service.AttributeService;
import erdari.renformer_android.data.model.relational.RelationalAttribute;
import erdari.renformer_android.data.model.Attribute;
import erdari.renformer_android.network.ApiConfig;
import erdari.renformer_android.network.ApiErrorMessageRetriever;
import erdari.renformer_android.network.RetrofitRequest;
import erdari.renformer_android.security.Session;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Attribute api management.
 *
 * @author Ricard Pinilla Barnes
 */
public class AttributeApi {

    private final AttributeService attributeService;
    private final Context context;

    /**
     * Class constructor.
     *
     * @param context The app context that will handle the outputs.
     * @author Ricard Pinilla Barnes
     */
    public AttributeApi(Context context) {
        Session session = Session.getInstance(context);
        this.context = context;
        this.attributeService = RetrofitRequest.getUnsafeRetrofitInstanceWithToken(
                session.getSessionToken()
        ).create(AttributeService.class);
    }

    /**
     * Finds all the attributes into a specific reform type.
     *
     * @param reformTypeId The parent attribute id.
     * @return A list with the required attributes.
     * @author Ricard Pinilla Barnes
     */
    public MutableLiveData<List<Attribute>> findAllAttributesByReformTypeId(long reformTypeId) {
        FindAttributesPaginated helper = new FindAttributesPaginated(context, attributeService);
        return helper.findAttributesByReformTypeId(reformTypeId);
    }

    /**
     * Creates a new attribute inside the corresponding reform type.
     *
     * @param attribute The attribute to be created.
     * @return True if the creation has been successful, false if not.
     * @author Ricard Pinilla Barnes
     */
    public MutableLiveData<Attribute> createAttribute(RelationalAttribute attribute) {
        final MutableLiveData<Attribute> data = new MutableLiveData<>();

        attributeService.createAttribute(attribute)
                .enqueue(new Callback<RelationalAttribute>() {
                    @Override
                    public void onResponse(
                            @NotNull Call<RelationalAttribute> call,
                            @NotNull Response<RelationalAttribute> response
                    ) {
                        int responseCode = response.code();

                        if (responseCode == 201) {
                            data.setValue(response.body());
                        } else {
                            data.setValue(null);

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
                            @NotNull Call<RelationalAttribute> call,
                            @NotNull Throwable t
                    ) {
                        ApiConfig.showStandardFailureToast(context);
                    }
                });

        return data;
    }

    /**
     * Finds an attribute by its id.
     *
     * @param id The attribute id.
     * @return The requested attribute, if found.
     * @author Ricard Pinilla Barnes
     */
    public MutableLiveData<Attribute> findAttributeById(long id) {
        final MutableLiveData<Attribute> data = new MutableLiveData<>();

        attributeService.findAttributeById(id)
                .enqueue(new Callback<RelationalAttribute>() {
                    @Override
                    public void onResponse(
                            @NotNull Call<RelationalAttribute> call,
                            @NotNull Response<RelationalAttribute> response
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
                            @NotNull Call<RelationalAttribute> call,
                            @NotNull Throwable t
                    ) {
                        ApiConfig.showStandardFailureToast(context);
                    }
                });

        return data;
    }

    /**
     * Updates an attribute.
     *
     * @param attribute The attribute to be patched.
     * @return True if the patching is done, false if not.
     * @author Ricard Pinilla Barnes
     */
    public MutableLiveData<Boolean> patchAttribute(RelationalAttribute attribute) {
        final MutableLiveData<Boolean> data = new MutableLiveData<>();

        attributeService.patchAttributeById(attribute.getId(), attribute)
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

    /**
     * Deletes an attribute.
     *
     * @param attribute The attribute to be deleted.
     * @return True if deleted, false if not.
     * @author Ricard Pinilla Barnes
     */
    public MutableLiveData<Boolean> deleteAttribute(@NotNull Attribute attribute) {
        final MutableLiveData<Boolean> data = new MutableLiveData<>();

        attributeService.deleteUserById(attribute.getId())
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
