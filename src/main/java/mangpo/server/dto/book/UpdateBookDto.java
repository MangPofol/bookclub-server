package mangpo.server.dto.book;


import lombok.Data;
import mangpo.server.entity.book.Book;
import mangpo.server.entity.book.BookCategory;

@Data
public class UpdateBookDto {
    private BookCategory category;

    public Book toEntityExceptIdAndPosts(UpdateBookDto updateBookDto) {
        return Book.builder()
                .bookCategory(this.category)
                .build();
    }
}