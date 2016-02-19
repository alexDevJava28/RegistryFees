package frames;

import data.Companies;
import data.Payments;
import data.Purposes;
import org.jdatepicker.impl.JDatePickerImpl;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.sql.Connection;
import java.util.ResourceBundle;

/**
 * Created by Пользователь on 27.12.2015.
 */
public class LoadFrame extends JFrame {

    private JDatePickerImpl datePickerFrom = new DatePicker().getjDatePicker();
    private JDatePickerImpl datePickerTo = new DatePicker().getjDatePicker();
    private JPanel boxes;
    private JLabel lblCompanies;
    private JLabel lblPurposes;
    private JLabel lblDatefrom;
    private JLabel lblDateTo;
    private CompaniesField companies;
    private PurposesField purposes;
    private JButton confirm;

    public LoadFrame(Connection conn,
                     JTable tableLoad,
                     LoadTableModel modelLoad,
                     JLabel lblTotal,
                     ResourceBundle resString){

        //Set the main theme to the load window
        try {

            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());

        }catch (UnsupportedLookAndFeelException |
                ClassNotFoundException |
                InstantiationException |
                IllegalAccessException e ){

            e.printStackTrace();

        }

        companies = new CompaniesField(conn, resString);
        purposes = new PurposesField(conn, resString);

        lblCompanies = new JLabel(resString.getString("chooseCompany"));
        lblPurposes = new JLabel(resString.getString("choosePurpose"));
        lblDatefrom = new JLabel(resString.getString("chooseDateFrom"));
        lblDateTo = new JLabel(resString.getString("chooseDateTo"));

        boxes = new JPanel();
        boxes.setLayout(new GridLayout(8, 1));

        confirm = new JButton(resString.getString("confirmButton"));

        boxes.add(lblDatefrom);
        boxes.add(datePickerFrom);
        boxes.add(lblDateTo);
        boxes.add(datePickerTo);

        boxes.add(lblCompanies);
        boxes.add(companies);
        boxes.add(lblPurposes);
        boxes.add(purposes);

        this.add(boxes, BorderLayout.NORTH);
        this.add(confirm, BorderLayout.SOUTH);

        this.setSize(300, 250);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.pack();
        this.setVisible(true);
        this.setResizable(false);

        confirm.addActionListener(e -> {

            if (modelLoad.getRowCount() > 0){

                modelLoad.setRowCount(0);
            }

            tableLoad.setModel(modelLoad);

            TableColumn columnLoad;
            for (int i = 0; i < 6; i++) {

                columnLoad = tableLoad.getColumnModel().getColumn(i);

                if (i == 0) {
                    columnLoad.setPreferredWidth(30);
                } else if (i == 1) {
                    columnLoad.setPreferredWidth(100);
                } else if (i == 2) {
                    columnLoad.setPreferredWidth(300);
                } else if (i == 3) {
                    columnLoad.setPreferredWidth(120);
                } else if (i == 4) {
                    columnLoad.setPreferredWidth(300);
                }else
                    columnLoad.setPreferredWidth(50);
            }

            tableLoad.setBackground(Color.white);
            tableLoad.setForeground(Color.black);
            Font font1 = new Font("", 2, 15);
            tableLoad.setFont(font1);
            tableLoad.setRowHeight(20);

            Payments payments = new Payments(conn, tableLoad, modelLoad);
            Companies com = new Companies(conn, companies.getSelectedItem());
            Purposes pur = new Purposes(conn, purposes.getSelectedItem());

            Long companyId = com.getCompanyNameId();
            Long purposeId = pur.getWhatPayForId();

            payments.selectForLoad(getDateFrom(), getDateTo(), companyId, purposeId);

            lblTotal.setText(resString.getString("total") + " " + payments.getTotalLoad(getDateFrom(), getDateTo(), companyId, purposeId));

            dispose();
        });
    }

    public java.sql.Date getDateFrom (){

        java.util.Date selectedDate = (java.util.Date) datePickerFrom.getModel().getValue();
        java.sql.Date sqlDate = new java.sql.Date(selectedDate.getTime());
        return sqlDate;
    }

    public java.sql.Date getDateTo (){

        java.util.Date selectedDate = (java.util.Date) datePickerTo.getModel().getValue();
        java.sql.Date sqlDate = new java.sql.Date(selectedDate.getTime());
        return sqlDate;
    }
}
