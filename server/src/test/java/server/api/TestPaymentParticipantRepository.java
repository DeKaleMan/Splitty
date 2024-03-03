package server.api;

import commons.PaymentParticipant;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.PaymentParticipantRepository;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TestPaymentParticipantRepository implements PaymentParticipantRepository {

    private final Map<Long, PaymentParticipant> payments = new HashMap<>();
    private long currentId = 1; // auto-increment id

    @Override
    public <S extends PaymentParticipant> S save(S entity) {
        return null;
    }

    @Override
    public <S extends PaymentParticipant> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<PaymentParticipant> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public List<PaymentParticipant> findAll() {
        return new ArrayList<>(payments.values());
    }

    @Override
    public List<PaymentParticipant> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(PaymentParticipant entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends PaymentParticipant> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<PaymentParticipant> findByPayerEmail(String payerEmail) {
        return payments.values().stream()
                .filter(payment -> payment.getPayer().getEmail().equals(payerEmail))
                .collect(Collectors.toList());
    }

    @Override
    public List<PaymentParticipant> findByPayeeEmail(String payeeEmail) {
        return payments.values().stream()
                .filter(payment -> payment.getPayee().getEmail().equals(payeeEmail))
                .collect(Collectors.toList());
    }

    @Override
    public Double sumPaymentsByPayerEmail(String payerEmail) {
        return payments.values().stream()
                .filter(payment -> payment.getPayer().getEmail().equals(payerEmail))
                .mapToDouble(PaymentParticipant::getAmount)
                .sum();
    }

    @Override
    public Double sumPaymentsByPayeeEmail(String payeeEmail) {
        return payments.values().stream()
                .filter(payment -> payment.getPayee().getEmail().equals(payeeEmail))
                .mapToDouble(PaymentParticipant::getAmount)
                .sum();
    }

    public void addPayment(PaymentParticipant payment) {
        payment.setId(currentId++);
        payments.put(payment.getId(), payment);
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends PaymentParticipant> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends PaymentParticipant> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<PaymentParticipant> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public PaymentParticipant getOne(Long aLong) {
        return null;
    }

    @Override
    public PaymentParticipant getById(Long aLong) {
        return null;
    }

    @Override
    public PaymentParticipant getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends PaymentParticipant> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends PaymentParticipant> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends PaymentParticipant> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends PaymentParticipant> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends PaymentParticipant> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends PaymentParticipant> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends PaymentParticipant, R> R findBy(Example<S> example,
                                                      Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public List<PaymentParticipant> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<PaymentParticipant> findAll(Pageable pageable) {
        return null;
    }
}
