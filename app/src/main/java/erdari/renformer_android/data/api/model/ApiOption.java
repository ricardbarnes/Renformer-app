package erdari.renformer_android.data.api.model;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import erdari.renformer_android.data.model.Option;

/**
 * Auxiliary extending class intended to adapt the full OOP client architecture to the
 * non-OOP/semi-OOP Api request architecture and to preserve the Repository and the Service
 * application patterns.
 *
 * @author Ricard Pinilla Barnes
 */
public class ApiOption {

    /**
     * Converts an adapted option into a normal OOP object.
     *
     * @param apiOption The adapted attribute object to be converted.
     * @return The normal OOP option.
     * @author Ricard Pinilla Barnes
     */
    @NotNull
    private static Option convertToPOJO(ApiOption apiOption) {
        Option option = new Option();

        if (apiOption != null) {
            option.setId(apiOption.id);
            option.setName(apiOption.name);
            option.setPrice(apiOption.price);
        } else {
            option.setName("No option"); // Some old options can still be null
        }

        return option;
    }

    /**
     * Converts a list of adapted options into a list of normal OOP object.
     *
     * @param apiOptions The list of adapted attribute objects to be converted.
     * @return The list of normal OOP attributes.
     * @author Ricard Pinilla Barnes
     */
    @NotNull
    static List<Option> convertListToPOJOList(@NotNull List<ApiOption> apiOptions) {
        List<Option> options = new ArrayList<>();
        for (ApiOption apiOption : apiOptions) {
            options.add(convertToPOJO(apiOption));
        }
        return options;
    }

    private long id;
    private String name;
    private boolean isCountable; // Useless but needed
    private long attributeId; // Parent attribute id
    private long precisionId; // Useless but needed
    private float price;

    /**
     * Class constructor.
     * Rebuilds the whole object to a non-OOP/semi-OOP shape.
     *
     * @param baseOption The base OOP object.
     * @author Ricard Pinilla Barnes
     */
    public ApiOption(@NotNull Option baseOption) {
        id = baseOption.getId();
        name = baseOption.getName();
        price = baseOption.getPrice();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCountable() {
        return isCountable;
    }

    public void setCountable(boolean countable) {
        isCountable = countable;
    }

    public long getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(long attributeId) {
        this.attributeId = attributeId;
    }

    public long getPrecisionId() {
        return precisionId;
    }

    public void setPrecisionId(long precisionId) {
        this.precisionId = precisionId;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "ApiOption{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isCountable=" + isCountable +
                ", attributeId=" + attributeId +
                ", precisionId=" + precisionId +
                ", price=" + price +
                '}';
    }
}
