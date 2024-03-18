package server.api;

import commons.Event;
import commons.Payment;
import commons.Participant;
import commons.dto.PaymentDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.sql.Date;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PaymentControllerTest {

    private PaymentController paymentController;
    private TestPaymentRepository testPaymentParticipantRepository;
    private TestEventRepository testEventRepository;
    private TestParticipantRepository testParticipantRepository;
    Participant participant1;
    Participant participant2;

    @BeforeEach
    void setUp() {
        testPaymentParticipantRepository = new TestPaymentRepository();
        testParticipantRepository = new TestParticipantRepository();
        testEventRepository = new TestEventRepository();

        Event event = new Event("Event1", "2021-01-01", "Yavor", "Description1");
        participant1 = new Participant("Yavor", 100.0, "IBAN1",
                "BIC1", "Yavor", "yavor@tudelft.nl", event);
        participant2 = new Participant("Jesse", 200.0, "IBAN2",
                "BIC2", "Jesse", "Jesse@tudelft.nl", event);
        event.setId(1);
        testEventRepository.save(event);
        testParticipantRepository.save(participant1);
        testParticipantRepository.save(participant2);


        paymentController = new PaymentController(testPaymentParticipantRepository,
                testParticipantRepository, testEventRepository);
    }

    @Test
    void testGetAllPayments() {
        Payment payment = new Payment(participant1, participant2, 100, new Date(0));
        testPaymentParticipantRepository.payments.add(payment);
        List<Payment> payments = paymentController.getAllPayments();
        assertEquals(testPaymentParticipantRepository.payments, payments);
    }

    @Test
    void testCreatePayment() {
        Payment payment = new Payment(participant1, participant2, 100, new Date(0));
        testPaymentParticipantRepository.payments.add(payment);
        PaymentDTO paymentDTO = new PaymentDTO("yavor@tudelft.nl",
                "Jesse@tudelft.nl",
                1, 100);
        ResponseEntity<Payment> response = paymentController.createPayment(paymentDTO);
        System.out.println(response.getStatusCode());
        assertEquals(payment, response.getBody());
    }

}
