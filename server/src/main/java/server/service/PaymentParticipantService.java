package server.service;

import server.database.PaymentParticipantRepository;
import commons.PaymentParticipant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentParticipantService {

    private final PaymentParticipantRepository paymentParticipantRepository;

    @Autowired
    public PaymentParticipantService(PaymentParticipantRepository paymentParticipantRepository) {
        this.paymentParticipantRepository = paymentParticipantRepository;
    }

    public List<PaymentParticipant> findAllPayments() {
        return paymentParticipantRepository.findAll();
    }

    public PaymentParticipant savePayment(PaymentParticipant payment) {
        return paymentParticipantRepository.save(payment);
    }
}
