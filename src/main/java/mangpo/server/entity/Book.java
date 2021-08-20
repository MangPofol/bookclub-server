package mangpo.server.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
//noargs accesslevel protected 로 변경하고 allargs 때서 리팩토링
public class Book {

    @Id @GeneratedValue
    @Column(name = "book_id")
    private Long id;

    @Column(name = "book_name")
    private String name;

    @Column(name = "book_isbn")
    private String isbn;

    @Enumerated(EnumType.STRING)
    @Column(name = "book_category")
    private BookCategory category;

    @OneToMany(mappedBy = "book",fetch = FetchType.LAZY)
    private List<Post> posts = new ArrayList<>();
}
