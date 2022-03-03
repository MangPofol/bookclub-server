package mangpo.server.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mangpo.server.dto.post.PostRequestDto;
import mangpo.server.dto.post.PostResponseDto;
import mangpo.server.dto.Result;
import mangpo.server.entity.post.Post;
import mangpo.server.entity.post.PostClubScope;
import mangpo.server.entity.post.PostScope;
import mangpo.server.service.post.PostClubScopeService;
import mangpo.server.service.post.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

//    private final PostComplexService postComplexService;

    private final PostService postService;
    private final PostClubScopeService pcsService;

    //Todo dto로 직접 조회 고려
//    @GetMapping
//    public Result<List<PostResponseDto>> getPostsByBookId(@RequestParam Long bookId) {
//        List<Post> posts = postService.findPostsByBookId(bookId);
//
//        List<PostResponseDto> collect = posts.stream()
//                .map(PostResponseDto::new)
//                .collect(Collectors.toList());
//
//        return new Result(collect);
//    }

    @GetMapping
    public Result<List<PostResponseDto>> getPostsByBookIdAndClubScope(@RequestParam Long bookId, @RequestParam(defaultValue = "-1") Long clubId) {
        List<Post> posts = postService.findPostsByBookIdAndClubScope(bookId, clubId);

        List<PostResponseDto> postResponseDtoList = pcsService.createPostResponseDtoList(posts);

        return new Result<>(postResponseDtoList);
    }

    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(@RequestBody PostRequestDto requestDto, UriComponentsBuilder b) {
        Long postId = postService.createPost(requestDto);

        UriComponents uriComponents =
                b.path("/posts/{postId}").buildAndExpand(postId);

        PostResponseDto postResponseDto = pcsService.createPostResponseDto(postService.findPostById(postId));

//        PostResponseDto postResponseDto = new PostResponseDto(postService.findPostById(postId));

        return ResponseEntity.created(uriComponents.toUri()).body(postResponseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
        postService.deletePostById(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(@PathVariable Long id, @RequestBody PostRequestDto requestDto) {
        postService.updatePost(id, requestDto);

        PostResponseDto postResponseDto = pcsService.createPostResponseDto(postService.findPostById(id));

        return ResponseEntity.ok().body(postResponseDto);
    }

    @PostMapping("/{postId}/do-like")
    public ResponseEntity<?> doLikePost(@PathVariable Long postId) {
        postService.doLikePost(postId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{postId}/undo-like")
    public ResponseEntity<?> undoLikePost(@PathVariable Long postId) {
        postService.undoLikePost(postId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/total-count")
    public Result<?> getTotalCount() {
        Integer totalCount = postService.findTotalCount();
        return new Result<>(totalCount);
    }


}
