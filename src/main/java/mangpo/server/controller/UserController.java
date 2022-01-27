package mangpo.server.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mangpo.server.dto.*;
import mangpo.server.dto.user.UserResponseDto;
import mangpo.server.entity.user.User;

import mangpo.server.service.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

//    @GetMapping("/user")
//    @PreAuthorize("hasAnyRole('USER','ADMIN')")
//    public ResponseEntity<UserDto> getMyUserInfo(HttpServletRequest request) {
//        return ResponseEntity.ok(userService.getMyUserWithAuthorities());
//    }
//
//    @GetMapping("/user/{username}")
//    @PreAuthorize("hasAnyRole('ADMIN')")
//    public ResponseEntity<UserDto> getUserInfo(@PathVariable String username) {
//        return ResponseEntity.ok(userService.getUserWithAuthorities(username));
//    }

    @GetMapping
    public ResponseEntity<Result<UserResponseDto>> getUserInfo(@RequestParam Long userId) {
        User user = userService.findById(userId);
        UserResponseDto userResponseDto = new UserResponseDto(user);

        return ResponseEntity.ok(new Result<>(userResponseDto));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable Long userId, @RequestBody UpdateUserDto updateUserDto) {
        User user = userService.findById(userId);
        userService.updateUser(userId, updateUserDto);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/current")
    public ResponseEntity<Result<UserResponseDto>> getCurrentUserInfo() {
        User user = userService.findUserFromToken();
        UserResponseDto userResponseDto = new UserResponseDto(user);

        return ResponseEntity.ok(new Result<>(userResponseDto));
    }



    @PostMapping("/{userId}/change-dormant")
    public ResponseEntity<?> changeUserDormant(@PathVariable Long userId) {
        User user = userService.findById(userId);
        userService.changeDormant(user);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/validate-duplicate")
    public ResponseEntity<?> validateDuplicate(@RequestBody UserValidationDto userValidationDto) {
        userService.validateDuplicateUser(userValidationDto.getEmail());

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/lost-pw")
    public ResponseEntity<?> lostPassword(@RequestParam String userEmail) {
        userService.lostPassword(userEmail);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/change-pw")
    public ResponseEntity<?> changePassword(@RequestBody ChangePwDto changePwDto) {
        userService.changePassword(changePwDto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/validate-email")
    public ResponseEntity<?> validateEmail() {
        userService.validateEmail();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/validate-email-send-code")
    public ResponseEntity<?> validateEmailSendCode(@RequestParam String emailCode) {
        userService.validateEmailSendCode(emailCode);
        return ResponseEntity.noContent().build();
    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
//        User user = userService.findUser(id);
//    }

    @Data
    static class UserValidationDto {
        String email;
    }
}
