//package mangpo.server.service;
//
//import mangpo.server.entity.Liked;
//import mangpo.server.repository.LikedRepository;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@Transactional
//public class LikedServiceTest {
//
//    @Autowired LikedService likedService;
//    @Autowired
//    LikedRepository likedRepository;
//
//    @Test
//    void 좋아요_생성(){
//        //given
//        Liked liked = Liked.builder().build();
//
//        //when
//        Long likedId = likedService.createLiked(liked);
//
//        ///then
//        Optional<Liked> byId = likedRepository.findById(likedId);
//        Assertions.assertThat(byId.get()).isEqualTo(liked);
//    }
//
//    @Test
//    void 좋아요_삭제(){
//        //given
//        Liked liked = Liked.builder().build();
//        Long likedId = likedService.createLiked(liked);
//
//        //when
//        likedService.deleteLiked(liked);
//
//        ///then
//        Optional<Liked> byId = likedRepository.findById(likedId);
//        Assertions.assertThat(byId).isEqualTo(Optional.empty());
//    }
//}