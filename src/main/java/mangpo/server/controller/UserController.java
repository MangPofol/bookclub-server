package mangpo.server.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import mangpo.server.entity.Sex;
import mangpo.server.entity.User;
import mangpo.server.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<CreateUserResponseDto> createUser(@RequestBody CreateUserRequestDto createUserRequestDto){

        User user = User.builder()
                .email(createUserRequestDto.email)
                .password(createUserRequestDto.password)
                .birthdate(createUserRequestDto.birthdate)
                .nickname(createUserRequestDto.nickname)
                .sex(createUserRequestDto.sex)
                .profileImgLocation(createUserRequestDto.profileImgLocation)
                .build();

        try{
            userService.join(user);
            CreateUserResponseDto response = new CreateUserResponseDto(user.getEmail(),user.getNickname(),user.getSex(),user.getBirthdate(),user.getProfileImgLocation());

            return ResponseEntity.ok(response);
        }catch (IllegalStateException e){
            return ResponseEntity.badRequest().build();
        }
    }

//    @GetMapping
//    public Result<FindOneUserDto> findOneUser(@RequestBody FindOneUserDto findOneUserDto){
//        String email = findOneUserDto.getEmail();
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
        private String password;
        private String nickname;
        private Sex sex;
        private LocalDate birthdate;
        private String profileImgLocation;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class CreateUserResponseDto {
        private String email;
        private String nickname;
        private Sex sex;
        private LocalDate birthdate;
        private String profileImgLocation;
    }

//    @Data
//    @AllArgsConstructor
//    @NoArgsConstructor
//    static class FindOneUserDto {
//        private String email;
//    }

}
