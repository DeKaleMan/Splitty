package server.api;

import commons.*;
import commons.dto.ExpenseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.EventRepository;
import server.database.ExpenseRepository;
import server.database.ParticipantRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {
    private final ExpenseRepository expenseRepo;
    private final EventRepository eventRepo;

    private final ParticipantRepository participantRepo;

    public ExpenseController(ExpenseRepository expenseRepo, EventRepository eventRepo,
                             ParticipantRepository participantRepo) {
        this.expenseRepo = expenseRepo;
        this.eventRepo = eventRepo;
        this.participantRepo = participantRepo;
    }

    @GetMapping(path = {"", "/"})
    public ResponseEntity<List<Expense>> findByEventCode(@RequestParam int eventCode) {
        Optional<Event> event = eventRepo.findById(eventCode);
        if (event.isEmpty()) {
            return ResponseEntity.notFound().build();
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
        Participant payer = participantRepo.findById(new ParticipantId(email,event.get()));
        if(payer == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(expenseRepo.findByEventAndPayer(event.get(), payer));
    }

    @PostMapping(path = {"", "/"})
    public ResponseEntity<Expense> saveExpense(@RequestBody ExpenseDTO expenseDTO) {
        if (expenseDTO == null || isNullOrEmpty(expenseDTO.getPayerEmail()) ||
                expenseDTO.getTotalExpense() < 0.0 || expenseDTO.getDate() == null)
            return ResponseEntity.badRequest().build();
        Optional<Event> event = eventRepo.findById(expenseDTO.getEventId());
        if (event.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Participant payer = participantRepo.findById(new ParticipantId(expenseDTO.getPayerEmail(),event.get()));
        if(payer == null) return ResponseEntity.notFound().build();
        Expense expense =
                new Expense(event.get(), expenseDTO.getDescription(), expenseDTO.getType(),
                        expenseDTO.getDate(), expenseDTO.getTotalExpense(),
                        payer);
        return ResponseEntity.ok(expenseRepo.save(expense));
    }


    @DeleteMapping("/{eventID}/{expenseID}")
    public ResponseEntity<Expense> deleteExpenseByEventIdAndExpenseId(@PathVariable int eventID,
                                                                      @PathVariable int expenseID) {
        Optional<Event> event = eventRepo.findById(eventID);
        if (event.isEmpty()) return ResponseEntity.notFound().build();
        ExpenseId expenseId = new ExpenseId(event.get(), expenseID);

        Optional<Expense> expense = expenseRepo.findById(expenseId);

        if (expense.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Expense res = expense.get();
        expenseRepo.deleteById(expenseId);

        System.out.println("Deleted" + res);
        return ResponseEntity.ok(res);
    }

    private boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }
}
