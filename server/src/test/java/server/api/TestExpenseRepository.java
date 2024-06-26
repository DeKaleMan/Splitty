package server.api;

import commons.*;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.ExpenseRepository;

import java.util.*;
import java.util.function.Function;

public class TestExpenseRepository implements ExpenseRepository {
    public List<Expense> expenses = new ArrayList<>();
    public List<String> methods = new ArrayList<>();


    @Override
    public List<Expense> findByEvent(Event event) {
        methods.add("findByEvent");
        return expenses
            .stream()
            .filter(x -> x.getEvent().equals(event))
            .toList();
    }

    @Override
    public List<Expense> findByEventAndPayer(Event event, Participant payer) {
        methods.add("findByEventAndPayerUuid");
        return expenses
            .stream()
            .filter(x -> x.getEvent().equals(event) && x.getPayer().equals(payer))
            .toList();
    }

    @Override
    public Double findTotalAmountByTag(int eventId, String tagName) {
        List<Tag> tags = expenses.stream().
                filter(expense -> expense.getEvent().id == eventId)
                .map(Expense::getTag).toList();
        Tag t = tags.stream().filter(tag -> tag.getName().equals(tagName)).toList().getFirst();
        return expenses.stream().filter(expense -> expense.getTag().equals(t))
                .mapToDouble(Expense::getTotalExpense)
                .sum();
    }


    @Override
    public List<Expense> deleteExpenseByEventIdAndExpenseId(int eventID, int expenseID) {
        methods.add("deleteExpense");
        expenses
            .stream()
            .filter(x -> x.getExpenseId() == expenseID && x.getEvent().id == eventID)
            .forEach(x -> expenses.remove(x));

        return expenses;
    }

    @Override
    public double getTotalCostByEvent(int eventID) {
        double res = 0;
        for(Expense e: expenses.stream().filter(e -> eventID == e.getEvent().id).toList()){
            res += e.getTotalExpense();
        }
        return res;
    }


    @Override
    public <S extends Expense> S save(S entity) {
        expenses.removeIf(x -> x.getExpenseId() == entity.getExpenseId() && x.getEvent().id == entity.getEvent().id);
        methods.add("save");
        expenses.add(entity);
        return entity;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Expense> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Expense> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Expense> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<ExpenseId> expenseIds) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Expense getOne(ExpenseId expenseId) {
        return null;
    }

    @Override
    public Expense getById(ExpenseId expenseId) {
        return null;
    }

    @Override
    public Expense getReferenceById(ExpenseId expenseId) {
        return null;
    }

    @Override
    public <S extends Expense> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Expense> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Expense> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Expense> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Expense> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Expense> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Expense, R> R findBy(Example<S> example,
                                           Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }


    @Override
    public <S extends Expense> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    //to change
    @Override
    public Optional<Expense> findById(ExpenseId expenseId) {
        methods.add("findById");
        return expenses.stream().filter(x -> x.getEvent().equals(expenseId.getEvent()) &&
            x.getExpenseId() == expenseId.getExpenseId()).findFirst();
    }

    @Override
    public boolean existsById(ExpenseId expenseId) {
        return false;
    }

    @Override
    public List<Expense> findAll() {
        return null;
    }

    @Override
    public List<Expense> findAllById(Iterable<ExpenseId> expenseIds) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(ExpenseId expenseId) {

    }

    @Override
    public void delete(Expense entity) {
        expenses.removeIf(x -> Objects.equals(x,entity));
        methods.add("delete");
    }

    @Override
    public void deleteAllById(Iterable<? extends ExpenseId> expenseIds) {

    }

    @Override
    public void deleteAll(Iterable<? extends Expense> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<Expense> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Expense> findAll(Pageable pageable) {
        return null;
    }


}
