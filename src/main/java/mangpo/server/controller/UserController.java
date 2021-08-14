package mangpo.server.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import mangpo.server.entity.User;
import mangpo.server.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

//    @PostMapping
//    public Result saveUser(@RequestBody CreateUserRequestDto createUserRequestDto){
//
//        User user = User.builder()
//                .email(createUserRequestDto.email)
//                .userPassword(createUserRequestDto.userPassword)
//                .build();
//
//        userService.join(user);
//
//        CreateUserResponseDto response = new CreateUserResponseDto(user.getEmail());
//
//        return new Result(response);
//    }

    @PostMapping
    public ResponseEntity<CreateUserResponseDto> createUser(@RequestBody CreateUserRequestDto createUserRequestDto){

        User user = User.builder()
                .email(createUserRequestDto.email)
                .userPassword(createUserRequestDto.userPassword)
                .build();

        try{
            userService.join(user);
            CreateUserResponseDto response = new CreateUserResponseDto(user.getEmail());

            return ResponseEntity.ok(response);
        }catch (IllegalStateException e){
            return ResponseEntity.badRequest().build();
        }
    }

//    @GetMapping
//    public Result<FindOneUserResponseDto> findOneUser(@RequestBody FindOneUserRequestDto findOneUserRequestDto){
//
//    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class Result<T>{
        private T data;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class CreateUserRequestDto {
        private String email;
        private String userPassword;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class CreateUserResponseDto {
        private String email;
    }

//    @Data
//    @AllArgsConstructor
//    @NoArgsConstructor
//    static class FindOneUserRequestDto {
//        private String email;
//    }
//
//    @Data
//    @AllArgsConstructor
//    @NoArgsConstructor
//    static class FindOneUserResponseDto {
//        private String email;
//    }
}
