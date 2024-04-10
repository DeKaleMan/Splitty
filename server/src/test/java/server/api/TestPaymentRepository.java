package server.api;

import commons.Event;
import commons.ParticipantId;
import commons.Payment;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.PaymentRepository;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TestPaymentRepository implements PaymentRepository {
    public List<Payment> payments = new ArrayList<>();
    public List<String> methods = new ArrayList<>();

    @Override
    public List<Payment> findByPayerId(ParticipantId payerId) {
        methods.add("findByPayerId");
        return payments.stream().filter(x -> x.getPayer().getId().equals(payerId)).collect(Collectors.toList());
    }

    @Override
    public List<Payment> findByPayeeId(ParticipantId payeeId) {
        methods.add("findByPayeeId");
        return payments.stream().filter(x -> x.getPayee().getId().equals(payeeId)).collect(Collectors.toList());
    }

    @Override
    public List<Payment> findByEvent(Event event) {
        methods.add("findByEvent");
        return payments.stream().filter(x -> x.getPayer().getId().getEvent().equals(event)).toList();
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Payment> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Payment> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Payment> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Payment getOne(Long aLong) {
        return null;
    }

    @Override
    public Payment getById(Long aLong) {
        return null;
    }

    @Override
    public Payment getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends Payment> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Payment> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Payment> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Payment> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Payment> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Payment> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Payment, R> R findBy(Example<S> example,
                                           Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends Payment> S save(S entity) {
        payments.removeIf(x -> x.getId() == entity.getId());
        methods.add("save");
        payments.add(entity);
        return entity;
    }

    @Override
    public <S extends Payment> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Payment> findById(Long aLong) {
        methods.add("findById");
        return payments.stream().filter(x -> Objects.equals(x.getId(), aLong)).findFirst();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public List<Payment> findAll() {
        methods.add("findAll");
        return payments;
    }

    @Override
    public List<Payment> findAllById(Iterable<Long> longs) {
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
    public void delete(Payment entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Payment> entities) {
        List<Payment> toDelete = new ArrayList<>();
        entities.forEach(toDelete::add);
        payments.removeAll(toDelete);
        methods.add("deleteAll");
    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<Payment> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Payment> findAll(Pageable pageable) {
        return null;
    }
}