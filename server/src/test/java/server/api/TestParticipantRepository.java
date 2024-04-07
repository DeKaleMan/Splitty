package server.api;

import commons.Event;
import commons.ParticipantId;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.ParticipantRepository;
import commons.Participant;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TestParticipantRepository implements ParticipantRepository {
    public final List<Participant> participants = new ArrayList<>();
    public final List<String> methods = new ArrayList<>();


    @Override
    public List<Participant> findByName(String name) {
        methods.add("findByName");
        return participants.stream().filter(x -> x.getName().equals(name)).collect(Collectors.toList());
    }

    @Override
    public Participant findById(ParticipantId id) {
        methods.add("findById");
        return participants.stream().filter(x -> x.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public List<Participant> findByEventId(int eventId) {
        methods.add("findByEventId");
        return participants.stream().filter(
                x -> x.getEvent().getId() == eventId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Event> findEventsByParticipant(String uuid) {
        methods.add("findEventsByParticipant");
        return participants.stream().filter(
                x -> x.getUuid().equals(uuid))
                .map(Participant::getEvent)
                .collect(Collectors.toList());
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Participant> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Participant> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Participant> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Participant getOne(Long aLong) {
        return null;
    }

    @Override
    public Participant getById(Long aLong) {
        return null;
    }

    @Override
    public Participant getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends Participant> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Participant> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Participant> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Participant> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Participant> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Participant> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Participant, R> R findBy(Example<S> example,
                                               Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends Participant> S save(S entity) {
        participants.add(entity);
        methods.add("save");
        return entity;
    }

    @Override
    public <S extends Participant> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Participant> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public List<Participant> findAll() {
        return participants;
    }

    @Override
    public List<Participant> findAllById(Iterable<Long> longs) {
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
    public void delete(Participant entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Participant> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<Participant> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Participant> findAll(Pageable pageable) {
        return null;
    }
}
