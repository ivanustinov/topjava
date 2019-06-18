package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface MealService {
    Meal create(Meal meal, int userId);

    void delete(int id, int userId) throws NotFoundException;

    Meal get(int id, int userId) throws NotFoundException;

    void update(Meal meal, int userId);

    List<MealTo> getAll(int userId);

    List<MealTo> getAllWithFilter(Map<String, LocalDateTime> localDateTimeMap, int userId);
}