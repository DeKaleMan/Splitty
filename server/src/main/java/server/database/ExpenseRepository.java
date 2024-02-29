package server.database;

import commons.Expense;
import commons.ExpenseId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, ExpenseId> {
    List<Expense> findByEventCode(int eventCode);
}
