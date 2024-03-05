package server.api;

import commons.Participant;
import commons.Transaction;
import jakarta.servlet.http.Part;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.TransactionRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/Transaction")
public class TransactionController {

    private final TransactionRepository transactionDB;

    @Autowired
    public TransactionController(TransactionRepository transactionDB){
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
    public List<Transaction> getAllTransaction(){
        return transactionDB.findAll();
    }


}
