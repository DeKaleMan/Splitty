package server.api;

import commons.Expense;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.ExpenseRepository;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {
    private final ExpenseRepository repo;

    public ExpenseController(ExpenseRepository repo) {
        this.repo = repo;
    }

    @GetMapping(path={"","/"})
    public List<Expense> findByEventCode(@RequestParam int eventCode) {
        return repo.findByEventCode(eventCode);
    }

    @GetMapping("{payerEmail}")
    public List<Expense> findByEventCodeAndPayerEmail(@RequestParam int eventCode,
                                                      @PathVariable("payerEmail") String email) {
        return repo.findByEventCodeAndPayerEmail(eventCode, email);
    }

    @PostMapping(path = {"", "/"})
    public ResponseEntity<Expense> saveExpense(@RequestBody Expense expense) {
        if (expense == null || isNullOrEmpty(expense.getDate()) || isNullOrEmpty(
            expense.getPayerEmail()) || expense.getTotalExpense() < 0.0)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(repo.save(expense));
    }

    private boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }
}
