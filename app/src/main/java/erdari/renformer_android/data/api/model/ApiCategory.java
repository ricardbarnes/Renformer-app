package erdari.renformer_android.data.api.model;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import erdari.renformer_android.data.model.Category;

/**
 * Auxiliary extending class intended to adapt the full OOP client architecture to the
 * non-OOP/semi-OOP Api request architecture.
 *
 * @author Ricard Pinilla Barnes
 */
public class ApiCategory {

    /**
     * Converts an adapted category into a normal OOP object.
     *
     * @param apiCategory The adapted category object to be converted.
     * @return The normal OOP attribute.
     * @author Ricard Pinilla Barnes
     */
    @NotNull
    private static Category convertToPOJO(@NotNull ApiCategory apiCategory) {
        Category category = new Category();
        category.setId(apiCategory.id);
        category.setName(apiCategory.name);
        category.setFactor(apiCategory.factor);
        return category;
    }

    /**
     * Converts a list of adapted categories into a list of normal OOP object.
     *
     * @param apiCategories The list of adapted categories objects to be converted.
     * @return The list of normal OOP attributes.
     * @author Ricard Pinilla Barnes
     */
    @NotNull
    static List<Category> convertListToPOJOList(@NotNull List<ApiCategory> apiCategories) {
        List<Category> categories = new ArrayList<>();
        for (ApiCategory apiCategory : apiCategories) {
            categories.add(convertToPOJO(apiCategory));
        }
        return categories;
    }

    private long id;
    private String name;
    private float factor;
    private long reformTypeId; // Parent ReformType id

    /**
     * Parametrized constructor.
     * Rebuilds the whole object to a non-OOP/semi-OOP shape.
     *
     * @param baseCategory The base OOP object.
     * @author Ricard Pinilla Barnes
     */
    public ApiCategory(@NotNull Category baseCategory) {
        id = baseCategory.getId();
        name = baseCategory.getName();
        factor = baseCategory.getFactor();
    }

    public long getReformTypeId() {
        return reformTypeId;
    }

    public void setReformTypeId(long reformTypeId) {
        this.reformTypeId = reformTypeId;
    }

    @Override
    public String toString() {
        return "ApiCategory{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", factor=" + factor +
                ", reformTypeId=" + reformTypeId +
                '}';
    }
}
