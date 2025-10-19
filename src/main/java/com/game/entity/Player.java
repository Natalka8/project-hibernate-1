package com.game.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "player")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 12, nullable = false)
    private String name;

    @Column(name = "title", length = 30)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "race")
    private Race race;

    @Enumerated(EnumType.STRING)
    @Column(name = "profession")
    private Profession profession;

    @Column(name = "birthday")
    @Temporal(TemporalType.DATE)
    private Date birthday;

    @Column(name = "banned")
    private Boolean banned;

    @Column(name = "experience")
    private Integer experience;

    @Column(name = "level")
    private Integer level;

    @Column(name = "untilNextLevel")
    private Integer untilNextLevel;

    // Конструктор по умолчанию (обязателен для Hibernate)
    public Player() {}

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Race getRace() { return race; }
    public void setRace(Race race) { this.race = race; }

    public Profession getProfession() { return profession; }
    public void setProfession(Profession profession) { this.profession = profession; }

    public Date getBirthday() { return birthday; }
    public void setBirthday(Date birthday) { this.birthday = birthday; }

    public Boolean getBanned() { return banned; }
    public void setBanned(Boolean banned) { this.banned = banned; }

    public Integer getExperience() { return experience; }
    public void setExperience(Integer experience) {
        this.experience = experience;
        calculateLevel();
    }

    public Integer getLevel() { return level; }
    public void setLevel(Integer level) { this.level = level; }

    public Integer getUntilNextLevel() { return untilNextLevel; }
    public void setUntilNextLevel(Integer untilNextLevel) { this.untilNextLevel = untilNextLevel; }

    // Метод для расчета уровня
    private void calculateLevel() {
        if (experience == null) return;

        int lvl = (int) (Math.sqrt(2500 + 200 * experience) - 50) / 100;
        this.level = lvl;

        if (level != null) {
            this.untilNextLevel = 50 * (level + 1) * (level + 2) - experience;
        }
    }
}