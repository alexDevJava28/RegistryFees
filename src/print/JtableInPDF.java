package print;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import javax.swing.*;
import java.awt.print.Printable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;

/**
 * Created by khodackovskiy on 28.01.2016.
 */
public class JtableInPDF {

    private JTable table;
    private Date date;
    File file;

    public JtableInPDF(JTable table, Date date) {

        this.table = table;
        this.date = date;
    }

    public File createPDF() {

        try {
            file = new File("print.pdf");
            OutputStream os = new FileOutputStream(file);

            Document doc = new Document(PageSize.A4.rotate());
            PdfWriter.getInstance(doc, os);

            doc.open();
            doc.add(new Paragraph("Registry of payments"));
            doc.add(new Paragraph(new Date().toString()));

            //Create Paragraph
            Paragraph paragraph = new Paragraph(date.toString(),
                    new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN,
                            18,
                            com.itextpdf.text.Font.BOLD));
            paragraph.setAlignment(Paragraph.ALIGN_CENTER);

            //New line
            paragraph.add(new Paragraph(" "));
            doc.add(paragraph);

            //create  Table
            PdfPTable pdfPTable = new PdfPTable(table.getColumnCount());
            pdfPTable.setWidthPercentage(100);
            pdfPTable.setSpacingBefore(0f);
            pdfPTable.setSpacingAfter(0f);

            //adding table headers
            for (int i = 0; i < table.getColumnCount(); i++) {

                PdfPCell cell = new PdfPCell(new Phrase(table.getColumnName(i)));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                pdfPTable.addCell(cell);

            }

            // Defiles the relative width of the columns
            float[] columnWidths = new float[]{3f, 20f, 10f, 30f, 7f};
            pdfPTable.setWidths(columnWidths);

            //extracting data from the Jtable  and inserting it to Pdf file
            PdfPCell cell;

            for (int rows = 0; rows < table.getRowCount() - 1; rows++) {

                for (int cols = 0; cols < table.getColumnCount(); cols++) {

                    cell = new PdfPCell(new Phrase(table.getModel().getValueAt(rows, cols).toString()));

                    switch (cols) {

                        case 0:
                            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                            break;
                        case 1:
                            cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                            break;
                        case 2:
                            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                            break;
                        case 3:
                            cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                            break;
                        case 4:
                            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                    }

                    pdfPTable.addCell(cell);
                }

            }

            doc.add(pdfPTable);
            doc.close();

        } catch (DocumentException de) {

            de.printStackTrace();

        } catch (FileNotFoundException fnfe) {

            fnfe.printStackTrace();
        }

        return file;
    }
}
