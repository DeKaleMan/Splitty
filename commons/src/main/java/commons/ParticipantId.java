package commons;

import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ParticipantId implements Serializable {

//    @Column(length = 36) - for now to simplify testing length won't be enforced,
//    in the future that has to change though
    private String uuid;

    @ManyToOne
    private Event event;

    public ParticipantId() {}

    public ParticipantId(String uuid, Event event) {
        this.uuid = uuid;
        this.event = event;
    }

    // Getters and setters

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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
        return Objects.equals(getUuid(), that.getUuid()) &&
                Objects.equals(getEvent(), that.getEvent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUuid(), getEvent());
    }

    @Override
    public String toString() {
        return "ParticipantId{" +
                "uuid='" + uuid + '\'' +
                ", event=" + event +
                '}';
    }
}
