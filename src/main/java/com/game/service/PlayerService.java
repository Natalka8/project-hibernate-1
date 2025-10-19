package com.game.service;

import com.game.entity.Player;
import com.game.repository.IPlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class PlayerService {

    @Autowired
    @Qualifier("db") // или "memory" в зависимости от того, что используете
    private IPlayerRepository playerRepository;

    public List<Player> getAllPlayers(Integer pageNumber, Integer pageSize) {
        if (pageNumber == null) pageNumber = 0;
        if (pageSize == null) pageSize = 3;
        return playerRepository.getAll(pageNumber, pageSize);
    }

    public int getPlayersCount() {
        return playerRepository.getAllCount();
    }

    public Player createPlayer(Player player) {
        if (!isValidPlayer(player)) {
            return null;
        }

        // Устанавливаем уровень и untilNextLevel
        player.setExperience(player.getExperience());

        return playerRepository.save(player);
    }

    public Player getPlayerById(Long id) {
        if (id == null || id <= 0) {
            return null;
        }
        return playerRepository.findById(id); // Исправлено на findById
    }

    public Player updatePlayer(Long id, Player updateData) {
        Player existingPlayer = getPlayerById(id);
        if (existingPlayer == null) {
            return null;
        }

        if (updateData.getName() != null) {
            // Используем hasText вместо deprecated isEmpty
            if (StringUtils.hasText(updateData.getName()) &&
                    updateData.getName().length() <= 12) {
                existingPlayer.setName(updateData.getName());
            } else {
                return null;
            }
        }

        if (updateData.getTitle() != null) {
            if (StringUtils.hasText(updateData.getTitle()) &&
                    updateData.getTitle().length() <= 30) {
                existingPlayer.setTitle(updateData.getTitle());
            } else {
                return null;
            }
        }

        if (updateData.getRace() != null) {
            existingPlayer.setRace(updateData.getRace());
        }

        if (updateData.getProfession() != null) {
            existingPlayer.setProfession(updateData.getProfession());
        }

        if (updateData.getBirthday() != null) {
            if (isValidBirthday(updateData.getBirthday())) {
                existingPlayer.setBirthday(updateData.getBirthday());
            } else {
                return null;
            }
        }

        if (updateData.getBanned() != null) {
            existingPlayer.setBanned(updateData.getBanned());
        }

        if (updateData.getExperience() != null) {
            if (updateData.getExperience() >= 0 && updateData.getExperience() <= 10_000_000) {
                existingPlayer.setExperience(updateData.getExperience());
            } else {
                return null;
            }
        }

        return playerRepository.update(existingPlayer);
    }

    public boolean deletePlayer(Long id) {
        if (id == null || id <= 0) {
            return false;
        }

        Player player = getPlayerById(id);
        if (player == null) {
            return false;
        }

        playerRepository.delete(id);
        return true;
    }

    private boolean isValidPlayer(Player player) {
        return player != null &&
                StringUtils.hasText(player.getName()) &&
                player.getName().length() <= 12 &&
                StringUtils.hasText(player.getTitle()) &&
                player.getTitle().length() <= 30 &&
                player.getRace() != null &&
                player.getProfession() != null &&
                isValidBirthday(player.getBirthday()) &&
                player.getExperience() != null &&
                player.getExperience() >= 0 &&
                player.getExperience() <= 10_000_000;
    }

    private boolean isValidBirthday(Date birthday) {
        if (birthday == null) return false;

        Calendar cal = Calendar.getInstance();
        cal.set(2000, Calendar.JANUARY, 1);
        Date minDate = cal.getTime();

        cal.set(3000, Calendar.DECEMBER, 31);
        Date maxDate = cal.getTime();

        return birthday.after(minDate) && birthday.before(maxDate);
    }
}