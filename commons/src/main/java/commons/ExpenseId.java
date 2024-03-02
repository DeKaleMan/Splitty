package commons;

import java.io.Serializable;
import java.util.Objects;

public class ExpenseId implements Serializable {
    private int eventCode;
    private int expenseId;

    @SuppressWarnings("unused")
    public ExpenseId() {
    }

    public ExpenseId(int eventCode, int expenseId) {
        this.eventCode = eventCode;
        this.expenseId = expenseId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExpenseId expenseId1 = (ExpenseId) o;
        return eventCode == expenseId1.eventCode && expenseId == expenseId1.expenseId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventCode, expenseId);
    }
}
