package erdari.renformer_android.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Customer info retrieved from the UI form to be used for the budget.
 *
 * @author Ricard Pinilla Barnes
 */
@Entity(tableName = "customer_reform")
public class ReformBudget implements Serializable {

    @PrimaryKey
    private long id;

    private float amount;

    private Date date;

    @ColumnInfo(name = "expire_date")
    private Date expireDate;

    @ColumnInfo(name = "tax_included")
    private boolean taxIncluded;

    @ColumnInfo(name = "square_meters")
    private float squareMeters;

    @Ignore
    private User user;

    @Ignore
    private ReformType reformType;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public boolean isTaxIncluded() {
        return taxIncluded;
    }

    public void setTaxIncluded(boolean taxIncluded) {
        this.taxIncluded = taxIncluded;
    }

    public float getSquareMeters() {
        return squareMeters;
    }

    public void setSquareMeters(float squareMeters) {
        this.squareMeters = squareMeters;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ReformType getReformType() {
        return reformType;
    }

    public void setReformType(ReformType reformType) {
        this.reformType = reformType;
    }

    @Override
    public String toString() {
        return "ReformBudget{" +
                "id=" + id +
                ", amount=" + amount +
                ", date=" + date +
                ", expireDate=" + expireDate +
                ", taxIncluded=" + taxIncluded +
                ", squareMeters=" + squareMeters +
                ", user=" + user +
                ", reformType=" + reformType +
                '}';
    }

    /**
     * Builder pattern used to build the reform budget in the most simple way as possible.
     *
     * @author Ricard Pinilla Barnes
     */
    public static class Builder {

        private ReformType mReformType;
        private Map<Long, Category> mCategories;
        private List<Attribute> mAttributesWithOptions; // Map<attributeId, option>
        private float mSquareMeters;

        /**
         * Non parametrized builder constructor.
         * <p>
         * Use setter to set the needed reform type id.
         *
         * @author Ricard Pinilla Barnes
         */
        public Builder() {
            mReformType = new ReformType();
            mCategories = new HashMap<>();
            mAttributesWithOptions = new ArrayList<>();
        }

        /**
         * Parametrized builder constructor.
         *
         * @param reformType The budgeted reform type.
         * @author Ricard Pinilla Barnes
         */
        public Builder(@NotNull ReformType reformType) {
            mReformType = new ReformType();
            mReformType.setId(reformType.getId());
            mReformType.setName(reformType.getName());
            mReformType.setPrice(reformType.getPrice());
            mCategories = new HashMap<>();
            mAttributesWithOptions = new ArrayList<>();
        }

        /**
         * Builds the reform budget from the added data.
         *
         * @return a reform budget corresponding to the budgeted reform type.
         * @author Ricard Pinilla Barnes
         */
        public ReformBudget build() {
            List<Category> categories = new ArrayList<>(mCategories.values());
            mReformType.setCategories(categories);
            mReformType.setAttributes(mAttributesWithOptions);

            ReformBudget reformBudget = new ReformBudget();
            reformBudget.setReformType(mReformType);
            reformBudget.setSquareMeters(mSquareMeters);
            return reformBudget;
        }

        /**
         * Adds the passed category to the reform budget.
         * <p>
         * NOTE: It could be just a setter since there can be only one category but it is left like
         * this for scalability purposes.
         *
         * @param category The category to be added.
         * @author Ricard Pinilla Barnes
         */
        public void addCategory(Category category) {
            mCategories.put(category.getId(), category);
        }

        /**
         * Removes the passed category from the reform budget.
         *
         * @param category The category to be added.
         * @author Ricard Pinilla Barnes
         */
        public void removeCategory(@NotNull Category category) {
            mCategories.remove(category.getId());
        }

        /**
         * Adds the passed option to the proper attribute of the reform budget.
         *
         * @param attribute The attribute to be modified.
         * @param option    The option to be added.
         * @author Ricard Pinilla Barnes
         */
        public void addAttributeAndOption(@NotNull Attribute attribute, @NotNull Option option) {
            attribute.setOptions(new ArrayList<>());
            attribute.getOptions().add(option);
            mAttributesWithOptions.add(attribute);
        }

        /**
         * Removes the passed option from the proper attribute of the reform budget.
         *
         * @param attribute The attribute to be modified.
         * @author Ricard Pinilla Barnes
         */
        public void removeAttributeAndOption(@NotNull Attribute attribute) {
            mAttributesWithOptions.remove(attribute);
        }

        public ReformType getReformType() {
            return mReformType;
        }

        public void setReformType(ReformType mReformType) {
            this.mReformType = mReformType;
        }

        public float getSquareMeters() {
            return mSquareMeters;
        }

        public void setSquareMeters(float mSquareMeters) {
            this.mSquareMeters = mSquareMeters;
        }
    }

}
