
import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

class JTableRow {

    private static int count = 1;

    public static void main(String[] args) {

        // create JFrame and JTable
        JFrame frame = new JFrame();
        final JTable table = new JTable();

        // create a table model and set a Column Identifiers to this model 
        Object[] columns = {"Id", "COMPANY NAME", "SUM", "WHAT PAY FOR"};
        final DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(columns);

        // set the model to the table
        table.setModel(model);

        // Change A JTable Background Color, Font Size, Font Color, Row Height
        table.setBackground(Color.WHITE);
        table.setForeground(Color.black);
        Font font = new Font("", 1, 22);
        table.setFont(font);
        table.setRowHeight(30);

        // create JLabels
        final JLabel labelCompanyName = new JLabel("Company");
        final JLabel labelSum = new JLabel("Sum");
        final JLabel labelWhatPayFor = new JLabel("What");

        // create JTextFields
        final JTextField textCompanyName = new JTextField();
        final JTextField textSum = new JTextField();
        final JTextField textWhatPayFor = new JTextField();

        // create JButtons
        JButton btnAdd = new JButton("Add");
        JButton btnDelete = new JButton("Delete");
        JButton btnUpdate = new JButton("Update");

        labelCompanyName.setBounds(20, 200, 250, 15);
        labelSum.setBounds(290, 200, 150, 15);
        labelWhatPayFor.setBounds(460, 200, 420, 15);

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

        // add JLabels to the jframe
        frame.add(labelCompanyName);
        frame.add(labelSum);
        frame.add(labelWhatPayFor);

        // add JTextFields to the jframe
        frame.add(textCompanyName);
        frame.add(textSum);
        frame.add(textWhatPayFor);

        // add JButtons to the jframe
        frame.add(btnAdd);
        frame.add(btnDelete);
        frame.add(btnUpdate);

        // create an array of objects to set the row data
        final Object[] row = new Object[4];

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

        btnAdd.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    btnAdd.doClick();

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        btnAdd.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                row[0] = count;
                row[1] = textCompanyName.getText();
                row[2] = textSum.getText();
                row[3] = textWhatPayFor.getText();

                // add row to the model
                model.addRow(row);
                count++;
            }
        });

        // button remove row
        btnDelete.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                // i = the index of the selected row
                int i = table.getSelectedRow();
                if (i >= 0) {
                    // remove a row from jtable
                    model.removeRow(i);
                } else {
                    System.out.println("Delete Error");
                }
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
                    model.setValueAt(count, i, 0);
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
}

