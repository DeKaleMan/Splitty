package server.database;

import commons.Debt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DebtRepository extends JpaRepository<Debt, Integer> {
    @Query("SELECT d FROM Debt d JOIN Expense e ON e = d.expense JOIN Event ev ON ev = e.event WHERE ev.id = :eventId")
    List<Debt> findByEvent(@Param("eventId") int eventId);
}
