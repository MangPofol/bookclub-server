package mangpo.server.service;

import lombok.RequiredArgsConstructor;
import mangpo.server.dto.UserRequestDto;
import mangpo.server.entity.ToDo;
import mangpo.server.entity.User;
import mangpo.server.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public Long createUser(User user){
        validateDuplicateUser(user.getEmail());
        userRepository.save(user);
        return user.getId();
    }

    public void validateDuplicateUser(String email) {
        if(email == null)
            return;
        Optional<User> findUser = userRepository.findUserByEmail(email);
        if (findUser.isPresent()){
            throw new IllegalStateException("이미 사용중인 이메일입니다.");
        }
    }

    public List<User> findUsers(){ return userRepository.findAll();}

    public User findById(Long userId){
        return userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 유저입니다."));
    }

    public User findUserByEmail(String email){
        return userRepository.findUserByEmail(email).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 유저입니다."));
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
