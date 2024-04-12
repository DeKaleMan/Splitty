package server.api;

import commons.Event;
import commons.Payment;
import commons.Participant;
import commons.dto.PaymentDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.service.PaymentService;

import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class PaymentControllerTest {
    @Mock
    PaymentService service;
    @InjectMocks
    private PaymentController sut;
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
        MockitoAnnotations.openMocks(this);
        e1.id = 1;
        e2.id = 2;
        payment1.setId(1L);
        payment2.setId(2L);
        payment3.setId(3L);
    }

    @Test
    void testGetAllPayments() {
        List<Payment> expected = List.of(payment1, payment2, payment3);
        when(service.getAllPayments()).thenReturn(expected);
        ResponseEntity<List<Payment>> payments = sut.getAllPayments();
        assertEquals(HttpStatus.OK, payments.getStatusCode());
        assertEquals(expected, payments.getBody());
    }

    @Test
    void testGetAllPaymentOfEvent() {
        List<Payment> expected = List.of(payment1, payment2);
        when(service.getPaymentsOfEvent(anyInt())).thenReturn(expected);
        ResponseEntity<List<Payment>> payments = sut.getPaymentsOfEvent(1);
        assertEquals(expected, payments.getBody());
        assertEquals(HttpStatus.OK, payments.getStatusCode());
    }

    @Test
    void testGetAllPaymentOfEventInvalid() {
        when(service.getPaymentsOfEvent(anyInt())).thenReturn(null);
        ResponseEntity<List<Payment>> res = sut.getPaymentsOfEvent(-1);
        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
    }

    @Test
    void testCreatePayment() {
        Payment payment = new Payment(participant1, participant2, 300, false);
        PaymentDTO paymentDTO = new PaymentDTO("uuid1",
            "uuid2",
            1, 300, false);
        when(service.createPayment(any(PaymentDTO.class))).thenReturn(payment);
        ResponseEntity<Payment> response = sut.createPayment(paymentDTO);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(payment, response.getBody());
    }

    @Test
    void testCreatePaymentInvalid() {
        when(service.createPayment(any(PaymentDTO.class))).thenReturn(null);
        ResponseEntity<Payment> res = sut.createPayment(new PaymentDTO());
        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
    }

    @Test
    void testUpdate() {
        PaymentDTO paymentDTO = new PaymentDTO("uuid1",
            "uuid2",
            1, 300, true);
        Payment updated = new Payment(participant1, participant2, 300, true);
        when(service.updatePayment(any(PaymentDTO.class), anyLong())).thenReturn(updated);
        ResponseEntity<Payment> response = sut.updatePayment(paymentDTO, 4L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updated, response.getBody());
    }

    @Test
    void testUpdateInvalid() {
        when(service.updatePayment(any(PaymentDTO.class), anyLong())).thenReturn(null);
        ResponseEntity<Payment> res = sut.updatePayment(new PaymentDTO(), -1);
        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
    }

    @Test
    void testDelete() {
        List<Payment> expected = List.of(payment1, payment2);
        when(service.deletePaymentsOfEvent(anyInt())).thenReturn(expected);
        ResponseEntity<List<Payment>> response = sut.deletePaymentsOfEvent(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
    }

    @Test
    void testDeleteInvalid() {
        when(service.deletePaymentsOfEvent(anyInt())).thenReturn(null);
        ResponseEntity<List<Payment>> res = sut.deletePaymentsOfEvent(-1);
        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
    }

    @Test
    void testGetByID() {
        when(service.getPayment(anyLong())).thenReturn(payment1);
        ResponseEntity<Payment> response = sut.getPayment(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(payment1, response.getBody());
    }

    @Test
    void testGetByIDInvalid() {
        when(service.getPayment(anyLong())).thenReturn(null);
        ResponseEntity<Payment> response = sut.getPayment(-1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
