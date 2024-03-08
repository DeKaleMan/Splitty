package server.database;

import commons.Event;
import commons.Expense;
import commons.ExpenseId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense, ExpenseId> {
    List<Expense> findByEvent(Event event);
    List<Expense> findByEventAndPayerEmail(Event event, String payerEmail);
}
