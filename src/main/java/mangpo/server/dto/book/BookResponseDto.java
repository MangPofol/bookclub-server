package mangpo.server.dto.book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mangpo.server.controller.BookController;
import mangpo.server.entity.Book;
import mangpo.server.entity.BookCategory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookResponseDto {
    private Long id;
    private String name;
    private String isbn;
    private BookCategory category;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public BookResponseDto(Book book) {
        this.id = book.getId();
        this.category = book.getCategory();
        this.createdDate = book.getCreatedDate();
        this.modifiedDate = book.getModifiedDate();
        this.name = book.getBookInfo().getName();
        this.isbn = book.getBookInfo().getIsbn();
    }
}