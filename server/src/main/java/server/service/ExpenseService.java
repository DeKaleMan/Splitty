package server.service;

import commons.*;
import commons.dto.ExpenseDTO;
import org.springframework.stereotype.Service;
import server.database.EventRepository;
import server.database.ExpenseRepository;
import server.database.ParticipantRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepo;
    private final EventRepository eventRepo;

    private final ParticipantRepository participantRepo;

    public ExpenseService(ExpenseRepository expenseRepo, EventRepository eventRepo,
                             ParticipantRepository participantRepo) {
        this.expenseRepo = expenseRepo;
        this.eventRepo = eventRepo;
        this.participantRepo = participantRepo;
    }

    public List<Expense> getByEventCode(int eventCode){
        Optional<Event> event = eventRepo.findById(eventCode);
        if (event.isEmpty()) {
            return null;
        }
        return expenseRepo.findByEvent(event.get());
    }

    public List<Expense> getByEventCodeAndPayerUuid(int eventCode, String uuid){
        Optional<Event> event = eventRepo.findById(eventCode);
        if (event.isEmpty()) {
            return null;
        }
        Participant payer = participantRepo.findById(new ParticipantId(uuid,event.get()));
        if(payer == null) return null;
        return expenseRepo.findByEventAndPayer(event.get(), payer);
    }

    public Expense saveExpense(ExpenseDTO expenseDTO){
        if (expenseDTO == null || isNullOrEmpty(expenseDTO.getPayerUuid()) ||
            expenseDTO.getTotalExpense() < 0.0 || expenseDTO.getDate() == null)
            return null;
        Optional<Event> event = eventRepo.findById(expenseDTO.getEventId());
        if (event.isEmpty()) {
            return null;
        }
        Participant payer = participantRepo.findById(new ParticipantId(expenseDTO.getPayerUuid(), event.get()));
        if(payer == null) return null;
        Expense expense =
            new Expense(event.get(), expenseDTO.getDescription(), expenseDTO.getTag(),
                expenseDTO.getDate(), expenseDTO.getTotalExpense(),
                payer, expenseDTO.isSharedExpense());
        return expenseRepo.save(expense);
    }

    public Expense updateExpense(int eventCode, int expenseId, ExpenseDTO expenseDTO){
        Optional<Event> optionalEvent = eventRepo.findById(eventCode);
        if (optionalEvent.isEmpty()) {
            return null;
        }
        Event event = optionalEvent.get();
        Optional<Expense> optionalExpense = expenseRepo.findById(new ExpenseId(event,expenseId));
        if (optionalExpense.isEmpty()) {
            return null;
        }
        Participant payer = participantRepo.findById(new ParticipantId(expenseDTO.getPayerUuid(), event));
        if(payer == null) return null;
        Expense expense = optionalExpense.get();
        expense.setTotalExpense(expenseDTO.getTotalExpense());
        expense.setDate(expenseDTO.getDate());
        expense.setDescription(expenseDTO.getDescription());
        expense.setPayer(payer);
        expense.setTag(expenseDTO.getTag());
        expense.setSharedExpense(expenseDTO.isSharedExpense());

        return expenseRepo.save(expense);
    }

    public Expense deleteExpense(int eventID, int expenseID){
        Optional<Event> event = eventRepo.findById(eventID);
        if (event.isEmpty()) return null;
        ExpenseId expenseId = new ExpenseId(event.get(), expenseID);

        Optional<Expense> expense = expenseRepo.findById(expenseId);

        if (expense.isEmpty()) {
            return null;
        }

        Expense res = expense.get();
        expenseRepo.delete(res);

        return res;
    }

    private boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }
}
