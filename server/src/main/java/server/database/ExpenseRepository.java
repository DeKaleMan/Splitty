package server.database;

import commons.Expense;
import commons.ExpenseId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<Expense, ExpenseId> {
}
