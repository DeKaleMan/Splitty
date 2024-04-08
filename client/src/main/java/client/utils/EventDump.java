package client.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import commons.*;
import commons.dto.DebtDTO;
import commons.dto.EventDTO;
import commons.dto.ExpenseDTO;
import commons.dto.ParticipantDTO;

import java.io.Serializable;
import java.util.*;

public class EventDump implements Serializable {

    private EventDTO eventDTO;
    private List<ExpenseDTO> expenseDTOList;
    private List<ParticipantDTO> participantDTOList;
    private List<DebtDTO> debtDTOList;
    private Queue<Integer> oldExpenseIds = new LinkedList<>();
    private HashMap<Integer, Integer> oldExpenseIdToNewExpenseId = new HashMap<>();

    @JsonIgnore
    ServerUtils serverUtils;

    public EventDump(int eventId, ServerUtils serverUtils) {
        this.serverUtils = serverUtils;
        expenseDTOList = new ArrayList<>();
        participantDTOList = new ArrayList<>();
        debtDTOList = new ArrayList<>();
        initializeDtoFields(eventId);
    }

    public EventDump() {

    }

    public void initializeDtoFields(int eventId) {
        Event event = serverUtils.getEventById(eventId);
        eventDTO = new EventDTO(event.getName(), event.getDate(), event.getHost(), event.getDescription());

        serverUtils.getExpense(eventId).forEach(expense -> {
            expenseDTOList.add(new ExpenseDTO(0, expense.getDescription(), expense.getType(),
                    expense.getDate(), expense.getTotalExpense(), expense.getPayer().getUuid(),
                    expense.isSharedExpense()));
            oldExpenseIds.add(expense.getExpenseId());
        });

        serverUtils.getParticipants(eventId).forEach(participant -> {
            participantDTOList.add(new ParticipantDTO(participant.getName(), participant.getBalance(),
                    participant.getIBan(), participant.getBIC(), participant.getEmail(),
                    participant.getAccountHolder(), 0, participant.getUuid(), "tmp"));
        });

        serverUtils.getDebtByEventCode(eventId).forEach(debt -> {
            debtDTOList.add(new DebtDTO(debt.getBalance(), 0,
                    debt.getExpense().getExpenseId(), debt.getParticipant().getUuid()));
        });
    }

    public void importEvent() {
        Event newAddedEvent = serverUtils.addEvent(eventDTO);

        participantDTOList.forEach(participantDTO -> {
            participantDTO.setEventId(newAddedEvent.getId());
            participantDTO.setEventInviteCode(newAddedEvent.getInviteCode());
            serverUtils.createParticipant(participantDTO);
        });

        expenseDTOList.forEach(expenseDTO -> {
            expenseDTO.setEventId(newAddedEvent.getId());
            Expense newExpense = serverUtils.addExpense(expenseDTO);
            oldExpenseIdToNewExpenseId.put(oldExpenseIds.remove(), newExpense.getExpenseId());
        });

        debtDTOList.forEach(debtDTO -> {
            debtDTO.setEventId(newAddedEvent.getId());
            debtDTO.setExpenseId(oldExpenseIdToNewExpenseId.get(debtDTO.getExpenseId()));
            serverUtils.saveDebt(debtDTO);
        });

        serverUtils.generatePaymentsForEvent(newAddedEvent.getId());
    }

    public String exportEvent() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    // Getters and setters are needed for serialization

    public EventDTO getEventDTO() {
        return eventDTO;
    }

    public void setEventDTO(EventDTO eventDTO) {
        this.eventDTO = eventDTO;
    }

    public List<ExpenseDTO> getExpenseDTOList() {
        return expenseDTOList;
    }

    public void setExpenseDTOList(List<ExpenseDTO> expenseDTOList) {
        this.expenseDTOList = expenseDTOList;
    }

    public List<ParticipantDTO> getParticipantDTOList() {
        return participantDTOList;
    }

    public void setParticipantDTOList(List<ParticipantDTO> participantDTOList) {
        this.participantDTOList = participantDTOList;
    }

    public List<DebtDTO> getDebtDTOList() {
        return debtDTOList;
    }

    public void setDebtDTOList(List<DebtDTO> debtDTOList) {
        this.debtDTOList = debtDTOList;
    }

    public Queue<Integer> getOldExpenseIds() {
        return oldExpenseIds;
    }

    public void setOldExpenseIds(Queue<Integer> oldExpenseIds) {
        this.oldExpenseIds = oldExpenseIds;
    }

    public HashMap<Integer, Integer> getOldExpenseIdToNewExpenseId() {
        return oldExpenseIdToNewExpenseId;
    }

    public void setOldExpenseIdToNewExpenseId(HashMap<Integer, Integer> oldExpenseIdToNewExpenseId) {
        this.oldExpenseIdToNewExpenseId = oldExpenseIdToNewExpenseId;
    }

    public void setServerUtils(ServerUtils serverUtils) {
        this.serverUtils = serverUtils;
    }
}
