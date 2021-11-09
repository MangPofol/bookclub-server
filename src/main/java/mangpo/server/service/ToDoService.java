package mangpo.server.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mangpo.server.dto.ToDoCreateDto;
import mangpo.server.dto.ToDoDeleteDto;
import mangpo.server.entity.ToDo;
import mangpo.server.entity.User;
import mangpo.server.repository.ToDoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ToDoService {

    private final ToDoRepository toDoRepository;

    @Transactional
    public void createToDos(User user, ToDoCreateDto toDoCreateDto){
        List<String> contents = toDoCreateDto.getContents();

        for (String content : contents) {
            ToDo toDo = ToDo.builder()
                    .content(content)
                    .build();

            toDo.addUser(user);
        }
    }

    public List<ToDo> findToDos(User user){
        return toDoRepository.findByUser(user);
    }

    @Transactional
    public void deleteToDos(User user, ToDoDeleteDto toDoDeleteDto){
        List<Long> request = toDoDeleteDto.getToDoIds();

        List<ToDo> todos = user.getTodos();
        List<Long> todoIds = todos.stream()
                .map(m -> m.getId())
                .collect(Collectors.toList());

        for (Long l : request) {
            int i = todoIds.indexOf(l);
            todoIds.remove(i);
            todos.remove(i);
        }
    }
}
