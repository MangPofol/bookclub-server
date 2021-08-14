package mangpo.server.entity;

import lombok.Getter;
import mangpo.server.entity.common.BaseTimeEntity;

import javax.persistence.*;

@Entity
@Getter
public class ClubBookUser extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "club_book_user_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}