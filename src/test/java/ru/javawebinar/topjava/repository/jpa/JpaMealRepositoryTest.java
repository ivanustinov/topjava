package ru.javawebinar.topjava.repository.jpa;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.Month;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.MealTestData.MEAL1;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static ru.javawebinar.topjava.util.DateTimeUtil.adjustEndDateTime;
import static ru.javawebinar.topjava.util.DateTimeUtil.adjustStartDateTime;

/**
 * //TODO add comments.
 *
 * @author Ivan Ustinov(ivanustinov1985@yandex.ru)
 * @version 1.0
 * @since 02.07.2019
 */
@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class JpaMealRepositoryTest {

    @Autowired
    private MealRepository repository;

    @Test
    public void delete() throws Exception {
        repository.delete(MEAL1_ID, USER_ID);
        assertMatch(repository.getAll(USER_ID), MEAL6, MEAL5, MEAL4, MEAL3, MEAL2);
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotFound() throws Exception {
        repository.delete(1, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotOwn() throws Exception {
        repository.delete(MEAL1_ID, ADMIN_ID);
    }

    @Test
    public void create() throws Exception {
        Meal newMeal = getCreated();
        Meal created = repository.save(newMeal, USER_ID);
        newMeal.setId(created.getId());
        assertMatch(newMeal, created);
        assertMatch(repository.getAll(USER_ID), newMeal, MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1);
    }

    @Test
    public void get() throws Exception {
        Meal actual = repository.get(ADMIN_MEAL_ID, ADMIN_ID);
        assertMatch(actual, ADMIN_MEAL1);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() throws Exception {
        repository.get(1, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void getNotOwn() throws Exception {
        repository.get(MEAL1_ID, ADMIN_ID);
    }

    @Test
    public void update() throws Exception {
        Meal updated = getUpdated();
        repository.save(updated, USER_ID);
        assertMatch(repository.get(MEAL1_ID, USER_ID), updated);
    }

    @Test(expected = NotFoundException.class)
    public void updateNotFound() throws Exception {
        repository.save(MEAL1, ADMIN_ID);
    }

    @Test
    public void getAll() throws Exception {
        assertMatch(repository.getAll(USER_ID), MEALS);
    }

    @Test
    public void getBetween() throws Exception {
        assertMatch(repository.getBetween(adjustStartDateTime(LocalDate.of(2015, Month.MAY, 30)),
                adjustEndDateTime(LocalDate.of(2015, Month.MAY, 30)), USER_ID), MEAL3, MEAL2, MEAL1);
    }
}