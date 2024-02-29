package server.api;

import commons.Expense;
import commons.ExpenseId;
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
    public List<Expense> getExpensesOfEvent(@RequestParam int eventCode){
        return repo.findByEventCode(eventCode);
    }

    @PostMapping(path={"","/"})
    public ResponseEntity<Expense> postExpense(@RequestBody Expense expense){
        if(expense == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(repo.save(expense));
    }

}
