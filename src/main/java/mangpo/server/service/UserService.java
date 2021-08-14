package mangpo.server.service;

import lombok.RequiredArgsConstructor;
import mangpo.server.entity.User;
import mangpo.server.exeption.NotExistUserException;
import mangpo.server.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public Long join(User user){
        validateDuplicateUser(user);
        userRepository.save(user);
        return user.getId();
    }

    private void validateDuplicateUser(User user) {
        List<User> findUser = userRepository.findByEmail(user.getEmail());
        if (!findUser.isEmpty()){
            throw new IllegalStateException("이미 사용중인 이메일입니다.");
        }
    }

    public List<User> findUsers(){ return userRepository.findAll();}

    public User findUser(Long userId){
        return userRepository.findById(userId).orElseThrow(() -> new NotExistUserException("존재하지 않는 유저입니다."));
    }

    @Transactional
    public void updateUser(Long id, User userRequest){
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalStateException("이미 사용중인 이메일입니다."));

        validateDuplicateUser(userRequest);

        if(userRequest.getEmail() != null)
            user.changeEmail(userRequest.getEmail());
        if(userRequest.getUserPassword() != null)
            user.changeUserPassword(userRequest.getUserPassword());
    }

    @Transactional
    public void deleteUser(Long id){
        User user = userRepository.findById(id).orElseThrow(() -> new NotExistUserException("존재하지 않는 유저입니다."));

        userRepository.delete(user);
    }
}
