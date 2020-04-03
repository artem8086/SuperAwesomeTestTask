package art.soft.test.models;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PostRepository extends MongoRepository<Post, String> {

    public List<Post> findByTitle(String title);
}
