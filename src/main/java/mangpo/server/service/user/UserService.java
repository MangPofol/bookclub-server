package mangpo.server.service.user;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import mangpo.server.dto.user.UserRequestDto;
import mangpo.server.entity.Authority;
import mangpo.server.entity.User;
import mangpo.server.entity.UserAuthority;
import mangpo.server.repository.AuthorityRepository;
import mangpo.server.repository.UserAuthorityRepository;
import mangpo.server.repository.UserRepository;
import mangpo.server.util.SecurityUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final UserAuthorityRepository userAuthorityRepository;

    private final PasswordEncoder passwordEncoder;

//    private final PasswordEncoder passwordEncoder;

//    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
//        this.userRepository = userRepository;
//        this.passwordEncoder = passwordEncoder;
//    }

//    public UserDto getUserWithAuthorities(String email) {
//        return UserDto.from(userRepository.findWithAuthByEmail(email).orElse(null));
//    }
//
//    public UserDto getMyUserWithAuthorities() {
//        return UserDto.from(SecurityUtil.getCurrentUserId().flatMap(userRepository::findWithAuthByEmail).orElse(null));
//    }

    public User findUserFromToken(){
        Long currentUserId = SecurityUtil.getCurrentUserId();
        return findById(currentUserId);
    }

    @Transactional
    public Long createUser(User user){
        user.changePw(passwordEncoder.encode(user.getPassword()));

        validateDuplicateUser(user.getEmail());
        userRepository.save(user);


        Authority role = authorityRepository.findById("ROLE_USER").get();

        UserAuthority userAuthority = UserAuthority.builder()
                .authority(role)
                .user(user)
                .build();
        userAuthorityRepository.save(userAuthority);

        return user.getId();
    }

    public void validateDuplicateUser(String email) {
        if(email == null)
            return;
        Optional<User> findUser = userRepository.findByEmail(email);
        if (findUser.isPresent()){
            throw new IllegalStateException("이미 사용중인 이메일입니다.");
        }
    }

    public List<User> findUsers(){ return userRepository.findAll();}

    public User findById(Long userId){
        return userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 유저입니다."));
    }

    public User findUserByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 유저입니다."));
    }

    @Transactional
    public void updateUser(Long id, UserRequestDto userRequest){
        User user = userRepository.findById(id).orElseThrow(() ->  new EntityNotFoundException("존재하지 않는 유저입니다."));
        if (!userRequest.getEmail().equals(user.getEmail()))
            validateDuplicateUser(userRequest.getEmail());

        user.update(userRequest);
    }

    @Transactional
    public void deleteUser(Long id){
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 유저입니다."));

        userRepository.delete(user);
    }

    @Transactional
    public void changeDormant(User user){
        user.changeIsDormant();
    }
}
