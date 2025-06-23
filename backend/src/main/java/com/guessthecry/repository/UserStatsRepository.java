package com.guessthecry.repository;

import com.guessthecry.model.User;
import com.guessthecry.model.UserStats;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserStatsRepository extends JpaRepository<UserStats, Long> {
    Optional<UserStats> findByUserAndDifficulty(User user, String difficulty);
    java.util.List<UserStats> findAllByUser(User user);
}