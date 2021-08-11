package mangpo.server.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
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
}
