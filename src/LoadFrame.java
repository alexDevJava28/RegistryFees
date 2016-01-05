
import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by Пользователь on 27.12.2015.
 */
public class LoadFrame extends JFrame {

    Connection conn;
    DatePicker datePickerFrom = new DatePicker();
    DatePicker datePickerTo = new DatePicker();
    JPanel boxes;
    JPanel datePeriod;
    JLabel lblCompanies;
    JLabel lblPurposes;
    JLabel lblDatefrom;
    JLabel lblDateTo;
    CompaniesField companies;
    PurposesField purposes;
    JButton confirm;
    JTable tableLoad;
    LoadTableModel modelLoad;
    JButton btnBack;
    JButton btnAdd;
    JButton btnConfirmStatus;
    JButton btnDelete;
    JButton btnUpdate;
    JLabel lblTotal;

    public LoadFrame(Connection conn,
                     JTable tableLoad,
                     LoadTableModel modelLoad,
                     JButton btnBack,
                     JButton btnAdd,
                     JButton btnConfirmStatus,
                     JButton btnDelete,
                     JButton btnUpdate,
                     JLabel lblTotal){

        this.conn = conn;
        this.tableLoad = tableLoad;
        this.modelLoad = modelLoad;
        this.btnBack = btnBack;
        this.btnAdd = btnAdd;
        this.btnConfirmStatus = btnConfirmStatus;
        this.btnDelete = btnDelete;
        this.btnUpdate = btnUpdate;
        this.lblTotal = lblTotal;

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

        companies = new CompaniesField(conn);
        purposes = new PurposesField(conn);

        lblCompanies = new JLabel("Choose company");
        lblPurposes = new JLabel("Choose purpose");
        lblDatefrom = new JLabel("Choose date from");
        lblDateTo = new JLabel("Choose date to");

        boxes = new JPanel();
        boxes.setLayout(new GridLayout(4, 1));

        datePeriod = new JPanel();
        datePeriod.setLayout(new GridLayout(4, 1));

        confirm = new JButton("CONFIRM");

        boxes.add(lblCompanies);
        boxes.add(companies);
        boxes.add(lblPurposes);
        boxes.add(purposes);
        boxes.add(confirm, new FlowLayout(FlowLayout.CENTER));

        datePeriod.add(lblDatefrom);
        datePeriod.add(datePickerFrom.getjDatePicker());
        datePeriod.add(lblDateTo);
        datePeriod.add(datePickerTo.getjDatePicker());

        this.add(boxes, BorderLayout.SOUTH);
        this.add(datePeriod);

        this.pack();
        this.setSize(300, 230);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
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

            Object name1 = companies.getSelectedItem();
            Object what = purposes.getSelectedItem();

            if (name1 == null && what == null) {

                Payments payments = new Payments(conn, tableLoad, modelLoad);
                payments.selectForLoad(getDateFrom(), getDateTo(), 0, 0);

            }else if (name1 == null){

                Purposes pur = new Purposes(conn, what);
                Payments payments = new Payments(conn, tableLoad, modelLoad);
                payments.selectForLoad(getDateFrom(), getDateTo(), 0, pur.getWhatPayForId());

            }else if (what == null){

                Companies com = new Companies(conn, name1);
                Payments payments = new Payments(conn, tableLoad, modelLoad);
                payments.selectForLoad(getDateFrom(), getDateTo(), com.getCompanyNameId(), 0);

            }else {

                Companies com = new Companies(conn, name1);
                Purposes pur = new Purposes(conn, what);
                Payments payments = new Payments(conn, tableLoad, modelLoad);
                payments.selectForLoad(getDateFrom(), getDateTo(), com.getCompanyNameId(), pur.getWhatPayForId());

            }

            btnBack.setVisible(true);
            btnAdd.setEnabled(false);
            btnConfirmStatus.setEnabled(false);
            btnDelete.setEnabled(false);
            btnUpdate.setEnabled(false);

            dispose();
        });
    }

    public String getDateFrom (){

        java.util.Date selectedDate = (java.util.Date) datePickerFrom.getjDatePicker().getModel().getValue();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        return df.format(selectedDate);
    }

    public String getDateTo (){

        java.util.Date selectedDate = (java.util.Date) datePickerTo.getjDatePicker().getModel().getValue();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        return df.format(selectedDate);
    }
}
