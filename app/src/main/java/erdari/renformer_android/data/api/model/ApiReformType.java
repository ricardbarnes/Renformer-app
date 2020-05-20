package erdari.renformer_android.data.api.model;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;

import erdari.renformer_android.data.model.ReformType;

/**
 * Auxiliary extending class intended to adapt the full OOP client architecture to the
 * non-OOP/semi-OOP Api request architecture.
 *
 * @author Ricard Pinilla Barnes
 */
public class ApiReformType implements Serializable {

    /**
     * Converts an adapted reform type into a normal OOP object.
     *
     * @param apiReformType The adapted reform type object to be converted.
     * @return The normal OOP reform type.
     * @author Ricard Pinilla Barnes
     */
    @NotNull
    static ReformType convertToPOJO(@NotNull ApiReformType apiReformType) {
        ReformType reformType = new ReformType();
        reformType.setId(apiReformType.id);
        reformType.setName(apiReformType.name);
        reformType.setCategories(
                ApiCategory.convertListToPOJOList(apiReformType.categories)
        );
        reformType.setAttributes(
                ApiAttribute.convertListToPOJOList(apiReformType.attributes)
        );
        return reformType;
    }

    private long id;
    private String name;
    private float price; // Square meter base price
    private List<ApiAttribute> attributes;
    private List<ApiCategory> categories;

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

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public List<ApiAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<ApiAttribute> attributes) {
        this.attributes = attributes;
    }

    public List<ApiCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<ApiCategory> categories) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        return "ApiReformType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", attributes=" + attributes +
                ", categories=" + categories +
                '}';
    }
}
