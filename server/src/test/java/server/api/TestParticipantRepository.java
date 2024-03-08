//package server.api;
//
//import org.springframework.data.domain.Example;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.repository.query.FluentQuery;
//import server.database.ParticipantRepository;
//import commons.Participant;
//
//import java.util.*;
//import java.util.function.Function;
//import java.util.stream.Collectors;
//
//public class TestParticipantRepository implements ParticipantRepository {
//
//    private final Map<String, Participant> participants = new HashMap<>();
//
//    @Override
//    public <S extends Participant> S save(S entity) {
//        return null;
//    }
//
//    @Override
//    public <S extends Participant> List<S> saveAll(Iterable<S> entities) {
//        return null;
//    }
//
//    @Override
//    public Optional<Participant> findById(Long aLong) {
//        return Optional.empty();
//    }
//
//    @Override
//    public boolean existsById(Long aLong) {
//        return false;
//    }
//
//    @Override
//    public List<Participant> findAll() {
//        return new ArrayList<>(participants.values());
//    }
//
//    @Override
//    public List<Participant> findAllById(Iterable<Long> longs) {
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
//    public void delete(Participant entity) {
//
//    }
//
//    @Override
//    public void deleteAllById(Iterable<? extends Long> longs) {
//
//    }
//
//    @Override
//    public void deleteAll(Iterable<? extends Participant> entities) {
//
//    }
//
//    @Override
//    public void deleteAll() {
//
//    }
//
//    @Override
//    public Participant findByEmail(String email) {
//        return participants.get(email);
//    }
//
//    @Override
//    public List<Participant> findByName(String name) {
//        return participants.values().stream()
//                .filter(participant -> participant.getName().equals(name))
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public List<Participant> findByAccountHolder(String accountHolder) {
//        return participants.values().stream()
//                .filter(participant -> participant.getAccountHolder().equals(accountHolder))
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public void deleteByEmail(String email) {
//        participants.remove(email);
//    }
//
//    public void addParticipant(Participant participant) {
//        participants.put(participant.getEmail(), participant);
//    }
//
//    @Override
//    public void flush() {
//
//    }
//
//    @Override
//    public <S extends Participant> S saveAndFlush(S entity) {
//        return null;
//    }
//
//    @Override
//    public <S extends Participant> List<S> saveAllAndFlush(Iterable<S> entities) {
//        return null;
//    }
//
//    @Override
//    public void deleteAllInBatch(Iterable<Participant> entities) {
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
//    public Participant getOne(Long aLong) {
//        return null;
//    }
//
//    @Override
//    public Participant getById(Long aLong) {
//        return null;
//    }
//
//    @Override
//    public Participant getReferenceById(Long aLong) {
//        return null;
//    }
//
//    @Override
//    public <S extends Participant> Optional<S> findOne(Example<S> example) {
//        return Optional.empty();
//    }
//
//    @Override
//    public <S extends Participant> List<S> findAll(Example<S> example) {
//        return null;
//    }
//
//    @Override
//    public <S extends Participant> List<S> findAll(Example<S> example, Sort sort) {
//        return null;
//    }
//
//    @Override
//    public <S extends Participant> Page<S> findAll(Example<S> example, Pageable pageable) {
//        return null;
//    }
//
//    @Override
//    public <S extends Participant> long count(Example<S> example) {
//        return 0;
//    }
//
//    @Override
//    public <S extends Participant> boolean exists(Example<S> example) {
//        return false;
//    }
//
//    @Override
//    public <S extends Participant, R> R findBy(Example<S> example,
//                                               Function<FluentQuery.FetchableFluentQuery<S>,
//                                                       R> queryFunction) {
//        return null;
//    }
//
//    @Override
//    public List<Participant> findAll(Sort sort) {
//        return null;
//    }
//
//    @Override
//    public Page<Participant> findAll(Pageable pageable) {
//        return null;
//    }
//}
