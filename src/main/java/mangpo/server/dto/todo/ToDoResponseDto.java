package mangpo.server.dto.todo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mangpo.server.entity.ToDo;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ToDoResponseDto {

    private Long ToDoId;
    private String content;
    private Boolean isComplete;
    private LocalDateTime createDate;
    private LocalDateTime modifiedDate;

    public ToDoResponseDto(ToDo toDo){
        this.ToDoId = toDo.getId();
        this.content = toDo.getContent();
        this.isComplete = toDo.getIsComplete();
        this.createDate = toDo.getCreatedDate();
        this.modifiedDate = toDo.getModifiedDate();
    }
}
