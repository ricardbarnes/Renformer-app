package erdari.renformer_android.data.model.relational;

import org.jetbrains.annotations.NotNull;

import erdari.renformer_android.data.model.Category;

/**
 * Auxiliary extending class intended to adapt the full OOP client architecture to the
 * non-OOP/semi-OOP Api request architecture.
 *
 * @author Ricard Pinilla Barnes
 */
public class RelationalCategory extends Category {

    private long reformTypeId; // Parent ReformType id

    /**
     * Parametrized constructor.
     * Rebuilds the whole object to a non-OOP/semi-OOP shape.
     *
     * @param baseCategory The base OOP object.
     * @author Ricard Pinilla Barnes
     */
    public RelationalCategory(@NotNull Category baseCategory) {
        super.setId(baseCategory.getId());
        super.setName(baseCategory.getName());
        super.setFactor(baseCategory.getFactor());
    }

    public long getReformTypeId() {
        return reformTypeId;
    }

    public void setReformTypeId(long reformTypeId) {
        this.reformTypeId = reformTypeId;
    }

}
