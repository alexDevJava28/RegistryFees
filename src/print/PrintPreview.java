package print;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.print.Pageable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.IOException;
import java.util.List;

public class PrintPreview extends JFrame implements ActionListener{

    PDDocument doc;

    CardLayout cl = new CardLayout();
    JPanel imagePanel;
    JPanel topPanel;
    JScrollPane jcp;

    JButton btnPrint;
    JButton btnClose;
    JButton btnStart;
    JButton btnEnd;
    JButton btnForward;
    JButton btnBack;
    JLabel lblCountPages;

    int count;

    public PrintPreview(PDDocument doc) {

        super("Print Preview");
        this.doc = doc;
        createPreview();

    }

    private void createPreview(){

        imagePanel = new JPanel(cl);
        topPanel = new JPanel(new FlowLayout());
        jcp = new JScrollPane(imagePanel);
        jcp.setVisible(true);

        lblCountPages = new JLabel();

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
        btnClose = new JButton("Close");

        List<PDPage> pages = doc.getDocumentCatalog().getAllPages();

        for (int i = 0; i < pages.size(); i++) {

            try{

                BufferedImage bi = pages.get(i).convertToImage();
                JLabel lblWithImage = new JLabel();
                lblWithImage.setIcon(new ImageIcon(bi));
                imagePanel.add(lblWithImage);

                ++count;
                lblCountPages.setText("Page " + count + " of " + pages.size());


            }catch (IOException ioe){

                ioe.printStackTrace();
            }

        }

        topPanel.add(btnStart);
        topPanel.add(btnBack);
        topPanel.add(lblCountPages);
        topPanel.add(btnForward);
        topPanel.add(btnEnd);
        topPanel.add(btnPrint);
        topPanel.add(btnClose);

        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.getContentPane().add(topPanel, BorderLayout.NORTH);
        this.getContentPane().add(jcp);
        this.setVisible(true);

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

            imagePanel.getComponentAt(0, 0).setFocusable(true);

        } else if(obj == btnBack) {


        } else if(obj == btnForward) {


        } else if(obj == btnEnd) {



        } else if(obj == btnClose)

            this.dispose();
    }
}