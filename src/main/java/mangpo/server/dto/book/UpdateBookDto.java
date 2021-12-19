package mangpo.server.dto.book;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mangpo.server.entity.book.Book;
import mangpo.server.entity.book.BookCategory;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateBookDto {
    private BookCategory category;

    public Book toEntityExceptIdAndPosts(UpdateBookDto updateBookDto){
        return Book.builder()
                .bookCategory(this.category)
                .build();
    }
}