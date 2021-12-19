package mangpo.server.entity.book;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookInfo {

    @Id
    @GeneratedValue
    @Column(name = "book_info_id")
    private Long id;

    @Column(name = "book_info_name")
    private String name;

    @Column(name = "book_info_isbn")
    private String isbn;
}
