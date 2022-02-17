package controller;

import model.CarDAO;
import model.OrderDAO;
import model.dto.OrderDTO;
import model.entity.Order;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "OrderPossibilityServlet", value = "/orderSubmit")
public class OrderPossibilityServlet extends HttpServlet {

    static Logger log = Logger.getLogger(OrderPossibilityServlet.class);

    private static String PATH = "/WEB-INF/pages/orderPossibility.jsp";

    private final static String CHOICE_ATTRIBUTE = "orderChoice";
    private static final String ABSENT_CHOICE_ATTRIBUTE = "absentUserChoice";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(PATH).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession();

        if (req.getParameter("submit") != null) {

             log.info(req.getParameter("orderCheck"));

            if(session.getAttribute(ABSENT_CHOICE_ATTRIBUTE) == null) {

                OrderDTO order = (OrderDTO) session.getAttribute(CHOICE_ATTRIBUTE);

                OrderDAO.addOrder(order.getUserId(), order.getCarId(),
                        order.getLocationFrom(), order.getLocationTo(),
                        order.getOrderDate(), order.getPassengers(), order.getCost());

                CarDAO.updateStatus(order.getCarId(), 2);
            }else{

                log.info("in the other choice section");


            }

            resp.sendRedirect("/");

        } else if (req.getParameter("cancel") != null) {
            session.removeAttribute(CHOICE_ATTRIBUTE);

            resp.sendRedirect("/");
        }

    }

    private void passErrorToView(HttpServletRequest req, HttpServletResponse resp, Map<String, String> viewAttributes) throws ServletException, IOException {
        for(Map.Entry<String, String> entry : viewAttributes.entrySet())
            req.setAttribute(entry.getKey(), entry.getValue());

        doGet(req, resp);
    }
}