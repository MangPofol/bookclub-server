package mangpo.server.service.user;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import lombok.RequiredArgsConstructor;
import mangpo.server.dto.ChangePwDto;
import mangpo.server.dto.UpdateUserDto;
import mangpo.server.dto.user.CreateUserDto;
import mangpo.server.entity.user.Authority;
import mangpo.server.entity.user.User;
import mangpo.server.entity.user.UserAuthority;
import mangpo.server.repository.AuthorityRepository;
import mangpo.server.repository.UserAuthorityRepository;
import mangpo.server.repository.UserRepository;
import mangpo.server.service.MailService;
import mangpo.server.util.SecurityUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserService {

    private final MailService mailService;

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
    public void updateUser(Long id, UpdateUserDto updateUserDto){
        User user = this.findById(id);
        if (!updateUserDto.getEmail().equals(user.getEmail()))
            validateDuplicateUser(updateUserDto.getEmail());

        user.update(updateUserDto);
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

    @Transactional
    public void changePassword(ChangePwDto changePwDto){
        User user = this.findUserFromToken();

        if(changePwDto.getPassword() != null)
            user.changePw(passwordEncoder.encode(changePwDto.getPassword()));
    }

    @Transactional
    public void lostPassword(String userEmail){
        //generate random password
        String randomNum = generateRandomNum();

        //send mail
        mailService.sendMail(userEmail, randomNum);

        //change user's pw to new random password
        User user = findUserByEmail(userEmail);
        user.changePw(passwordEncoder.encode(randomNum));
    }

    private String generateRandomNum() {
        int max = 9999999;
        int min = 1000000;
        int randomNum = (int) ((Math.random() * (max - min)) + min);
        return String.valueOf(randomNum);
    }
}
