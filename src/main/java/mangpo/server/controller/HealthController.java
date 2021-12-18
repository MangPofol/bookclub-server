package mangpo.server.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mangpo.server.dto.Result;
import mangpo.server.dto.post.PostResponseDto;
import mangpo.server.entity.Post;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/health")
public class HealthController {
    @GetMapping
    public ResponseEntity<?> getPostsByBookIdAndClubScope() {

        return ResponseEntity.ok().build();
    }
}
