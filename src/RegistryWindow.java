

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by ������������ on 22.11.2015.
 */
public class RegistryWindow extends JFrame{

    MyTableModel modelMain;
    LoadTableModel modelLoad;
    DatePicker datePicker = new DatePicker();
    JLabel labelSumOfSums;
    JTable tableMain;

    JButton btnAdd;
    JButton btnDelete;
    JButton btnUpdate;
    JButton btnLoadData;
    JButton btnConfirmStatus;
    JButton btnBack;

    private static JLabel lblClock;
    private static long sum = 0;

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
        CompaniesField companiesField = new CompaniesField(conn);
        companiesField.setBounds(20, 220, 250, 25);
        SumTextField sumTextField = new SumTextField();
        PurposesField purposesField = new PurposesField(conn);
        purposesField.setBounds(460, 220, 420, 25);

        // create JLabels for time and total
        lblClock = new JLabel();
        lblClock.setFont(new Font("", 1, 22));
        currentTime();

        sum = payments.getTotal();
        labelSumOfSums = new JLabel("TOTAL: " + sum);
        labelSumOfSums.setFont(new Font ("", Font.BOLD, 22));

        // create JButtons
        btnAdd = new JButton("Add");
        btnDelete = new JButton("Delete");
        btnUpdate = new JButton("Update");
        btnLoadData = new JButton("Load Data");
        btnConfirmStatus = new JButton("Confirm Status");
        btnBack = new JButton("Back");

        lblClock.setBounds(350, 320, 250, 30);
        labelSumOfSums.setBounds(300, 250, 150, 30);

        btnAdd.setBounds(20, 250, 100, 25);
        btnUpdate.setBounds(20, 285, 100, 25);
        btnDelete.setBounds(20, 320, 100, 25);
        btnLoadData.setBounds(130, 320, 100, 25);
        btnConfirmStatus.setBounds(130, 285, 100, 25);
        btnBack.setBounds(750, 320, 100, 25);
        btnBack.setVisible(false);

        // create JScrollPane for Main Table
        JScrollPane paneMain = new JScrollPane(tableMain);
        paneMain.setBounds(0, 0, 880, 200);

        setLayout(null);

        add(paneMain);

        add(datePicker.getjDatePicker());

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
        getRootPane().setDefaultButton(btnAdd);

        // create an array of objects to set the row data
        Object[] row = new Object[5];

        companiesField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                sumTextField.requestFocus();
            }
        });

        sumTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                purposesField.requestFocus();
            }
        });

        purposesField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                btnAdd.requestFocus();
            }
        });

        // button add row
        btnAdd.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                if (companiesField.getSelectedItem().equals("") ||
                        sumTextField.getText().isEmpty() ||
                        purposesField.getSelectedItem().equals("")) {

                    JOptionPane.showMessageDialog(null, "Fill all fields, please!");

                } else {

                    row[0] = modelMain.getRowCount() + 1;
                    row[1] = companiesField.getSelectedItem();
                    row[2] = sumTextField.getText();
                    row[3] = purposesField.getSelectedItem();

                    //get selected date from datePicker
                    String reportDate = getDate();

                    Companies companies = new Companies(conn, row[1]);
                    companies.insert();

                    Purposes purposes = new Purposes(conn, row[3]);
                    purposes.insert();

                    Payments payments = new Payments(
                            conn,
                            reportDate,
                            row[2],
                            companies.getCompanyNameId(),
                            purposes.getWhatPayForId());

                    payments.insert();

                    // add row to the model
                    modelMain.addRow(row);

                    //make text fields empty
                    sumTextField.setText("");

                    //refresh a total of registry
                    sum = payments.getTotal();
                    labelSumOfSums.setText("TOTAL: " + sum);

                    //change focus
                    companiesField.requestFocus();
                }
            }
        });

        // button remove row
        btnDelete.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                // i = the index of the selected row
                int i = tableMain.getSelectedRow();

                String reportDate = getDate();

                    Companies companies = new Companies(
                            conn,
                            tableMain.getValueAt(i, 1));

                    Purposes purposes = new Purposes(
                            conn,
                            tableMain.getValueAt(i, 3));

                    Payments payments = new Payments(
                            conn,
                            reportDate,
                            tableMain.getValueAt(i, 2),
                            companies.getCompanyNameId(),
                            purposes.getWhatPayForId());

                    payments.delete();
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

                sumTextField.setText("");

                sum = payments.getTotal();
                labelSumOfSums.setText("TOTAL: " + sum);

                companiesField.requestFocus();
            }
        });

        //button for load registry from database
        btnLoadData.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Thread loadThread = new Thread(){

                    public void run(){

                        new LoadFrame(conn, tableMain, modelLoad, btnBack, btnAdd, btnConfirmStatus, btnDelete, btnUpdate);

                    }
                };

                loadThread.start();
            }
        });

        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (modelMain.getRowCount() > 0){

                    modelMain.setRowCount(0);
                }

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

                tableMain.setBackground(Color.WHITE);
                tableMain.setForeground(Color.black);
                Font font = new Font("", 1, 22);
                tableMain.setFont(font);
                tableMain.setRowHeight(30);

                payments.select(0);

                btnBack.setVisible(false);
                btnAdd.setEnabled(true);
                btnConfirmStatus.setEnabled(true);
                btnDelete.setEnabled(true);
                btnUpdate.setEnabled(true);
            }
        });

        // get selected row data From table to textfields
        tableMain.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                    // i = the index of the selected row
                    int i = tableMain.getSelectedRow();

                    companiesField.setSelectedItem(tableMain.getValueAt(i, 1).toString());
                    sumTextField.setText(tableMain.getValueAt(i, 2).toString());
                    purposesField.setSelectedItem(tableMain.getValueAt(i, 3).toString());

            }
        });

        // button update row
        btnUpdate.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                // i = the index of the selected row
                int i = tableMain.getSelectedRow();

                String reportDate = getDate();
                String nameInCompany = companiesField.getSelectedItem().toString();
                String sumInSum = sumTextField.getText();
                String purposeInPurpose = purposesField.getSelectedItem().toString();

                if (!nameInCompany.equals(tableMain.getValueAt(i, 1))){

                    Companies companie = new Companies(conn, tableMain.getValueAt(i, 1));

                    companie.update(companie.getCompanyNameId(), nameInCompany);

                    tableMain.setValueAt(nameInCompany, i, 1);
                    companiesField.fillComboBox();
                    companiesField.setSelectedItem(tableMain.getValueAt(i, 1));

                }else if (!sumInSum.equals(tableMain.getValueAt(i, 2))){

                    long company= new Companies(conn, nameInCompany).getCompanyNameId();
                    long purpose= new Purposes(conn, purposeInPurpose).getWhatPayForId();

                    Payments payment = new Payments(conn,
                                                    reportDate,
                                                    tableMain.getValueAt(i, 2),
                                                    company,
                                                    purpose);

                    payment.update(payment.getPayment(), sumInSum);

                    sum = payment.getTotal();
                    labelSumOfSums.setText("TOTAL: " + sum);

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
            }
        });

        btnConfirmStatus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

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
                                                    tableMain.getValueAt(i, 2),
                                                    companies.getCompanyNameId(),
                                                    purposes.getWhatPayForId());

                            payments.updateStatus((boolean)tableMain.getValueAt(i, 4));
                        }
                    }
                };

                statusUpdate.start();
            }
        });

        pack();
        setSize(900, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        companiesField.requestFocusInWindow();
        setVisible(true);

    }

    private String getDate() {
        Date selectedDate = (Date) datePicker.getjDatePicker().getModel().getValue();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        return df.format(selectedDate);
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
