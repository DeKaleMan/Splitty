package commons.dto;

import commons.Tag;

import java.util.Date;

import java.util.Objects;


public class ExpenseDTO {

    private int eventId;

    private String description;

    private Tag tag; // type of expense (i.e. food, drinks, travel)


    private Date date; // date of expense

    private double totalExpense; // the amount of money of the expense

    private String payerUuid; // the participant who paid

    private boolean sharedExpense;


    public ExpenseDTO() {

    }

    public ExpenseDTO(int eventId, String description, Tag tag, Date date,
                   double totalExpense, String payerUuid, boolean sharedExpense) {
        this.eventId = eventId;
        this.description = description;
        this.tag = tag;
        this.date = date;
        this.totalExpense = totalExpense;
        this.payerUuid = payerUuid;
        this.sharedExpense = sharedExpense;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag type) {
        this.tag = tag;
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
        if (o == null || getClass() != o.getClass()) return false;
        ExpenseDTO that = (ExpenseDTO) o;
        return eventId == that.eventId &&
            Double.compare(totalExpense, that.totalExpense) == 0 &&
            sharedExpense == that.sharedExpense &&
            Objects.equals(description, that.description) && tag.equals(that.tag) &&
            Objects.equals(date, that.date) &&
            Objects.equals(payerUuid, that.payerUuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId, description, tag, date, totalExpense, payerUuid,
            sharedExpense);
    }

    @Override
    public String toString() {
        return "This is an expense:\n" + description + "\nThe expense type is: " + this.tag.getName()
            + ".\nThe total amount spent is: "
            + totalExpense + "."
            + "\nThe person who paid was: " + payerUuid + ", on " + date
            + ".";

    }

}

