package service;

import commons.Event;
import commons.Participant;
import commons.Payment;
import commons.dto.PaymentDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.api.TestEventRepository;
import server.api.TestParticipantRepository;
import server.api.TestPaymentRepository;
import server.service.PaymentService;

import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PaymentServiceTest {
    private PaymentService sut;
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

        sut = new PaymentService(paymentRepository,
            participantRepository, eventRepository);
    }

    @Test
    void testGetAllPayments() {
        List<Payment> actual = sut.getAllPayments();
        assertEquals(paymentRepository.payments, actual);
        assertEquals("findAll", paymentRepository.methods.getLast());
    }

    @Test
    void testGetAllPaymentOfEvent() {
        List<Payment> expected = List.of(payment1, payment2);
        List<Payment> actual = sut.getPaymentsOfEvent(1);
        assertEquals(expected, actual);
        assertEquals("findByEvent", paymentRepository.methods.getLast());
    }

    @Test
    void testGetAllPaymentOfEventInvalid() {
        List<Payment> actual = sut.getPaymentsOfEvent(-1);
        assertNull(actual);
    }

    @Test
    void testCreatePayment() {
        Payment payment = new Payment(participant1, participant2, 300, false);
        paymentRepository.payments.add(payment);
        PaymentDTO paymentDTO = new PaymentDTO("uuid1",
            "uuid2",
            1, 300, false);
        Payment actual = sut.createPayment(paymentDTO);
        assertEquals(payment, actual);
        assertEquals("save", paymentRepository.methods.getLast());
        //cleanup
        paymentRepository.payments.removeLast();
    }

    @Test
    void testCreatePaymentInvalidEvent() {
        Payment actual = sut.createPayment(new PaymentDTO("uuid",
            "uuid1",
            -1, 100, false));
        assertNull(actual);
    }

    @Test
    void testCreatePaymentInvalidPayer() {
        Payment actual = sut.createPayment(new PaymentDTO("uuidInvalid",
            "uuid1",
            1, 100, false));
        assertNull(actual);
    }

    @Test
    void testCreatePaymentInvalidPayee() {
        Payment actual = sut.createPayment(new PaymentDTO("uuid1",
            "uuidInvalid",
            1, 100, false));
        assertNull(actual);
    }

    @Test
    void testUpdate() {
        Payment toUpdate = new Payment(participant1, participant2, 300, false);
        toUpdate.setId(4L);
        paymentRepository.payments.add(toUpdate);
        PaymentDTO paymentDTO = new PaymentDTO("uuid1",
            "uuid2",
            1, 300, true);
        Payment updated = new Payment(participant1, participant2, 300, true);
        updated.setId(4L);
        Payment actual = sut.updatePayment(paymentDTO, 4L);
        assertEquals(updated, actual);
        assertEquals("save", paymentRepository.methods.getLast());
        //cleanup
        paymentRepository.payments.removeLast();
    }

    @Test
    void testUpdateInvalidEvent() {
        Payment actual = sut.updatePayment(new PaymentDTO("uuid1",
            "uuid2",
            -1, 100, false), 1);
        assertNull(actual);
    }

    @Test
    void testUpdateInvalidPayment() {
        Payment actual = sut.updatePayment(new PaymentDTO("uuid1",
            "uuid2",
            1, 100, false), -1);
        assertNull(actual);
    }

    @Test
    void testUpdateInvalidPayer() {
        Payment actual = sut.updatePayment(new PaymentDTO("uuidInvalid",
            "uuid2",
            1, 100, false), 1);
        assertNull(actual);
    }

    @Test
    void testUpdateInvalidPayee() {
        Payment actual = sut.updatePayment(new PaymentDTO("uuid1",
            "uuidInvalid",
            1, 100, false), 1);
        assertNull(actual);
    }

    @Test
    void testDelete() {
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
        List<Payment> expected = List.of(p1, p2);
        paymentRepository.payments.addAll(expected);
        List<Payment> actual = sut.deletePaymentsOfEvent(3);
        assertEquals(expected, actual);
        assertEquals("deleteAll", paymentRepository.methods.getLast());

        //cleanup
        eventRepository.events.removeLast();
        participantRepository.participants.removeLast();
        paymentRepository.payments.removeIf(x -> x.getId() == 4L || x.getId() == 5L);
    }

    @Test
    void testDeleteInvalidEvent() {
        List<Payment> actual = sut.deletePaymentsOfEvent(-1);
        assertNull(actual);
    }

    @Test
    void testGetByID() {
        Payment actual = sut.getPayment(1L);
        assertEquals(payment1, actual);
        assertEquals("findById", paymentRepository.methods.getLast());
    }

    @Test
    void testGetByIDInvalid() {
        Payment actual = sut.getPayment(-1L);
        assertNull(actual);
    }

}