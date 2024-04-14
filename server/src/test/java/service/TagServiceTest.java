package service;

import commons.*;
import commons.dto.TagDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.api.TestEventRepository;
import server.api.TestExpenseRepository;
import server.api.TestTagRepository;
import server.service.TagService;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TagServiceTest {

    private TagService tagService;
    private TestTagRepository testTagRepository;
    private TestEventRepository testEventRepository;
    private TestExpenseRepository testExpenseRepository;

    @BeforeEach
    void setUp() {
        testTagRepository = new TestTagRepository();
        testEventRepository = new TestEventRepository();
        testExpenseRepository = new TestExpenseRepository();
        tagService = new TagService(testTagRepository, testEventRepository, testExpenseRepository);
    }

    @Test
    void getTagsByEventIdEventNotFound() {
        assertNull(tagService.getTagsByEventId(999));
    }

    @Test
    void getTagsByEventIdEventFound1() {
        Event event = new Event();
        event.setId(1);
        testEventRepository.save(event);

        assertTrue(tagService.getTagsByEventId(1).isEmpty());
    }

    @Test
    void getTagsByEventIdEventFound() {
        Event event = new Event();
        event.setId(1);
        testEventRepository.save(event);

        testTagRepository.save(new Tag(event, "Food", "#33FF57"));
        testTagRepository.save(new Tag(event, "Drinks", "#77A9FF"));

        List<Tag> tags = tagService.getTagsByEventId(1);
        assertEquals(2, tags.size());
    }

    @Test
    void getTagsByEventIdEventFoundButNoTags(){
        Event event = new Event();
        event.setId(1);
        testEventRepository.save(event);

        assertTrue(tagService.getTagsByEventId(1).isEmpty());
    }

    @Test
    void testSetExpensesWithTag() {
        // Arrange
        Event event = new Event();
        event.setId(1);
        testEventRepository.save(event);

        Tag oldTag = new Tag(event, "OldTag", "#33FF57");
        testTagRepository.save(oldTag);

        Tag newTag = new Tag(event, "NewTag", "#77A9FF");
        testTagRepository.save(newTag);

        Expense expense1 = new Expense(new Event(), "descr", oldTag,
                new Date(), 10, new Participant(), true);

        testExpenseRepository.save(expense1);

        Expense expense2 = new Expense(new Event(), "descr", oldTag,
                new Date(), 10, new Participant(), true);
        testExpenseRepository.save(expense2);

        tagService.setExpensesWithTag(oldTag, event, newTag);

        List<Expense> updatedExpenses = testExpenseRepository.findByEvent(event);
        for (Expense expense : updatedExpenses) {
            assertEquals(newTag, expense.getTag());
        }
    }
    @Test
    void saveTagEventNotFound() {
        assertNull(tagService.saveTag(new TagDTO(1, "Food", "#33FF57")));
    }

    @Test
    void saveTagTagExists() {
        Event event = new Event();
        event.setId(1);
        testEventRepository.save(event);

        testTagRepository.save(new Tag(event, "Food", "#33FF57"));

        assertNull(tagService.saveTag(new TagDTO(1, "Food", "#33FF57")));
    }

    @Test
    void saveTagValid() {
        Event event = new Event();
        event.setId(1);
        testEventRepository.save(event);

        Tag savedTag = tagService.saveTag(new TagDTO(1, "Food", "#33FF57"));

        assertNotNull(savedTag);
        assertEquals("Food", savedTag.getName());
        assertEquals("#33FF57", savedTag.getColour());
    }

    @Test
    void deleteTagEventNotFound() {
        assertNull(tagService.deleteTag("Food", 42354));
    }

    @Test
    void deleteTagTagNotFound() {
        Event event = new Event();
        event.setId(1);
        testEventRepository.save(event);

        assertNull(tagService.deleteTag("Food", 1));
    }

    @Test
    void deleteTagValidTag() {
        Event event = new Event();
        event.setId(1);
        testEventRepository.save(event);

        Tag tag = new Tag(event, "Food", "#33FF57");
        testTagRepository.save(tag);

        assertNotNull(tagService.deleteTag("Food", 1));
    }

    @Test
    void updateTagEventNotFound() {
        assertNull(tagService.updateTag(new TagDTO(1, "Food", "#FFFFFF"), "Food", 999));
    }

    @Test
    void updateTagTagNotFound() {
        Event event = new Event();
        event.setId(1);
        testEventRepository.save(event);

        assertNull(tagService.updateTag(new TagDTO(1, "Food", "#FFFFFF"), "Food", 1));
    }

    @Test
    void updateTagNameExists() {
        Event event = new Event();
        event.setId(1);
        testEventRepository.save(event);

        testTagRepository.save(new Tag(event, "Food", "#33FF57"));
        testTagRepository.save(new Tag(event, "Drinks", "#77A9FF"));

        assertNull(tagService.updateTag(new TagDTO(1, "Drinks", "#FFFFFF"), "Food", 1));
    }

    @Test
    void updateTagChangeColour() {
        Event event = new Event();
        event.setId(1);
        testEventRepository.save(event);

        Tag foodTag = new Tag(event, "Food", "#33FF57");
        testTagRepository.save(foodTag);

        Tag updatedTag = tagService.updateTag(new TagDTO(1, "Food", "#FFFFFF"), "Food", 1);
        assertNotNull(updatedTag);
        assertEquals("#FFFFFF", updatedTag.getColour());
    }

    @Test
    void setUpTags() {
        Event event = new Event();
        event.setId(1);
        testEventRepository.save(event);

        List<Tag> tags = tagService.setUpTags(1);

        assertNotNull(tags);
        assertEquals(5, tags.size());
    }

}