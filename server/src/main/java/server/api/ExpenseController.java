package server.api;

import commons.Event;
import commons.Expense;
import commons.ExpenseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.EventRepository;
import server.database.ExpenseRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {
    private final ExpenseRepository expenseRepo;
    private final EventRepository eventRepo;

    public ExpenseController(ExpenseRepository expenseRepo, EventRepository eventRepo) {
        this.expenseRepo = expenseRepo;
        this.eventRepo = eventRepo;
    }

    @GetMapping(path = {"", "/"})
    public ResponseEntity<List<Expense>> findByEventCode(@RequestParam int eventCode) {
        Optional<Event> event = eventRepo.findById(eventCode);
        if (event.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(expenseRepo.findByEvent(event.get()));
    }

    @GetMapping("{payerEmail}")
    public ResponseEntity<List<Expense>> findByEventCodeAndPayerEmail(@RequestParam int eventCode,
                                                                      @PathVariable("payerEmail")
                                                                      String email) {
        Optional<Event> event = eventRepo.findById(eventCode);
        if (event.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(expenseRepo.findByEventAndPayerEmail(event.get(), email));
    }

    @PostMapping(path = {"", "/"})
    public ResponseEntity<Expense> saveExpense(@RequestBody ExpenseDTO expenseDTO) {
        if (expenseDTO == null || isNullOrEmpty(expenseDTO.getPayerEmail()) ||
            expenseDTO.getTotalExpense() < 0.0 || expenseDTO.getDate() == null)
            return ResponseEntity.badRequest().build();
        Optional<Event> event = eventRepo.findById(expenseDTO.getEventId());
        if (event.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Expense expense =
            new Expense(event.get(), expenseDTO.getDescription(), expenseDTO.getType(),
                expenseDTO.getCurrency(), expenseDTO.getDate(), expenseDTO.getTotalExpense(),
                expenseDTO.getPayerEmail());
        return ResponseEntity.ok(expenseRepo.save(expense));
    }

    private boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }
}
