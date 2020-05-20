package erdari.renformer_android.data.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;

/**
 * Attribute class corresponding to a Reform type attribute ("option" for customers).
 *
 * @author Ricard Pinilla Barnes
 */
@Entity(tableName = "attribute")
public class Attribute implements Serializable {

    @PrimaryKey
    private long id;

    private String name;

    @Ignore
    private List<Option> options;

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

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    @NotNull
    @Override
    public String toString() {
        return "Attribute{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", options=" + options +
                '}';
    }
}
