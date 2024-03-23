package server.api;

import commons.*;
import commons.dto.ExpenseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
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

    @MessageMapping("/addExpense") // -> /app/addExpense
    @SendTo("/topic/addExpense") // when we are done processing it we send it to the path provided
    public ResponseEntity<Expense> expenseByEventWS(ExpenseDTO expenseDTO){
        ResponseEntity<Expense> expPerEvent = saveExpense(expenseDTO);
        return expPerEvent;
    }

    @GetMapping(path = {"", "/"})
    public ResponseEntity<List<Expense>> findByEventCode(@RequestParam int eventCode) {
        Optional<Event> event = eventRepo.findById(eventCode);
        if (event.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(expenseRepo.findByEvent(event.get()));
    }

    @GetMapping("{payerUuid}")
    public ResponseEntity<List<Expense>> findByEventCodeAndPayerUuid(@RequestParam int eventCode,
                                                                      @PathVariable("payerUuid")
                                                                      String uuid) {
        Optional<Event> event = eventRepo.findById(eventCode);
        if (event.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Participant payer = participantRepo.findById(new ParticipantId(uuid,event.get()));
        if(payer == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(expenseRepo.findByEventAndPayer(event.get(), payer));
    }

    @PostMapping(path = {"", "/"})
    public ResponseEntity<Expense> saveExpense(@RequestBody ExpenseDTO expenseDTO) {
        if (expenseDTO == null || isNullOrEmpty(expenseDTO.getPayerUuid()) ||
                expenseDTO.getTotalExpense() < 0.0 || expenseDTO.getDate() == null)
            return ResponseEntity.badRequest().build();
        Optional<Event> event = eventRepo.findById(expenseDTO.getEventId());
        if (event.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Participant payer = participantRepo.findById(new ParticipantId(expenseDTO.getPayerUuid(), event.get()));
        if(payer == null) return ResponseEntity.notFound().build();
        Expense expense =
                new Expense(event.get(), expenseDTO.getDescription(), expenseDTO.getType(),
                        expenseDTO.getDate(), expenseDTO.getTotalExpense(),
                        payer);
        return ResponseEntity.ok(expenseRepo.save(expense));
    }


    @DeleteMapping(path = {"", "/"})
    public ResponseEntity<Expense> deleteExpenseByEventIdAndExpenseId(@RequestParam int eventID,
                                                                      @RequestParam int expenseID) {
        Optional<Event> event = eventRepo.findById(eventID);
        if (event.isEmpty()) return ResponseEntity.notFound().build();
        ExpenseId expenseId = new ExpenseId(event.get(), expenseID);

        Optional<Expense> expense = expenseRepo.findById(expenseId);

        if (expense.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Expense res = expense.get();
        expenseRepo.deleteById(expenseId);

        System.out.println("Deleted\n" + res);
        return ResponseEntity.ok(res);
    }

    private boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }
}
