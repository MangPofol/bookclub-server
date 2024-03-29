package mangpo.server.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mangpo.server.dto.todo.ToDoDeleteDto;
import mangpo.server.dto.todo.ToDoListCreateDto;
import mangpo.server.dto.user.ToDoDto;
import mangpo.server.entity.user.ToDo;
import mangpo.server.entity.user.User;
import mangpo.server.repository.user.ToDoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ToDoService {

    private final ToDoRepository toDoRepository;

    private final UserService userService;

    @Transactional
    public Long createToDo(ToDoDto toDoDto) {
        //여기서 조립하는게 맞는듯.
        // 1.지연로딩 때문이라도 그렇고
        // 2.컨트롤러가 지저분하면 딱 봤을때 햇갈림
        // 3. 조립할때 순서가 필요할일 있기도 함
        // 4. DTO를 넘기니까 재사용성이 좀 떨어진다고 하는데 난 잘 모르겠음
        User user = userService.findUserFromToken();

        ToDo todo = ToDo.builder()
                .user(user)
                .content(toDoDto.getContent())
                .isComplete(Boolean.FALSE)
                .build();

        ToDo save = toDoRepository.save(todo);
        return save.getId();
    }

    @Transactional
    public void updateTodo(Long toDoId, ToDoDto toDoDto) {
        ToDo toDo = findById(toDoId);
        toDo.update(toDoDto.getContent(), toDoDto.getIsComplete());
    }

    @Transactional
    public void deleteById(Long toDoId) {
        toDoRepository.deleteById(toDoId);
    }

    @Transactional
    public void createToDoList(ToDoListCreateDto toDoListCreateDto) {
        User user = userService.findUserFromToken();
        List<String> contents = toDoListCreateDto.getContents();

        for (String content : contents) {
            ToDo toDo = ToDo.builder()
                    .content(content)
                    .build();

            toDo.addUser(user);
        }
    }

    public List<ToDo> findList() {
        User user = userService.findUserFromToken();
        return toDoRepository.findListByUser(user);
    }

    public ToDo findById(Long toDoId) {
        return toDoRepository.findById(toDoId).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 todo 입니다."));
    }

    @Transactional
    public void deleteToDos(ToDoDeleteDto toDoDeleteDto) {
        User user = userService.findUserFromToken();
        List<Long> request = toDoDeleteDto.getToDoIds();

        List<ToDo> todos = user.getTodos();
        List<Long> todoIds = todos.stream()
                .map(ToDo::getId)
                .collect(Collectors.toList());

        for (Long l : request) {
            int i = todoIds.indexOf(l);
            todoIds.remove(i);
            todos.remove(i);
        }
    }
}
