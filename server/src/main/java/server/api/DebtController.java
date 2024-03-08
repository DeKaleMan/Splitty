package server.api;

import commons.*;
import commons.dto.DebtDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.DebtRepository;
import server.database.EventRepository;
import server.database.ExpenseRepository;
import server.database.ParticipantRepository;

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

    @GetMapping("/{eventId}/expense/{expenseId}")
    public ResponseEntity<List<Debt>> getAllDebtsOfExpense(@PathVariable("eventId") int eventId, @PathVariable("expenseId") int expenseId){
        Optional<Event> eventOptional = eventRepo.findById(eventId);
        if(eventOptional.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        Event event = eventOptional.get();
        Optional<Expense> expenseOptional =
            expenseRepo.findById(new ExpenseId(event, expenseId));
        if (expenseOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Expense expense = expenseOptional.get();
        return ResponseEntity.ok(debtRepo.findByExpense(expense));
    }

    @GetMapping("/{eventId}/participant/{email}")
    public ResponseEntity<List<Debt>> getAllDebtsOfParticipant(@PathVariable("eventId") int eventId, @PathVariable("email") String email){
        Optional<Event> eventOptional = eventRepo.findById(eventId);
        if(eventOptional.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        Event event = eventOptional.get();
        Participant participant = participantRepo.findById(new ParticipantId(email,event));
        if(participant == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(debtRepo.findByParticipant(participant));
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
        Participant participant = participantRepo.findById(
            new ParticipantId(debtDTO.getParticipantEmail(), event));//To change when Participant class is restructured
        if (participant == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(
            debtRepo.save(new Debt(expense, debtDTO.getBalance(), participant)));
    }
}
