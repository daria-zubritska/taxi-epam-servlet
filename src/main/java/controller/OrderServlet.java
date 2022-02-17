package controller;

import model.dto.OrderDTO;
import org.apache.log4j.Logger;

import model.CarDAO;
import model.OrderDAO;
import model.service.OrderService;
import model.entity.Car;
import model.entity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.*;

@WebServlet(name = "OrderServlet", value = "/makeOrder")
public class OrderServlet extends HttpServlet {

    private static String PATH = "/WEB-INF/pages/order.jsp";
    private final static String LOCATION_ATTRIBUTE = "locationList";
    private final static String ERROR_ATTRIBUTE = "error";
    private final static String CHOICE_ATTRIBUTE = "orderChoice";
    private static final String USER_ATTRIBUTE = "user";
    private static final String ABSENT_CHOICE_ATTRIBUTE = "absentUserChoice";

    static Logger log = Logger.getLogger(OrderServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setAttribute(LOCATION_ATTRIBUTE, OrderDAO.getAllLocations());

        req.getRequestDispatcher(PATH).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession(true);

        String loc_from = req.getParameter("loc_from");
        String loc_to = req.getParameter("loc_to");
        String passengers = req.getParameter("passengers");
        String carClass = req.getParameter("class");

        Map<String, String> viewAttributes = new HashMap<>();
        viewAttributes.put("loc_from", loc_from);
        viewAttributes.put("loc_to", loc_to);
        viewAttributes.put("passengers", passengers);
        viewAttributes.put("class", carClass);


        log.info(loc_from + " " + loc_to + " " + passengers + " " + carClass);

        if(loc_from == null || loc_to == null){
            viewAttributes.put(ERROR_ATTRIBUTE, "locationNotValid");
            passErrorToView(req, resp, viewAttributes);
            return;
        }
        if(passengers == null){
            viewAttributes.put(ERROR_ATTRIBUTE, "passengersNotValid");
            passErrorToView(req, resp, viewAttributes);
            return;
        }

        Car car = CarDAO.findAppropriateCar(carClass, Integer.parseInt(passengers));


        if(car == null){

            Car carByPass = CarDAO.findCarByPassengers(Integer.parseInt(passengers));

            if(carByPass != null){

                BigDecimal idealCost = BigDecimal.valueOf(CarDAO.findAppropriateCarCost(carClass, Integer.parseInt(passengers)));

                User user = (User) session.getAttribute(USER_ATTRIBUTE);
                Date date = new Date();

                BigDecimal cost = OrderService.cost(carByPass.getCost(), loc_from, loc_to);
                BigDecimal costWithDiscount = OrderService.costWithDiscount(idealCost, carByPass.getCost(), loc_from, loc_to);

                session.setAttribute(ABSENT_CHOICE_ATTRIBUTE, "noNeeded");

                OrderDTO order = new OrderDTO(0,
                        carByPass.getName(),
                        user.getId(),
                        carByPass.getId(),
                        date.toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate(),
                        loc_to,
                        loc_from,
                        Integer.parseInt(passengers),
                        cost);

                order.setCostWithDiscount(costWithDiscount);

                session.setAttribute(CHOICE_ATTRIBUTE, order);
            }else {


            }

            resp.sendRedirect("/orderSubmit");

        }else{

            User user = (User) session.getAttribute(USER_ATTRIBUTE);
            Date date = new Date();

            BigDecimal cost = OrderService.cost(car.getCost(), loc_from, loc_to);

            session.setAttribute(CHOICE_ATTRIBUTE, new OrderDTO(0,
                    car.getName(),
                    user.getId(),
                    car.getId(),
                    date.toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate(),
                    loc_to,
                    loc_from,
                    Integer.parseInt(passengers),
                    cost));

            resp.sendRedirect("/orderSubmit");

        }

    }

    private void passErrorToView(HttpServletRequest req, HttpServletResponse resp, Map<String, String> viewAttributes) throws ServletException, IOException {
        for(Map.Entry<String, String> entry : viewAttributes.entrySet())
            req.setAttribute(entry.getKey(), entry.getValue());

        doGet(req, resp);
    }
}