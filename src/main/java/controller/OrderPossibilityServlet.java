package controller;

import model.CarDAO;
import model.OrderDAO;
import model.dto.DoubleOrderDTO;
import model.dto.OrderDTO;
import model.entity.User;
import org.apache.log4j.Logger;
import utils.RequestUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

import static filter.LoginFilter.USER_ATTRIBUTE;

@WebServlet(name = "OrderPossibilityServlet", value = "/orderSubmit")
public class OrderPossibilityServlet extends HttpServlet {

    static Logger log = Logger.getLogger(OrderPossibilityServlet.class);

    private static String PATH = "/WEB-INF/pages/orderPossibility.jsp";

    private final static String CHOICE_ATTRIBUTE = "orderChoice";
    private static final String DOUBLE_ORDER_ATTRIBUTE = "doubleOrder";
    private static final String ABSENT_CHOICE_ATTRIBUTE = "absentUserChoice";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (RequestUtils.getSessionAttribute(req, USER_ATTRIBUTE, User.class) == null) {
            resp.sendRedirect("/logIn");
            return;
        }
        if(RequestUtils.getSessionAttribute(req, CHOICE_ATTRIBUTE, OrderDTO.class) == null &&
                RequestUtils.getSessionAttribute(req, DOUBLE_ORDER_ATTRIBUTE, DoubleOrderDTO.class) == null){

            resp.sendRedirect("/");
            return;
        }

        req.getRequestDispatcher(PATH).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (RequestUtils.getSessionAttribute(req, USER_ATTRIBUTE, User.class) == null) {
            resp.sendRedirect("/logIn");
            return;
        }
        if(RequestUtils.getSessionAttribute(req, CHOICE_ATTRIBUTE, OrderDTO.class) == null &&
                RequestUtils.getSessionAttribute(req, DOUBLE_ORDER_ATTRIBUTE, DoubleOrderDTO.class) == null){

            resp.sendRedirect("/makeOrder");
            return;
        }

        HttpSession session = req.getSession();

        if (req.getParameter("submit") != null) {

            OrderDTO order = (OrderDTO) session.getAttribute(CHOICE_ATTRIBUTE);

            if(order !=null) {

                if(session.getAttribute(ABSENT_CHOICE_ATTRIBUTE) == null){
                    OrderDAO.addOrder(order.getUserId(), order.getCarId(),
                            order.getLocationFrom(), order.getLocationTo(),
                            order.getOrderDate(), order.getPassengers(), order.getCost());
                } else{
                    OrderDAO.addOrder(order.getUserId(), order.getCarId(),
                            order.getLocationFrom(), order.getLocationTo(),
                            order.getOrderDate(), order.getPassengers(), order.getCostWithDiscount());
                }

                CarDAO.updateStatus(order.getCarId(), 2);

            } else {
                DoubleOrderDTO dOrder = (DoubleOrderDTO) session.getAttribute(DOUBLE_ORDER_ATTRIBUTE);

                OrderDTO order1 = dOrder.getOrder1();
                OrderDTO order2 = dOrder.getOrder2();

                BigDecimal costOneCar = dOrder.getCostWithDiscount().divide(BigDecimal.valueOf(2));

                OrderDAO.addOrder(order1.getUserId(), order1.getCarId(),
                        order1.getLocationFrom(), order1.getLocationTo(),
                        order1.getOrderDate(), order1.getPassengers(), costOneCar);

                OrderDAO.addOrder(order2.getUserId(), order2.getCarId(),
                        order2.getLocationFrom(), order2.getLocationTo(),
                        order2.getOrderDate(), order1.getPassengers(), costOneCar);

                CarDAO.updateStatus(order1.getCarId(), 2);
                CarDAO.updateStatus(order2.getCarId(), 2);
            }

            resp.sendRedirect("/");

        } else if (req.getParameter("cancel") != null) {
            session.removeAttribute(CHOICE_ATTRIBUTE);

            resp.sendRedirect("/");
        }

    }

    private void passErrorToView(HttpServletRequest req, HttpServletResponse resp, Map<String, String> viewAttributes) throws ServletException, IOException {
        for (Map.Entry<String, String> entry : viewAttributes.entrySet())
            req.setAttribute(entry.getKey(), entry.getValue());

        doGet(req, resp);
    }
}