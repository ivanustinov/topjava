package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    private MealRestController mealRestController;
    private ConfigurableApplicationContext appCtx;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        mealRestController = appCtx.getBean(MealRestController.class);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");

        Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));

        log.info(meal.isNew() ? "Create {}" : "Update {}", meal);
        mealRestController.create(meal);
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete {}", id);
                mealRestController.delete(id);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                        mealRestController.get(getId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "dateTimeFilter":
                log.info("getDateTimeFilter");
                request.setAttribute("meals", mealRestController.getAllWithFilter(getDateTimeFilter(request)));
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
            default:
                log.info("getAllWithFilter");
                request.setAttribute("meals", mealRestController.getAll());
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        appCtx.close();
    }

    private Map<String, LocalDateTime> getDateTimeFilter(HttpServletRequest request) {
        Map<String, LocalDateTime> localDateTimeMap = new HashMap<>();
        String fromDate = request.getParameter("fromDate");
        String fromTime = request.getParameter("fromTime");
        String toDate = request.getParameter("toDate");
        String toTime = request.getParameter("toTime");
        LocalDateTime dateTimeFrom = LocalDateTime.parse(fromDate + "" + fromTime);
        LocalDateTime dateTimeTo = LocalDateTime.parse(toDate + "" + toTime);
        log.info("dateTimeFrom{)", dateTimeFrom);
        log.info("dateTimeTo{)", dateTimeTo);
        localDateTimeMap.put("from", dateTimeFrom);
        localDateTimeMap.put("to", dateTimeTo);
        return localDateTimeMap;
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
