package controller.admin;

import model.OrderDAO;
import model.entity.Order;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "StatisticsServlet", value = "/statistics")
public class StatisticsServlet extends HttpServlet {

    static Logger log = Logger.getLogger(StatisticsServlet.class);

    private static String PATH = "/WEB-INF/pages/statistics.jsp";
    private final static String STATISTICS_ATTRIBUTE = "statsList";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        int currentPage;
        int recordsPerPage;

        String userName = req.getParameter("userName");
        String date = req.getParameter("date");

        String order = req.getParameter("orderBy");

        if (order != null) {
            if (order.equals("noOrder")) {
                req.setAttribute("currOrder", order);
            } else if (order.equals("byDate")) {
                req.setAttribute("currOrder", order);
            } else if (order.equals("byCost")) {
                req.setAttribute("currOrder", order);
            }
        }

        if (userName != null && date != null) {

            if (!userName.equals("") && date.equals("")) {
                req.setAttribute("currFilter", "user");
                req.setAttribute("userField", userName);
            } else if (userName.equals("") && !date.equals("")) {
                req.setAttribute("currFilter", "date");
                req.setAttribute("dateField", date);
            } else if (!userName.equals("") && !date.equals("")) {
                req.setAttribute("currFilter", "all");
                req.setAttribute("userField", userName);
                req.setAttribute("dateField", date);
            }
        }

        if (req.getParameter("currentPage") == null || req.getParameter("recordsPerPage") == null) {
            currentPage = 1;
            recordsPerPage = 4;
        } else {
            currentPage = Integer.parseInt(req.getParameter("currentPage"));
            recordsPerPage = Integer.parseInt(req.getParameter("recordsPerPage"));
        }

        List<Order> orders;
        int rows;

        if (req.getAttribute("currFilter") == null) {

            if(order != null) {
                switch (order) {
                    case ("byCost"): {
                        orders = OrderDAO.getOrdersNoFilterOrderedCost(currentPage * recordsPerPage - recordsPerPage,
                                recordsPerPage);
                        break;
                    }
                    case ("byDate"): {
                        orders = OrderDAO.getOrdersNoFilterOrderedDate(currentPage * recordsPerPage - recordsPerPage,
                                recordsPerPage);
                        break;
                    }
                    default: {
                        orders = OrderDAO.getOrdersNoFilter(currentPage * recordsPerPage - recordsPerPage,
                                recordsPerPage);
                        break;
                    }
                }
            }else{
                orders = OrderDAO.getOrdersNoFilter(currentPage * recordsPerPage - recordsPerPage,
                        recordsPerPage);
            }

            rows = OrderDAO.getNumberOfRows();
        } else if (req.getAttribute("currFilter") == "user") {

            switch (order) {
                case ("byCost"): {
                    orders = OrderDAO.getOrdersUserFilterOrderedCost(currentPage * recordsPerPage - recordsPerPage,
                            recordsPerPage, req.getAttribute("userField").toString());
                    break;
                }
                case ("byDate"): {
                    orders = OrderDAO.getOrdersUserFilterOrderedDate(currentPage * recordsPerPage - recordsPerPage,
                            recordsPerPage, req.getAttribute("userField").toString());
                    break;
                }
                default: {
                    orders = OrderDAO.getOrdersUserFilter(currentPage * recordsPerPage - recordsPerPage,
                            recordsPerPage, req.getAttribute("userField").toString());
                    break;
                }
            }

            rows = OrderDAO.getNumberOfRowsFilterUser(req.getAttribute("userField").toString());
        } else if (req.getAttribute("currFilter") == "date") {

            switch (order) {
                case ("byCost"): {
                    orders = OrderDAO.getOrdersDateFilterOrderedCost(currentPage * recordsPerPage - recordsPerPage,
                            recordsPerPage, req.getAttribute("dateField").toString());
                    break;
                }
                case ("byDate"): {
                    orders = OrderDAO.getOrdersDateFilterOrderedDate(currentPage * recordsPerPage - recordsPerPage,
                            recordsPerPage, req.getAttribute("dateField").toString());
                    break;
                }
                default: {
                    orders = OrderDAO.getOrdersDateFilter(currentPage * recordsPerPage - recordsPerPage,
                            recordsPerPage, req.getAttribute("dateField").toString());
                    break;
                }
            }

            rows = OrderDAO.getNumberOfRowsFilterDate(req.getAttribute("dateField").toString());
        } else {

            switch (order) {
                case ("byCost"): {
                    orders = OrderDAO.getOrdersUserAndDateFilterOrderedCost(currentPage * recordsPerPage - recordsPerPage,
                            recordsPerPage, req.getAttribute("userField").toString(), req.getAttribute("dateField").toString());
                    break;
                }
                case ("byDate"): {
                    orders = OrderDAO.getOrdersUserAndDateFilterOrderedDate(currentPage * recordsPerPage - recordsPerPage,
                            recordsPerPage, req.getAttribute("userField").toString(), req.getAttribute("dateField").toString());
                    break;
                }
                default: {
                    orders = OrderDAO.getOrdersUserAndDateFilter(currentPage * recordsPerPage - recordsPerPage,
                            recordsPerPage, req.getAttribute("userField").toString(), req.getAttribute("dateField").toString());
                    break;
                }
            }

            rows = OrderDAO.getNumberOfRowsFilterDateUser(req.getAttribute("dateField").toString(), req.getAttribute("userField").toString());
        }

        req.setAttribute(STATISTICS_ATTRIBUTE, orders);

        int nOfPages = rows / recordsPerPage;

        if (nOfPages % recordsPerPage > 0) {

            nOfPages++;
        }


        req.setAttribute("noOfPages", nOfPages);
        req.setAttribute("currentPage", currentPage);
        req.setAttribute("recordsPerPage", recordsPerPage);

        req.getRequestDispatcher(PATH).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
