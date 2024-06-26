package server.database;

import commons.Event;
import commons.Expense;
import commons.ExpenseId;
import commons.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, ExpenseId> {
    List<Expense> findByEvent(Event event);
    List<Expense> findByEventAndPayer(Event event, Participant payer);

    @Query("SELECT SUM(e.totalExpense) FROM Expense e WHERE e.event.id = ?1 AND e.tag.tagId.event.id = ?1 " +
            "AND e.tag.tagId.name = ?2 GROUP BY e.tag")
    Double findTotalAmountByTag(int eventId, String tagName);


    List<Expense> deleteExpenseByEventIdAndExpenseId(int eventID, int expenseID);

    @Query("SELECT COALESCE(SUM(e.totalExpense), 0.0) FROM Expense e WHERE e.event.id = ?1")
    double getTotalCostByEvent(int eventID);
}
