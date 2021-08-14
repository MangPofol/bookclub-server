package mangpo.server.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import mangpo.server.dto.UserRequestDto;
import mangpo.server.entity.User;
import mangpo.server.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public CreateUserResponseDto saveUser(@RequestBody UserRequestDto userRequestDto){

        User user = userRequestDto.toEntity();

        userService.join(user);
        return new CreateUserResponseDto(user.getEmail());
    }

    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class CreateUserResponseDto {
        private String email;
    }
}
