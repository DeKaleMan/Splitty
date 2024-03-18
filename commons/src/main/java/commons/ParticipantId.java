package commons;

import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ParticipantId implements Serializable {

    private String email;

    @ManyToOne
    private Event event;

    public ParticipantId() {}

    public ParticipantId(String email, Event event) {
        this.email = email;
        this.event = event;
    }

    // Getters and setters

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
        if (!(o instanceof ParticipantId)) return false;
        ParticipantId that = (ParticipantId) o;
        return Objects.equals(getEmail(), that.getEmail()) &&
                Objects.equals(getEvent(), that.getEvent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmail(), getEvent());
    }
}
