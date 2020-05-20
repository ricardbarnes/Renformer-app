package erdari.renformer_android.tools;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import erdari.renformer_android.data.model.Attribute;
import erdari.renformer_android.data.model.Option;
import erdari.renformer_android.data.model.relational.RelationalOption;
import erdari.renformer_android.data.repository.OptionRepository;

/**
 * At least one option is needed to do the proper calculus of a reform budget.
 * So, this class handles the first option creator method, that ensures that there will be at least
 * one option into every attribute.
 *
 * @author Ricard Pinilla Barnes
 */
public class FirstOptionCreator {

    private static final String DEF_NAME = "Unique option";
    private static final float DEF_PRICE = 1.0f;

    /**
     * Creates the first option for every new attribute.
     *
     * @param context   The app context.
     * @param attribute A new attribute.
     * @return True if the option has been created, false if not.
     */
    @NotNull
    @Contract(pure = true)
    public static LiveData<Boolean> createFirstOption(Context context, @NotNull Attribute attribute) {
        final MutableLiveData<Boolean> data = new MutableLiveData<>();

        Option option = new Option();
        option.setName(DEF_NAME);
        option.setPrice(DEF_PRICE);

        RelationalOption relationalOption = new RelationalOption(option);
        relationalOption.setAttributeId(attribute.getId());

        OptionRepository optionRepo = new OptionRepository(context);
        optionRepo.createOption(relationalOption).observe((LifecycleOwner) context, isCreated -> {
            if (isCreated) {
                data.setValue(true);
            } else {
                data.setValue(false);
            }
        });

        return data;
    }

}
