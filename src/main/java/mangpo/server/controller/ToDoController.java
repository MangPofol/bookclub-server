package mangpo.server.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mangpo.server.dto.*;
import mangpo.server.entity.ToDo;
import mangpo.server.entity.User;
import mangpo.server.service.ToDoService;
import mangpo.server.service.UserService;
import mangpo.server.session.SessionConst;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public Result<List<ToDoResponseDto>> getToDos(@SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser) {
        List<ToDo> toDos = toDoService.findToDos(loginUser);

        List<ToDoResponseDto> collect = toDos.stream()
                .map(ToDoResponseDto::new)
                .collect(Collectors.toList());

        return new Result<>(collect);
    }

    //복수의 리소스 생성
    @PostMapping("/create-todos")
    public ResponseEntity<?> createToDos(@SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser , @RequestBody ToDoCreateDto toDoCreateDto){
        User initProxy = userService.findById(loginUser.getId());
        toDoService.createToDos(initProxy, toDoCreateDto);

        return ResponseEntity.noContent().build();
    }
    //복수의 리소스 삭제
    @PostMapping("/delete-todos")
    public ResponseEntity<?> deleteToDos(@SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser , @RequestBody ToDoDeleteDto toDoDeleteDto){
        User initProxy = userService.findById(loginUser.getId());
        toDoService.deleteToDos(initProxy,toDoDeleteDto);

        return ResponseEntity.noContent().build();
    }
}
