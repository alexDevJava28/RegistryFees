/**
 * Created by khodackovskiy on 07.12.2015.
 */
import java.sql.*;
import java.util.Locale;

public class DBConnector {

    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";
    static final String DB_URL = "jdbc:oracle:thin:ALEX/007007@localhost:1521:XE";

    static Connection conn = null;

    //method for get and return connection to the database
    public static Connection getConn () {


        try {

            Class.forName(JDBC_DRIVER);
            Locale.setDefault(Locale.ENGLISH);
            conn = DriverManager.getConnection(DB_URL);

        }catch (ClassNotFoundException e){

            System.out.println("Connection FAILED!");

        }catch (SQLException r){

            System.out.println("SQLException in getConn method");
        }

        return conn;
    }
}

