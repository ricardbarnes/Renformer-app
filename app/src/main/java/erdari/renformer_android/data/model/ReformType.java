package erdari.renformer_android.data.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.List;

/**
 * Reform type class. The main business logic model.
 *
 * @author Ricard Pinilla Barnes
 */
@Entity(tableName = "reform_type")
public class ReformType implements Serializable {

    @PrimaryKey
    private long id;

    private String name;
    private float price; // Square meter base price

    @Ignore
    private List<Attribute> attributes;

    @Ignore
    private List<Category> categories;

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

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        return "ReformType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", attributes=" + attributes +
                ", categories=" + categories +
                '}';
    }
}
