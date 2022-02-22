package databaseTesting;

import database.DBManager;
import model.CarDAO;
import model.OrderDAO;
import model.UserDAO;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

public class DAOTest {

    @Test
    public void CarDaoRowsTest(){
        CarDAO carDAO = CarDAO.getInstance();

        int carCount = 0;

        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement("SELECT COUNT(car_id) FROM cars")) {
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    carCount = rs.getInt("COUNT(car_id)");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        assertEquals(carCount, carDAO.getAllCars().size());
    }

    @Test
    public void OrderDaoLocRowsTest(){
        OrderDAO orderDAO = OrderDAO.getInstance();

        int locCount = 0;

        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement("SELECT COUNT(location_id) FROM locations")) {
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    locCount = rs.getInt("COUNT(location_id)");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        assertEquals(locCount, orderDAO.getAllLocations().size());
    }

    @Test
    public void UserDaoRoleRowsTest(){
        UserDAO userDAO = UserDAO.getInstance();

        int locCount = 0;

        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement("SELECT COUNT(role_id) FROM roles")) {
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    locCount = rs.getInt("COUNT(role_id)");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        assertEquals(locCount, userDAO.getAllRoles());
    }
}
