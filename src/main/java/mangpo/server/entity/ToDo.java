package mangpo.server.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mangpo.server.entity.common.BaseTimeEntity;
import org.joda.time.base.BaseDateTime;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
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
