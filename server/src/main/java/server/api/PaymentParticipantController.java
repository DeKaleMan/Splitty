package server.api;

import commons.PaymentParticipant;
import server.service.PaymentParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
public class PaymentParticipantController {

    private final PaymentParticipantService paymentParticipantService;

    @Autowired
    public PaymentParticipantController(PaymentParticipantService paymentParticipantService) {
        this.paymentParticipantService = paymentParticipantService;
    }

    @GetMapping
    public List<PaymentParticipant> getAllPayments() {
        return paymentParticipantService.findAllPayments();
    }
}