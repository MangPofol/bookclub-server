package mangpo.server.domain;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
public class User {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_pw")
    private String userPassword;

    @Column(name = "user_email")
    private String email;

    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;


}
