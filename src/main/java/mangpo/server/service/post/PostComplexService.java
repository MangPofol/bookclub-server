//package mangpo.server.service.post;
//
//import lombok.RequiredArgsConstructor;
//import mangpo.server.service.user.LikedService;
//import mangpo.server.service.user.UserService;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//@Transactional(readOnly = true)
//@RequiredArgsConstructor
//public class PostComplexService {
//
//    private final UserService userService;
//    private final PostService postService;
//    private final LikedService likedService;
//
//
//
////    @Transactional
////    public void doLikePost(Long postId){
////        User user = userService.findUserFromToken();
////        Post post = postService.findPost(postId);
////
////        Liked liked = Liked.builder()
////                .user(user)
////                .isLiked(true)
////                .build();
////        liked.doLikeToPost(post);
////
////        likedService.createLiked(liked);
////    }
////
////    @Transactional
////    public void undoLikePost(Long postId){
////        User user = userService.findUserFromToken();
////        Post post = postService.findPost(postId);
////
////        Liked liked = likedUserFromPost(user, post);
////        liked.undoLikeToPost(post);
////
////        likedService.deleteLiked(liked);
////    }
////
////    private Liked likedUserFromPost(User user, Post post) {
////        List<Liked> collect = post.getLikedList().stream()
////                .filter(l -> l.getUser().getId().equals(user.getId()))
////                .collect(Collectors.toList());
////        Liked liked = collect.get(0);
////        return liked;
////    }
//}
