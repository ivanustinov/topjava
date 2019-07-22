package ru.javawebinar.topjava.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;
import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;

/**
 * //TODO add comments.
 *
 * @author Ivan Ustinov(ivanustinov1985@yandex.ru)
 * @version 1.0
 * @since 21.07.2019
 */
@Controller
public class JspMealController {

    private final static int USER_ID = SecurityUtil.authUserId();

    @Autowired
    private MealService service;


    @GetMapping("/filterMeals")
    public String filterMeals(HttpServletRequest request) {
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
        List<Meal> mealsDateFiltered = service.getBetweenDates(startDate, endDate, USER_ID);
        request.setAttribute("meals", MealsUtil.getFilteredWithExcess(mealsDateFiltered, SecurityUtil.authUserCaloriesPerDay(), startTime, endTime));
        return "meals";
    }

    @GetMapping("/createMeal")
    public String toCreateMeal(HttpServletRequest request) {
        final Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        request.setAttribute("meal", meal);
        request.setAttribute("action", "create");
        return "mealForm";
    }

    @GetMapping("/deleteMeal")
    public String toDeleteMeal(HttpServletRequest request, Model model) {
        service.delete(Integer.valueOf(request.getParameter("id")), USER_ID);
        return meals(model);
    }

    @GetMapping("/updateMeal")
    public String toUpdateMeal(HttpServletRequest request) {
        final Meal meal = service.get(Integer.valueOf(request.getParameter("id")), USER_ID);
        request.setAttribute("meal", meal);
        return "mealForm";
    }

    @GetMapping("/meals")
    public String meals(Model model) {
        model.addAttribute("meals", MealsUtil.getWithExcess(service.getAll(USER_ID), SecurityUtil.authUserCaloriesPerDay()));
        return "meals";
    }

    @PostMapping("/meals")
    public String createUpdateMeal(HttpServletRequest request) throws UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));

        if (StringUtils.isEmpty(request.getParameter("id"))) {
            service.create(meal, USER_ID);
        } else {
            assureIdConsistent(meal, Integer.valueOf(request.getParameter("id")));
            service.update(meal, USER_ID);
        }
        return "redirect:meals";
    }
}
