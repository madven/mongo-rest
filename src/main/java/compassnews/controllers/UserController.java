package compassnews.controllers;

import com.google.common.base.Strings;
import compassnews.exceptions.UserNotFoundException;
import compassnews.models.UserEntity;
import compassnews.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UsersRepository repository;

	@Autowired
	private MongoTemplate mongoTemplate;

	@GetMapping(value = "/")
	public List<UserEntity> getAllUsers() {
		return repository.findAll();
	}

	@PostMapping(value = "/_search")
	public List<UserEntity> searchUser(
			@RequestParam(value = "type", required = false) final String type,
			@RequestParam(value = "platform", required = false) final String platform,
			@RequestParam(value = "validated", required = false) final Boolean validated,
			@RequestParam(value = "subscribed", required = false) final String subscribed,
			@RequestParam(value = "paying", required = false) final Boolean paying,
			@RequestParam(value = "facebook", required = false) final Boolean facebook,
			@RequestParam(value = "before", required = false) final Long before,
			@RequestParam(value = "after", required = false) final Long after,
			@RequestParam(value = "email", required = false) final String username,
//			@RequestParam(value = "uni", required = false) final String uni,
//			@RequestParam(value = "page", required = false) final Integer page,
			@RequestParam(value = "geo", required = false) final String geoSearch) {

		Query dynamicQuery = new Query();

		if (!Strings.isNullOrEmpty(username))
			dynamicQuery.addCriteria(Criteria.where("username").is(username));

		return mongoTemplate.find(dynamicQuery, UserEntity.class);
	}

	@GetMapping(value = "/{id}")
	public UserEntity getUserById(@PathVariable("id") String id) {
//		return repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
		return repository.findBy_id(id).orElseThrow(() -> new UserNotFoundException(id));
	}

	@PutMapping(value = "/{id}")
	public UserEntity modifyUserById(@PathVariable("id") String id, @Valid @RequestBody UserEntity user) {
		user.set_id(id);
		return repository.save(user);
	}

	@PostMapping(value = "/")
	public UserEntity createUser(@Valid @RequestBody UserEntity user) {
		user.set_id(UUID.randomUUID().toString());
		return repository.save(user);
	}

	@DeleteMapping(value = "/{id}")
	public void deleteUser(@PathVariable String id) {
//		repository.deleteById(id);
		UserEntity userEntity = repository.findBy_id(id).orElseThrow(() -> new UserNotFoundException(id));
		repository.delete(userEntity);

	}

}
