package server.database;

import commons.Debt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Debt, Integer> {
}
