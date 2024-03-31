package commons.dto;

import commons.Type;

import java.util.Date;

import java.util.Objects;


public class ExpenseDTO {

    private int eventId;

    private String description;

    private Type type; // type of expense (i.e. food, drinks, travel)


    private Date date; // date of expense

    private double totalExpense; // the amount of money of the expense

    private String payerUuid; // the participant who paid

    private boolean sharedExpense;


    public ExpenseDTO() {

    }

    public ExpenseDTO(int eventId, String description, Type type, Date date,
                   double totalExpense, String payerUuid, boolean sharedExpense) {
        this.eventId = eventId;
        this.description = description;
        this.type = type;
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


    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
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
            Objects.equals(description, that.description) && type == that.type &&
            Objects.equals(date, that.date) &&
            Objects.equals(payerUuid, that.payerUuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId, description, type, date, totalExpense, payerUuid,
            sharedExpense);
    }

    @Override
    public String toString() {
        return "This is an expense:\n" + description + "\nThe expense type is: " + this.type
            + ".\nThe total amount spent is: "
            + totalExpense + "."
            + "\nThe person who paid was: " + payerUuid + ", on " + date
            + ".";

    }

}

