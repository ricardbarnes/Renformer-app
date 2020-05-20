package erdari.renformer_android.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

/**
 * Option class corresponding to a Reform type attribute ("inner option" for customers).
 *
 * @author Ricard Pinilla Barnes
 */
@Entity(tableName = "option")
public class Option implements Serializable {

    @PrimaryKey
    private long id;

    private String name;
    private float price;

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

    @Override
    public String toString() {
        return "Option{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
