package commons.dto;

import java.util.Date;
import java.util.Objects;


public class ExpenseDTO {

    private int eventId;

    private String description;

    private String tagName;
    private String tagColour;

    private Date date; // date of expense

    private double totalExpense; // the amount of money of the expense

    private String payerUuid; // the participant who paid

    private boolean sharedExpense;


    public ExpenseDTO() {

    }

    public ExpenseDTO(int eventId, String description, String tagName, String tagColour, Date date,
                   double totalExpense, String payerUuid, boolean sharedExpense) {
        this.eventId = eventId;
        this.description = description;
        this.tagName = tagName;
        this.tagColour = tagColour;
        this.date = date;
        this.totalExpense = totalExpense;
        this.payerUuid = payerUuid;
        this.sharedExpense = sharedExpense;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getTagColour() {
        return tagColour;
    }

    public void setTagColour(String tagColour) {
        this.tagColour = tagColour;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(double totalExpense) {
        this.totalExpense = totalExpense;
    }

    public String getPayerUuid() {
        return payerUuid;
    }

    public void setPayerUuid(String payer) {
        this.payerUuid = payer;
    }

    public int getEventId() {
        return eventId;
    }

    public void setSharedExpense(boolean sharedExpense) {
        this.sharedExpense = sharedExpense;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public boolean isSharedExpense() {
        return sharedExpense;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExpenseDTO that)) return false;
        return eventId == that.eventId && Double.compare(totalExpense, that.totalExpense) == 0
                && sharedExpense == that.sharedExpense && Objects.equals(description, that.description)
                && Objects.equals(tagName, that.tagName) && Objects.equals(tagColour, that.tagColour)
                && Objects.equals(date, that.date) && Objects.equals(payerUuid, that.payerUuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId, description, tagName, tagColour, date, totalExpense, payerUuid, sharedExpense);
    }

    @Override
    public String toString() {
        return "This is an expense:\n" + description + "\nThe expense type is: " + this.tagName
            + ".\nThe total amount spent is: "
            + totalExpense + "."
            + "\nThe person who paid was: " + payerUuid + ", on " + date
            + ".";
    }

}

