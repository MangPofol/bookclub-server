package mangpo.server.dto.book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mangpo.server.entity.BookCategory;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookDto {
    private String name;
    private String isbn;
    private BookCategory category;

}
