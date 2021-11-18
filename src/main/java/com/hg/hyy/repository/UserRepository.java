package com.hg.hyy.repository;

import org.springframework.data.repository.CrudRepository;

import com.hg.hyy.entity.User;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete
import org.springframework.data.jpa.repository.JpaRepository;

interface SysUserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}

public interface UserRepository extends CrudRepository<User, Integer> {

}