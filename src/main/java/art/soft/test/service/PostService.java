package art.soft.test.service;

import art.soft.test.dto.PostDTO;
import art.soft.test.exception.CustomException;
import art.soft.test.model.Post;
import art.soft.test.repository.PostRepository;
import art.soft.test.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public Post create(User user, PostDTO postDTO) {
        return postRepository.insert(new Post(postDTO.getTitle(), postDTO.getContent(), user));
    }

    public Post modify(User user, String id, PostDTO postDTO) {
        Post post;
        try {
            post = postRepository.findById(id).get();
        } catch (NoSuchElementException e) {
            throw new CustomException("Post not found!", HttpStatus.NOT_FOUND);
        }
        if (!user.isAdmin() && !post.getOwner().equals(user)) {
            throw new CustomException("This post doesn't belong to you!", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (postDTO.getTitle() != null) post.setTitle(postDTO.getTitle());
        if (postDTO.getContent() != null) post.setContent(postDTO.getContent());
        postRepository.save(post);
        return post;
    }

    public Post delete(User user, String id) {
        Post post;
        try {
            post = postRepository.findById(id).get();
        } catch (NoSuchElementException e) {
            throw new CustomException("Post not found!", HttpStatus.NOT_FOUND);
        }
        if (!user.isAdmin() && !post.getOwner().equals(user)) {
            throw new CustomException("This post doesn't belong to you!", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        postRepository.delete(post);
        return post;
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public List<Post> getUserPosts(User user) {
        return postRepository.findByOwner(user, Sort.by(Sort.Direction.ASC, "Date"));
    }

    public List<Post> getUserFeed(User user) {
        return postRepository.findBySubsribers(
                user.getSubsRef(),
                Sort.by(Sort.Direction.ASC, "Date"));
    }

    public List<Post> findInFeedByTitle(User user, String title) {
        return postRepository.findBySubsribersWithTitle(
                user.getSubsRef(),
                title,
                Sort.by(Sort.Direction.ASC, "Date"));
    }
}
