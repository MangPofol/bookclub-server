package mangpo.server.entity.club;

import lombok.*;
import mangpo.server.entity.common.BaseTimeEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Club extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "club_name")
    private String name;

    @Column(name = "club_level")
    private Integer level;

    @Column(name = "club_president")
    private Long presidentId;

    @Column(name = "club_description")
    private String description;

    private LocalDateTime lastAddBookDate;

    public void update(Club request) {
        if (request.getName() != null)
            this.name = request.name;
        if (request.getLevel() != null)
            this.level = request.level;
        if (request.getPresidentId() != null)
            this.presidentId = request.presidentId;
        if (request.getDescription() != null)
            this.description = request.description;
    }

    public void updateLastAddBookDate() {
        this.lastAddBookDate = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Club club = (Club) o;
        return Objects.equals(id, club.id) &&
                Objects.equals(name, club.name) &&
                Objects.equals(level, club.level) &&
                Objects.equals(presidentId, club.presidentId) &&
                Objects.equals(description, club.description) &&
                Objects.equals(lastAddBookDate, club.lastAddBookDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, level, presidentId, description, lastAddBookDate);
    }
}
