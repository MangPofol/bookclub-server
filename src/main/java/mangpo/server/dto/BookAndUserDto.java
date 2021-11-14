package mangpo.server.dto;

import lombok.Data;
import mangpo.server.entity.Book;
import mangpo.server.entity.BookCategory;
import mangpo.server.entity.ClubBookUser;
import mangpo.server.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class BookAndUserDto {

    private Long userId;
    private String userNickname;

    private Long bookId;
    private String bookName;
    private String isbn;
    private BookCategory category;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public BookAndUserDto(ClubBookUser cbu) {
        User user = cbu.getUser();
        this.userId = user.getId();
        this.userNickname = user.getNickname();

        Book book = cbu.getBook();
        this.bookId = book.getId();
        this.bookName = book.getBookInfo().getName();
        this.isbn = book.getBookInfo().getIsbn();
        this.category = book.getCategory();
        this.createdDate = book.getCreatedDate();
        this.modifiedDate = book.getModifiedDate();
    }
}
