package server.api;

import commons.Event;
import commons.Participant;
import commons.ParticipantId;
import commons.Payment;
import commons.dto.PaymentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.EventRepository;
import server.database.ParticipantRepository;
import server.database.PaymentRepository;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentRepository paymentRepository;
    private final ParticipantRepository participantRepository;
    private EventRepository eventRepository;

    @Autowired
    public PaymentController(PaymentRepository paymentRepository,
                             ParticipantRepository participantRepository,
                             EventRepository eventRepository){
        this.paymentRepository = paymentRepository;
        this.participantRepository = participantRepository;
        this.eventRepository = eventRepository;
    }

    @GetMapping
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }


    @PostMapping
    public ResponseEntity<Payment> createPayment(@RequestBody PaymentDTO paymentDTO) {
        Event event = eventRepository.findEventById(paymentDTO.getEventCode());
        if (event == null) {
            return ResponseEntity.notFound().build();
        }

        Participant payee = participantRepository.findById(new ParticipantId(paymentDTO.getPayeeEmail(), event));
        Participant payer = participantRepository.findById(new ParticipantId(paymentDTO.getPayerEmail(), event));
        if (payee == null || payer == null) {
            return ResponseEntity.notFound().build();
        }
        Date now = new Date();
        Payment payment = new Payment(payer, payee, paymentDTO.getAmount(), now);
        Payment savedPayment = paymentRepository.save(payment);
        return ResponseEntity.ok(savedPayment);
    }

}