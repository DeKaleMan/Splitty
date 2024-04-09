package commons;

import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class TagId implements Serializable {
    private String name;

    @ManyToOne
    private Event event;

    public TagId(String name, Event event) {
        this.name = name;
        this.event = event;
    }
    public TagId() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TagId tagId)) return false;
        return Objects.equals(name, tagId.name) && Objects.equals(event, tagId.event);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, event);
    }
}
