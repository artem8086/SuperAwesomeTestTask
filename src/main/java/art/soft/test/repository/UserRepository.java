package art.soft.test.repository;

import art.soft.test.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    User findByLogin(String login);

    User findByToken(String token);
}
