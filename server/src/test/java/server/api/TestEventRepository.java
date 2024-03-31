package server.api;

import commons.Event;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.EventRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class TestEventRepository implements EventRepository {

    public List<Event> events = new ArrayList<>();
    public List<String> methods = new ArrayList<>();

    @Override
    public <S extends Event> S save(S entity) {
        events.add(entity);
        methods.add("save");
        return entity;
    }

    @Override
    public Optional<Event> findById(Integer integer) {
        methods.add("findById");
        return events.stream().filter(x -> x.id == integer).findFirst();
    }

    @Override
    public List<Event> findAll() {
        methods.add("findAll");
        return events;
    }


    @Override
    public void flush() {
        
    }

    @Override
    public <S extends Event> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Event> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Event> entities) {

    }
    @Override
    public <S extends Event, R> R findBy(Example<S> example,
                                         Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
    @Override
    public void deleteAllByIdInBatch(Iterable<Integer> integers) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Event getOne(Integer integer) {
        return null;
    }

    @Override
    public Event getById(Integer integer) {
        return null;
    }

    @Override
    public Event getReferenceById(Integer integer) {
        return null;
    }

    @Override
    public <S extends Event> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Event> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Event> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Event> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Event> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Event> boolean exists(Example<S> example) {
        return false;
    }


    @Override
    public <S extends Event> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public boolean existsById(Integer integer) {
        return false;
    }


    @Override
    public List<Event> findAllById(Iterable<Integer> integers) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Integer integer) {

    }

    @Override
    public void delete(Event entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Integer> integers) {

    }

    @Override
    public void deleteAll(Iterable<? extends Event> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<Event> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Event> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public Event findEventById(int eventId) {
        return events.stream().filter(x -> x.id == eventId).findFirst().orElse(null);
    }

    @Override
    public Event findByInviteCode(String code) {
        return events.stream().filter(x -> x.getInviteCode().equals(code)).findFirst().orElse(null);
    }
}
