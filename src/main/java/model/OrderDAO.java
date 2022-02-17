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
    public static final String SQL_CREATE_ORDER = "INSERT INTO orders(user_id, car_id, location_from, location_to, order_date, passengers, cost) VALUES(?, ?, ?, ?, ?, ?, ?)";
    public static final String SQL_DELETE_ORDER = "DELETE from orders WHERE id=?";
    public static final String SQL_ORDER_BY_USER = "SELECT * FROM orders LEFT JOIN cars c on c.car_id = orders.car_id LEFT JOIN users u on u.user_id = orders.user_id WHERE user_id=?";
    public static final String SQL_ORDER_BY_DATE = "SELECT * FROM orders LEFT JOIN cars c on c.car_id = orders.car_id LEFT JOIN users u on u.user_id = orders.user_id WHERE order_date=?";
    public static final String SQL_GET_ORDERS = "SELECT * FROM orders LEFT JOIN cars c on c.car_id = orders.car_id LEFT JOIN users u on u.user_id = orders.user_id";
    public static final String SQL_GET_LOCATIONS = "SELECT * FROM locations";
    public static final String SQL_GET_LOCATION_ID = "SELECT location_id FROM locations WHERE location_name=?";
    public static final String SQL_GET_DISTANCE = "SELECT distance FROM distances WHERE location_1=? AND location_2=?";

    public static final String FIELD_ID = "id";
    public static final String FIELD_USER_ID = "user_id";
    public static final String FIELD_CAR_ID = "car_id";
    public static final String FIELD_LOCATION_FROM = "location_from";
    public static final String FIELD_LOCATION_TO = "location_to";
    public static final String FIELD_ORDER_DATE = "order_date";
    public static final String FIELD_PASSENGERS = "passengers";
    public static final String FIELD_COST = "cost";

    public static final String FIELD_CAR_NAME = "car_name";
    public static final String FIELD_USER_NAME = "user_name";


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
            order.setPassengers(rs.getInt(FIELD_PASSENGERS));
            order.setCost(BigDecimal.valueOf(rs.getLong(FIELD_COST)));
            order.setUserName(rs.getString(FIELD_USER_NAME));
            order.setCarName(rs.getString(FIELD_CAR_NAME));
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

    public static boolean addOrder(int user_id, int car_id, String location_from, String location_to, LocalDate order_date, int passengers, BigDecimal cost) {
        boolean result = false;
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_CREATE_ORDER)) {

            pst.setInt(1, user_id);
            pst.setInt(2, car_id);
            pst.setString(3, location_from);
            pst.setString(4, location_to);
            pst.setDate(5, Date.valueOf(order_date));

            pst.setInt(6, passengers);
            pst.setLong(7, Long.parseLong(cost.toString()));

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

    public static List<String> getAllLocations() {
        List<String> locations = new ArrayList<>();
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_GET_LOCATIONS)) {
            try(ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    locations.add(rs.getString("location_name"));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return locations;
    }

    private static Integer getLocIdByName(String location){
        Integer locId = null;
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_GET_LOCATION_ID)) {
            pst.setString(1, location);
            try (ResultSet rs = pst.executeQuery()) {
                if(rs.next())
                    locId = Integer.valueOf(rs.getInt("location_id"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return locId;
    }

    public static int getDistance(String loc_from, String loc_to){
        int dist = 1;

        int loc1Id = getLocIdByName(loc_from);
        int loc2Id = getLocIdByName(loc_to);

        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_GET_DISTANCE)) {

            if(loc1Id <= loc2Id){
                pst.setInt(1, loc1Id);
                pst.setInt(2, loc2Id);
            }else{
                pst.setInt(1, loc2Id);
                pst.setInt(2, loc1Id);
            }

            try (ResultSet rs = pst.executeQuery()) {
                if(rs.next())
                    dist = rs.getInt("distance");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return dist;
    }

}
