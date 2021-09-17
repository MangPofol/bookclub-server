package mangpo.server.dto;

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
    private List<LikedResponseDto> likedList;

    public BookResponseDto(Book book){
        this.id = book.getId();
        this.name = book.getName();
        this.isbn = book.getIsbn();
        this.category = book.getCategory();
        this.createdDate = book.getCreatedDate();
        this.modifiedDate = book.getModifiedDate();
        this.likedList = book.getLikedList()
                .stream()
                .map(m-> new LikedResponseDto(m.getUser().getNickname(),m.getIsLiked()))
                .collect(Collectors.toList());
    }
}