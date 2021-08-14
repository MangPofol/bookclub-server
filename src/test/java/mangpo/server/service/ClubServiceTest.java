package mangpo.server.service;

import mangpo.server.dto.ClubRequestDto;
import mangpo.server.entity.Club;
import mangpo.server.entity.ColorSet;
import mangpo.server.exeption.NotExistClubException;
import mangpo.server.repository.ClubRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@Transactional
@SpringBootTest
class ClubServiceTest {

    @Autowired ClubService clubService;
    @Autowired ClubRepository clubRepository;
    @Autowired EntityManager em;


    @Test
    void 클럽생성_정상() {
        //given
        Club club = Club.builder()
                .name("hello club")
                .colorSet(ColorSet.A)
                .presidentId(1L)
                .level(1)
                .build();


        //when
        Long clubId = clubService.createClub(club);


        //then
        assertThat(club).isEqualTo(clubRepository.findById(clubId).get());
    }

    @Test()
    void 클럽생성_중복_실패() {
        //given
        Club club1 = Club.builder()
                .name("hello club")
                .colorSet(ColorSet.A)
                .presidentId(1L)
                .level(1)
                .build();

        Club club2 = Club.builder()
                .name("hello club")
                .colorSet(ColorSet.B)
                .presidentId(2L)
                .level(1)
                .build();

        clubService.createClub(club1);

        //when,then
        assertThatThrownBy(() -> clubService.createClub(club2))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("이미 존재하는 클럽 이름입니다.");

    }


    @Test
    void 클럽조회_단일_성공() {
        //given
        Club club1 = Club.builder()
                .name("hello club")
                .colorSet(ColorSet.A)
                .presidentId(1L)
                .level(1)
                .build();

        Long clubId1 = clubService.createClub(club1);
        //when
        Club result = clubService.findClub(clubId1);

        //then
        assertThat(club1.getId()).isEqualTo(result.getId());
    }

    @Test
    void 클럽조회_리스트() {
        //given
        Club club1 = Club.builder()
                .name("hello club 1")
                .colorSet(ColorSet.A)
                .presidentId(1L)
                .level(1)
                .build();

        Club club2 = Club.builder()
                .name("hello club 2")
                .colorSet(ColorSet.B)
                .presidentId(2L)
                .level(1)
                .build();

        Long clubId1 = clubService.createClub(club1);
        Long clubId2 = clubService.createClub(club2);
        //when
        List<Club> clubs = clubService.findClubs();


        //then
        assertThat(clubs.size()).isEqualTo(2);
        assertThat(clubs).contains(club1,club2);
    }

    @Test
    void 클럽_정보_수정() {
        //given
        Club club1 = Club.builder()
                .name("hello club")
                .colorSet(ColorSet.A)
                .presidentId(1L)
                .level(1)
                .build();

        Long clubId1 = clubService.createClub(club1);

        em.flush();
        em.clear();

        Club result = clubService.findClub(clubId1);

        Club clubRequest = Club.builder()
//              .name("hello club")
                .colorSet(ColorSet.B)
                .presidentId(2L)
                .level(2)
                .build();

        //when
        clubService.updateClub(result.getId(), clubRequest);

        em.flush();
        em.clear();

        Club updatedClub = clubService.findClub(clubId1);

        //then
        assertThat(updatedClub.getColorSet()).isEqualTo(ColorSet.B);
        assertThat(updatedClub.getPresidentId()).isEqualTo(2L);
        assertThat(updatedClub.getLevel()).isEqualTo(2);
        assertThat(updatedClub.getName()).isEqualTo("hello club");
    }

    @Test
    void 클럽_정보_삭제() {
        //given
        Club club1 = Club.builder()
                .name("hello club")
                .colorSet(ColorSet.A)
                .presidentId(1L)
                .level(1)
                .build();

        Long clubId1 = clubService.createClub(club1);
        //when
        clubService.deleteClub(clubId1);

        em.flush();
        em.clear();

        //then
        assertThatThrownBy(() -> clubService.findClub(clubId1))
                .isInstanceOf(NotExistClubException.class)
                .hasMessageContaining("존재하지 않는 클럽입니다.");
    }

}