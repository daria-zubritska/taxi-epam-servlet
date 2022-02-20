package model.service;

import model.OrderDAO;
import model.entity.Car;

import java.math.BigDecimal;
import java.util.List;

public class OrderService {

    private final static double DISCOUNT = 30;

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

    public static BigDecimal costForTwoCars(List<Car> cars, String loc_from, String loc_to) {
        BigDecimal costPerK = cars.get(0).getCost();

        costPerK.add(cars.get(1).getCost());

        return cost(costPerK, loc_from, loc_to);
    }

    public static BigDecimal costWithDiscountForTwoCars(BigDecimal idealCost, List<Car> cars, String loc_from, String loc_to) {
        BigDecimal costPerK = cars.get(0).getCost();

        costPerK.add(cars.get(1).getCost());

        return costWithDiscount(idealCost, costPerK, loc_from, loc_to);
    }


}
