package art.soft.test.repository;

import art.soft.test.model.Post;
import art.soft.test.model.User;
import com.mongodb.DBRef;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface PostRepository extends MongoRepository<Post, String> {
    List<Post> findByOwner(User owner);
    List<Post> findByOwner(User owner, Sort sort);

    @Query("{ owner: { $in : ?0 } }")
    List<Post> findBySubsribers(List<DBRef> users, Sort sort);

    @Query("{ owner: { $in : ?0 }, title: ?1 }")
    List<Post> findBySubsribersWithTitle(List<DBRef> users, String title, Sort sort);
}
