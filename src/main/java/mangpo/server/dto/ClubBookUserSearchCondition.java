package mangpo.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mangpo.server.entity.book.Book;
import mangpo.server.entity.club.Club;
import mangpo.server.entity.cbu.ClubBookUser;
import mangpo.server.entity.user.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClubBookUserSearchCondition {

    private Club club;
    private Book book;
    private User user;

    public ClubBookUserSearchCondition(ClubBookUser clubBookUser){
        this.club = clubBookUser.getClub();
        this.book = clubBookUser.getBook();
        this.user = clubBookUser.getUser();
    }
}
