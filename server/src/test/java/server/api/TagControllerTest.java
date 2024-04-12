package server.api;

import commons.Event;
import commons.Tag;
import commons.TagId;
import commons.dto.TagDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.service.TagService;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TagControllerTest {
    TagController sut;
    private TestTagRepository tagRepo;
    private TestEventRepository eventRepo;
    private TestExpenseRepository expenseRepo;

    Event e1 = new Event("test1",new Date(10, 10, 2005),"owner","desc");
    Event e2 = new Event("test2",new Date(15, 10, 2015),"owner","desc");
    TagDTO t1 = new TagDTO(1, "Food", "2a8000");
    TagDTO t2 = new TagDTO(1, "Travel", "3700ff");
    TagDTO t3 = new TagDTO(2, "Entrance Fees", "c50000");
    @BeforeEach
    public void setup() {
        tagRepo = new TestTagRepository();
        eventRepo = new TestEventRepository();
        expenseRepo = new TestExpenseRepository();
        TagService service = new TagService(tagRepo, eventRepo, expenseRepo);
        sut = new TagController(service);
        e1.setId(1);
        e2.setId(2);
        eventRepo.save(e1);
        eventRepo.save(e2);
    }
    @Test
    public void postTestInvalid() {
        t1.setName(null);
        ResponseEntity<Tag> response = sut.saveTag(t1);
        assertEquals(0,tagRepo.methods.size());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
    @Test
    public void postTestInvalidEvent() {
        t1.setEventId(-1);
        ResponseEntity<Tag> response = sut.saveTag(t1);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void postTestValid() {
        ResponseEntity<Tag> response = sut.saveTag(t1);
        assertEquals("save", tagRepo.methods.getLast());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, tagRepo.tags.size());
        Tag tag = new Tag(e1, t1.getName(), t1.getColour());
        assertEquals(tag, tagRepo.findTagByTagId(new TagId(t1.getName(), e1)));
    }
    @Test
    public void putTestValid() {
        ResponseEntity<Tag> response = sut.saveTag(t2);
        assertEquals("save", eventRepo.methods.getLast());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        TagDTO temp = new TagDTO(t2.getEventId(), "New Name", "New Colour");
        ResponseEntity<Tag> response2 = sut.updateTag(t2.getName(), t2.getEventId(), temp);
        assertEquals(HttpStatus.OK, response2.getStatusCode());
        assertEquals("delete", tagRepo.methods.getLast());
        assertEquals(response2.getBody(), tagRepo.findTagByTagId(new TagId("New Name", e1)));
        assertEquals("find tag by id", tagRepo.methods.getLast());
    }
    @Test
    public void putTestInvalid() {
        sut.saveTag(t1);
        sut.saveTag(t2);
        sut.saveTag(t3);
        t3.setEventId(e1.getId());
        TagDTO temp = new TagDTO(t3.getEventId(), "Food", "Random Colour");
        ResponseEntity<Tag> response = sut.updateTag(t3.getName(), t3.getEventId(), temp);
        assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());
        assertEquals(t3.getName(),tagRepo.findTagByTagId(new TagId(t3.getName(), e2)).getName());
    }
    @Test
    public void deleteTestValid() {
        sut.saveTag(t1);
        sut.saveTag(t2);
        sut.saveTag(t3);
        assertEquals(3,tagRepo.tags.size());
        sut.deleteTag(t1.getName(), t1.getEventId());
        assertEquals(2,tagRepo.tags.size());
        ResponseEntity<Tag> deleted = sut.deleteTag(t2.getName(), t2.getEventId());
        Tag temp = new Tag(eventRepo.findEventById(t2.getEventId()), t2.getName(), t2.getColour());
        assertEquals(deleted.getBody(),temp);
    }

}
