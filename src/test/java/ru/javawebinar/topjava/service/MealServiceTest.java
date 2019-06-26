package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;

/**
 * //TODO add comments.
 *
 * @author Ivan Ustinov(ivanustinov1985@yandex.ru)
 * @version 1.0
 * @since 26.06.2019
 */
@ContextConfiguration({
        "classpath:spring/spring-db.xml",
        "classpath:spring/spring-web-service.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;


    @Test
    public void get() {
        Meal meal = service.get(MEAL_ID, USER_ID);
        assertMatch(meal, MEAL_BREAKFAST);
    }

    @Test
    public void delete() {
        service.delete(MEAL_ID, USER_ID);
        assertMatch(service.getAll(USER_ID), MEAL_LUNCH, MEAL_DINNER);
    }

    @Test
    public void getBetweenDates() {
        List<Meal> betweenDates = service.getBetweenDates(LocalDate.parse("2015-06-29"), LocalDate.parse("2015-06-30"), USER_ID);
        assertMatch(betweenDates, MEAL_DINNER);
    }


    @Test
    public void getAll() {
        List<Meal> meals = service.getAll(USER_ID);
        assertMatch(meals, MEAL_BREAKFAST, MEAL_DINNER, MEAL_LUNCH);
    }

    @Test
    public void update() {
        Meal meal = new Meal(MEAL_BREAKFAST);
        meal.setDescription("Updated");
        service.update(meal, USER_ID);
        assertMatch(service.get(MEAL_ID, USER_ID), meal);
    }

    @Test
    public void create() {
        Meal meal = new Meal(LocalDateTime.now(), "Завтрак", 200);
        service.create(meal, USER_ID);
        assertMatch(service.getAll(USER_ID), meal, MEAL_BREAKFAST, MEAL_DINNER, MEAL_LUNCH);
    }

    @Test(expected = NotFoundException.class)
    public void getForeignMeal() {
        service.get(MEAL_ID, 100001);
    }

    @Test(expected = NotFoundException.class)
    public void deleteForeignMeal() {
        service.delete(MEAL_ID, 100001);
    }

    @Test(expected = NotFoundException.class)
    public void updateForeignMeal() {
        Meal meal = new Meal(MEAL_BREAKFAST);
        meal.setDescription("Updated");
        service.update(meal, 100001);
    }
}