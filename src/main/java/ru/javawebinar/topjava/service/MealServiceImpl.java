package ru.javawebinar.topjava.service;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;

@Service
public class MealServiceImpl implements MealService {

    private final MealRepository repository;

    public MealServiceImpl(MealRepository repository) {
        this.repository = repository;
    }

    @Override
    public Meal create(Meal meal, int userId) {
        return repository.save(meal, userId);
    }

    @Override
    public void delete(int id, int userId) throws NotFoundException {
        checkNotFoundWithId(repository.delete(id, userId), id);
    }

    @Override
    public Meal get(int id, int userId) throws NotFoundException {
        return checkNotFoundWithId(repository.get(id, userId), id);
    }

    @Override
    public void update(Meal meal, int id, int userId) {
        checkNotFoundWithId(repository.save(meal, userId), userId);
    }

    @Override
    public List<MealTo> getAll(int userId) {
        return MealsUtil.getWithExcess(repository.getAll(userId), authUserCaloriesPerDay());
    }

    @Override
    public List<MealTo> getAllWithFilter(LocalDate dateFrom, LocalDate dateTo, LocalTime timeFrom, LocalTime timeTo, int userId) {
        return MealsUtil.getFilteredWithExcess(repository.getAllWithFilter(dateFrom, dateTo, userId), authUserCaloriesPerDay(), timeFrom, timeTo);
    }
}