package ru.javawebinar.topjava.web;

import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;

public class SecurityUtil {
    private static Integer authsUserId;

    public static void setAuthUserId(int authUserId) {
        authsUserId = authUserId;
    }

    public static Integer authUserId() {
        return authsUserId;
    }

    public static int authUserCaloriesPerDay() {
        return DEFAULT_CALORIES_PER_DAY;
    }
}