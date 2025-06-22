package com.devrahul.userservice.repository;

import com.devrahul.userservice.entities.UserInfo;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserInfo, String> {

    Optional<UserInfo> findByUserId(String user_id);
}
