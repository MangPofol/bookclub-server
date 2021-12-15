package mangpo.server.dto;

import lombok.Data;

@Data
public class ToDoDto {

    private String content;
    private Boolean isComplete;
}
