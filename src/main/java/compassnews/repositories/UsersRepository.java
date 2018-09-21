package compassnews.repositories;

import compassnews.models.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UsersRepository extends MongoRepository<UserEntity, String> {
	List<UserEntity> findByUsername(String username);
}
