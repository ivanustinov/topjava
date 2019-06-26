package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

/**
 * //TODO add comments.
 *
 * @author Ivan Ustinov(ivanustinov1985@yandex.ru)
 * @version 1.0
 * @since 26.06.2019
 */
public class MealTestData {
    public static final int USER_ID = START_SEQ;
    public static final int MEAL_ID = START_SEQ + 2;
    public static final Meal MEAL_BREAKFAST = new Meal(MEAL_ID, LocalDateTime.parse("2015-05-30T10:00"), "Завтрак", 200);
    public static final Meal MEAL_LUNCH = new Meal(MEAL_ID + 1, LocalDateTime.parse("2015-05-30T13:30"), "Обед", 600);
    public static final Meal MEAL_DINNER = new Meal(MEAL_ID + 2, LocalDateTime.parse("2015-06-30T18:00"), "Ужин", 700);


    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualToComparingFieldByField(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        List<Meal> list = Arrays.asList(expected);
        list.sort(Comparator.comparing(Meal::getDateTime).reversed());
        assertMatch(actual, list);
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).isEqualTo(expected);
    }
}
