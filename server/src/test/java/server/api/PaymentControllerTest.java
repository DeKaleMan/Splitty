package server.api;

import commons.Event;
import commons.Payment;
import commons.Participant;
import commons.dto.PaymentDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.service.PaymentService;

import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PaymentControllerTest {

    private PaymentController sut;
    private TestPaymentRepository paymentRepository;
    private TestEventRepository eventRepository;
    private TestParticipantRepository participantRepository;
    Event e1 = new Event("Event1", new Date(10, 10, 2005), "Yavor", "Description1");

    Event e2 = new Event("Event2", new Date(10, 10, 2005), "Maciej", "Description2");

    Participant participant1 = new Participant("Yavor", 100.0, "IBAN1",
        "BIC1", "yavor@tudelft.nl", "", "uuid1", e1);
    Participant participant2 = new Participant("Jesse", 200.0, "IBAN2",
        "BIC2", "Jesse@tudelft.nl", "", "uuid2", e1);

    Participant participant3 = new Participant("Maciej", 300.0, "IBAN3",
        "BIC3", "Maciej@tudelft.nl", "", "uuid3", e2);


    Payment payment1 = new Payment(participant1, participant2, 100, false);
    Payment payment2 = new Payment(participant1, participant2, 200, false);
    Payment payment3 = new Payment(participant3, participant3, 100, false);

    @BeforeEach
    void setUp() {
        paymentRepository = new TestPaymentRepository();
        participantRepository = new TestParticipantRepository();
        eventRepository = new TestEventRepository();

        e1.id = 1;
        e2.id = 2;
        eventRepository.events.add(e1);
        eventRepository.events.add(e2);
        participantRepository.participants.add(participant1);
        participantRepository.participants.add(participant2);
        participantRepository.participants.add(participant3);
        payment1.setId(1L);
        payment2.setId(2L);
        payment3.setId(3L);
        paymentRepository.payments.add(payment1);
        paymentRepository.payments.add(payment2);
        paymentRepository.payments.add(payment3);

        PaymentService paymentService = new PaymentService(paymentRepository,
            participantRepository, eventRepository);
        sut = new PaymentController(paymentService);
    }

    @Test
    void testGetAllPayments() {
        ResponseEntity<List<Payment>> payments = sut.getAllPayments();
        assertEquals(paymentRepository.payments, payments.getBody());
        assertEquals(HttpStatus.OK, payments.getStatusCode());
        assertEquals("findAll", paymentRepository.methods.getLast());
    }

    @Test
    void testGetAllPaymentOfEvent(){
        List<Payment> expected = List.of(payment1,payment2);
        ResponseEntity<List<Payment>> payments = sut.getPaymentsOfEvent(1);
        assertEquals(expected, payments.getBody());
        assertEquals(HttpStatus.OK, payments.getStatusCode());
        assertEquals("findByEvent", paymentRepository.methods.getLast());
    }

    @Test
    void testGetAllPaymentOfEventInvalid(){
        ResponseEntity<List<Payment>> res = sut.getPaymentsOfEvent(-1);
        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
    }

    @Test
    void testCreatePayment() {
        Payment payment = new Payment(participant1, participant2, 300, false);
        paymentRepository.payments.add(payment);
        PaymentDTO paymentDTO = new PaymentDTO("uuid1",
            "uuid2",
            1, 300, false);
        ResponseEntity<Payment> response = sut.createPayment(paymentDTO);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(payment, response.getBody());
        assertEquals("save", paymentRepository.methods.getLast());
        //cleanup
        paymentRepository.payments.removeLast();
    }

    @Test
    void testCreatePaymentInvalidEvent() {
        ResponseEntity<Payment> res = sut.createPayment(new PaymentDTO("uuid",
            "uuid1",
            -1, 100, false));
        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
    }

    @Test
    void testCreatePaymentInvalidPayer() {
        ResponseEntity<Payment> res = sut.createPayment(new PaymentDTO("uuidInvalid",
            "uuid1",
            1, 100, false));
        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
    }

    @Test
    void testCreatePaymentInvalidPayee() {
        ResponseEntity<Payment> res = sut.createPayment(new PaymentDTO("uuid1",
            "uuidInvalid",
            1, 100, false));
        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
    }

    @Test
    void testUpdate(){
        Payment toUpdate = new Payment(participant1, participant2, 300, false);
        toUpdate.setId(4L);
        paymentRepository.payments.add(toUpdate);
        PaymentDTO paymentDTO = new PaymentDTO("uuid1",
            "uuid2",
            1, 300, true);
        Payment updated = new Payment(participant1, participant2, 300, true);
        updated.setId(4L);
        ResponseEntity<Payment> response = sut.updatePayment(paymentDTO, 4L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updated, response.getBody());
        assertEquals("save", paymentRepository.methods.getLast());
        //cleanup
        paymentRepository.payments.removeLast();
    }

    @Test
    void testUpdateInvalidEvent(){
        ResponseEntity<Payment> res = sut.updatePayment(new PaymentDTO("uuid1",
            "uuid2",
            -1, 100, false),1);
        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
    }

    @Test
    void testUpdateInvalidPayment(){
        ResponseEntity<Payment> res = sut.updatePayment(new PaymentDTO("uuid1",
            "uuid2",
            1, 100, false),-1);
        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
    }

    @Test
    void testUpdateInvalidPayer(){
        ResponseEntity<Payment> res = sut.updatePayment(new PaymentDTO("uuidInvalid",
            "uuid2",
            1, 100, false),1);
        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
    }

    @Test
    void testUpdateInvalidPayee(){
        ResponseEntity<Payment> res = sut.updatePayment(new PaymentDTO("uuid1",
            "uuidInvalid",
            1, 100, false),1);
        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
    }

    @Test
    void testDelete(){
        Event e = new Event("Event3", new Date(10, 10, 2005), "test", "Description3");
        e.id = 3;
        eventRepository.events.add(e);
        Participant participant = new Participant("Yavor", 100.0, "IBAN1",
            "BIC1", "yavor@tudelft.nl", "", "uuid1", e);
        participantRepository.participants.add(participant);
        Payment p1 = new Payment(participant, participant, 100, false);
        p1.setId(4L);
        Payment p2 = new Payment(participant, participant, 100, false);
        p2.setId(5L);
        List<Payment> expected = List.of(p1,p2);
        paymentRepository.payments.addAll(expected);
        ResponseEntity<List<Payment>> response = sut.deletePaymentsOfEvent(3);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(expected, response.getBody());
        assertEquals("deleteAll", paymentRepository.methods.getLast());

        //cleanup
        eventRepository.events.removeLast();
        participantRepository.participants.removeLast();
        paymentRepository.payments.removeIf(x -> x.getId() == 4L || x.getId() == 5L);
    }

    @Test
    void testDeleteInvalidEvent(){
        ResponseEntity<List<Payment>> res = sut.deletePaymentsOfEvent(-1);
        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
    }

}
