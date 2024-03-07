//package server.api;
//
//import commons.Payment;
//import org.springframework.data.domain.Example;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.repository.query.FluentQuery;
//import server.database.PaymentRepository;
//
//import java.util.*;
//import java.util.function.Function;
//import java.util.stream.Collectors;
//
//public class TestPaymentRepository implements PaymentRepository {
//
//    private final Map<Long, Payment> payments = new HashMap<>();
//    private long currentId = 1; // auto-increment id
//
//    @Override
//    public <S extends Payment> S save(S entity) {
//        return null;
//    }
//
//    @Override
//    public <S extends Payment> List<S> saveAll(Iterable<S> entities) {
//        return null;
//    }
//
//    @Override
//    public Optional<Payment> findById(Long aLong) {
//        return Optional.empty();
//    }
//
//    @Override
//    public boolean existsById(Long aLong) {
//        return false;
//    }
//
//    @Override
//    public List<Payment> findAll() {
//        return new ArrayList<>(payments.values());
//    }
//
//    @Override
//    public List<Payment> findAllById(Iterable<Long> longs) {
//        return null;
//    }
//
//    @Override
//    public long count() {
//        return 0;
//    }
//
//    @Override
//    public void deleteById(Long aLong) {
//
//    }
//
//    @Override
//    public void delete(Payment entity) {
//
//    }
//
//    @Override
//    public void deleteAllById(Iterable<? extends Long> longs) {
//
//    }
//
//    @Override
//    public void deleteAll(Iterable<? extends Payment> entities) {
//
//    }
//
//    @Override
//    public void deleteAll() {
//
//    }
//
//    @Override
//    public List<Payment> findByPayerEmail(String payerEmail) {
//        return payments.values().stream()
//                .filter(payment -> payment.getPayer().getEmail().equals(payerEmail))
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public List<Payment> findByPayeeEmail(String payeeEmail) {
//        return payments.values().stream()
//                .filter(payment -> payment.getPayee().getEmail().equals(payeeEmail))
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public Double sumPaymentsByPayerEmail(String payerEmail) {
//        return payments.values().stream()
//                .filter(payment -> payment.getPayer().getEmail().equals(payerEmail))
//                .mapToDouble(Payment::getAmount)
//                .sum();
//    }
//
//    @Override
//    public Double sumPaymentsByPayeeEmail(String payeeEmail) {
//        return payments.values().stream()
//                .filter(payment -> payment.getPayee().getEmail().equals(payeeEmail))
//                .mapToDouble(Payment::getAmount)
//                .sum();
//    }
//
//    public void addPayment(Payment payment) {
//        payment.setId(currentId++);
//        payments.put(payment.getId(), payment);
//    }
//
//    @Override
//    public void flush() {
//
//    }
//
//    @Override
//    public <S extends Payment> S saveAndFlush(S entity) {
//        return null;
//    }
//
//    @Override
//    public <S extends Payment> List<S> saveAllAndFlush(Iterable<S> entities) {
//        return null;
//    }
//
//    @Override
//    public void deleteAllInBatch(Iterable<Payment> entities) {
//
//    }
//
//    @Override
//    public void deleteAllByIdInBatch(Iterable<Long> longs) {
//
//    }
//
//    @Override
//    public void deleteAllInBatch() {
//
//    }
//
//    @Override
//    public Payment getOne(Long aLong) {
//        return null;
//    }
//
//    @Override
//    public Payment getById(Long aLong) {
//        return null;
//    }
//
//    @Override
//    public Payment getReferenceById(Long aLong) {
//        return null;
//    }
//
//    @Override
//    public <S extends Payment> Optional<S> findOne(Example<S> example) {
//        return Optional.empty();
//    }
//
//    @Override
//    public <S extends Payment> List<S> findAll(Example<S> example) {
//        return null;
//    }
//
//    @Override
//    public <S extends Payment> List<S> findAll(Example<S> example, Sort sort) {
//        return null;
//    }
//
//    @Override
//    public <S extends Payment> Page<S> findAll(Example<S> example, Pageable pageable) {
//        return null;
//    }
//
//    @Override
//    public <S extends Payment> long count(Example<S> example) {
//        return 0;
//    }
//
//    @Override
//    public <S extends Payment> boolean exists(Example<S> example) {
//        return false;
//    }
//
//    @Override
//    public <S extends Payment, R> R findBy(Example<S> example,
//                                           Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
//        return null;
//    }
//
//    @Override
//    public List<Payment> findAll(Sort sort) {
//        return null;
//    }
//
//    @Override
//    public Page<Payment> findAll(Pageable pageable) {
//        return null;
//    }
//}
