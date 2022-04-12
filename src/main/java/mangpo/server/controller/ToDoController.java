package mangpo.server.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mangpo.server.dto.*;
import mangpo.server.dto.todo.ToDoListCreateDto;
import mangpo.server.dto.todo.ToDoDeleteDto;
import mangpo.server.dto.todo.ToDoResponseDto;
import mangpo.server.dto.user.ToDoDto;
import mangpo.server.entity.user.ToDo;
import mangpo.server.service.user.ToDoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/todos")
public class ToDoController {

    private final ToDoService toDoService;

    @GetMapping
    public Result<List<ToDoResponseDto>> getToDos() {
        List<ToDo> toDos = toDoService.findList();

        List<ToDoResponseDto> collect = toDos.stream()
                .map(ToDoResponseDto::new)
                .collect(Collectors.toList());

        return new Result<>(collect);
    }

    @PostMapping
    public ResponseEntity<?> createToDo(@RequestBody ToDoDto toDoDto, UriComponentsBuilder b){
        Long toDoId = toDoService.createToDo(toDoDto);

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

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteToDo(@PathVariable Long id){
        toDoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    //복수의 리소스 생성
    @PostMapping("/create-todos")
    public ResponseEntity<?> createToDoList(@RequestBody ToDoListCreateDto toDoListCreateDto){
        toDoService.createToDoList(toDoListCreateDto);
        return ResponseEntity.noContent().build();
    }

    //복수의 리소스 삭제
    @PostMapping("/delete-todos")
    public ResponseEntity<?> deleteToDos(@RequestBody ToDoDeleteDto toDoDeleteDto){
        toDoService.deleteToDos(toDoDeleteDto);
        return ResponseEntity.noContent().build();
    }


}
