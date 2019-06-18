package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {

    private final MealService service;


    public MealRestController(MealService service) {
        this.service = service;
    }

    public void delete(int id) {
        service.delete(id, authUserId());
    }

    public Meal create(Meal meal) {
        return service.create(meal, authUserId());
    }

    public Meal get(int id) {
        return service.get(id, authUserId());
    }

    public void update(Meal meal) {
        service.update(meal, authUserId());
    }


    public List<MealTo> getAll() {
        return service.getAll(authUserId());
    }

    public List<MealTo> getAllWithFilter(Map<String, LocalDateTime> dateTimeMap) {
        return service.getAllWithFilter(dateTimeMap, authUserId());
    }


}