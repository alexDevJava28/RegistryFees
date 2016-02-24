package data; /**
 * Created by khodackovskiy on 07.12.2015.
 */

import org.apache.derby.drda.NetworkServerControl;

import javax.swing.*;
import java.net.InetAddress;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Locale;

public class DBConnector {

    private static Connection conn;
    private static NetworkServerControl server;
    private static final String driver = "org.apache.derby.jdbc.ClientDriver";
    private static final String url = "jdbc:derby://";
    private static String host = "localhost";
    private static String port = "1527";
    private static String dbName = "alexDB";

    public DBConnector(String host, String port, String dbName) {

        this.host = host;
        this.port = port;
        this.dbName = dbName;

    }

    public static Connection getConn(){

        if (!dbExists()){

            try {

                server = new NetworkServerControl();
                server.start(null);

                Class.forName(driver);

                //connect to database or create it
                conn = DriverManager.getConnection(url + host + ":" + port + "/" + dbName + ";create=true");

            }catch (ClassNotFoundException cnfe){

                JOptionPane.showMessageDialog(null, cnfe);

            }catch (SQLException sqle){

                JOptionPane.showMessageDialog(null, sqle);

            }catch (Exception e){

                JOptionPane.showMessageDialog(null, e);
            }

        }

        return conn;
    }

    public static void closeConn(Connection conn){

        try{

            conn.close();
            server.shutdown();

        }catch (SQLException sqle){

            JOptionPane.showMessageDialog(null, sqle);

        }catch (Exception e){

            JOptionPane.showMessageDialog(null, e);
        }
    }

    private static boolean dbExists(){

        boolean exists = false;

        try{

            Class.forName(driver);

            //connect to database or create it
            conn = DriverManager.getConnection(url + host + ":" + port + "/" + dbName);

        }catch (Exception e){

            //if database didn't create, doing nothing
        }

        return exists;
    }


}

