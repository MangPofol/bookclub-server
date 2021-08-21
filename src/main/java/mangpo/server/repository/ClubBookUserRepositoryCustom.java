package mangpo.server.repository;

import mangpo.server.entity.Book;
import mangpo.server.entity.ClubBookUser;
import mangpo.server.entity.User;

public interface ClubBookUserRepositoryCustom {
    public ClubBookUser findByUserAndBook(User user, Book book);
}
