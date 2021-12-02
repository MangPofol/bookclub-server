package mangpo.server.dto.book;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mangpo.server.entity.Book;
import mangpo.server.entity.BookCategory;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateBookDto {
    private BookCategory category;

    public Book toEntityExceptIdAndPosts(UpdateBookDto updateBookDto){
        return Book.builder()
                .category(this.category)
                .build();
    }
}