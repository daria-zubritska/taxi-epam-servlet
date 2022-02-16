package model;

import database.DBManager;
import model.entity.Car;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CarDAO {

    public static final String SQL_GET_CAR_BY_ID = "SELECT * FROM cars LEFT JOIN status ON cars.status_id=status.status_id LEFT JOIN car_type ON cars.type_id = car_type.type_id WHERE id=?";
    public static final String SQL_GET_CAR_BY_TYPE = "SELECT * FROM cars LEFT JOIN status ON cars.status_id=status.status_id LEFT JOIN car_type ON cars.type_id = car_type.type_id WHERE type_name=?";
    public static final String SQL_GET_ALL_CARS = "SELECT * FROM cars LEFT JOIN status ON cars.status_id=status.status_id LEFT JOIN car_type ON cars.type_id = car_type.type_id";
    public static final String SQL_GET_CAR_BY_PASSENGERS = "SELECT * FROM cars LEFT JOIN status ON cars.status_id=status.status_id LEFT JOIN car_type ON cars.type_id = car_type.type_id WHERE passengers=?";
    public static final String SQL_UPDATE_STATUS = "UPDATE cars SET cars.status_id=? WHERE cars.id=?";

    private static final String FIELD_ID = "id";
    private static final String FIELD_NAME = "name";
    private static final String FIELD_COST = "cost";
    private static final String FIELD_STATUS = "status";
    private static final String FIELD_TYPE = "type_name";
    private static final String FIELD_PASSENGERS = "passengers";

    private static Car mapResultSet(ResultSet rs) {
        Car car = null;
        try {
            car = new Car();
            car.setId(rs.getInt(FIELD_ID));
            car.setName(rs.getString(FIELD_NAME));
            car.setCost(BigDecimal.valueOf(rs.getLong(FIELD_COST)));
            car.setStatus(Car.Status.valueOf(rs.getString(FIELD_STATUS).toUpperCase()));
            car.setCategory(Car.CarType.valueOf(rs.getString(FIELD_TYPE).toUpperCase()));
            car.setPassengers(rs.getInt(FIELD_PASSENGERS));

        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
        return car;
    }

    public static Car findCarById(long id) {
        Car car = null;
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_GET_CAR_BY_ID)) {
            pst.setLong(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if(rs.next())
                    car = mapResultSet(rs);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return car;
    }

    public static Car findCarByType(String type) {
        Car car = null;
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_GET_CAR_BY_TYPE)) {
            pst.setString(1, type);
            try (ResultSet rs = pst.executeQuery()) {
                if(rs.next())
                    car = mapResultSet(rs);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return car;
    }

    public static void updateStatus(long carId, int statusId) {
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_UPDATE_STATUS)) {
            pst.setInt(1, statusId);
            pst.setLong(2, carId);
            pst.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static List<Car> findCarsByPassengers(int passengers) {
        List<Car> cars = new ArrayList<>();
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_GET_CAR_BY_PASSENGERS)) {
            pst.setInt(1, passengers);
            try(ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    cars.add(mapResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return cars;
    }

    public static List<Car> getAllCars() {
        List<Car> cars = new ArrayList<>();
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_GET_ALL_CARS)) {
            try(ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    cars.add(mapResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return cars;
    }


}

