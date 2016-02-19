package print;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;

public class PrintPreview extends JFrame implements ActionListener{

    private PDDocument doc;
    private ResourceBundle resString;

    private CardLayout cl = new CardLayout();
    private JPanel imagePanel;
    private JPanel topPanel;

    private JButton btnPrint;
    private JButton btnClose;
    private JButton btnStart;
    private JButton btnEnd;
    private JButton btnForward;
    private JButton btnBack;
    private JLabel lblCountPagesLeft;
    private JLabel lblCountPagesRight;
    private JComboBox comboBox;

    public PrintPreview(PDDocument doc, ResourceBundle resString) {

        super("Print Preview");
        this.doc = doc;
        this.resString = resString;
        createPreview();

    }

    private void createPreview(){

        imagePanel = new JPanel(cl);

        topPanel = new JPanel(new FlowLayout());

        lblCountPagesLeft = new JLabel(resString.getString("printPage") + " ");
        lblCountPagesRight = new JLabel();

        comboBox = new JComboBox();

        btnStart = new JButton(
                        new ImageIcon(
                            new ImageIcon(getClass()
                                    .getResource("/resources/images/begin.png"))
                                        .getImage()
                                            .getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        btnBack = new JButton(
                        new ImageIcon(
                            new ImageIcon(getClass()
                                    .getResource("/resources/images/stepBack.png"))
                                        .getImage()
                                            .getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        btnForward = new JButton(
                        new ImageIcon(
                            new ImageIcon(getClass()
                                    .getResource("/resources/images/stepForward.png"))
                                        .getImage()
                                            .getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        btnEnd = new JButton(
                        new ImageIcon(
                            new ImageIcon(getClass()
                                    .getResource("/resources/images/end.png"))
                                        .getImage()
                                            .getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        btnPrint = new JButton(
                        new ImageIcon(
                            new ImageIcon(getClass()
                                    .getResource("/resources/images/printPreview.png"))
                                        .getImage()
                                            .getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        btnClose = new JButton(resString.getString("printButtonClose"));

        btnStart.addActionListener(this);
        btnBack.addActionListener(this);
        btnForward.addActionListener(this);
        btnEnd.addActionListener(this);
        btnPrint.addActionListener(this);
        btnClose.addActionListener(this);

        List<PDPage> pages = doc.getDocumentCatalog().getAllPages();

        for (int i = 0; i < pages.size(); i++) {

            try{

                BufferedImage bi = pages.get(i).convertToImage();
                JLabel lblWithImage = new JLabel();
                lblWithImage.setIcon(new ImageIcon(bi));
                JScrollPane pane = new JScrollPane(lblWithImage);
                imagePanel.add(String.valueOf(i+1), pane);
                comboBox.addItem(String.valueOf(i+1));

                lblCountPagesRight.setText(" of " + pages.size());

            }catch (IOException ioe){

                ioe.printStackTrace();
            }

        }

        topPanel.add(btnStart);
        topPanel.add(btnBack);
        topPanel.add(lblCountPagesLeft);
        topPanel.add(comboBox);
        topPanel.add(lblCountPagesRight);
        topPanel.add(btnForward);
        topPanel.add(btnEnd);
        topPanel.add(btnPrint);
        topPanel.add(btnClose);

        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.getContentPane().add(topPanel, BorderLayout.NORTH);
        this.getContentPane().add(imagePanel);
        this.setVisible(true);

        comboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {

                cl.show(imagePanel, (String) comboBox.getSelectedItem());
                btnBack.setEnabled(comboBox.getSelectedIndex() == 0 ? false : true);
                btnForward.setEnabled(comboBox.getSelectedIndex() == (comboBox.getItemCount()-1) ? false : true);
                btnStart.setEnabled(comboBox.getSelectedIndex() == 0 ? false : true);
                btnEnd.setEnabled(comboBox.getSelectedIndex() == (comboBox.getItemCount()-1) ? false : true);
                validate();

            }
        });

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        Object obj = e.getSource();

        if(obj == btnPrint) {

            try {

                PrinterJob pj = PrinterJob.getPrinterJob();
                pj.setPageable(doc);

                if(pj.printDialog())

                    pj.print();

            } catch(PrinterException px) {

                JOptionPane.showMessageDialog(null,px.toString(), "Error in Printing",1);

            }

        } else if(obj == btnStart) {

            comboBox.setSelectedIndex(0);

        } else if(obj == btnBack) {

            comboBox.setSelectedIndex(comboBox.getSelectedIndex() == 0 ? 0 : comboBox.getSelectedIndex() - 1);

            if (comboBox.getSelectedIndex() == 0) {

                btnBack.setEnabled(false);

            }


        } else if(obj == btnForward) {

            comboBox.setSelectedIndex(comboBox.getSelectedIndex() == comboBox.getItemCount() - 1 ? 0: comboBox.getSelectedIndex() + 1);

            if (comboBox.getSelectedIndex() == comboBox.getItemCount() - 1) {

                btnForward.setEnabled(false);

            }

        } else if(obj == btnEnd) {

            comboBox.setSelectedIndex(comboBox.getItemCount()-1);

        } else if(obj == btnClose)

            this.dispose();
    }
}