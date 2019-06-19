package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.ValidationUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
        ValidationUtil.checkNew(meal);
        return service.create(meal, authUserId());
    }

    public Meal get(int id) {
        return service.get(id, authUserId());
    }

    public void update(Meal meal, int id) {
        service.update(meal, id, authUserId());
    }


    public List<MealTo> getAll() {
        return service.getAll(authUserId());
    }

    public List<MealTo> getAllWithFilter(String dateFrom, String timeFrom, String dateTo, String timeTo) {
        LocalDate localDateFrom = dateFrom.equals("") ? LocalDate.MIN : LocalDate.parse(dateFrom);
        LocalDate localDateTo = dateTo.equals("") ? LocalDate.MAX : LocalDate.parse(dateTo);
        LocalTime localTimeFrom = timeFrom.equals("") ? LocalTime.MIN : LocalTime.parse(timeFrom);
        LocalTime localTimeTo = timeTo.equals("") ? LocalTime.MAX : LocalTime.parse(timeTo);
        return service.getAllWithFilter(localDateFrom, localDateTo, localTimeFrom, localTimeTo, authUserId());
    }


}