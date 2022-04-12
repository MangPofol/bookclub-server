package mangpo.server.controller.auth;


import lombok.RequiredArgsConstructor;
import mangpo.server.dto.Result;
import mangpo.server.dto.auth.LoginDto;
import mangpo.server.dto.auth.TokenDto;
import mangpo.server.dto.user.CreateUserDto;
import mangpo.server.dto.user.UserResponseDto;
import mangpo.server.entity.user.User;
import mangpo.server.jwt.JwtFilter;
import mangpo.server.jwt.TokenProvider;
import mangpo.server.service.user.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Result<UserResponseDto>> createUser(@RequestBody CreateUserDto createUserDto, UriComponentsBuilder b) {
        User user = createUserDto.toEntityExceptId();
        Long userId = userService.createUser(user);

        UriComponents uriComponents =
                b.path("/users/{userId}").buildAndExpand(userId);

        UserResponseDto userResponseDto = new UserResponseDto(user);
        Result<UserResponseDto> result = new Result<>(userResponseDto);

        return ResponseEntity.created(uriComponents.toUri()).body(result);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> authorize(@Valid @RequestBody LoginDto loginDto) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.createToken(authentication);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        return new ResponseEntity<>(new TokenDto(jwt), httpHeaders, HttpStatus.OK);
    }
}
