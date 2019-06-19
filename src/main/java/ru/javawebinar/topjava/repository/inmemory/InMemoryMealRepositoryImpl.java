package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepositoryImpl.class);
    private Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS_BY_IVAN.forEach(meal -> save(meal, 1));
        MealsUtil.MEALS_BY_PETR.forEach(meal -> save(meal, 2));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        log.info("save meal {}", meal);
        repository.computeIfAbsent(userId, k -> new HashMap<>());
        Map<Integer, Meal> mealMap = repository.get(userId);
        if (meal.isNew()) {
            meal.setUserId(userId);
            meal.setId(counter.incrementAndGet());
            mealMap.put(meal.getId(), meal);
        }
        // treat case: update, but absent in storage
        else {
            Meal mealToUpdate = mealMap.get(meal.getId());
            if (mealToUpdate != null && mealToUpdate.getUserId() == userId) {
                meal.setUserId(userId);
                mealMap.put(meal.getId(), meal);
            } else meal = null;
        }
        return meal;
    }

    @Override
    public boolean delete(int id, int userId) {
        log.info("delete mealId {}", id);
        if (get(id, userId) != null) {
            repository.get(userId).remove(id);
            return true;
        } else return false;
    }

    @Override
    public Meal get(int id, int userId) {
        log.info("get meal {}", id);
        Map<Integer, Meal> meals = repository.get(userId);
        Meal meal = meals != null ? meals.get(id) : null;
        if (meal != null && meal.getUserId() == userId) {
            return meal;
        } else {
            return null;
        }
    }

    @Override
    public List<Meal> getAllWithFilter(LocalDate from, LocalDate to, int userId) {
        log.info("getAllWithFilter {} {}", from, to);
        return getSorted(userId, meal -> DateTimeUtil.isBetween(meal.getDate(), from, to));
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("getAll{}", userId);
        return getSorted(userId, meal -> true);
    }

    private List<Meal> getSorted(int userId, Predicate<Meal> filter) {
        return repository.get(userId).values().stream().
                filter(filter).
                sorted(Comparator.comparing(Meal::getDateTime).reversed()).
                collect(toList());
    }

}

