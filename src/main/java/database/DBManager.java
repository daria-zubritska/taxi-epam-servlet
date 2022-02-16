package database;

import model.entity.User;

import java.sql.*;
import java.util.Properties;

import static model.UserDAO.SQL_GET_USER_BY_EMAIL;
import static model.UserDAO.mapResultSet;

public class DBManager {

    private static final String USER = "root";
    private static final String PASSWORD = "64hogove";
    private static final String dbms = "mysql";
    private static final String serverName = "localhost";
    private static final String portNumber = "3306";
    private static final String dbName = "taxiapp";

    private static DBManager instance;

    private DBManager() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static DBManager getInstance() {
        if (instance == null)
            instance = new DBManager();

        return instance;
    }

    public Connection getConnection() throws SQLException {
        Connection conn = null;
        Properties connectionProps = new Properties();
        connectionProps.put("user", USER);
        connectionProps.put("password", PASSWORD);

        conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/taxiapp?autoReconnect=true&useSSL=false",
                USER, PASSWORD);

        System.out.println("Connected to database");
        return conn;
    }

    public static void main(String[] args){
        DBManager dbm = DBManager.getInstance();

        User user = null;
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_GET_USER_BY_EMAIL)) {
            pst.setString(1, "Ivan@gmail.com");
            try (ResultSet rs = pst.executeQuery()) {
                if(rs.next())
                    user = mapResultSet(rs);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        System.out.println(user);
    }


}
