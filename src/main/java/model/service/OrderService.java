package model.service;

import model.OrderDAO;

import java.math.BigDecimal;

public class OrderService {

    private final static double DISCOUNT = 20;

    public static BigDecimal cost(BigDecimal costPerK, String loc_from, String loc_to) {
        BigDecimal cost = costPerK;

        BigDecimal dist = BigDecimal.valueOf(OrderDAO.getDistance(loc_from, loc_to));

        return cost.multiply(dist);
    }

    public static BigDecimal costWithDiscount(BigDecimal idealCost, BigDecimal costPerK, String loc_from, String loc_to) {
        double cost = costPerK.doubleValue();
        double costIdeal = idealCost.doubleValue();

        double dist = OrderDAO.getDistance(loc_from, loc_to);

        double diskVal = cost/100 * DISCOUNT;

        cost = cost*dist;

        return BigDecimal.valueOf(cost - diskVal);
    }


}