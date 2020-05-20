package erdari.renformer_android.data.api.model;

import org.jetbrains.annotations.NotNull;

import erdari.renformer_android.data.model.ReformBudget;
import erdari.renformer_android.data.model.User;
import erdari.renformer_android.tools.DateFormatter;

/**
 * Auxiliary extending class intended to adapt the full OOP client architecture to the
 * non-OOP/semi-OOP Api request architecture.
 *
 * @author Ricard Pinilla Barnes
 */
public class ApiReformBudget {

    /**
     * Converts an adapted reform budget into a normal OOP object.
     *
     * @param apiBudget The adapted reform budget object to be converted.
     * @return The normal OOP attribute.
     * @author Ricard Pinilla Barnes
     */
    @NotNull
    public static ReformBudget convertToPOJO(@NotNull ApiReformBudget apiBudget) {
        User user = new User();
        user.setId(apiBudget.userId);

        ReformBudget budget = new ReformBudget();
        budget.setId(apiBudget.id);
        budget.setAmount(apiBudget.amount);
        budget.setDate(DateFormatter.stringToDate(apiBudget.date));
        budget.setExpireDate(DateFormatter.stringToDate(apiBudget.expireDate));
        budget.setTaxIncluded(apiBudget.taxIncluded);
        budget.setSquareMeters(apiBudget.squareMeters);
        budget.setUser(user);
        budget.setReformType(ApiReformType.convertToPOJO(apiBudget.reformType));
        return budget;
    }

    private long id;
    private long userId;
    private String date;
    private String expireDate;
    private boolean taxIncluded;
    private float amount;
    private float squareMeters;
    private ApiReformType reformType;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public boolean isTaxIncluded() {
        return taxIncluded;
    }

    public void setTaxIncluded(boolean taxIncluded) {
        this.taxIncluded = taxIncluded;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public float getSquareMeters() {
        return squareMeters;
    }

    public void setSquareMeters(float squareMeters) {
        this.squareMeters = squareMeters;
    }

    public ApiReformType getReformType() {
        return reformType;
    }

    public void setReformType(ApiReformType reformType) {
        this.reformType = reformType;
    }

    @Override
    public String toString() {
        return "ApiReformBudget{" +
                "id=" + id +
                ", userId=" + userId +
                ", date='" + date + '\'' +
                ", expireDate='" + expireDate + '\'' +
                ", taxIncluded=" + taxIncluded +
                ", amount=" + amount +
                ", squareMeters=" + squareMeters +
                ", reformType=" + reformType +
                '}';
    }
}
