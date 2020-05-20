package erdari.renformer_android.data.api.model;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import erdari.renformer_android.data.model.Attribute;

/**
 * Auxiliary extending class intended to adapt the full OOP client architecture to the
 * non-OOP/semi-OOP Api request architecture.
 *
 * @author Ricard Pinilla Barnes
 */
public class ApiAttribute {

    /**
     * Converts an adapted attribute into a normal OOP object.
     *
     * @param apiAttribute The adapted attribute object to be converted.
     * @return The normal OOP attribute.
     * @author Ricard Pinilla Barnes
     */
    @NotNull
    private static Attribute convertToPOJO(@NotNull ApiAttribute apiAttribute) {
        Attribute attribute = new Attribute();
        attribute.setId(apiAttribute.id);
        attribute.setName(apiAttribute.name);
        attribute.setOptions(ApiOption.convertListToPOJOList(apiAttribute.options));
        return attribute;
    }

    /**
     * Converts a list of adapted attributes into a list of normal OOP object.
     *
     * @param apiAttributes The list of adapted attribute objects to be converted.
     * @return The list of normal OOP attributes.
     * @author Ricard Pinilla Barnes
     */
    @NotNull
    static List<Attribute> convertListToPOJOList(@NotNull List<ApiAttribute> apiAttributes) {
        List<Attribute> attributes = new ArrayList<>();
        for (ApiAttribute apiAttribute : apiAttributes) {
            attributes.add(convertToPOJO(apiAttribute));
        }
        return attributes;
    }

    private long id;
    private String name;
    private long reformTypeId; // Parent ReformType id
    private List<ApiOption> options;

    /**
     * Parameterized class constructor.
     * Rebuilds the whole object to a non-OOP/semi-OOP shape.
     *
     * @param baseCategory The base OOP object.
     * @author Ricard Pinilla Barnes
     */
    public ApiAttribute(@NotNull Attribute baseCategory) {
        id = baseCategory.getId();
        name = baseCategory.getName();
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

    public long getReformTypeId() {
        return reformTypeId;
    }

    public void setReformTypeId(long reformTypeId) {
        this.reformTypeId = reformTypeId;
    }

    public List<ApiOption> getOptions() {
        return options;
    }

    public void setOptions(List<ApiOption> options) {
        this.options = options;
    }

    @Override
    public String toString() {
        return "ApiAttribute{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", reformTypeId=" + reformTypeId +
                ", options=" + options +
                '}';
    }
}
