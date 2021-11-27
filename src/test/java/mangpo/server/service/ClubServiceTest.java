package mangpo.server.service;

import mangpo.server.entity.Club;
import mangpo.server.repository.ClubRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
class ClubServiceTest {

    @InjectMocks
    private ClubService clubService;

    @Mock
    private ClubRepository clubRepository;
    private Club club;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        club = Club.builder().build();
    }

    @Test
    void createClub_정상() {
        //given
        given(clubRepository.save(any())).willReturn(club);
        given(clubRepository.findByName(any())).willReturn(Optional.empty());

        //when
        Long resultId = clubService.createClub(this.club);

        //then
        assertThat(resultId).isEqualTo(club.getId());
    }

    @Test
    void createClub_중복() {
        //given
        given(clubRepository.save(any())).willReturn(club);
        given(clubRepository.findByName(any())).willReturn(Optional.of(club));


        //when then
        assertThatThrownBy(() ->  clubService.createClub(this.club))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("이미 존재하는 클럽 이름입니다.");
    }


}
