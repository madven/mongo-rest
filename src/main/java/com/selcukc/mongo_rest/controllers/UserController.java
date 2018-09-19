package com.selcukc.mongo_rest.controllers;

import com.selcukc.mongo_rest.exceptions.UserNotFoundException;
import com.selcukc.mongo_rest.models.UserEntity;
import com.selcukc.mongo_rest.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UsersRepository repository;

	@GetMapping(value = "/")
	public List<UserEntity> getAllUserEntity() {
		return repository.findAll();
	}

	@GetMapping(value = "/{id}")
	public UserEntity getUserById(@PathVariable("id") String id) {
		return repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
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
		repository.deleteById(id);
	}

}
