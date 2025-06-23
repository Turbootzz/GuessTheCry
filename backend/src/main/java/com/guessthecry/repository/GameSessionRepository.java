package com.guessthecry.repository;

import com.guessthecry.model.GameSession;
import com.guessthecry.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface GameSessionRepository extends JpaRepository<GameSession, UUID> {
    Optional<GameSession> findByIdAndUser(UUID id, User user);
}
