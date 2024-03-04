package server.api;

import commons.PaymentParticipant;
import commons.Participant; // Assuming Participant class is used within PaymentParticipant
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PaymentParticipantControllerTest {

    private PaymentParticipantController paymentParticipantController;
    private TestPaymentParticipantRepository testPaymentParticipantRepository;

    @BeforeEach
    void setUp() {
        testPaymentParticipantRepository = new TestPaymentParticipantRepository();
        paymentParticipantController = new PaymentParticipantController(testPaymentParticipantRepository);
    }

    @Test
    void testGetAllPayments() {
        Participant payer = new Participant("Payer", 1000, "IBAN123", "BIC123", "Yavor", "yavor@tudelft.nl");
        Participant payee = new Participant("Payee", 500, "IBAN456", "BIC456", "Jesse", "jesse@tudelft.nl");
        PaymentParticipant payment1 = new PaymentParticipant(payer, payee, 100.0, new Date(2));
        PaymentParticipant payment2 = new PaymentParticipant(payer, payee, 200.0, new Date(3));
        testPaymentParticipantRepository.addPayment(payment1);
        testPaymentParticipantRepository.addPayment(payment2);

        List<PaymentParticipant> payments = paymentParticipantController.getAllPayments();
        assertEquals(2, payments.size());
    }

}
