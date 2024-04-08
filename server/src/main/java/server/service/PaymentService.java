package server.service;

import commons.Event;
import commons.Participant;
import commons.ParticipantId;
import commons.Payment;
import commons.dto.PaymentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.database.EventRepository;
import server.database.ParticipantRepository;
import server.database.PaymentRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ParticipantRepository participantRepository;
    private EventRepository eventRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository,
                             ParticipantRepository participantRepository,
                             EventRepository eventRepository){
        this.paymentRepository = paymentRepository;
        this.participantRepository = participantRepository;
        this.eventRepository = eventRepository;
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }


    public List<Payment> getPaymentsOfEvent(int eventId){
        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        if(optionalEvent.isEmpty()){
            return null;
        }

        Event event = optionalEvent.get();
        return paymentRepository.findByEvent(event);
    }

    public Payment createPayment(PaymentDTO paymentDTO) {
        Event event = eventRepository.findEventById(paymentDTO.getEventCode());
        if (event == null) {
            return null;
        }

        Participant payee = participantRepository.findById(new ParticipantId(paymentDTO.getPayeeUuid(), event));
        Participant payer = participantRepository.findById(new ParticipantId(paymentDTO.getPayerUuid(), event));
        if (payee == null || payer == null) {
            return null;
        }
        Payment payment = new Payment(payer, payee, paymentDTO.getAmount(), paymentDTO.isPaid());
        return paymentRepository.save(payment);
    }

    public Payment updatePayment(PaymentDTO paymentDTO, long id){
        Optional<Event> optionalEvent = eventRepository.findById(paymentDTO.getEventCode());
        if(optionalEvent.isEmpty()){
            return null;
        }

        Event event = optionalEvent.get();

        Optional<Payment> optionalPayment = paymentRepository.findById(id);
        if(optionalPayment.isEmpty()){
            return null;
        }

        Payment existingPayment = optionalPayment.get();

        Participant payer = participantRepository.findById(new ParticipantId(paymentDTO.getPayerUuid(), event));
        Participant payee = participantRepository.findById(new ParticipantId(paymentDTO.getPayeeUuid(), event));

        if(payer == null || payee == null){
            return null;
        }

        existingPayment.setPaid(paymentDTO.isPaid());
        existingPayment.setAmount(paymentDTO.getAmount());
        existingPayment.setPayee(payee);
        existingPayment.setPayer(payer);

        return  paymentRepository.save(existingPayment);
    }

    public List<Payment> deletePaymentsOfEvent(int id){
        Optional<Event> optionalEvent = eventRepository.findById(id);
        if(optionalEvent.isEmpty()){
            return null;
        }

        Event event = optionalEvent.get();

        List<Payment> payments = paymentRepository.findByEvent(event);
        paymentRepository.deleteAll(payments);

        return payments;
    }

    public Payment getPayment(long id) {
        Optional<Payment> optionalPayment = paymentRepository.findById(id);
        return optionalPayment.orElse(null);
    }
}
