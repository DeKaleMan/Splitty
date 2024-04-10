package server.api;

import commons.*;
import commons.dto.PaymentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.service.PaymentService;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService){
        this.paymentService = paymentService;
    }

    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPayment(@PathVariable("id") long id) {
        Payment payment = paymentService.getPayment(id);
        return (payment == null) ?
            ResponseEntity.notFound().build() :
            ResponseEntity.ok(payment);
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<Payment>> getPaymentsOfEvent(@PathVariable("eventId") int eventId){
        List<Payment> payments = paymentService.getPaymentsOfEvent(eventId);
        return (payments == null) ?
            ResponseEntity.badRequest().build() :
            ResponseEntity.ok(payments);
    }


    @PostMapping
    public ResponseEntity<Payment> createPayment(@RequestBody PaymentDTO paymentDTO) {
        Payment payment = paymentService.createPayment(paymentDTO);
        return (payment == null) ?
            ResponseEntity.badRequest().build() :
            ResponseEntity.ok(payment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Payment> updatePayment(@RequestBody PaymentDTO paymentDTO, @PathVariable("id") long id){
        Payment payment = paymentService.updatePayment(paymentDTO,id);
        return (payment == null) ?
            ResponseEntity.badRequest().build() :
            ResponseEntity.ok(payment);
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<List<Payment>> deletePaymentsOfEvent(@PathVariable("eventId") int id){
        List<Payment> payments = paymentService.deletePaymentsOfEvent(id);
        return (payments == null) ?
            ResponseEntity.badRequest().build() :
            ResponseEntity.ok(payments);
    }

}