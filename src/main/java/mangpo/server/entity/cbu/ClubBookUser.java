package mangpo.server.entity.cbu;

import lombok.*;
import mangpo.server.entity.book.Book;
import mangpo.server.entity.club.Club;
import mangpo.server.entity.common.BaseTimeEntity;
import mangpo.server.entity.user.User;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
