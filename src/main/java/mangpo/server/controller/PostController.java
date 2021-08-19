package mangpo.server.controller;

import lombok.RequiredArgsConstructor;
import mangpo.server.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

}
