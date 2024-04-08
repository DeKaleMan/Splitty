package commons;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;

import java.util.Objects;

@Entity
public class Tag {
    @EmbeddedId
    private TagId tagId;

    private String colour;

    public Tag(Event event, String name, String colour) {
        this.tagId = new TagId(name, event);
        this.colour = colour;
    }
    public Tag() {
    }

    public TagId getTagId() {
        return tagId;
    }

    public void setTagId(TagId tagId) {
        this.tagId = tagId;
    }

    public String getName() {
        if (tagId == null) {
            tagId = new TagId();
        }
        return tagId.getName();
    }
    public void setName(String name) {
        if (tagId == null) {
            tagId = new TagId();
        }
        tagId.setName(name);
    }
    public Event getEvent() {
        if (tagId == null) {
            tagId = new TagId();
        }
        return tagId.getEvent();
    }
    public void setEvent(Event event) {
        if (tagId == null) {
            tagId = new TagId();
        }
        tagId.setEvent(event);
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tag tag)) return false;
        return Objects.equals(tagId, tag.tagId);
    }

    @Override
    public int hashCode() {
        return tagId.hashCode();
    }
}
