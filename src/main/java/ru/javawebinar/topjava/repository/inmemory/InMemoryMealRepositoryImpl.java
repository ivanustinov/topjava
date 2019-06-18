package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.stream.Collectors.toList;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepositoryImpl.class);
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(meal -> save(meal, 1));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setUserId(userId);
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            return meal;
        }
        // treat case: update, but absent in storage
        else if (meal.getUserId() != userId) {
            return null;
        } else {
            return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
        }
    }

    @Override
    public boolean delete(int id, int userId) {
        Meal meal = repository.get(id);
        if (meal != null && meal.getUserId() != userId) {
            return false;
        } else
            return repository.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal = repository.get(id);
        if (meal != null && meal.getUserId() != userId) {
            meal = null;
        }
        return meal;
    }

    @Override
    public List<Meal> getAllWithFilter(Map<String, LocalDateTime> localDateTimeMap, int userId) {
        LocalDateTime from = localDateTimeMap.get("from");
        LocalDateTime to = localDateTimeMap.get("to");
        return getAllMealsAuthUser(userId).stream().filter(meal -> DateTimeUtil.isBetween(meal.getDate(),
                from.toLocalDate(), to.toLocalDate())).filter(meal ->
                DateTimeUtil.isBetween(meal.getTime(), from.toLocalTime(), to.toLocalTime())).collect(toList());
    }

    @Override
    public List<Meal> getAllMealsAuthUser(int userId) {
        return repository.values().stream().filter(meal -> meal.getUserId() == userId).
                sorted(Comparator.comparing(Meal::getDateTime).reversed()).collect(toList());
    }

}

