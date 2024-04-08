package server.api;

import commons.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.service.TagService;

import java.util.List;

@RestController
@RequestMapping("/api/tag")
public class TagController {
    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping("/getAll/{eventId}")
    public ResponseEntity<List<Tag>> getByEventId(@PathVariable int eventId) {
        List<Tag> tags = tagService.getTagsByEventId(eventId);
        if (tags == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(tags);
    }
}
