package server.database;

import commons.Event;
import commons.Expense;
import commons.ExpenseId;
import commons.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, ExpenseId> {
    List<Expense> findByEvent(Event event);
    List<Expense> findByEventAndPayer(Event event, Participant payer);

    List<Expense> deleteExpenseByEventIdAndExpenseId(int eventID, int expenseID);
}
