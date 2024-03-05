package server.api;

import commons.Debt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import server.database.DebtRepository;

import java.util.List;

@RestController
@RequestMapping("/api/Transaction")
public class DebtController {

    private final DebtRepository transactionDB;

    @Autowired
    public DebtController(DebtRepository transactionDB){
        this.transactionDB = transactionDB;
    }

//    @GetMapping(path = {"","/"})
//    public ResponseEntity<Transaction> getAllTransactionByOwer(@RequestBody Participant ower){
//        if(ower == null){
//            return ResponseEntity.badRequest().build();
//        }
//        Optional<Participant> = transactionDB.findAll(ower);
//    }

    @GetMapping({"", "/"})
    public List<Debt> getAllTransaction(){
        return transactionDB.findAll();
    }


}
