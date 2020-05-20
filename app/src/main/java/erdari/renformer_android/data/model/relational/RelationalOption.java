package erdari.renformer_android.data.model.relational;

import org.jetbrains.annotations.NotNull;

import erdari.renformer_android.data.model.Option;

/**
 * Auxiliary extending class intended to adapt the full OOP client architecture to the
 * non-OOP/semi-OOP Api request architecture and to preserve the Repository and the Service
 * application patterns.
 *
 * @author Ricard Pinilla Barnes
 */
public class RelationalOption extends Option {

    private long attributeId; // Parent attribute id

    /**
     * Class constructor.
     * Rebuilds the whole object to a non-OOP/semi-OOP shape.
     *
     * @param baseOption The base OOP object.
     * @author Ricard Pinilla Barnes
     */
    public RelationalOption(@NotNull Option baseOption) {
        super.setId(baseOption.getId());
        super.setName(baseOption.getName());
        super.setPrice(baseOption.getPrice());
    }

    public long getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(long attributeId) {
        this.attributeId = attributeId;
    }
}
