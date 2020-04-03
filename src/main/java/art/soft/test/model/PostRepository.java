package art.soft.test.model;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PostRepository extends MongoRepository<Post, String> {
    public List<Post> findByOwner(User owner, Sort sort);
}
