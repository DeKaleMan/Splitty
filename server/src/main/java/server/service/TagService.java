package server.service;

import commons.Event;
import commons.Tag;
import commons.TagId;
import commons.dto.TagDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.database.EventRepository;
import server.database.TagRepository;

import java.util.List;

@Service
public class TagService {

    private final TagRepository tagRepository;
    private final EventRepository eventRepository;

    @Autowired
    public TagService(TagRepository tagRepository, EventRepository eventRepository) {
        this.tagRepository = tagRepository;
        this.eventRepository = eventRepository;
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
        List<Tag> tags = tagRepository.findAll();
        tags = tags.stream().filter(tag -> event.equals(tag.getEvent())).toList();
        return tags;
    }

    public Tag saveTag(TagDTO tagDTO) {
        Event event = eventRepository.findEventById(tagDTO.getEventId());
        if (event == null) return null;
        Tag existingTag = tagRepository.findTagByTagIdIs(new TagId(tagDTO.getName(), event));
        if (existingTag != null) {
            return null;
        }
        Tag tag = new Tag(event, tagDTO.getName(), tagDTO.getColour());
        tagRepository.save(tag);
        return tag;
    }

    public Tag deleteTag(String name, int eventId) {
        Event event = eventRepository.findEventById(eventId);
        Tag toDelete = tagRepository.findTagByTagIdIs(new TagId(name, event));
        if (toDelete == null) {
            return  null;
        }
        tagRepository.delete(toDelete);
        return toDelete;
    }
}
