import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.sql.*;

/**
 * Created by khodackovskiy on 10.12.2015.
 */
public class Payments implements SQLCommands {

    Connection conn;
    PreparedStatement psta;
    ResultSet rs;
    Statement sta;

    String date;
    double sum;
    long companyNameLisId;
    long whatPayForListId;
    JTable table;
    DefaultTableModel model;

    Payments(Connection conn, String date, Double sum, long companyNameListId, long whatPayForListId){

        this.conn = conn;
        this.date = date;
        this.sum = sum;
        this.companyNameLisId = companyNameListId;
        this.whatPayForListId = whatPayForListId;
    }

    Payments(Connection conn, JTable table, DefaultTableModel model){

        this.conn = conn;
        this.table = table;
        this.model = model;
    }

    @Override
    public void insert() {

        try {

            String insert = "INSERT INTO Payments VALUES (PAYMENTS_SEQ.nextval, ?, ?, ?, ?, 0)";

            psta = conn.prepareStatement(insert);
            psta.setString(1, date);
            psta.setLong(2, companyNameLisId);
            psta.setDouble(3, sum);
            psta.setLong(4, whatPayForListId);


            psta.executeUpdate();

        }catch (SQLException e){

            e.printStackTrace();

        }finally {

            if (psta != null) {

                try {

                    psta.close();

                } catch (SQLException q) {

                    q.printStackTrace();
                }
            }
        }

    }

    public void select(int status) {

        try{

                String sql="SELECT c.NAME, p.SUM, pu.TEXT " +
                        "FROM Payments p " +
                        "LEFT JOIN Companies c ON c.COMPANY = p.COMPANY " +
                        "LEFT JOIN Purposes pu ON pu.PURPOSE = p.PURPOSE " +
                        "WHERE p.STATUS = " + status;

                sta = conn.createStatement();
                rs = sta.executeQuery(sql);

            while (rs.next()){

                int id = model.getRowCount()+1;
                String name = rs.getString(1);
                double sum = rs.getDouble(2);
                String what = rs.getString(3);

                model.addRow(new Object[]{id, name, sum, what});
            }
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null,e);
        }
        finally {

            if (rs!= null) try { rs.close(); } catch (SQLException e) {}
            if (psta != null) try { psta.close(); } catch (SQLException e) {}

        }

        table.setModel(model);
    }

    public void selectForLoad(String dateFrom, String dateTo, long company, long purpose) {

        String sql = "";
        Object[] row = new Object[6];

        try{

            if (company == 0 & purpose == 0) {

                sql = "SELECT p.DAY, c.NAME, p.SUM, pu.TEXT, p.STATUS " +
                        "FROM Payments p " +
                        "LEFT JOIN Companies c ON c.COMPANY = p.COMPANY " +
                        "LEFT JOIN Purposes pu ON pu.PURPOSE = p.PURPOSE " +
                        "WHERE p.DAY >= '" + dateFrom + "' " +
                        "AND p.DAY <= '" + dateTo + "'";

            }else if (purpose == 0){

                sql = "SELECT p.DAY, c.NAME, p.SUM, pu.TEXT, p.STATUS " +
                        "FROM Payments p " +
                        "LEFT JOIN Companies c ON c.COMPANY = p.COMPANY " +
                        "LEFT JOIN Purposes pu ON pu.PURPOSE = p.PURPOSE " +
                        "WHERE p.DAY >= '" + dateFrom + "' " +
                        "AND p.DAY <= '" + dateTo + "' " +
                        "AND p.COMPANY = " + company;
            }else if (company == 0) {

                sql = "SELECT p.DAY, c.NAME, p.SUM, pu.TEXT, p.STATUS " +
                        "FROM Payments p " +
                        "LEFT JOIN Companies c ON c.COMPANY = p.COMPANY " +
                        "LEFT JOIN Purposes pu ON pu.PURPOSE = p.PURPOSE " +
                        "WHERE p.DAY >= '" + dateFrom + "' " +
                        "AND p.DAY <= '" + dateTo + "' " +
                        "AND p.PURPOSE = " + purpose;
            }else {

                sql="SELECT p.DAY, c.NAME, p.SUM, pu.TEXT, p.STATUS " +
                        "FROM Payments p " +
                        "LEFT JOIN Companies c ON c.COMPANY = p.COMPANY " +
                        "LEFT JOIN Purposes pu ON pu.PURPOSE = p.PURPOSE " +
                        "WHERE p.DAY >= '" + dateFrom + "' " +
                        "AND p.DAY <= '" + dateTo + "' " +
                        "AND p.COMPANY = " + company +
                        " AND p.PURPOSE = " + purpose;
            }

            sta = conn.createStatement();
            rs = sta.executeQuery(sql);

            while (rs.next()){

                row [0] = model.getRowCount()+1;
                row [1] = rs.getString(1);
                row [2] = rs.getString(2);
                row [3] = rs.getDouble(3);
                row [4] = rs.getString(4);
                row [5] = rs.getBoolean(5);

                model.addRow(row);
            }
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null,e);
        }
        finally {

            if (rs!= null) try { rs.close(); } catch (SQLException e) {}
            if (psta != null) try { psta.close(); } catch (SQLException e) {}

        }

        table.setModel(model);
    }

    @Override
    public void delete() {

        try {

            String delete = "DELETE FROM Payments WHERE STATUS = 0 " +
                    "AND DAY = ? " +
                    "AND COMPANY = ? " +
                    "AND SUM = ? " +
                    "AND PURPOSE = ?";

            psta = conn.prepareStatement(delete);
            psta.setString(1, date);
            psta.setLong(2, companyNameLisId);
            psta.setDouble(3, sum);
            psta.setLong(4, whatPayForListId);

            psta.executeUpdate();

        }catch (SQLException e){

            e.printStackTrace();
        }

    }

    public void update(long payment, Object newSum) {

        try{

            String update = "UPDATE Payments SET SUM = " + newSum + " WHERE PAYMENT = " + payment;

            sta = conn.createStatement();
            sta.executeUpdate(update);

        }catch (SQLException e){

            e.printStackTrace();

        }finally {

            if (sta != null){

                try{

                    sta.close();

                }catch (SQLException e){

                    e.printStackTrace();
                }

            }

        }
    }

    public void updateDate(String date) {

        try{

            String update = "UPDATE Payments SET DAY = '" + date + "' WHERE STATUS = 0 " +
                    "AND DAY <> '" + date + "'";

            sta = conn.createStatement();
            sta.executeUpdate(update);

        }catch (SQLException e){

            e.printStackTrace();

        }finally {

            if (sta != null){

                try{

                    sta.close();

                }catch (SQLException e){

                    e.printStackTrace();
                }

            }

        }
    }

    public void updateStatus(boolean status) {

        String update;

        try{

            if (status) {

                update = "UPDATE Payments SET STATUS = 1 WHERE DAY = '" + date + "' " +
                        "AND COMPANY = " + companyNameLisId +
                        " AND SUM = " + sum +
                        " AND PURPOSE = " + whatPayForListId;
            }else {

                update = "UPDATE Payments SET STATUS = 0 WHERE DAY = '" + date + "' " +
                        "AND COMPANY = " + companyNameLisId +
                        " AND SUM = " + sum +
                        " AND PURPOSE = " + whatPayForListId;
            }

            sta = conn.createStatement();
            sta.executeUpdate(update);

        }catch (SQLException e){

            e.printStackTrace();

        }finally {

            if (sta != null){

                try{

                    sta.close();

                }catch (SQLException e){

                    e.printStackTrace();
                }

            }

        }
    }

    public long getPayment(){

        long payment = 0;

        try {

            String select = "SELECT PAYMENT FROM Payments WHERE " +
                    "STATUS = 0 " +
                    "AND DAY = '" + date + "' " +
                    "AND COMPANY = " + companyNameLisId +
                    " AND SUM = " + sum +
                    " AND PURPOSE = " + whatPayForListId;
            sta = conn.createStatement();

            try (ResultSet result = sta.executeQuery(select))
            {
                if (result.next()){

                    payment = result.getLong(1);
                }
            }

        }catch (SQLException e){

            JOptionPane.showMessageDialog(null, "Get Id Error in What pay for list");
            e.printStackTrace();
        }finally {

            if (sta != null){

                try{

                    sta.close();

                }catch (SQLException e){

                    e.printStackTrace();
                }

            }

        }

        return payment;
    }

    public double getTotal (){

        double total = 0.00;

        try{

            String sql = "SELECT SUM(SUM)FROM Payments WHERE STATUS = 0";

            sta = conn.createStatement();

            try (ResultSet rs = sta.executeQuery(sql)){

                if (rs.next()){

                    total = rs.getLong(1);
                }
            }
        }catch (SQLException e){

            e.printStackTrace();
        }finally {

            if (sta != null){

                try{

                    sta.close();

                }catch (SQLException e){

                    e.printStackTrace();
                }

            }

        }

        return total;
    }

    public double getTotal (String dateFrom, String dateTo){

        double total = 0.00;

        try{

            String sql = "SELECT SUM(SUM)FROM Payments WHERE DAY >= '" + date + "' " +
                    "AND DAY <= '" + dateTo + "'";

            sta = conn.createStatement();

            try (ResultSet rs = sta.executeQuery(sql)){

                if (rs.next()){

                    total = rs.getDouble(1);
                }
            }
        }catch (SQLException e){

            e.printStackTrace();
        }finally {

            if (sta != null){

                try{

                    sta.close();

                }catch (SQLException e){

                    e.printStackTrace();
                }

            }

        }

        return total;
    }

}
