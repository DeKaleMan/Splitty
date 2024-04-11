package server.service;

import commons.Event;
import commons.Expense;
import commons.Tag;
import commons.TagId;
import commons.dto.TagDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.database.EventRepository;
import server.database.ExpenseRepository;
import server.database.TagRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagService {

    private final TagRepository tagRepository;
    private final EventRepository eventRepository;
    private final ExpenseRepository expenseRepository;

    @Autowired
    public TagService(TagRepository tagRepository, EventRepository eventRepository,
                      ExpenseRepository expenseRepository) {
        this.tagRepository = tagRepository;
        this.eventRepository = eventRepository;
        this.expenseRepository = expenseRepository;
    }

    /**
     * gets the tags which belong to an event
     * @param eventId the id of the event where the tags belong to
     * @return the list of tags
     */
    public List<Tag> getTagsByEventId(int eventId) {
        Event event = eventRepository.findEventById(eventId);
        if (event == null) {
            return null;
        }
        List<Tag> tags = tagRepository.findTagsByEventId(eventId);
        if (tags == null) {
            tags = new ArrayList<>();
        }
        return tags;
    }

    public Tag saveTag(TagDTO tagDTO) {
        Event event = eventRepository.findEventById(tagDTO.getEventId());
        if (event == null || tagDTO.getName() == null ||
                tagDTO.getName().isEmpty()) return null;
        Tag existingTag = tagRepository.findTagByTagId(new TagId(tagDTO.getName(), event));
        if (existingTag != null) {
            return null;
        }
        Tag tag = new Tag(event, tagDTO.getName(), tagDTO.getColour());
        tagRepository.save(tag);
        return tag;
    }

    public Tag deleteTag(String name, int eventId) {
        Event event = eventRepository.findEventById(eventId);
        if (event == null) return null;
        Tag toDelete = tagRepository.findTagByTagId(new TagId(name, event));
        if (toDelete == null) {
            return  null;
        }
        setExpensesWithTag(toDelete, event, getOtherTag(eventId));
        try {
            tagRepository.delete(toDelete);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return toDelete;
    }

    private void setExpensesWithTag(Tag toDelete, Event event, Tag newTag) {
        List<Expense> expenses = expenseRepository.findByEvent(event);
        expenses = expenses.stream().filter(expense -> expense.getTag().equals(toDelete)).toList();
        for (Expense expense : expenses) {
            expense.setTag(newTag);
        }
        expenseRepository.saveAll(expenses);
    }

    public Tag updateTag(TagDTO tagDTO, String name, int eventId) {
        Event event = eventRepository.findEventById(eventId);
        if (event == null) {
            return null;
        }
        Tag toUpdate = tagRepository.findTagByTagId(new TagId(name, event));
        if (toUpdate == null) {
            return null;
        }
        if (toUpdate.getName().equals(tagDTO.getName())) {
            return changeColour(tagDTO, toUpdate);
        }
        Tag checkIfExists = tagRepository.findTagByTagId(new TagId(tagDTO.getName(), event));
        if (checkIfExists != null) {
            return null;
        }
        Tag newTag = new Tag(event, tagDTO.getName(), tagDTO.getColour());
        tagRepository.save(newTag);
        setExpensesWithTag(toUpdate, event, newTag);
        try {
            tagRepository.delete(toUpdate);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return newTag;
    }

    private Tag changeColour(TagDTO tagDTO, Tag toUpdate) {
        toUpdate.setColour(tagDTO.getColour());
        return tagRepository.save(toUpdate);
    }

    public List<Tag> setUpTags(int eventId) {
        Tag food = saveTag(new TagDTO(eventId, "Food", "#33FF57"));
        Tag entranceFees = saveTag(new TagDTO(eventId, "Entrance Fees", "#3356FF"));
        Tag travel = saveTag(new TagDTO(eventId, "Travel", "#CC0E25"));
        Tag drinks = saveTag(new TagDTO(eventId, "Drinks", "#77A9FF"));
        Tag other = saveTag(new TagDTO(eventId, "Other", "#666666"));
        if (food == null || entranceFees == null || travel == null || drinks == null || other == null) {
            return null;
        }
        return List.of(food, entranceFees, travel, drinks, other);
    }

    public Tag getOtherTag(int eventId) {
        Event event = eventRepository.findEventById(eventId);
        if (event == null) return null;
        return tagRepository.findTagByTagId(new TagId("Other", event));
    }
}
