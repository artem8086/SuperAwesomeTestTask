package art.soft.test.configuration;

import art.soft.test.model.Post;
import art.soft.test.model.User;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.data.mongodb.core.index.IndexResolver;
import org.springframework.data.mongodb.core.index.MongoPersistentEntityIndexResolver;

@Configuration
public class MongoConfiguration extends AbstractMongoClientConfiguration {
    @Override
    public MongoClient mongoClient() {
        return MongoClients.create(MongoClientSettings.builder().build());
    }

    @Override
    protected String getDatabaseName() {
        return "testtask";
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initIndicesAfterStartup() throws ClassNotFoundException {
        MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory());
        mongoMappingContext().setAutoIndexCreation(false);
        createIndexForEntity(User.class, mongoTemplate);
        createIndexForEntity(Post.class, mongoTemplate);
    }

    private void createIndexForEntity(Class<?> entityClass, MongoTemplate mongoTemplate) throws ClassNotFoundException {
        IndexOperations indexOps = mongoTemplate.indexOps(entityClass);
        IndexResolver resolver = new MongoPersistentEntityIndexResolver(this.mongoMappingContext());
        resolver.resolveIndexFor(entityClass).forEach(indexOps::ensureIndex);
    }
}
