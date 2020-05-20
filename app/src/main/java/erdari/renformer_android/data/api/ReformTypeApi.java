package erdari.renformer_android.data.api;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

import erdari.renformer_android.data.api.service.ReformTypeService;
import erdari.renformer_android.data.model.ReformType;
import erdari.renformer_android.network.ApiConfig;
import erdari.renformer_android.network.ApiErrorMessageRetriever;
import erdari.renformer_android.network.RetrofitRequest;
import erdari.renformer_android.security.Session;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Reform types api management.
 *
 * @author Ricard Pinilla Barnes
 */
public class ReformTypeApi {

    private final ReformTypeService reformTypeService;
    private final Context context;

    /**
     * Class constructor.
     *
     * @param context The app context that will handle the outputs.
     * @author Ricard Pinilla Barnes
     */
    public ReformTypeApi(Context context) {
        Session session = Session.getInstance(context);
        this.context = context;
        this.reformTypeService = RetrofitRequest.getUnsafeRetrofitInstanceWithToken(
                session.getSessionToken()
        ).create(ReformTypeService.class);
    }

    /**
     * Finds all the reform types.
     *
     * @return All the reform types, if any.
     * @author Ricard Pinilla Barnes
     */
    public MutableLiveData<List<ReformType>> findAllReformTypes() {
        final MutableLiveData<List<ReformType>> data = new MutableLiveData<>();

        reformTypeService.findAllReformTypes().enqueue(new Callback<List<ReformType>>() {
            @Override
            public void onResponse(
                    @NotNull Call<List<ReformType>> call,
                    @NotNull Response<List<ReformType>> response
            ) {
                int responseCode = response.code();
                if (responseCode == 200) {
                    List<ReformType> reformTypes = response.body();

                    if (reformTypes != null) {
                        Collections.sort(reformTypes, (o1, o2) -> // Sort alphabetically by name (upwards)
                                o1.getName().compareToIgnoreCase(o2.getName()));
                        data.setValue(reformTypes);
                    }

                } else {
                    String message = ApiErrorMessageRetriever.retrieveErrorMessage(response);
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(
                    @NotNull Call<List<ReformType>> call,
                    @NotNull Throwable t
            ) {
                Toast.makeText(
                        context,
                        ApiConfig.NO_CONNECTION_MSG,
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

        return data;
    }

    /**
     * Creates a new reform type.
     *
     * @param reformType The reform type to be created.
     * @return True if creation has been successful, false if not.
     */
    public MutableLiveData<Boolean> createReformType(ReformType reformType) {
        final MutableLiveData<Boolean> data = new MutableLiveData<>();

        reformTypeService.createReformType(reformType).enqueue(new Callback<Void>() {
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
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(
                    @NotNull Call<Void> call,
                    @NotNull Throwable t
            ) {
                Toast.makeText(
                        context,
                        ApiConfig.NO_CONNECTION_MSG,
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

        return data;
    }

    /**
     * Finds a reform type by its id.
     *
     * @param id The reform type id.
     * @return The requested reform type, if found.
     * @author Ricard Pinlla Barnes
     */
    public MutableLiveData<ReformType> findReformTypeById(long id) {
        final MutableLiveData<ReformType> data = new MutableLiveData<>();

        reformTypeService.findReformTypeById(id).enqueue(new Callback<ReformType>() {
            @Override
            public void onResponse(
                    @NotNull Call<ReformType> call,
                    @NotNull Response<ReformType> response
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
                    @NotNull Call<ReformType> call,
                    @NotNull Throwable t
            ) {
                Toast.makeText(
                        context,
                        ApiConfig.NO_CONNECTION_MSG,
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

        return data;
    }

    /**
     * Updates reformType.
     *
     * @param reformType The reformType to be patched.
     * @return True if the patching is done, false if not.
     * @author Ricard Pinilla Barnes
     */
    public MutableLiveData<Boolean> patchReformType(ReformType reformType) {
        final MutableLiveData<Boolean> data = new MutableLiveData<>();

        reformTypeService.patchReformTypeById(reformType.getId(), reformType)
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

                            data.setValue(false);
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
     * Deletes a reformType.
     *
     * @param reformType The reformType to be deleted.
     * @return True if deleted, false if not.
     * @author Ricard Pinilla Barnes
     */
    public MutableLiveData<Boolean> deleteReformType(@NotNull ReformType reformType) {
        final MutableLiveData<Boolean> data = new MutableLiveData<>();

        reformTypeService.deleteReformTypeById(reformType.getId())
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
