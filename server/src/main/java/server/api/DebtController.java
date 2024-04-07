package server.api;

import commons.*;
import commons.dto.DebtDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import server.service.DebtService;

import java.util.List;

@RestController
@RequestMapping("/api/debts")
public class DebtController {

    private final DebtService debtService;

    @Autowired
    public DebtController(DebtService debtService) {
        this.debtService = debtService;
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<List<Debt>> getAllDebtsOfEvent(@PathVariable("eventId") int eventId) {
        List<Debt> debts = debtService.getAllDebtsOfEvent(eventId);
        return (debts == null) ?
            ResponseEntity.badRequest().build() :
            ResponseEntity.ok(debts);
    }

    @GetMapping("/{eventId}/expense/{expenseId}")
    public ResponseEntity<List<Debt>> getAllDebtsOfExpense(@PathVariable("eventId") int eventId,
                                                           @PathVariable("expenseId") int expenseId){
        List<Debt> debts = debtService.getAllDebtsOfExpense(eventId,expenseId);
        return (debts == null) ?
            ResponseEntity.badRequest().build() :
            ResponseEntity.ok(debts);
    }

    @GetMapping("/{eventId}/participant/{uuid}")
    public ResponseEntity<List<Debt>> getAllDebtsOfParticipant(@PathVariable("eventId") int eventId,
                                                               @PathVariable("uuid") String uuid){
        List<Debt> debts = debtService.getAllDebtsOfParticipant(eventId,uuid);
        return (debts == null) ?
            ResponseEntity.badRequest().build() :
            ResponseEntity.ok(debts);
    }

    @GetMapping({"", "/"})
    public ResponseEntity<List<Debt>> getALlDebts() {
        return ResponseEntity.ok(debtService.getALlDebts());
    }

    @PostMapping(path = {"", "/"})
    public ResponseEntity<Debt> saveDebt(@RequestBody DebtDTO debtDTO) {
        Debt debt = debtService.saveDebt(debtDTO);
        return (debt == null) ?
            ResponseEntity.badRequest().build() :
            ResponseEntity.ok(debt);

    }

    @DeleteMapping("/{eventId}/{expenseId}")
    @Transactional
    public ResponseEntity<List<Debt>> deleteDebtsOfExpense(@PathVariable("eventId")
                                                               int eventId,
                                                           @PathVariable("expenseId")
                                                           int expenseId){
        List<Debt> debts = debtService.deleteDebtsOfExpense(eventId,expenseId);
        return (debts == null) ?
            ResponseEntity.badRequest().build() :
            ResponseEntity.ok(debts);
    }
}
