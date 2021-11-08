package mangpo.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mangpo.server.entity.ToDo;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ToDoResponseDto {

    private Long ToDoId;
    private String content;

    public ToDoResponseDto(ToDo toDo){
        this.ToDoId = toDo.getId();
        this.content = toDo.getContent();
    }
}
