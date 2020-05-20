package erdari.renformer_android.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * Category class corresponding to a Reform type category.
 *
 * @author Ricard Pinilla Barnes
 */
@Entity(tableName = "category")
public class Category implements Serializable {

    @PrimaryKey
    private long id;

    private String name;
    private float factor;

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

    public float getFactor() {
        return factor;
    }

    public void setFactor(float factor) {
        this.factor = factor;
    }

    @NotNull
    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", factor=" + factor +
                '}';
    }
}
