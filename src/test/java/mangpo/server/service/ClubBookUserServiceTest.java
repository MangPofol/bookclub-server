//package mangpo.server.service;
//
//import mangpo.server.entity.*;
//import mangpo.server.repository.ClubBookUserRepository;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.persistence.EntityManager;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//@Transactional
//public class ClubBookUserServiceTest {
//
//    @Autowired BookService bookService;
//    @Autowired UserService userService;
//    @Autowired ClubService clubService;
//
//    @Autowired ClubBookUserService clubBookUserService;
//    @Autowired ClubBookUserRepository clubBookUserRepository;
//
//    @Autowired EntityManager em;
//
//    @Test
//    void cbu_생성() {
//        //given
//        ClubBookUser cbu = ClubBookUser.builder().build();
//
//        //when
//        Long saved = clubBookUserService.createClubBookUser(cbu);
//
//        //then
//        assertThat(cbu).isEqualTo(clubBookUserRepository.findById(saved).get());
//    }
//
//    @Test
//    @DisplayName("클럽 기준으로 cbu 전부 삭제")
//    void deleteAllClubBookUserByClub() {
//        //given
//        Club club1 = Club.builder().build();
//        Book book1  = Book.builder().build();
//
//        ClubBookUser cbu1 = ClubBookUser.builder()
//                .club(club1)
//                .build();
//
//        ClubBookUser cbu2 = ClubBookUser.builder()
//                .club(club1)
//                .book(book1)
//                .build();
//
//
//        clubService.createClub(club1);
//        bookService.createBook(book1, );
//
//        Long saved1 = clubBookUserService.createClubBookUser(cbu1);
//        Long saved2 = clubBookUserService.createClubBookUser(cbu2);
//
//        //when
//        clubBookUserService.deleteAllByClub(club1);
//
//        em.flush();
//        em.clear();
//
//        //then
//        assertThat(clubBookUserRepository.findById(saved1).isEmpty()).isEqualTo(true);
//        assertThat(clubBookUserRepository.findById(saved2).isEmpty()).isEqualTo(true);
//    }
//
//    @Test
//    void cbu_리스트_조회_유저로() {
//        //given
//        User user = User.builder().build();
//        Book book1 = Book.builder().build();
//        Book book2 = Book.builder().build();
//
//        ClubBookUser cbu1 = ClubBookUser.builder()
//                .user(user)
//                .book(book1)
//                .build();
//
//        ClubBookUser cbu2 = ClubBookUser.builder()
//                .user(user)
//                .book(book2)
//                .build();
//
//        userService.createUser(user);
//        bookService.createBook(book1);
//        bookService.createBook(book2);
//
//        Long saved1 = clubBookUserService.createClubBookUser(cbu1);
//        Long saved2 = clubBookUserService.createClubBookUser(cbu2);
//
//        //when
//        List<ClubBookUser> listByUser = clubBookUserService.findListByUserExceptClub(user);
//
//        //then
//        assertThat(listByUser.size()).isEqualTo(2);
//    }
////
////    @Test
////    @DisplayName("클럽 제외 유저와 책 정보로 cbu 조회: 어느 유저의 책인지에 대한 정보만 있는 데이터 조회용")
////    void findByUserAndBookExceptClub() {
////        //given
////        User user = User.builder().build();
////        Book book = Book.builder().build();
////
////        ClubBookUser cbu1 = ClubBookUser.builder()
////                .user(user)
////                .book(book)
////                .build();
////
////        userService.createUser(user);
////        bookService.createBook(book);
////
////        Long clubBookUser = clubBookUserService.createClubBookUser(cbu1);
////
////        //when
//////        ClubBookUser byUserAndBookExceptClub = clubBookUserService.findByUserAndBookExceptClub(user, book);
////        ClubBookUserSearchCondition cbuCond = new ClubBookUserSearchCondition();
////        cbuCond.setUser(user);
////        cbuCond.setBook(book);
////        List<ClubBookUser> byCondition = clubBookUserService.findByCondition(cbuCond);
////        //then
////        assertThat(byCondition).isEqualTo(cbu1);
////    }
//
//}