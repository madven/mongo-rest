package compassnews.repositories;

import compassnews.models.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UsersRepository extends MongoRepository<UserEntity, String> {
	Optional<UserEntity> findBy_id(String _id);
//	List<UserEntity> findByUsername(String username);
}
