package art.soft.test.controller;

import art.soft.test.model.Post;
import art.soft.test.model.User;
import art.soft.test.service.PostService;
import art.soft.test.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/post")
@PreAuthorize("hasRole('ROLE_CLIENT')")
public class PostController {
    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @PostMapping("/create")
    public Post create(HttpServletRequest req,
                       @RequestParam String title,
                       @RequestParam String content) {
        return postService.create(userService.whoami(req), title, content);
    }

    @PostMapping("/modify/{postid}")
    public Post modify(HttpServletRequest req,
                       @PathVariable String postid,
                       @RequestParam(required = false) String title,
                       @RequestParam(required = false) String content) {
        return postService.modify(userService.whoami(req), postid, title, content);
    }

    @PostMapping("/delete/{postid}")
    public Post delete(HttpServletRequest req, @PathVariable String postid) {
        return postService.delete(userService.whoami(req), postid);
    }

    @GetMapping("/my")
    public List<Post> myPosts(HttpServletRequest req) {
        return postService.getUserPosts(userService.whoami(req));
    }

    @GetMapping("/feed")
    public List<Post> getFeed(HttpServletRequest req, @RequestParam(required = false) String title) {
        if (title == null) {
            return postService.getUserFeed(userService.whoami(req));
        } else {
            return postService.findInFeedByTitle(userService.whoami(req), title);
        }
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }
}
