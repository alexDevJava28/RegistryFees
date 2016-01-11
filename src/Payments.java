import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * Created by khodackovskiy on 10.12.2015.
 */
public class Payments implements SQLCommands {

    Connection conn;
    PreparedStatement psta;
    ResultSet rs;
    Statement sta;

    java.sql.Date date;
    double sum;
    Long companyNameLisId;
    Long whatPayForListId;
    JTable table;
    DefaultTableModel model;

    Payments(Connection conn, java.sql.Date date, Double sum, Long companyNameListId, Long whatPayForListId){

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
            psta.setDate(1, date);
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

                model.addRow(new Object[]{id, name, sum, what, false});
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

    public void selectForLoad(java.sql.Date dateFrom, java.sql.Date dateTo, Long company, Long purpose) {

        Object[] row = new Object[6];

        try{

            String sql = "SELECT p.DAY, c.NAME, p.SUM, pu.TEXT, p.STATUS " +
                    "FROM Payments p " +
                    "LEFT JOIN Companies c ON c.COMPANY = p.COMPANY " +
                    "LEFT JOIN Purposes pu ON pu.PURPOSE = p.PURPOSE " +
                    "WHERE p.DAY >= ? " +
                    "AND p.DAY <= ? " +
                    "AND p.COMPANY = NVL(?, p.COMPANY) " +
                    "AND p.PURPOSE = NVL(?, p.PURPOSE)";

            psta = conn.prepareStatement(sql);
            psta.setDate(1, dateFrom);
            psta.setDate(2, dateTo);
            psta.setObject(3, company);
            psta.setObject(4, purpose);

            rs = psta.executeQuery();

            while (rs.next()){

                row [0] = model.getRowCount()+1;
                row [1] = rs.getDate(1);
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
            psta.setDate(1, date);
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

    public void updateDate(java.sql.Date date) {

        try{

            String update = "UPDATE Payments SET DAY = ? WHERE STATUS = 0";

            psta = conn.prepareStatement(update);
            psta.setDate(1, date);

            psta.executeUpdate();

        }catch (SQLException e){

            e.printStackTrace();

        }finally {

            if (psta != null){

                try{

                    psta.close();

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

                update = "UPDATE Payments SET STATUS = 1 WHERE DAY = ? " +
                        "AND COMPANY = ? " +
                        "AND SUM = ? " +
                        "AND PURPOSE = ?";
            }else {

                update = "UPDATE Payments SET STATUS = 0 WHERE DAY = ? " +
                        "AND COMPANY = ? " +
                        "AND SUM = ? " +
                        "AND PURPOSE = ?";
            }

            psta = conn.prepareStatement(update);
            psta.setDate(1, date);
            psta.setLong(2, companyNameLisId);
            psta.setDouble(3, sum);
            psta.setLong(4, whatPayForListId);
            psta.executeUpdate();

        }catch (SQLException e){

            e.printStackTrace();

        }finally {

            if (psta != null){

                try{

                    psta.close();

                }catch (SQLException e){

                    e.printStackTrace();
                }

            }

        }
    }

    public Long getPayment(){

        Long payment = null;

        try {

            String select = "SELECT PAYMENT FROM Payments WHERE " +
                    "STATUS = 0 " +
                    "AND DAY = ? " +
                    "AND COMPANY = ? " +
                    "AND SUM = ?" +
                    "AND PURPOSE = ?";

            psta = conn.prepareStatement(select);
            psta.setDate(1, date);
            psta.setLong(2, companyNameLisId);
            psta.setDouble(3, sum);
            psta.setLong(4, whatPayForListId);

            try (ResultSet result = psta.executeQuery())
            {
                if (result.next()){

                    payment = result.getLong(1);
                }
            }

        }catch (SQLException e){

            e.printStackTrace();

        }finally {

            if (psta != null){

                try{

                    psta.close();

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
