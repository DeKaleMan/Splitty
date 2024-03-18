package commons;

import java.io.Serializable;
import java.util.Objects;

public class ExpenseId implements Serializable {
    private Event event;
    private int expenseId;

    @SuppressWarnings("unused")
    public ExpenseId() {
    }

    public ExpenseId(Event event, int expenseId) {
        this.event = event;
        this.expenseId = expenseId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExpenseId expenseId1 = (ExpenseId) o;
        return expenseId == expenseId1.expenseId && Objects.equals(event, expenseId1.event);
    }

    @Override
    public int hashCode() {
        return Objects.hash(event, expenseId);
    }
}
