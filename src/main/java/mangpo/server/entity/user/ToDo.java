package mangpo.server.entity.user;

import lombok.*;
import mangpo.server.entity.common.BaseTimeEntity;
import mangpo.server.entity.user.User;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ToDo extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "todo_id")
    private Long id;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String content;
    private Boolean isComplete;

    public void addUser(User user) {
        if(this.user != null)
            this.user.getTodos().remove(this);

        this.user = user;
        user.getTodos().add(this);
    }

    public void update(String content, Boolean isComplete){
        this.content = content;
        this.isComplete = isComplete;
    }

}
