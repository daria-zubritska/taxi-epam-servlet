package model;

import database.DBManager;
import model.entity.Order;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    public static final String SQL_ORDER_BY_ID = "SELECT * FROM orders WHERE id=?";
    public static final String SQL_CREATE_ORDER = "INSERT INTO orders(user_id, car_id, location_from, location_to, order_date, order_time, passengers, cost) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
    public static final String SQL_DELETE_ORDER = "DELETE from orders WHERE id=?";
    public static final String SQL_ORDER_BY_USER = "SELECT * FROM orders WHERE user_id=?";
    public static final String SQL_ORDER_BY_DATE = "SELECT * FROM orders WHERE order_date=?";
    public static final String SQL_GET_ORDERS = "SELECT * FROM orders";

    public static final String FIELD_ID = "id";
    public static final String FIELD_USER_ID = "user_id";
    public static final String FIELD_CAR_ID = "car_id";
    public static final String FIELD_LOCATION_FROM = "location_from";
    public static final String FIELD_LOCATION_TO = "location_to";
    public static final String FIELD_ORDER_DATE = "order_date";
    public static final String FIELD_ORDER_TIME = "order_time";
    public static final String FIELD_PASSENGERS = "passengers";
    public static final String FIELD_COST = "cost";


    private static Order mapResultSet(ResultSet rs) {
        Order order = null;
        try {
            order = new Order();
            order.setId(rs.getInt(FIELD_ID));
            order.setUserId(rs.getInt(FIELD_USER_ID));
            order.setCarId(rs.getInt(FIELD_CAR_ID));
            order.setLocationFrom(rs.getString(FIELD_LOCATION_FROM));
            order.setLocationTo(rs.getString(FIELD_LOCATION_TO));
            order.setOrderDate(rs.getDate(FIELD_ORDER_DATE).toLocalDate());
            order.setOrderTime(rs.getTime(FIELD_ORDER_TIME).toLocalTime());
            order.setPassengers(rs.getInt(FIELD_PASSENGERS));
            order.setCost(BigDecimal.valueOf(rs.getLong(FIELD_COST)));
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
        return order;
    }

    public static Order findOrderById(long id) {
        Order order = null;
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_ORDER_BY_ID)) {
            pst.setLong(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if(rs.next())
                    order = mapResultSet(rs);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return order;
    }

    public static boolean addOrder(int user_id, int car_id, String location_from, String location_to, LocalDate order_date, LocalDateTime order_time, int passengers, BigDecimal cost) {
        boolean result = false;
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_CREATE_ORDER)) {

            pst.setInt(1, user_id);
            pst.setInt(2, car_id);
            pst.setString(3, location_from);
            pst.setString(4, location_to);
            pst.setDate(5, Date.valueOf(order_date));
            pst.setTime(6, Time.valueOf(String.valueOf(order_time)));
            pst.setInt(7, passengers);
            pst.setLong(8, Long.parseLong(cost.toString()));

            result = pst.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return result;
    }

    public static boolean deleteOrder(int id){
        boolean result = false;
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_DELETE_ORDER)) {

            pst.setInt(1, id);

            result = pst.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return result;
    }

    public static List<Order> findOrdersByUser(int user_id) {
        List<Order> orders = new ArrayList<>();
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_ORDER_BY_USER)) {
            pst.setInt(1, user_id);
            try(ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    orders.add(mapResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return orders;
    }

    public static List<Order> findOrdersByDate(LocalDate date) {
        List<Order> orders = new ArrayList<>();
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_ORDER_BY_DATE)) {
            pst.setDate(1, Date.valueOf(date));
            try(ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    orders.add(mapResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return orders;
    }

    public static List<Order> getAll() {
        List<Order> orders = new ArrayList<>();
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_GET_ORDERS)) {
            try(ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    orders.add(mapResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return orders;
    }

}
