package server.api;

import commons.PaymentParticipant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import server.database.PaymentParticipantRepository;

import java.util.List;

@RestController
@RequestMapping("/payments")
public class PaymentParticipantController {

    private final PaymentParticipantRepository paymentParticipantRepository;

    @Autowired
    public PaymentParticipantController(PaymentParticipantRepository paymentParticipantRepository) {
        this.paymentParticipantRepository = paymentParticipantRepository;
    }

    @GetMapping
    public List<PaymentParticipant> getAllPayments() {
        return paymentParticipantRepository.findAll();
    }
}