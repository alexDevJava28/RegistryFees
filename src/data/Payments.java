package data;

import oracle.jdbc.OracleTypes;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by khodackovskiy on 10.12.2015.
 */
public class Payments implements SQLCommands {

    private Connection conn;
    private ResultSet rs;

    private byte statusNotPaid = 0;
    private java.sql.Date date;
    private double sum;
    private Long companyNameLisId;
    private Long whatPayForListId;
    private JTable table;
    private DefaultTableModel model;

    public Payments(Connection conn, java.sql.Date date, Double sum, Long companyNameListId, Long whatPayForListId){

        this.conn = conn;
        this.date = date;
        this.sum = sum;
        this.companyNameLisId = companyNameListId;
        this.whatPayForListId = whatPayForListId;
    }

    public Payments(Connection conn, JTable table, DefaultTableModel model){

        this.conn = conn;
        this.table = table;
        this.model = model;
    }

    @Override
    public void insert() {

        String sql = "{call INSERT_ROW_INTO_PAYMENTS(?, ?, ?, ?, ?)}";

        try (CallableStatement cs = conn.prepareCall(sql)){

            cs.setDate("IRIP_DAY", date);
            cs.setLong("IRIP_COMPANY", companyNameLisId);
            cs.setDouble("IRIP_SUM", sum);
            cs.setLong("IRIP_PURPOSE", whatPayForListId);
            cs.setByte("IRIP_STATUS", statusNotPaid);

            cs.executeUpdate();

        }catch (SQLException e){

            e.printStackTrace();

        }

    }

    public void select(byte status) {

        String sql = "{call AFP.SELECT_ROWS_STATUS_0(?)}";

        try(CallableStatement cs = conn.prepareCall(sql)){

            cs.setByte("SRS0_STATUS", status);

            cs.executeQuery();

            rs = cs.getResultSet();

            while (rs.next()){

                int id = model.getRowCount()+1;
                String name = rs.getString(1);
                double sum = rs.getDouble(2);
                String what = rs.getString(3);

                model.addRow(new Object[]{id, name, sum, what, false});
            }

        } catch(SQLException e){

            JOptionPane.showMessageDialog(null,e);

        }

        table.setModel(model);
    }

    public void selectForLoad(java.sql.Date dateFrom, java.sql.Date dateTo, Long company, Long purpose) {

        Object[] row = new Object[6];

        if (company == 0){

            company = null;
        }

        if (purpose == 0){

            purpose = null;
        }

        String sql = "{call SELECT_ROWS_LOAD_DATA(?, ?, ?, ?, ?)}";

        try(CallableStatement cs = conn.prepareCall(sql)){

            cs.setDate("SRLD_DAY_FROM", dateFrom);
            cs.setDate("SRLD_DAY_TO", dateTo);
            cs.setObject("SRLD_COMPANY", company);
            cs.setObject("SRLD_PURPOSE", purpose);
            cs.registerOutParameter("SRLD_CURS", OracleTypes.CURSOR);

            cs.executeUpdate();

            rs = (ResultSet) cs.getObject("SRLD_CURS");

            while (rs.next()){

                row [0] = model.getRowCount()+1;
                row [1] = rs.getDate(1);
                row [2] = rs.getString(2);
                row [3] = rs.getDouble(3);
                row [4] = rs.getString(4);
                row [5] = rs.getBoolean(5);

                model.addRow(row);
            }

        } catch(SQLException e){

            JOptionPane.showMessageDialog(null,e);

        }

        table.setModel(model);
    }

    @Override
    public void delete() {

        String sql = "{call DELETE_ROW_FROM_PAYMENTS(?, ?, ?, ?)}";

        try (CallableStatement cs = conn.prepareCall(sql)){

            cs.setDate("DRFP_DAY", date);
            cs.setLong("DRFP_COMPANY", companyNameLisId);
            cs.setDouble("DRFP_SUM", sum);
            cs.setLong("DRFP_PURPOSE", whatPayForListId);

            cs.executeUpdate();

        }catch (SQLException e){

            JOptionPane.showMessageDialog(null, e);
        }

    }

    public void updateSum(long payment, Object newSum) {

        String sql = "{call UPDATE_SUM_IN_PAYMENTS(?, ?)}";

        try(CallableStatement cs = conn.prepareCall(sql)){

            cs.setLong("USIP_PAYMENT", payment);
            cs.setDouble("USIP_SUM", Double.parseDouble(String.valueOf(newSum)));

            cs.executeUpdate();

        }catch (SQLException e){

            e.printStackTrace();

        }
    }

    public void updateDate(java.sql.Date date) {

        String sql = "{call AFP.UPDATE_DAY_IN_PAYMENTS(?)}";

        try(CallableStatement cs = conn.prepareCall(sql)){

            cs.setDate("UDIP_DAY", date);

            cs.executeUpdate();

        }catch (SQLException e){

            JOptionPane.showMessageDialog(null, e);

        }
    }

    public void updateStatus(boolean status) {

        String sql;

        if(status){

            sql = "{call UPDATE_STATUS_TO_1(?, ?, ?, ?)}";

        }else{

            sql = "{call UPDATE_STATUS_TO_0(?, ?, ?, ?)}";
        }

        try(CallableStatement cs = conn.prepareCall(sql)){

            cs.setDate("US_DAY", date);
            cs.setLong("US_COMPANY", companyNameLisId);
            cs.setDouble("US_SUM", sum);
            cs.setLong("US_PURPOSE", whatPayForListId);

            cs.executeUpdate();


        }catch (SQLException e){

            e.printStackTrace();

        }
    }

    public Long getPayment(){

        Long payment = null;

        String sql = "{call SELECT_PAYMENT_FROM_PAYMENTS(?, ?, ?, ?, ?)}";

        try (CallableStatement cs = conn.prepareCall(sql)){

            cs.setDate("SPFP_DAY", date);
            cs.setLong("SPFP_COMPANY", companyNameLisId);
            cs.setDouble("SPFP_SUM", sum);
            cs.setLong("SPFP_PURPOSE", whatPayForListId);
            cs.registerOutParameter("SPFP_PAYMENT", OracleTypes.NUMBER);

            cs.executeUpdate();

            payment = cs.getLong("SPFP_PAYMENT");

        }catch (SQLException e){

            e.printStackTrace();

        }

        return payment;
    }

    public double getTotal (){

        double total = 0.00;

        String sql = "{call GET_TOTAL_STATUS_0}";

        try(CallableStatement cs = conn.prepareCall(sql)){

            cs.executeUpdate();

            total = cs.getResultSet().getDouble(1);

        }catch (SQLException e){

            JOptionPane.showMessageDialog(null, e);

        }

        return total;
    }

    public double getTotalLoad (java.sql.Date dateFrom, java.sql.Date dateTo, Long company, Long purpose){

        double total = 0.00;

        String sql = "{call GET_TOTAL_FOR_LOAD(?, ?, ?, ?, ?)}";

        try(CallableStatement cs = conn.prepareCall(sql)){

            cs.setDate("GTFL_DAY_FROM", dateFrom);
            cs.setDate("GTFL_DAY_TO", dateTo);
            cs.setObject("GTFL_COMPANY", company);
            cs.setObject("GTFL_PURPOSE", purpose);
            cs.registerOutParameter("GTFL_TOTAL", OracleTypes.NUMBER);

            cs.executeUpdate();

            total = cs.getDouble("GTFL_TOTAL");

        }catch (SQLException e){

            JOptionPane.showMessageDialog(null, e);

        }

        return total;
    }

}
