package frames;

import data.Companies;
import data.DBConnector;
import data.Payments;
import data.Purposes;
import email.SwingEmailSender;
import excel.ExcelExporter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.jdatepicker.impl.JDatePickerImpl;
import print.JtableInPDF;
import print.PrintPreview;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.sql.Connection;
import java.util.*;

/**
 * Created by ������������ on 22.11.2015.
 */
public class RegistryWindow extends JFrame{

    private MyTableModel modelMain;
    private LoadTableModel modelLoad;
    private JDatePickerImpl datePicker = new DatePicker().getjDatePicker();
    private JLabel labelSumOfSums;
    private JTable tableMain;

    private SumTextField sumTextField;
    private CompaniesField companiesField;
    private PurposesField purposesField;

    private JButton btnAdd;
    private JButton btnDelete;
    private JButton btnUpdate;
    private JButton btnLoadData;
    private JButton btnConfirmStatus;
    private JButton btnBack;
    private JButton btnPDF;
    private JButton btnToExcel;
    private JButton btnPrint;
    private JButton btnMail;

    private JMenuBar menuBar;
    private JMenu menu;
    private JMenu menuLocale;
    private JMenuItem localeUSA;
    private JMenuItem localeRUS;
    private JMenuItem localeUA;

    private Properties properties = new Properties();
    private Locale currentLocale;
    private static ResourceBundle resString;
    private File configFile;

    private static JLabel lblClock;
    private byte statusNotPaid = 0;
    private byte statusPaid = 1;

    public RegistryWindow() {

        configFile = new File("defaultLocaleName.properties");

        try(InputStream inputStream = new FileInputStream(configFile)) {

            properties.load(inputStream);

        }catch (IOException ioe){

            ioe.printStackTrace();
        }

        currentLocale = new Locale(properties.getProperty("defaultLocale"));
        resString = ResourceBundle.getBundle("resources.locale.Registry", currentLocale);

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
        modelMain = new MyTableModel(resString);

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

        modelLoad = new LoadTableModel(resString);

        tableMain.setBackground(Color.WHITE);
        tableMain.setForeground(Color.black);
        Font font = new Font("", 1, 22);
        tableMain.setFont(font);
        tableMain.setRowHeight(30);

        //load data from database to table model and change date of payment for today, if status is 0 (not accept) and it is a payment
        // of previous day
        Payments payments = new Payments(conn, tableMain, modelMain);
        payments.updateDate(getDate());
        payments.select(statusNotPaid);

        // create JComboBoxes and JTextFields
        companiesField = new CompaniesField(conn, resString);
        companiesField.setBounds(20, 220, 250, 25);
        sumTextField = new SumTextField(resString);
        purposesField = new PurposesField(conn, resString);
        purposesField.setBounds(460, 220, 420, 25);

        // create JLabels for time and total
        lblClock = new JLabel();
        lblClock.setFont(new Font("", 1, 22));
        currentTime();

        labelSumOfSums = new JLabel(resString.getString("total")+ " " + payments.getTotal());
        labelSumOfSums.setFont(new Font ("", Font.BOLD, 20));

        // create JButtons
        btnAdd = new JButton(resString.getString("addButton"),
                    new ImageIcon(
                        new ImageIcon(getClass()
                                .getResource("/resources/images/addButton.png"))
                                    .getImage()
                                        .getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        btnDelete = new JButton(resString.getString("deleteButton"),
                        new ImageIcon(
                            new ImageIcon(getClass()
                                .getResource("/resources/images/deleteButton.png"))
                                    .getImage()
                                        .getScaledInstance(18, 18, Image.SCALE_SMOOTH)));
        btnUpdate = new JButton(resString.getString("updateButton"),
                        new ImageIcon(
                            new ImageIcon(getClass()
                                .getResource("/resources/images/updateButton.png"))
                                    .getImage()
                                        .getScaledInstance(18, 18, Image.SCALE_SMOOTH)));
        btnLoadData = new JButton(resString.getString("loadButton"),
                        new ImageIcon(
                            new ImageIcon(getClass()
                                .getResource("/resources/images/loadButton.png"))
                                    .getImage()
                                        .getScaledInstance(18, 18, Image.SCALE_SMOOTH)));
        btnConfirmStatus = new JButton(resString.getString("confirmStatusButton"),
                            new ImageIcon(
                                new ImageIcon(getClass()
                                    .getResource("/resources/images/confirmButton.png"))
                                        .getImage()
                                            .getScaledInstance(18, 18, Image.SCALE_SMOOTH)));
        btnBack = new JButton(resString.getString("backButton"),
                    new ImageIcon(
                            new ImageIcon(getClass()
                                 .getResource("/resources/images/backButton.png"))
                                     .getImage()
                                          .getScaledInstance(18, 18, Image.SCALE_SMOOTH)));
        btnPDF = new JButton(
                    new ImageIcon(
                            new ImageIcon(getClass()
                                .getResource("/resources/images/PDF.png"))
                                    .getImage()
                                        .getScaledInstance(25, 25, Image.SCALE_SMOOTH)));
        btnToExcel = new JButton(
                        new ImageIcon(
                                new ImageIcon(getClass()
                                        .getResource("/resources/images/excel.png"))
                                            .getImage()
                                                .getScaledInstance(25, 25, Image.SCALE_SMOOTH)));
        btnPrint = new JButton(
                        new ImageIcon(
                                new ImageIcon(getClass()
                                        .getResource("/resources/images/print.png"))
                                            .getImage()
                                                .getScaledInstance(25, 25, Image.SCALE_SMOOTH)));
        btnMail = new JButton(
                        new ImageIcon(
                            new ImageIcon(getClass()
                                .getResource("/resources/images/email.png"))
                                    .getImage()
                                        .getScaledInstance(25, 25, Image.SCALE_SMOOTH)));

        lblClock.setBounds(350, 320, 250, 30);
        labelSumOfSums.setBounds(300, 250, 250, 30);

        btnAdd.setBounds(20, 250, 100, 25);
        btnAdd.setHorizontalTextPosition(SwingConstants.TRAILING);
        btnAdd.setVerticalTextPosition(JButton.CENTER);
        btnAdd.setHorizontalAlignment(SwingConstants.LEFT);

        btnUpdate.setBounds(20, 285, 100, 25);
        btnUpdate.setHorizontalTextPosition(SwingConstants.TRAILING);
        btnUpdate.setVerticalTextPosition(JButton.CENTER);
        btnUpdate.setHorizontalAlignment(SwingConstants.LEFT);

        btnDelete.setBounds(20, 320, 100, 25);
        btnDelete.setHorizontalTextPosition(SwingConstants.TRAILING);
        btnDelete.setVerticalTextPosition(JButton.CENTER);
        btnDelete.setHorizontalAlignment(SwingConstants.LEFT);

        btnLoadData.setBounds(130, 320, 100, 25);
        btnLoadData.setHorizontalTextPosition(SwingConstants.TRAILING);
        btnLoadData.setVerticalTextPosition(JButton.CENTER);
        btnLoadData.setHorizontalAlignment(SwingConstants.LEFT);

        btnConfirmStatus.setBounds(130, 285, 100, 25);
        btnConfirmStatus.setHorizontalTextPosition(SwingConstants.TRAILING);
        btnConfirmStatus.setVerticalTextPosition(JButton.CENTER);
        btnConfirmStatus.setHorizontalAlignment(SwingConstants.LEFT);

        btnBack.setBounds(750, 320, 100, 25);
        btnBack.setHorizontalTextPosition(SwingConstants.TRAILING);
        btnBack.setVerticalTextPosition(JButton.CENTER);
        btnBack.setHorizontalAlignment(SwingConstants.LEFT);
        btnBack.setVisible(false);

        btnPDF.setBounds(840, 305, 40, 40);
        btnToExcel.setBounds(840, 305, 40, 40);

        // create JScrollPane for Main Table
        JScrollPane paneMain = new JScrollPane(tableMain);
        paneMain.setBounds(0, 0, 880, 200);

        menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        menu = new JMenu(resString.getString("menuBar"));
        menuLocale = new JMenu(resString.getString("menuLocale"));
        localeUSA = new JMenuItem(resString.getString("menuItemLocaleUSA"), new ImageIcon(
                                            new ImageIcon(
                                                getClass()
                                                    .getResource("/resources/images/flagUSA.png"))
                                                         .getImage()
                                                             .getScaledInstance(30, 25, Image.SCALE_SMOOTH)));
        localeRUS = new JMenuItem(resString.getString("menuItemLocaleRUS"), new ImageIcon(
                                                new ImageIcon(
                                                        getClass()
                                                            .getResource("/resources/images/flagRUS.png"))
                                                                .getImage()
                                                                    .getScaledInstance(30, 25, Image.SCALE_SMOOTH)));
        localeUA = new JMenuItem(resString.getString("menuItemLocaleUA"), new ImageIcon(
                                                new ImageIcon(
                                                    getClass()
                                                        .getResource("/resources/images/flagUA.png"))
                                                            .getImage()
                                                                .getScaledInstance(30, 25, Image.SCALE_SMOOTH)));
        menuLocale.add(localeUSA);
        menuLocale.add(localeRUS);
        menuLocale.add(localeUA);
        menu.add(menuLocale);

        menuBar.add(menu);
        menuBar.add(btnPDF);
        menuBar.add(btnToExcel);
        menuBar.add(btnPrint);
        menuBar.add(btnMail);

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
        getRootPane().setDefaultButton(btnAdd);

        pack();
        setSize(900, 430);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        companiesField.requestFocusInWindow();
        setVisible(true);
        setResizable(false);

        // create an array of objects to set the row data
        Object[] row = new Object[5];

        localeUSA.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                setCurrentLocale("en_EN");

            }
        });

        localeRUS.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                setCurrentLocale("ru_RU");

            }
        });

        localeUA.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                setCurrentLocale("uk_UA");

            }
        });

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
                labelSumOfSums.setText(resString.getString("total")+ " " + payments1.getTotal());

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

            labelSumOfSums.setText(resString.getString("total")+ " " + payments1.getTotal());

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

                    new LoadFrame(conn, tableMain, modelLoad, labelSumOfSums, resString);

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

            payments.select(statusNotPaid);
            labelSumOfSums.setText(resString.getString("total")+ " " + payments.getTotal());

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

                payment.updateSum(payment.getPayment(), sumInSum);

                labelSumOfSums.setText(resString.getString("total")+ " " + payments.getTotal());

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

        btnPDF.addActionListener(e -> new JtableInPDF(tableMain, getDate(), payments.getTotal(), resString).createPDF(false));

        btnToExcel.addActionListener(e -> new ExcelExporter().exportTable(tableMain, getDate()));

        btnPrint.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                    JtableInPDF pdf = new JtableInPDF(tableMain, getDate(), payments.getTotal(), resString);

                    try {

                        PDDocument doc = PDDocument.load(pdf.createPDF(true));

                        new PrintPreview(doc, resString);

                    }catch (IOException ioe){

                        ioe.printStackTrace();
                    }

            }
        });

        btnMail.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {

                        new SwingEmailSender(resString).setVisible(true);
                    }
                });
            }
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

                        lblClock.setText(resString.getString("time")+ " " + hour + ":" + minute + ":" + second);

                        sleep(1000);
                    }
                } catch (InterruptedException e) {

                    e.printStackTrace();
                }
            }
        };

        clock.start();
    }

    public void setCurrentLocale(String locale){

        properties.put("defaultLocale", locale);

        try(OutputStream out = new FileOutputStream(configFile)){

            properties.store(out, "defaultLocale");

        }catch(IOException ioe){

            ioe.printStackTrace();
        }

        this.setVisible(false);
        this.dispose();

        new RegistryWindow();
    }
}
