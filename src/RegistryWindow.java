
import org.jdatepicker.impl.JDatePickerImpl;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.*;

/**
 * Created by ������������ on 22.11.2015.
 */
public class RegistryWindow extends JFrame{

    MyTableModel modelMain;
    LoadTableModel modelLoad;
    JDatePickerImpl datePicker = new DatePicker().getjDatePicker();
    JLabel labelSumOfSums;
    JTable tableMain;

    SumTextField sumTextField;
    CompaniesField companiesField;
    PurposesField purposesField;

    JButton btnAdd;
    JButton btnDelete;
    JButton btnUpdate;
    JButton btnLoadData;
    JButton btnConfirmStatus;
    JButton btnBack;
    JButton btnToExcel;

    private static JLabel lblClock;

    public RegistryWindow() {

        Connection conn = DBConnector.getConn();

        //Set the main theme to the program
        try {

            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());

        }catch (UnsupportedLookAndFeelException |
                    ClassNotFoundException |
                        InstantiationException |
                            IllegalAccessException e ){

            e.printStackTrace();

        }

        // create JTable
        tableMain = new JTable();

        // create a table model and set a Column Identifiers to this model
        modelMain = new MyTableModel();

        // set the model to the table
        tableMain.setModel(modelMain);

        // Change A JTable Background Color, Font Size, Font Color, Row Height
        TableColumn column;
        for (int i = 0; i < 5; i++) {
            column = tableMain.getColumnModel().getColumn(i);
            if (i == 0) {
                column.setPreferredWidth(50); //third column is bigger
            } else if (i == 1) {
                column.setPreferredWidth(300);
            } else if (i == 2) {
                column.setPreferredWidth(150);
            } else if (i == 3) {
                column.setPreferredWidth(350);
            } else
                column.setPreferredWidth(50);
        }

        modelLoad = new LoadTableModel();

        tableMain.setBackground(Color.WHITE);
        tableMain.setForeground(Color.black);
        Font font = new Font("", 1, 22);
        tableMain.setFont(font);
        tableMain.setRowHeight(30);

        //load data from database to table model and change date of payment for today, if status is 0 (not accept) and it is a payment
        // of previous day
        Payments payments = new Payments(conn, tableMain, modelMain);
        payments.updateDate(getDate());
        payments.select(0);

        // create JComboBoxes and JTextFields
        companiesField = new CompaniesField(conn);
        companiesField.setBounds(20, 220, 250, 25);
        sumTextField = new SumTextField();
        purposesField = new PurposesField(conn);
        purposesField.setBounds(460, 220, 420, 25);

        // create JLabels for time and total
        lblClock = new JLabel();
        lblClock.setFont(new Font("", 1, 22));
        currentTime();

        labelSumOfSums = new JLabel("TOTAL: " + payments.getTotal());
        labelSumOfSums.setFont(new Font ("", Font.BOLD, 20));

        // create JButtons
        btnAdd = new JButton("Add");
        btnDelete = new JButton("Delete");
        btnUpdate = new JButton("Update");
        btnLoadData = new JButton("Load Data");
        btnConfirmStatus = new JButton("Confirm Status");
        btnBack = new JButton("Back");
        btnToExcel = new JButton(new ImageIcon("excel.png"));

        lblClock.setBounds(350, 320, 250, 30);
        labelSumOfSums.setBounds(300, 250, 250, 30);

        btnAdd.setBounds(20, 250, 100, 25);
        btnUpdate.setBounds(20, 285, 100, 25);
        btnDelete.setBounds(20, 320, 100, 25);
        btnLoadData.setBounds(130, 320, 100, 25);
        btnConfirmStatus.setBounds(130, 285, 100, 25);
        btnBack.setBounds(750, 320, 100, 25);
        btnBack.setVisible(false);
        btnToExcel.setBounds(855, 320, 25, 25);

        // create JScrollPane for Main Table
        JScrollPane paneMain = new JScrollPane(tableMain);
        paneMain.setBounds(0, 0, 880, 200);

        setLayout(null);

        add(paneMain);

        add(datePicker);

        // add JLabels to the jframe
        add(lblClock);
        add(companiesField.getLabel());
        add(sumTextField.getLabel());
        add(purposesField.getLabel());
        add(labelSumOfSums);

        // add JTextFields to the jframe
        add(companiesField);
        add(sumTextField);
        add(purposesField);

        // add JButtons to the jframe
        add(btnAdd);
        add(btnDelete);
        add(btnUpdate);
        add(btnLoadData);
        add(btnConfirmStatus);
        add(btnBack);
        add(btnToExcel);
        getRootPane().setDefaultButton(btnAdd);

        pack();
        setSize(900, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        companiesField.requestFocusInWindow();
        setVisible(true);
        setResizable(false);

        // create an array of objects to set the row data
        Object[] row = new Object[5];

        sumTextField.addActionListener(e -> purposesField.requestFocus());

        // button add row
        btnAdd.addActionListener(e -> {

            if (companiesField.getSelectedItem() == null ||
                    sumTextField.getValue() == null ||
                    purposesField.getSelectedItem() == null) {

                JOptionPane.showMessageDialog(null, "Fill all fields, please!");

            } else {

                row[0] = modelMain.getRowCount() + 1;
                row[1] = companiesField.getSelectedItem();
                row[2] = sumTextField.getValue();
                row[3] = purposesField.getSelectedItem();

                //get selected date from datePicker
                java.sql.Date reportDate = getDate();

                Companies companies = new Companies(conn, row[1]);
                companies.insert();

                Purposes purposes = new Purposes(conn, row[3]);
                purposes.insert();

                Payments payments1 = new Payments(
                        conn,
                        reportDate,
                        (Double) row[2],
                        companies.getCompanyNameId(),
                        purposes.getWhatPayForId());

                payments1.insert();

                // add row to the model
                modelMain.addRow(row);
                modelMain.setValueAt(false, tableMain.getRowCount()-1, 4);

                //make text fields empty
                sumTextField.setValue(null);

                //refresh a total of registry
                labelSumOfSums.setText("TOTAL: " + payments1.getTotal());

                companiesField.fillComboBox();
                companiesField.setSelectedItem(null);
                purposesField.fillComboBox();
                purposesField.setSelectedItem(null);

                //change focus
                companiesField.requestFocus();
            }
        });

        // button remove row
        btnDelete.addActionListener(e -> {

            // i = the index of the selected row
            int i = tableMain.getSelectedRow();

            java.sql.Date reportDate = getDate();

                Companies companies = new Companies(
                        conn,
                        tableMain.getValueAt(i, 1));

                Purposes purposes = new Purposes(
                        conn,
                        tableMain.getValueAt(i, 3));

                Payments payments1 = new Payments(
                        conn,
                        reportDate,
                        (Double) tableMain.getValueAt(i, 2),
                        companies.getCompanyNameId(),
                        purposes.getWhatPayForId());

                payments1.delete();
                companies.delete();
                purposes.delete();

            if (i >= 0) {
                // remove a row from jtable
                modelMain.removeRow(i);

                for (int j = 0; j < tableMain.getRowCount(); j++) {

                    tableMain.setValueAt(j + 1, j, 0);

                }
            } else {
                System.out.println("Delete Error");
            }

            sumTextField.setValue(null);

            labelSumOfSums.setText("TOTAL: " + payments1.getTotal());

            companiesField.requestFocus();
        });

        //button for load registry from database
        btnLoadData.addActionListener(e -> {

            companiesField.setSelectedItem("");
            sumTextField.setValue(null);
            purposesField.setSelectedItem("");

            Thread loadThread = new Thread(){

                public void run(){

                    btnAdd.setEnabled(false);
                    btnUpdate.setEnabled(false);
                    btnDelete.setEnabled(false);
                    btnConfirmStatus.setEnabled(false);
                    btnBack.setVisible(true);
                    datePicker.setVisible(false);

                    new LoadFrame(conn, tableMain, modelLoad, labelSumOfSums);

                }
            };

            loadThread.start();
        });

        btnBack.addActionListener(e -> {

            if (modelMain.getRowCount() > 0){

                modelMain.setRowCount(0);
            }

            // set the model to the table
            tableMain.setModel(modelMain);

            // Change A JTable Background Color, Font Size, Font Color, Row Height
            TableColumn column1;
            for (int i = 0; i < 5; i++) {
                column1 = tableMain.getColumnModel().getColumn(i);
                if (i == 0) {
                    column1.setPreferredWidth(50); //third column is bigger
                } else if (i == 1) {
                    column1.setPreferredWidth(300);
                } else if (i == 2) {
                    column1.setPreferredWidth(150);
                } else if (i == 3) {
                    column1.setPreferredWidth(350);
                } else
                    column1.setPreferredWidth(50);
            }

            tableMain.setBackground(Color.WHITE);
            tableMain.setForeground(Color.black);
            Font font1 = new Font("", 1, 22);
            tableMain.setFont(font1);
            tableMain.setRowHeight(30);

            payments.select(0);

            btnBack.setVisible(false);
            btnAdd.setEnabled(true);
            btnConfirmStatus.setEnabled(true);
            btnDelete.setEnabled(true);
            btnUpdate.setEnabled(true);
            datePicker.setVisible(true);
        });

        // get selected row data From table to textfields
        tableMain.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                if (tableMain.getModel() instanceof MyTableModel) {

                    // i = the index of the selected row
                    int i = tableMain.getSelectedRow();

                    companiesField.setSelectedItem(tableMain.getValueAt(i, 1).toString());
                    sumTextField.setValue(tableMain.getValueAt(i, 2));
                    purposesField.setSelectedItem(tableMain.getValueAt(i, 3).toString());

                }

            }
        });

        // button update row
        btnUpdate.addActionListener(e -> {

            // i = the index of the selected row
            int i = tableMain.getSelectedRow();

            java.sql.Date reportDate = getDate();
            String nameInCompany = companiesField.getSelectedItem().toString();
            Object sumInSum = sumTextField.getValue();
            String purposeInPurpose = purposesField.getSelectedItem().toString();

            if (!nameInCompany.equals(tableMain.getValueAt(i, 1))){

                Companies companie = new Companies(conn, tableMain.getValueAt(i, 1));

                companie.update(companie.getCompanyNameId(), nameInCompany);

                tableMain.setValueAt(nameInCompany, i, 1);
                companiesField.fillComboBox();
                companiesField.setSelectedItem(tableMain.getValueAt(i, 1));

            }else if (!sumInSum.equals(tableMain.getValueAt(i, 2))){

                Long company= new Companies(conn, nameInCompany).getCompanyNameId();
                Long purpose= new Purposes(conn, purposeInPurpose).getWhatPayForId();

                Payments payment = new Payments(conn,
                                                reportDate,
                        (Double) tableMain.getValueAt(i, 2),
                                                company,
                                                purpose);

                payment.update(payment.getPayment(), sumInSum);

                labelSumOfSums.setText("TOTAL: " + payments.getTotal());

                tableMain.setValueAt(sumInSum, i, 2);

            }else if(!purposeInPurpose.equals(tableMain.getValueAt(i, 3))){

                Purposes purpose = new Purposes(conn, tableMain.getValueAt(i, 3));

                purpose.update(purpose.getWhatPayForId(), purposeInPurpose);

                tableMain.setValueAt(purposeInPurpose, i, 3);
                purposesField.fillComboBox();
                purposesField.setSelectedItem(tableMain.getValueAt(i, 1));

            }else{

                JOptionPane.showMessageDialog(null, "No updates");
            }
        });

        btnConfirmStatus.addActionListener(e -> {

            Thread statusUpdate = new Thread(){

                Companies companies;
                Purposes purposes;
                Payments payments;

                int n = tableMain.getRowCount();

                public void run(){

                    for (int i = 0; i < n; i++) {

                        companies = new Companies(conn, tableMain.getValueAt(i, 1));
                        purposes = new Purposes(conn, tableMain.getValueAt(i, 3));

                        payments = new Payments(conn,
                                                getDate(),
                                (Double) tableMain.getValueAt(i, 2),
                                                companies.getCompanyNameId(),
                                                purposes.getWhatPayForId());

                        payments.updateStatus((boolean)tableMain.getValueAt(i, 4));
                    }
                }
            };

            statusUpdate.start();
        });
    }

    private java.sql.Date getDate() {

        java.util.Date date = (java.util.Date) datePicker.getModel().getValue();
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());

        return sqlDate;

    }

    public static void currentTime() {

        Thread clock = new Thread() {

            public void run() {

                try {

                    while (true) {


                        Calendar cal = new GregorianCalendar();

                        int hour = cal.get(Calendar.HOUR_OF_DAY);
                        int minute = cal.get(Calendar.MINUTE);
                        int second = cal.get(Calendar.SECOND);

                        lblClock.setText("TIME: " + hour + ":" + minute + ":" + second);

                        if (hour >= 9 & hour < 15) {
                            lblClock.setForeground(Color.blue);
                        }else if (hour == 15 & minute >= 30 ){
                            lblClock.setForeground(Color.yellow);
                        }else {
                            lblClock.setForeground(Color.red);

                        }

                        sleep(1000);
                    }
                } catch (InterruptedException e) {

                    e.printStackTrace();
                }
            }
        };

        clock.start();
    }
}
