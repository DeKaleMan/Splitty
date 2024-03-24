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

import java.util.List;
import java.util.Optional;

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

    @GetMapping("/{eventId}")
    public ResponseEntity<List<Payment>> getPaymentsOfEvent(@PathVariable("eventId") int eventId){
        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        if(optionalEvent.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        Event event = optionalEvent.get();
        return ResponseEntity.ok(paymentRepository.findByEvent(event));
    }


    @PostMapping
    public ResponseEntity<Payment> createPayment(@RequestBody PaymentDTO paymentDTO) {
        Event event = eventRepository.findEventById(paymentDTO.getEventCode());
        if (event == null) {
            return ResponseEntity.notFound().build();
        }

        Participant payee = participantRepository.findById(new ParticipantId(paymentDTO.getPayeeUuid(), event));
        Participant payer = participantRepository.findById(new ParticipantId(paymentDTO.getPayerUuid(), event));
        if (payee == null || payer == null) {
            return ResponseEntity.notFound().build();
        }
        Payment payment = new Payment(payer, payee, paymentDTO.getAmount(), paymentDTO.isPaid());
        Payment savedPayment = paymentRepository.save(payment);
        return ResponseEntity.ok(savedPayment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Payment> updatePayment(@RequestBody PaymentDTO paymentDTO, @PathVariable("id") long id){
        Optional<Event> optionalEvent = eventRepository.findById(paymentDTO.getEventCode());
        if(optionalEvent.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        Event event = optionalEvent.get();

        Optional<Payment> optionalPayment = paymentRepository.findById(id);
        if(optionalPayment.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        Payment existingPayment = optionalPayment.get();

        Participant payer = participantRepository.findById(new ParticipantId(paymentDTO.getPayerUuid(), event));
        Participant payee = participantRepository.findById(new ParticipantId(paymentDTO.getPayeeUuid(), event));

        if(payer == null || payee == null){
            return ResponseEntity.notFound().build();
        }

        existingPayment.setPaid(paymentDTO.isPaid());
        existingPayment.setAmount(paymentDTO.getAmount());
        existingPayment.setPayee(payee);
        existingPayment.setPayer(payer);

        return  ResponseEntity.ok(paymentRepository.save(existingPayment));
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Payment> deletePaymentsOfEvent(@PathVariable("eventId") int id){
        Optional<Event> optionalEvent = eventRepository.findById(id);
        if(optionalEvent.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        Event event = optionalEvent.get();

        List<Payment> payments = paymentRepository.findByEvent(event);
        paymentRepository.deleteAll(payments);

        return ResponseEntity.ok().build();
    }

}