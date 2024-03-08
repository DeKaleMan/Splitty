package server.api;

import commons.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.DebtRepository;
import server.database.EventRepository;
import server.database.ExpenseRepository;
import server.database.ParticipantRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/debts")
public class DebtController {

    private final DebtRepository debtRepo;
    private final ExpenseRepository expenseRepo;
    private final EventRepository eventRepo;
    private final ParticipantRepository participantRepo;

    @Autowired
    public DebtController(DebtRepository debtRepo, ExpenseRepository expenseRepo,
                          ParticipantRepository participantRepo,
                          EventRepository eventRepo) {
        this.debtRepo = debtRepo;
        this.expenseRepo = expenseRepo;
        this.participantRepo = participantRepo;
        this.eventRepo = eventRepo;
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<List<Debt>> getAllDebtsOfEvent(@PathVariable("eventId") int eventId) {
        Optional<Event> eventOptional = eventRepo.findById(eventId);
        if(eventOptional.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        Event event = eventOptional.get();
        return ResponseEntity.ok(debtRepo.findByEvent(event));
    }

    @GetMapping({"", "/"})
    public List<Debt> getALlDebts() {
        return debtRepo.findAll();
    }

    @PostMapping(path = {"", "/"})
    public ResponseEntity<Debt> saveDebt(@RequestBody DebtDTO debtDTO) {
        Optional<Event> eventOptional = eventRepo.findById(debtDTO.getEventId());
        if (eventOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Event event = eventOptional.get();
        Optional<Expense> expenseOptional =
            expenseRepo.findById(new ExpenseId(event, debtDTO.getExpenseId()));
        if (expenseOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Expense expense = expenseOptional.get();
        Optional<Participant> participantOptional = participantRepo.findById(
            Long.parseLong("1"));//To change when Participant class is restructured
        if (participantOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Participant participant = participantOptional.get();
        return ResponseEntity.ok(
            debtRepo.save(new Debt(expense, debtDTO.getBalance(), participant)));
    }
}
