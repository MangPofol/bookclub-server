//package mangpo.server.service;
//
//import mangpo.server.entity.ClubBookUser;
//import mangpo.server.entity.user.User;
//import mangpo.server.repository.ClubBookUserRepository;
//import mangpo.server.service.user.UserService;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.springframework.transaction.annotation.Transactional;
//
//import static org.assertj.core.api.Assertions.*;
//import static org.mockito.BDDMockito.*;
//
//@Transactional
//@RunWith(MockitoJUnitRunner.class)
//class ClubBookUserServiceTest {
//
//    @InjectMocks
//    private ClubBookUserService cbuService;
//
//    @Mock
//    private ClubBookUserRepository mockCbuRepository;
//    @Mock
//    private UserService mockUserService;
//
//    private ClubBookUser cbu;
//    private User user;
//    @BeforeEach
//    public void setUp() throws Exception {
//        MockitoAnnotations.openMocks(this);
//
//        cbu = ClubBookUser.builder()
//                .id(1L)
//                .build();
//
//        user = User.builder()
//                .id(1L)
//                .build();
//    }
//
//    @Test
//    void createClubBookUser_정상() {
//        //given
//        given(mockCbuRepository.save(any())).willReturn(cbu);
//        given(mockCbuRepository.isDuplicate(any())).willReturn(Boolean.FALSE);
//
//        //when
//        Long cbuId = cbuService.createClubBookUser(cbu);
//
//        //then
//        assertThat(cbuId).isEqualTo(cbu.getId());
//    }
//
//    @Test
//    void createClubBookUser_중복() {
//        //given
//        given(mockCbuRepository.save(any())).willReturn(cbu);
//        given(mockCbuRepository.isDuplicate(any())).willReturn(Boolean.TRUE);
//
//        //when then
//        assertThatThrownBy(() -> cbuService.createClubBookUser(cbu))
//                .isInstanceOf(IllegalStateException.class)
//                .hasMessageContaining("이미 존재하는 정보입니다");
//    }
//
////    @Test
////    void countByUserAndClubIsNull() {
////        //given
////        given(mockUserService.findUserFromToken()).willReturn(user);
////
////    }
//
//}