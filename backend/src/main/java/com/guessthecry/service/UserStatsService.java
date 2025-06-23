package com.guessthecry.service;

import com.guessthecry.model.User;
import com.guessthecry.model.UserStats;
import com.guessthecry.repository.UserStatsRepository;
import com.guessthecry.webservices.authentication.UserStatsDTO;
import jakarta.inject.Inject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service //Spring Service Bean
public class UserStatsService {

    @Inject
    private UserStatsRepository statsRepository;

    // @Transactional this will make sure everything is done in one database request, if it goes wrong it will rollback
    @Transactional
    public UserStatsDTO updateUserStats(User user, String difficulty, int finalScore, int totalQuestions) {

        // look for stats of user with that difficulty. if it doesnt exist then create one.
        UserStats stats = statsRepository.findByUserAndDifficulty(user, difficulty)
                .orElse(new UserStats(user, difficulty));

        // calculate new stat
        double accuracyOfThisGame = ((double) finalScore / totalQuestions) * 100.0;

        // use of 'running average' formula for precise calc
        int oldGamesPlayed = stats.getGamesPlayed();
        double oldAverageAccuracy = stats.getAverageAccuracy();

        int newGamesPlayed = oldGamesPlayed + 1;
        double newAverageAccuracy = ((oldAverageAccuracy * oldGamesPlayed) + accuracyOfThisGame) / newGamesPlayed;

        // update stats object
        stats.setGamesPlayed(newGamesPlayed);
        stats.setAverageAccuracy(newAverageAccuracy);

        // save stats to database
        UserStats savedStats = statsRepository.save(stats);

        // give dto back with new stats
        return new UserStatsDTO(
                savedStats.getDifficulty(),
                savedStats.getGamesPlayed(),
                savedStats.getAverageAccuracy(),
                finalScore // give score of current game
        );
    }
}