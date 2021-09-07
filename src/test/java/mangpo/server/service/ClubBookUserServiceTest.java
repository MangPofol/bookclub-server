package mangpo.server.service;

import mangpo.server.entity.*;
import mangpo.server.repository.ClubBookUserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ClubBookUserServiceTest {

    @Autowired BookService bookService;
    @Autowired UserService userService;
    @Autowired ClubService clubService;

    @Autowired ClubBookUserService clubBookUserService;
    @Autowired ClubBookUserRepository clubBookUserRepository;

    @Autowired EntityManager em;

    @Test
    void cbu_생성() {
        //given
        ClubBookUser cbu = ClubBookUser.builder().build();

        //when
        Long saved = clubBookUserService.createClubBookUser(cbu);

        //then
        assertThat(cbu).isEqualTo(clubBookUserRepository.findById(saved).get());
    }

    @Test
    @DisplayName("클럽 기준으로 cbu 전부 삭제")
    void deleteAllClubBookUserByClub() {
        //given
        Club club = Club.builder().build();

        ClubBookUser cbu1 = ClubBookUser.builder()
                .club(club)
                .build();

        ClubBookUser cbu2 = ClubBookUser.builder()
                .club(club)
                .build();


        clubService.createClub(club);
        Long saved1 = clubBookUserService.createClubBookUser(cbu1);
        Long saved2 = clubBookUserService.createClubBookUser(cbu2);

        //when
        clubBookUserService.deleteAllClubBookUserByClub(club);

        em.flush();
        em.clear();

        //then
        assertThat(clubBookUserRepository.findById(saved1).isEmpty()).isEqualTo(true);
        assertThat(clubBookUserRepository.findById(saved2).isEmpty()).isEqualTo(true);
    }

    @Test
    void cbu_리스트_조회_유저로() {
        //given
        User user = User.builder().build();
        ClubBookUser cbu1 = ClubBookUser.builder()
                .user(user)
                .build();

        ClubBookUser cbu2 = ClubBookUser.builder()
                .user(user)
                .build();

        userService.join(user);
        Long saved1 = clubBookUserService.createClubBookUser(cbu1);
        Long saved2 = clubBookUserService.createClubBookUser(cbu2);

        //when
        List<ClubBookUser> listByUser = clubBookUserService.findListByUser(user);

        //then
        assertThat(listByUser.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("클럽 제외 유저와 책 정보로 cbu 조회: 어느 유저의 책인지에 대한 정보만 있는 데이터 조회용")
    void findByUserAndBookExceptClub() {
        //given
        User user = User.builder().build();
        Book book = Book.builder().build();

        ClubBookUser cbu1 = ClubBookUser.builder()
                .user(user)
                .book(book)
                .build();

        userService.join(user);
        bookService.createBook(book);

        Long clubBookUser = clubBookUserService.createClubBookUser(cbu1);

        //when
        ClubBookUser byUserAndBookExceptClub = clubBookUserService.findByUserAndBookExceptClub(user, book);

        //then
        assertThat(byUserAndBookExceptClub).isEqualTo(cbu1);
    }

}