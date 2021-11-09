package mangpo.server.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mangpo.server.dto.*;
import mangpo.server.entity.User;
import mangpo.server.service.ClubBookUserService;
import mangpo.server.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final ClubBookUserService cbuService;

    @GetMapping
    public ResponseEntity<Result<UserResponseDto>> getUserInfo(@RequestParam Long userId) {
        User user = userService.findById(userId);

        UserResponseDto userResponseDto = new UserResponseDto(user);
        return ResponseEntity.ok(new Result<>(userResponseDto));
    }

    @PostMapping
    public ResponseEntity<Result<UserResponseDto>> createUser(@RequestBody UserRequestDto userRequestDto, UriComponentsBuilder b) {

        User user = userRequestDto.toEntityExceptId();

        Long userId = userService.createUser(user);

        UriComponents uriComponents =
                b.path("/users/{userId}").buildAndExpand(userId);

        UserResponseDto userResponseDto = new UserResponseDto(user);
        Result<UserResponseDto> result = new Result<>(userResponseDto);

        return ResponseEntity.created(uriComponents.toUri()).body(result);
    }


    @PostMapping("/{userId}/change-dormant")
    public ResponseEntity<?> changeUserDormant(@PathVariable Long userId) {
        User user = userService.findById(userId);
        userService.changeDormant(user);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/validate-duplicate")
    public ResponseEntity<?> validateDuplicate(@RequestBody UserValidationDto userValidationDto){
        userService.validateDuplicateUser(userValidationDto.getEmail());

        return ResponseEntity.noContent().build();
    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
//        User user = userService.findUser(id);
//    }

    @PatchMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable Long userId, @RequestBody UserRequestDto userRequestDto) {
        User user = userService.findById(userId);
//        User userRequest = userRequestDto.toEntityExceptId();

//        for (Genre genre : userRequest.getGenres()) {
//            genre.addUser(user);
//        }

        userService.updateUser(userId,userRequestDto);

        return ResponseEntity.noContent().build();
    }


    @Data
    static class UserValidationDto{
        String email;
    }
}
