package mangpo.server.service;

import lombok.RequiredArgsConstructor;
import mangpo.server.dto.club.InviteRequestDto;
import mangpo.server.entity.Club;
import mangpo.server.entity.Invite;
import mangpo.server.entity.user.User;
import mangpo.server.repository.ClubRepository;
import mangpo.server.repository.InviteRepository;
import mangpo.server.service.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InviteService {

    private final InviteRepository inviteRepository;
    private final ClubRepository clubRepository;
//    private final UserRepository userRepository;
    private final UserService userService;

    //ToDo: 추후 firebase push message 추가
    @Transactional
    public Long createInvite(InviteRequestDto inviteRequestDto){
//        User user = userRepository.findByEmail(inviteRequestDto.getUserEmailToInvite()).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 유저입니다."));
        User user = userService.findUserByEmail(inviteRequestDto.getUserEmailToInvite());
        Club club = clubRepository.findById(inviteRequestDto.getClubId()).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 클럽입니다."));

        if(inviteRepository.findByUserAndClub(user, club).isPresent())
            throw new IllegalStateException("이미 초대한 클럽원 입니다");


        Invite invite = Invite.builder()
                .user(user)
                .club(club)
                .message(inviteRequestDto.getMessage())
                .build();
        inviteRepository.save(invite);

        return invite.getId();
    }

    //예비 클럽원이 거절하면 사용
    //초대 정보 삭제
    @Transactional
    public void deleteById(Long inviteId){
        Invite invite = findById(inviteId);
        inviteRepository.delete(invite);
    }

    public Invite findById(Long inviteId){
        return inviteRepository.findById(inviteId).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 초대 정보입니다."));
    }

    public List<Invite> findListByUser(){
        User user = userService.findUserFromToken();
        return inviteRepository.findListByUser(user);
    }

    public Invite findByUserAndClub(User user, Club club){
        return inviteRepository.findByUserAndClub(user,club).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 유저 혹은 클럽입니다."));
    }

}
