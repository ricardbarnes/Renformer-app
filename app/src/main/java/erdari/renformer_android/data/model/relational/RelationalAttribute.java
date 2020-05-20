package erdari.renformer_android.data.model.relational;

import erdari.renformer_android.data.model.Attribute;

/**
 * Auxiliary extending class intended to adapt the full OOP client architecture to the
 * non-OOP/semi-OOP Api request architecture.
 *
 * @author Ricard Pinilla Barnes
 */
public class RelationalAttribute extends Attribute {

    private long reformTypeId; // Parent ReformType id

    /**
     * Class constructor.
     * Rebuilds the whole object to a non-OOP/semi-OOP shape.
     *
     * @param baseCategory The base OOP object.
     * @author Ricard Pinilla Barnes
     */
    public RelationalAttribute(Attribute baseCategory) {
        super.setId(baseCategory.getId());
        super.setName(baseCategory.getName());
    }

    public long getReformTypeId() {
        return reformTypeId;
    }

    public void setReformTypeId(long reformTypeId) {
        this.reformTypeId = reformTypeId;
    }
}
