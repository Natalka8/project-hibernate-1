package com.game.repository;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

@Repository(value = "memory")
public class PlayerRepositoryMemory implements IPlayerRepository {

    private static final List<Player> storage = new CopyOnWriteArrayList<>();
    private static final AtomicLong idCounter = new AtomicLong(300);

    static {
        // Инициализация тестовых данных
        storage.add(createPlayer(1L, "Huyc", "Приходящий Без Шума", Race.HOBBIT, Profession.ROGUE,
                createDate(2000, 0, 1), false, 1000));
        storage.add(createPlayer(2L, "Никраши", "НайтВульф", Race.ORC, Profession.WARRIOR,
                createDate(2001, 1, 15), false, 2000));
        storage.add(createPlayer(3L, "Зазэссоль", "Шипящая", Race.DWARF, Profession.CLERIC,
                createDate(1999, 2, 20), false, 1500));
        storage.add(createPlayer(4L, "Бэлан", "Тсе Раа", Race.DWARF, Profession.ROGUE,
                createDate(2002, 3, 10), false, 1800));
        storage.add(createPlayer(5L, "Элеонора", "Бабушка", Race.HUMAN, Profession.SORCERER,
                createDate(1998, 4, 5), false, 2500));
        storage.add(createPlayer(6L, "Эман", "Ухастый Летун", Race.ELF, Profession.SORCERER,
                createDate(2003, 5, 25), false, 1200));
        storage.add(createPlayer(7L, "Талан", "Рожденный в Бронксе", Race.GIANT, Profession.WARRIOR,
                createDate(2001, 6, 30), false, 3000));
        storage.add(createPlayer(8L, "Арилан", "Благотворитель", Race.ELF, Profession.CLERIC,
                createDate(2000, 7, 12), false, 1700));
        storage.add(createPlayer(9L, "Деракт", "Эльфёнок Красное Ухо", Race.ELF, Profession.ROGUE,
                createDate(2004, 8, 8), false, 900));
        storage.add(createPlayer(10L, "Архилл", "Смертоносный", Race.GIANT, Profession.WARRIOR,
                createDate(1997, 9, 18), false, 3500));
        storage.add(createPlayer(11L, "Зндармон", "Маленький эльфенок", Race.ELF, Profession.SORCERER,
                createDate(2005, 10, 22), false, 800));
        storage.add(createPlayer(12L, "Фазовин", "Темный Идеолог", Race.HUMAN, Profession.WARLOCK,
                createDate(1996, 11, 3), false, 2200));
        storage.add(createPlayer(13L, "Хардин", "Бедуин", Race.TROLL, Profession.WARRIOR,
                createDate(2002, 0, 14), false, 2800));
        storage.add(createPlayer(14L, "Джур", "Борец с жаждой", Race.ORC, Profession.WARRIOR,
                createDate(2001, 1, 28), false, 1900));
        storage.add(createPlayer(15L, "Грон", "Они обреченный на бой", Race.GIANT, Profession.WARRIOR,
                createDate(1995, 2, 7), false, 4000));
        storage.add(createPlayer(16L, "Морвиел", "Копье Калимы", Race.ELF, Profession.WARRIOR,
                createDate(2003, 3, 19), false, 2100));
        storage.add(createPlayer(17L, "Ниуфис", "Диамантовая", Race.HUMAN, Profession.CLERIC,
                createDate(2000, 4, 11), false, 1600));
        storage.add(createPlayer(18L, "Ырх", "Тропь Гнет ель", Race.TROLL, Profession.WARRIOR,
                createDate(1999, 5, 23), false, 3200));
        storage.add(createPlayer(19L, "Блэйк", "Серый Воин", Race.HUMAN, Profession.WARRIOR,
                createDate(2004, 6, 30), false, 2300));
    }

    // Метод для создания игрока (как у вас в примере)
    private static Player createPlayer(Long id, String name, String title, Race race,
                                       Profession profession, Date birthday, Boolean banned,
                                       Integer experience) {
        Player player = new Player();
        player.setId(id);
        player.setName(name);
        player.setTitle(title);
        player.setRace(race);
        player.setProfession(profession);
        player.setBirthday(birthday);
        player.setBanned(banned);
        player.setExperience(experience);
        return player;
    }

    // Вспомогательный метод для создания даты
    private static Date createDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    @Override
    public List<Player> getAll(int pageNumber, int pageSize) {
        int fromIndex = pageNumber * pageSize;
        if (fromIndex >= storage.size()) {
            return new ArrayList<>();
        }
        int toIndex = Math.min(fromIndex + pageSize, storage.size());
        return new ArrayList<>(storage.subList(fromIndex, toIndex));
    }

    @Override
    public int getAllCount() {
        return storage.size();
    }

    @Override
    public Player save(Player player) {
        if (player.getId() == null) {
            player.setId(idCounter.incrementAndGet());
        }
        storage.add(player);
        return player;
    }

    @Override
    public Player update(Player player) {
        Optional<Player> existingPlayer = storage.stream()
                .filter(p -> p.getId().equals(player.getId()))
                .findFirst();

        if (existingPlayer.isPresent()) {
            storage.remove(existingPlayer.get());
            storage.add(player);
            return player;
        }
        return null;
    }

    @Override
    public Player getById(Long id) {
        return storage.stream()
                .filter(player -> player.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Player findById(long id) {
        return getById(id);
    }

    @Override
    public void delete(Long id) {
        storage.removeIf(player -> player.getId().equals(id));
    }
}