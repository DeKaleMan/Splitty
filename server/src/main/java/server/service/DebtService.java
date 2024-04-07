package server.service;

import commons.*;
import commons.dto.DebtDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.database.DebtRepository;
import server.database.EventRepository;
import server.database.ExpenseRepository;
import server.database.ParticipantRepository;

import java.util.List;
import java.util.Optional;

@Service
public class DebtService {
    private final DebtRepository debtRepo;
    private final ExpenseRepository expenseRepo;
    private final EventRepository eventRepo;
    private final ParticipantRepository participantRepo;

    @Autowired
    public DebtService(DebtRepository debtRepo, ExpenseRepository expenseRepo,
                          ParticipantRepository participantRepo,
                          EventRepository eventRepo) {
        this.debtRepo = debtRepo;
        this.expenseRepo = expenseRepo;
        this.participantRepo = participantRepo;
        this.eventRepo = eventRepo;
    }

    public List<Debt> getAllDebtsOfEvent(int eventId) {
        Optional<Event> eventOptional = eventRepo.findById(eventId);
        if(eventOptional.isEmpty()){
            return null;
        }
        Event event = eventOptional.get();
        return debtRepo.findByEvent(event);
    }

    public List<Debt> getAllDebtsOfExpense(int eventId, int expenseId){
        Optional<Event> eventOptional = eventRepo.findById(eventId);
        if(eventOptional.isEmpty()){
            return null;
        }
        Event event = eventOptional.get();
        Optional<Expense> expenseOptional =
            expenseRepo.findById(new ExpenseId(event, expenseId));
        if (expenseOptional.isEmpty()) {
            return null;
        }
        Expense expense = expenseOptional.get();
        return debtRepo.findByExpense(expense);
    }

    public List<Debt> getAllDebtsOfParticipant(int eventId, String uuid){
        Optional<Event> eventOptional = eventRepo.findById(eventId);
        if(eventOptional.isEmpty()){
            return null;
        }
        Event event = eventOptional.get();
        Participant participant = participantRepo.findById(new ParticipantId(uuid, event));
        if(participant == null){
            return null;
        }
        return debtRepo.findByParticipant(participant);
    }

    public List<Debt> getALlDebts() {
        return debtRepo.findAll();
    }

    public Debt saveDebt(DebtDTO debtDTO) {
        Optional<Event> eventOptional = eventRepo.findById(debtDTO.getEventId());
        if (eventOptional.isEmpty()) {
            return null;
        }
        Event event = eventOptional.get();
        Optional<Expense> expenseOptional =
            expenseRepo.findById(new ExpenseId(event, debtDTO.getExpenseId()));
        if (expenseOptional.isEmpty()) {
            return null;
        }
        Expense expense = expenseOptional.get();
        Participant participant = participantRepo.findById(
            new ParticipantId(debtDTO.getParticipantUuid(), event));//To change when Participant class is restructured
        if (participant == null) {
            return null;
        }
        return debtRepo.save(new Debt(expense, debtDTO.getBalance(), participant));
    }

    public List<Debt> deleteDebtsOfExpense(int eventId, int expenseId){
        Optional<Event> eventOptional = eventRepo.findById(eventId);
        if (eventOptional.isEmpty()) return null;
        Event event = eventOptional.get();
        Optional<Expense> expenseOptional =
            expenseRepo.findById(new ExpenseId(event, expenseId));
        if (expenseOptional.isEmpty()) return null;
        Expense expense = expenseOptional.get();
        return debtRepo.deleteDebtsByExpense(expense);
    }
}
