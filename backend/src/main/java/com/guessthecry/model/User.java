package com.guessthecry.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "app_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    private String passwordHash; // wachtwoord veilig opslaan (bcrypt ofzo)

    // Relatie naar stats
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserStats> stats = new ArrayList<>();

    public User(Long id, String username, String passwordHash, List<UserStats> stats) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.stats = stats;
    }

    public User() {}

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPasswordHash() {
        return passwordHash;
    }
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
    public List<UserStats> getStats() {
        return stats;
    }
    public void setStats(List<UserStats> stats) {
        this.stats = stats;
    }
}