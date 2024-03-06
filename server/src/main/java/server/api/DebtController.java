package server.api;

import commons.Debt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import server.database.DebtRepository;
import server.database.ExpenseRepository;
import server.database.ParticipantRepository;

import java.util.List;

@RestController
@RequestMapping("/api/debts")
public class DebtController {

    private final DebtRepository debtRepo;
    private final ExpenseRepository expenseRepo;
    private final ParticipantRepository participantRepo;

    @Autowired
    public DebtController(DebtRepository debtRepo, ExpenseRepository expenseRepo,
                          ParticipantRepository participantRepo) {
        this.debtRepo = debtRepo;
        this.expenseRepo = expenseRepo;
        this.participantRepo = participantRepo;
    }

    @GetMapping("/{eventId}")
    public List<Debt> getAllDebtsOfEvent(@PathVariable("eventId") int eventId) {
        return debtRepo.findByEvent(eventId);
    }

    @GetMapping({"", "/"})
    public List<Debt> getAllTransaction() {
        return debtRepo.findAll();
    }


}
