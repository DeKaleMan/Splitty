package server.api;

import commons.*;
import commons.dto.ExpenseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import server.service.ExpenseService;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {
    private final ExpenseService expenseService;

    @Autowired
    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @MessageMapping("/updateExpense") // -> /app/addExpense
    @SendTo("/topic/updateExpense") // when we are done processing it we send it to the path provided
    public Expense expenseByEventWS(@RequestBody Expense expense){
        return expense;
    }

    @GetMapping(path = {"", "/"})
    public ResponseEntity<List<Expense>> findByEventCode(@RequestParam int eventCode) {
        List<Expense> expenses = expenseService.getByEventCode(eventCode);
        return (expenses == null) ?
            ResponseEntity.badRequest().build() :
            ResponseEntity.ok(expenses);
    }

    @GetMapping("/{payerUuid}")
    public ResponseEntity<List<Expense>> findByEventCodeAndPayerUuid(@RequestParam int eventCode,
                                                                      @PathVariable("payerUuid")
                                                                      String uuid) {
        List<Expense> expenses = expenseService.getByEventCodeAndPayerUuid(eventCode,uuid);
        return (expenses == null) ?
            ResponseEntity.badRequest().build() :
            ResponseEntity.ok(expenses);
    }

    @PostMapping("addExp")
    @Transactional
    public ResponseEntity<Expense> saveExpense(@RequestBody ExpenseDTO expenseDTO) {
        Expense expense = expenseService.saveExpense(expenseDTO);
        return (expense == null) ?
            ResponseEntity.badRequest().build() :
            ResponseEntity.ok(expense);
    }

    @PutMapping("/{eventId}/{expenseId}")
    public ResponseEntity<Expense> updateExpense(@PathVariable("eventId") int eventId,
                                                 @PathVariable("expenseId") int expenseId,
                                                 @RequestBody ExpenseDTO expenseDTO){
        Expense expense = expenseService.updateExpense(eventId,expenseId,expenseDTO);
        return (expense == null) ?
            ResponseEntity.badRequest().build() :
            ResponseEntity.ok(expense);
    }


    @DeleteMapping(path = {"", "/"})
    public ResponseEntity<Expense> deleteExpenseByEventIdAndExpenseId(@RequestParam int eventID,
                                                                      @RequestParam int expenseID) {
        Expense expense = expenseService.deleteExpense(eventID,expenseID);
        return (expense == null) ?
            ResponseEntity.badRequest().build() :
            ResponseEntity.ok(expense);
    }
}
