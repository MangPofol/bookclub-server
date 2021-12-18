package mangpo.server.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mangpo.server.dto.*;
import mangpo.server.dto.todo.ToDoListCreateDto;
import mangpo.server.dto.todo.ToDoDeleteDto;
import mangpo.server.dto.todo.ToDoResponseDto;
import mangpo.server.entity.ToDo;
import mangpo.server.entity.User;
import mangpo.server.service.user.ToDoService;
import mangpo.server.service.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/todos")
public class ToDoController {

    private final ToDoService toDoService;
    private final UserService userService;

    @GetMapping
    public Result<List<ToDoResponseDto>> getToDos() {
        User user = userService.findUserFromToken();

        List<ToDo> toDos = toDoService.findToDos(user);

        List<ToDoResponseDto> collect = toDos.stream()
                .map(ToDoResponseDto::new)
                .collect(Collectors.toList());

        return new Result<>(collect);
    }

    @PostMapping
    public ResponseEntity<?> createToDo(@RequestBody ToDoDto toDoDto, UriComponentsBuilder b){
        User user = userService.findUserFromToken();
        Long toDoId = toDoService.createToDo(user, toDoDto);

        UriComponents uriComponents =
                b.path("/todos/{toDoId}").buildAndExpand(toDoId);

        ToDoResponseDto toDoResponseDto = new ToDoResponseDto(toDoService.findById(toDoId));
        return ResponseEntity.created(uriComponents.toUri()).body(toDoResponseDto);
    }

    @PutMapping("/{toDoId}")
    public ResponseEntity<?> updateTodo(@PathVariable Long toDoId, @RequestBody ToDoDto toDoDto){
        toDoService.updateTodo(toDoId,toDoDto);

        return ResponseEntity.noContent().build();
    }

    //복수의 리소스 생성
    @PostMapping("/create-todos")
    public ResponseEntity<?> createToDoList(@RequestBody ToDoListCreateDto toDoListCreateDto){
        User user = userService.findUserFromToken();

        toDoService.createToDoList(user, toDoListCreateDto);

        return ResponseEntity.noContent().build();
    }

    //복수의 리소스 삭제
    @PostMapping("/delete-todos")
    public ResponseEntity<?> deleteToDos(@RequestBody ToDoDeleteDto toDoDeleteDto){
        User user = userService.findUserFromToken();

        toDoService.deleteToDos(user,toDoDeleteDto);

        return ResponseEntity.noContent().build();
    }
}
