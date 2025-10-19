package com.game.controller;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/players")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    // Получение списка игроков
    @GetMapping
    public ResponseEntity<List<Player>> getPlayers(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "race", required = false) Race race,
            @RequestParam(value = "profession", required = false) Profession profession,
            @RequestParam(value = "after", required = false) Long after,
            @RequestParam(value = "before", required = false) Long before,
            @RequestParam(value = "banned", required = false) Boolean banned,
            @RequestParam(value = "minExperience", required = false) Integer minExperience,
            @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
            @RequestParam(value = "minLevel", required = false) Integer minLevel,
            @RequestParam(value = "maxLevel", required = false) Integer maxLevel,
            @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "order", required = false) PlayerOrder order) {

        List<Player> players = playerService.getAllPlayers(pageNumber, pageSize);
        return new ResponseEntity<>(players, HttpStatus.OK);
    }

    // Получение количества игроков
    @GetMapping("/count")
    public ResponseEntity<Integer> getPlayersCount(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "race", required = false) Race race,
            @RequestParam(value = "profession", required = false) Profession profession,
            @RequestParam(value = "after", required = false) Long after,
            @RequestParam(value = "before", required = false) Long before,
            @RequestParam(value = "banned", required = false) Boolean banned,
            @RequestParam(value = "minExperience", required = false) Integer minExperience,
            @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
            @RequestParam(value = "minLevel", required = false) Integer minLevel,
            @RequestParam(value = "maxLevel", required = false) Integer maxLevel) {

        int count = playerService.getPlayersCount();
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    // Создание игрока
    @PostMapping
    public ResponseEntity<Player> createPlayer(@RequestBody Player player) {
        if (player == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Валидация обязательных полей с использованием hasText вместо isEmpty
        if (!StringUtils.hasText(player.getName()) ||
                !StringUtils.hasText(player.getTitle()) ||
                player.getRace() == null ||
                player.getProfession() == null ||
                player.getBirthday() == null ||
                player.getExperience() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Player createdPlayer = playerService.createPlayer(player);
        if (createdPlayer == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(createdPlayer, HttpStatus.OK);
    }

    // Получение игрока по ID
    @GetMapping("/{id}")
    public ResponseEntity<Player> getPlayer(@PathVariable String id) {
        Long playerId = parseId(id);
        if (playerId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Player player = playerService.getPlayerById(playerId);
        if (player == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(player, HttpStatus.OK);
    }

    // Обновление игрока
    @PostMapping("/{id}")
    public ResponseEntity<Player> updatePlayer(@PathVariable String id, @RequestBody Player player) {
        Long playerId = parseId(id);
        if (playerId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Player existingPlayer = playerService.getPlayerById(playerId);
        if (existingPlayer == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Player updatedPlayer = playerService.updatePlayer(playerId, player);
        if (updatedPlayer == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(updatedPlayer, HttpStatus.OK);
    }

    // Удаление игрока
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlayer(@PathVariable String id) {
        Long playerId = parseId(id);
        if (playerId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Player existingPlayer = playerService.getPlayerById(playerId);
        if (existingPlayer == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        boolean deleted = playerService.deletePlayer(playerId);
        if (!deleted) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Вспомогательный метод для парсинга ID
    private Long parseId(String id) {
        try {
            Long playerId = Long.parseLong(id);
            return playerId > 0 ? playerId : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}