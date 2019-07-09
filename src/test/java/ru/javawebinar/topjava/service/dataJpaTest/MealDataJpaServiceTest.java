package ru.javawebinar.topjava.service.dataJpaTest;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.service.MealServiceTest;

/**
 * //TODO add comments.
 *
 * @author Ivan Ustinov(ivanustinov1985@yandex.ru)
 * @version 1.0
 * @since 08.07.2019
 */
@ActiveProfiles(value = Profiles.DATAJPA)
public class MealDataJpaServiceTest extends MealServiceTest {
}
