
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by ������������ on 22.11.2015.
 */
public class RegistryWindow {

    private static JLabel lblClock;
    private static int sum = 0;

    public static void main(String[] args) {

        try {

            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());

        }catch (UnsupportedLookAndFeelException |
                    ClassNotFoundException |
                        InstantiationException |
                            IllegalAccessException e ){

        }

        // create JFrame and JTable
        JFrame frame = new JFrame();
        JTable table = new JTable();
        ListSelectionModel listSelectionModel;
        NumberFormatter numberFormat;

        listSelectionModel = table.getSelectionModel();
        listSelectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {

                listSelectionModel.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            }
        });

        table.setSelectionModel(listSelectionModel);

        // create a table model and set a Column Identifiers to this model
        Object[] columns = {"Id", "COMPANY NAME", "SUM", "WHAT PAY FOR"};
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(columns);

        // set the model to the table
        table.setModel(model);

        // Change A JTable Background Color, Font Size, Font Color, Row Height
        TableColumn column = null;
        for (int i = 0; i < 4; i++) {
            column = table.getColumnModel().getColumn(i);
            if (i == 0) {
                column.setPreferredWidth(50); //third column is bigger
            } else if (i == 1) {
                column.setPreferredWidth(300);
            }else if (i == 2) {
                column.setPreferredWidth(150);
            }else
                column.setPreferredWidth(400);
            }

        table.setBackground(Color.WHITE);
        table.setForeground(Color.black);
        Font font = new Font("", 1, 22);
        table.setFont(font);
        table.setRowHeight(30);

        // create JLabels
        lblClock = new JLabel();
        lblClock.setFont(new Font("", 1, 22));
        currentTime();
        JLabel labelCompanyName = new JLabel("Company");
        JLabel labelSum = new JLabel("Sum");
        JLabel labelWhatPayFor = new JLabel("What");
        JLabel labelSumOfSums = new JLabel("TOTAL: " + sum);
        labelSumOfSums.setFont(new Font ("", Font.BOLD, 22));

        // create JTextFields
        JTextField textCompanyName = new JTextField();
        JFormattedTextField textSum = new JFormattedTextField(NumberFormat.INTEGER_FIELD);
        JTextField textWhatPayFor = new JTextField();

        // create JButtons
        JButton btnAdd = new JButton("Add");
        JButton btnDelete = new JButton("Delete");
        JButton btnUpdate = new JButton("Update");

        lblClock.setBounds(350, 320, 250, 30);
        labelCompanyName.setBounds(20, 200, 250, 15);
        labelSum.setBounds(290, 200, 150, 15);
        labelWhatPayFor.setBounds(460, 200, 420, 15);
        labelSumOfSums.setBounds(300, 250, 100, 30);

        textCompanyName.setBounds(20, 220, 250, 25);
        textSum.setBounds(290, 220, 150, 25);
        textWhatPayFor.setBounds(460, 220, 420, 25);

        btnAdd.setBounds(20, 250, 100, 25);
        btnUpdate.setBounds(20, 285, 100, 25);
        btnDelete.setBounds(20, 320, 100, 25);

        // create JScrollPane
        JScrollPane pane = new JScrollPane(table);
        pane.setBounds(0, 0, 880, 200);

        frame.setLayout(null);

        frame.add(pane);

        JDatePickerImpl datePicker = getjDatePicker();

        frame.add(datePicker);

        // add JLabels to the jframe
        frame.add(lblClock);
        frame.add(labelCompanyName);
        frame.add(labelSum);
        frame.add(labelWhatPayFor);
        frame.add(labelSumOfSums);

        // add JTextFields to the jframe
        frame.add(textCompanyName);
        frame.add(textSum);
        frame.add(textWhatPayFor);

        // add JButtons to the jframe
        frame.add(btnAdd);
        frame.add(btnDelete);
        frame.add(btnUpdate);
        frame.getRootPane().setDefaultButton(btnAdd);

        // create an array of objects to set the row data
        Object[] row = new Object[4];

        textCompanyName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                textSum.requestFocus();
            }
        });

        textSum.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                textWhatPayFor.requestFocus();
            }
        });

        textWhatPayFor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                btnAdd.requestFocus();
            }
        });

        // button add row
        btnAdd.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                row[0] = model.getRowCount()+1;
                row[1] = textCompanyName.getText();
                row[2] = textSum.getText();
                row[3] = textWhatPayFor.getText();

                //get selected date from datePicker
                Date selectedDate = (Date) datePicker.getModel().getValue();
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                String reportDate = df.format(selectedDate);

                try (Connection conn = DBConnector.getConn()) {

                    CompanyNameListTableRow companyNameListTableRow = new CompanyNameListTableRow(conn, row[1]);
                    companyNameListTableRow.insert();

                    WhatPayForListRow whatPayForListRow = new WhatPayForListRow(conn, row[3]);
                    whatPayForListRow.insert();

                    TemporaryRegistryTableRow temporaryRegistryTableRow = new TemporaryRegistryTableRow(
                            conn,
                            reportDate,
                            row[2],
                            companyNameListTableRow.getCompanyNameId(),
                            whatPayForListRow.getWhatPayForId());

                    temporaryRegistryTableRow.insert();

                }catch (SQLException c){

                    System.out.println("SQLException in connection close in Registry Window add button");
                }


                // add row to the model
                model.addRow(row);

                //make text fields empty
                textCompanyName.setText("");
                textSum.setText("");
                textWhatPayFor.setText("");

                //refresh a total of registry
                sum = sum + Integer.parseInt(table.getValueAt(table.getRowCount()-1, 2).toString());
                labelSumOfSums.setText("TOTAL: " + sum);

                //change focus
                textCompanyName.requestFocus();
            }
        });

        // button remove row
        btnDelete.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                // i = the index of the selected row
                int i = table.getSelectedRow();

                sum = sum - Integer.parseInt(table.getValueAt(i, 2).toString());
                labelSumOfSums.setText("TOTAL: " + sum);

                Date selectedDate = (Date) datePicker.getModel().getValue();
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                String reportDate = df.format(selectedDate);

                try (Connection conn = DBConnector.getConn()) {

                    CompanyNameListTableRow companyNameListTableRow = new CompanyNameListTableRow(conn, row[1]);
                    companyNameListTableRow.delete();

                    WhatPayForListRow whatPayForListRow = new WhatPayForListRow(conn, row[3]);
                    whatPayForListRow.delete();

                    TemporaryRegistryTableRow temporaryRegistryTableRow = new TemporaryRegistryTableRow(
                            conn,
                            reportDate,
                            model.getValueAt(i, 2).toString(),
                            Long.parseLong(model.getValueAt(i, 1).toString()),
                            Long.parseLong(model.getValueAt(i, 3).toString()));

                    temporaryRegistryTableRow.delete();

                }catch (SQLException c){

                    System.out.println("SQLException in connection close in Registry Window add button");
                }

                if (i >= 0) {
                    // remove a row from jtable
                    model.removeRow(i);

                    for (int j = 0; j < table.getRowCount(); j++) {

                        table.setValueAt(j + 1, j, 0);

                    }
                } else {
                    System.out.println("Delete Error");
                }

                textCompanyName.setText("");
                textSum.setText("");
                textWhatPayFor.setText("");

                textCompanyName.requestFocus();
            }
        });

        // get selected row data From table to textfields
        table.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                // i = the index of the selected row
                int i = table.getSelectedRow();

                textCompanyName.setText(model.getValueAt(i, 1).toString());
                textSum.setText(model.getValueAt(i, 2).toString());
                textWhatPayFor.setText(model.getValueAt(i, 3).toString());
            }
        });

        // button update row
        btnUpdate.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                // i = the index of the selected row
                int i = table.getSelectedRow();

                if (i >= 0) {
                    model.setValueAt(model.getRowCount()+1, i, 0);
                    model.setValueAt(textCompanyName.getText(), i, 1);
                    model.setValueAt(textSum.getText(), i, 2);
                    model.setValueAt(textWhatPayFor.getText(), i, 3);
                } else {
                    System.out.println("Update Error");
                }
            }
        });

        frame.pack();
        frame.setSize(900, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        textCompanyName.requestFocusInWindow();
        frame.setVisible(true);

    }

    private static JDatePickerImpl getjDatePicker() {

        UtilDateModel jmodel = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(jmodel, p);
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        datePicker.setBounds(680, 250, 200, 30);
        datePicker.setTextEditable(true);
        return datePicker;
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
