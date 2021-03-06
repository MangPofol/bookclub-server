package mangpo.server.dto.todo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ToDoDeleteDto {

    private List<Long> toDoIds;
}