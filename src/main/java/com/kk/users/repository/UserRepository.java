package com.kk.users.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.kk.users.entity.User;


public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT e FROM Employee e WHERE " +
    "LOWER(e.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
    "OR LOWER(e.occupation) LIKE LOWER(CONCAT('%', :keyword, '%'))")
List<User> search(String keyword);

Optional<User> findByFullName(String fullName);

}