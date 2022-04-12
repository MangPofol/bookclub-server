package mangpo.server.service.user;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import mangpo.server.dto.user.ChangePwDto;
import mangpo.server.dto.user.UpdateUserDto;
import mangpo.server.entity.user.Authority;
import mangpo.server.entity.user.User;
import mangpo.server.entity.user.UserAuthority;
import mangpo.server.repository.user.AuthorityRepository;
import mangpo.server.repository.user.UserAuthorityRepository;
import mangpo.server.repository.user.UserRepository;
import mangpo.server.service.common.MailService;
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


    public User findUserFromToken(){
        Long currentUserId = SecurityUtil.getCurrentUserId();
        return findById(currentUserId);
    }

    @Transactional
    public Long createUser(User user){
        user.changePw(passwordEncoder.encode(user.getPassword()));

        validateDuplicateUser(user.getEmail());
        userRepository.save(user);

        Authority role = authorityRepository.findById("ROLE_NEED_EMAIL").get();

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
        String randomPw = generateRandomPw();

        //send mail
        mailService.lostPw(userEmail, randomPw);

        //change user's pw to new random password
        User user = findUserByEmail(userEmail);
        user.changePw(passwordEncoder.encode(randomPw));
    }

    @Transactional
    public void validateEmail(){
        String randomValidCode = generateRandomValidCode();

        User user = findUserFromToken();
        user.changeEmailValidCode(randomValidCode);

        //send mail
        String userEmail = user.getEmail();
        mailService.validateEmail(userEmail,randomValidCode);
    }

    @Transactional
    public void validateEmailSendCode(String emailValidCode){
        User user = findUserFromToken();

        if (!user.getEmailValidCode().equals(emailValidCode))
            throw new IllegalStateException("잘못된 인증 코드를 입력했습니다.");

        user.changeEmailValidCode(null);

        Authority role = authorityRepository.findById("ROLE_USER").get();
        UserAuthority userAuthority = userAuthorityRepository.findByUser(user);
        userAuthority.changeAuthority(role);
    }

    private String generateRandomPw() {
        int max = 9999999;
        int min = 1000000;
        int randomNum = (int) ((Math.random() * (max - min)) + min);
        return String.valueOf(randomNum);
    }

    private String generateRandomValidCode() {
        int max = 99;
        int min = 10;
        int randomNum = (int) ((Math.random() * (max - min)) + min);
        return String.valueOf(randomNum);
    }
}
