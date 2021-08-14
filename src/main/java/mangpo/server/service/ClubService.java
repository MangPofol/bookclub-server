package mangpo.server.service;


import lombok.RequiredArgsConstructor;
import mangpo.server.entity.Club;
import mangpo.server.entity.ColorSet;
import mangpo.server.entity.User;
import mangpo.server.exeption.NotExistClubException;
import mangpo.server.exeption.NotExistUserException;
import mangpo.server.repository.ClubRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ClubService {

    private final ClubRepository clubRepository;

    @Transactional
    public Long createClub(Club club){
        validateDuplicateClubName(club);

        clubRepository.save(club);
        return club.getId();
    }

    private void validateDuplicateClubName(Club club) {
        List<Club> findClub = clubRepository.findByName(club.getName());
        if(!findClub.isEmpty()){
            throw new IllegalStateException("이미 존재하는 클럽 이름입니다.");
        }
    }

    //TODO 문제 발생 가능성 높음: 나중에 잘 살펴보기
    @Transactional
    public void updateClub(Long id, Club clubRequest){
        Club club = clubRepository.findById(id).orElseThrow(() ->   new NotExistClubException("존재하지 않는 클럽입니다."));

        validateDuplicateClubName(clubRequest);

        if(clubRequest.getName() != null)
            club.changeName(clubRequest.getName());
        if(clubRequest.getColorSet() != null)
            club.changeColorSet(clubRequest.getColorSet());
        if(clubRequest.getLevel() != null)
            club.changeLevel(clubRequest.getLevel());
        if(clubRequest.getPresidentId() != null)
            club.changePresident(clubRequest.getPresidentId());
    }

    @Transactional
    public void deleteClub(Long id){
        Club club = clubRepository.findById(id).orElseThrow(() -> new NotExistClubException("존재하지 않는 클럽입니다."));
        clubRepository.delete(club);
    }

    public List<Club> findClubs(){
        return clubRepository.findAll();
    }
    public Club findClub(Long clubId){
        return clubRepository.findById(clubId).orElseThrow(() -> new NotExistClubException("존재하지 않는 클럽입니다."));
    }

}
