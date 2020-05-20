package erdari.renformer_android.data.auxiliary;

/**
 * Bundle class used to store context-relevant object ids inside a single object.
 *
 * @author Ricard Pinilla Barnes
 */
public class ReformIdObjectBundle {

    private long reformTypeId;
    private long attributeId;
    private long optionId;

    public long getReformTypeId() {
        return reformTypeId;
    }

    public void setReformTypeId(long reformTypeId) {
        this.reformTypeId = reformTypeId;
    }

    public long getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(long attributeId) {
        this.attributeId = attributeId;
    }

    public long getOptionId() {
        return optionId;
    }

    public void setOptionId(long optionId) {
        this.optionId = optionId;
    }
}
