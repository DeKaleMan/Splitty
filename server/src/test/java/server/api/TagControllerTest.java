package server.api;

import commons.Tag;
import commons.dto.TagDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import server.api.TagController;
import server.service.TagService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class TagControllerTest {

    @Mock
    private TagService tagService;

    private TagController tagController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        tagController = new TagController(tagService);
    }

    @Test
    void getByEventIdTagsExist() {
        List<Tag> tags = Arrays.asList(new Tag(), new Tag());
        when(tagService.getTagsByEventId(1)).thenReturn(tags);
        ResponseEntity<List<Tag>> response = tagController.getByEventId(1);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void getByEventIdNoTags() {
        when(tagService.getTagsByEventId(1)).thenReturn(null);
        ResponseEntity<List<Tag>> response = tagController.getByEventId(1);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void saveTagValidTag() {
        TagDTO tagDTO = new TagDTO(1, "Food", "#FF0000");
        Tag tag = new Tag();
        when(tagService.saveTag(tagDTO)).thenReturn(tag);
        ResponseEntity<Tag> response = tagController.saveTag(tagDTO);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void saveTagInvalidTag() {
        TagDTO tagDTO = new TagDTO();


        when(tagService.saveTag(tagDTO)).thenReturn(null);
        ResponseEntity<Tag> response = tagController.saveTag(tagDTO);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void deleteTagFail() {
        ResponseEntity<Tag> response = tagController.deleteTag("Other", 1);


        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void deleteTagOk() {
        Tag tag = new Tag();

        when(tagService.deleteTag("Food", 1)).thenReturn(tag);

        ResponseEntity<Tag> response = tagController.deleteTag("Food", 1);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void deleteTagNotFound() {
        when(tagService.deleteTag("Food", 1)).thenReturn(null);
        ResponseEntity<Tag> response = tagController.deleteTag("Food", 1);
        assertEquals(404, response.getStatusCodeValue());


    }

    @Test
    void updateTagValid() {
        TagDTO tagDTO = new TagDTO(1, "Food", "#FF0000");
        Tag updatedTag = new Tag();

        when(tagService.updateTag(tagDTO, "Food", 1)).thenReturn(updatedTag);
        ResponseEntity<Tag> response = tagController.updateTag("Food", 1, tagDTO);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void updateTagFail() {
        TagDTO tagDTO = new TagDTO(1, "Food", "#FF0000");
        when(tagService.updateTag(tagDTO, "Food", 1)).thenReturn(null);
        ResponseEntity<Tag> response = tagController.updateTag("Food", 1, tagDTO);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void setupTagsSuccess() {
        List<Tag> tags = Arrays.asList(new Tag(), new Tag());
        when(tagService.setUpTags(1)).thenReturn(tags);
        ResponseEntity<List<Tag>> response = tagController.setupTags(1);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void setupTagsFailed() {
        when(tagService.setUpTags(1)).thenReturn(null);
        ResponseEntity<List<Tag>> response = tagController.setupTags(1);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void getOtherTagFound() {
        Tag tag = new Tag();

        when(tagService.getOtherTag(1)).thenReturn(tag);

        ResponseEntity<Tag> response = tagController.getOtherTag(1);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void getOtherTagNotFound() {
        when(tagService.getOtherTag(1)).thenReturn(null);

        ResponseEntity<Tag> response = tagController.getOtherTag(1);


        assertEquals(400, response.getStatusCodeValue());
    }
}
