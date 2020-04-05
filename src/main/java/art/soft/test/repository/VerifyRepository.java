package art.soft.test.repository;

import art.soft.test.model.User;
import art.soft.test.model.VerificationToken;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface VerifyRepository extends MongoRepository<VerificationToken, String> {
    List<VerificationToken> findByUser(User user);
}
