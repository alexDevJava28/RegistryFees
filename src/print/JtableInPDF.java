package print;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.TableModel;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * Created by khodackovskiy on 28.01.2016.
 */
public class JtableInPDF {

    private JTable table;
    private Date date;
    private double total;
    private PDDocument doc;
    private File file;
    private ResourceBundle resString;

    private final int colWidth1 = 30;
    private final int colWidth2 = 250;
    private final int colWidth3 = 100;
    private final int colWidth4 = 350;
    private final int colWidth5 = 72;
    private final int rowHeight = 20;
    private final int endPage = 70;
    private int pageCountLoad = 0;
    private int rowCountByDate = 0;
    private int rowsByDateText = 0;

    public JtableInPDF(JTable table, Date date, double total, ResourceBundle resString) {

        this.table = table;
        this.date = date;
        this.total = total;
        this.resString = resString;
    }

    public File createPDF(boolean print) {

        if (print) {

            file = new File("print.pdf");

        }else{

            FileSystemView fs = FileSystemView.getFileSystemView();
            file = new File(fs.getHomeDirectory() + "\\" + resString.getString("filename") + ".pdf");
        }

        try (PDDocument insideDoc = new PDDocument()) {

                if (table.getModel().getColumnCount() == 5) {

                    createNewPage(insideDoc);

                } else {

                    createNewPageLoad(insideDoc);

                }

                doc = insideDoc;
                doc.save(file);

            } catch (IOException ioe) {

                ioe.printStackTrace();

            } catch (COSVisitorException cosve) {

                cosve.printStackTrace();
            }

        return file;
    }

    public void createNewPage(PDDocument doc){

        TableModel model = table.getModel();
        int rows = model.getRowCount();
        int cols = model.getColumnCount();

        int x = 20;
        int y = 550;

            //Create new page
            PDFont font = PDType1Font.HELVETICA;
            int fontSize = 12;

            PDPage page = new PDPage(PDPage.PAGE_SIZE_A4);
            page.setRotation(90);
            doc.addPage(page);

            PDRectangle pageSize = page.findMediaBox();
            float pageWidth = pageSize.getWidth();

            try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {

                // add the rotation using the current transformation matrix
                // including a translation of pageWidth to use the lower left corner as 0,0 reference
                cs.concatenate2CTM(0, 1, -1, 0, pageWidth, 0);

                cs.setFont(font, fontSize);
                cs.beginText();
                cs.moveTextPositionByAmount(x, y);
                cs.drawString(resString.getString("headPdf"));
                cs.endText();
                cs.beginText();
                cs.moveTextPositionByAmount(x, y - 20);
                cs.drawString(new Date().toString());
                cs.endText();

                cs.setFont(PDType1Font.HELVETICA_BOLD, 24);
                cs.beginText();
                cs.moveTextPositionByAmount(x + 330, y - 70);
                cs.drawString(date.toString());
                cs.endText();

            } catch (IOException ioe) {

                ioe.printStackTrace();
            }

            try (PDPageContentStream cs = new PDPageContentStream(doc, page, true, true)) {


                int nexty = y - 100;

                drawTable(x, nexty, rows, cs, page, doc);

                font = PDType1Font.HELVETICA_BOLD;
                fontSize = 12;
                cs.setFont(font, fontSize);

                float textx = x;
                float texty = nexty - rowHeight + (rowHeight - fontSize) / 2;

                for (int i = 0; i < cols; i++) {

                    float width = font.getStringWidth(model.getColumnName(i)) / 1000 * fontSize;

                    switch (i) {

                        case 0:
                            textx += (colWidth1 - width) / 2;
                            break;
                        case 1:
                            textx += (colWidth2 - width) / 2;
                            break;
                        case 2:
                            textx += (colWidth3 - width) / 2;
                            break;
                        case 3:
                            textx += (colWidth4 - width) / 2;
                            break;
                        case 4:
                            textx += (colWidth5 - width) / 2;
                            break;
                    }

                    cs.beginText();
                    cs.moveTextPositionByAmount(textx, texty);
                    cs.drawString(model.getColumnName(i));
                    cs.endText();

                    switch (i) {

                        case 0:
                            textx += width + (colWidth1 - width) / 2;
                            break;
                        case 1:
                            textx += width + (colWidth2 - width) / 2;
                            break;
                        case 2:
                            textx += width + (colWidth3 - width) / 2;
                            break;
                        case 3:
                            textx += width + (colWidth4 - width) / 2;
                            break;
                        case 4:
                            textx += width + (colWidth5 - width) / 2;
                            break;
                    }

                }

                fillTable(x, nexty, rows, cols, model, 0, 1, cs, doc);

            } catch (IOException ioe) {

                ioe.printStackTrace();
            }
    }

    public void createNewPageLoad(PDDocument doc){

        TableModel model = table.getModel();
        date = null;
        int rows = model.getRowCount();
        int cols = model.getColumnCount();

        int x = 20;
        int y = 550;

        for (int k = 0; k < rows; k++) {

            if (!model.getValueAt(k, 1).equals(date)) {

                date = (Date) model.getValueAt(k, 1);

                //Create new page
                PDFont font = PDType1Font.HELVETICA;
                int fontSize = 12;

                PDPage page = new PDPage(PDPage.PAGE_SIZE_A4);
                page.setRotation(90);
                doc.addPage(page);
                ++pageCountLoad;

                PDRectangle pageSize = page.findMediaBox();
                float pageWidth = pageSize.getWidth();

                try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {

                    // add the rotation using the current transformation matrix
                    // including a translation of pageWidth to use the lower left corner as 0,0 reference
                    cs.concatenate2CTM(0, 1, -1, 0, pageWidth, 0);

                    cs.setFont(font, fontSize);
                    cs.beginText();
                    cs.moveTextPositionByAmount(x, y);
                    cs.drawString(resString.getString("headPdf"));
                    cs.endText();
                    cs.beginText();
                    cs.moveTextPositionByAmount(x, y - 20);
                    cs.drawString(new Date().toString());
                    cs.endText();

                    cs.setFont(PDType1Font.HELVETICA_BOLD, 24);
                    cs.beginText();
                    cs.moveTextPositionByAmount(x + 330, y - 70);
                    cs.drawString(date.toString());
                    cs.endText();

                } catch (IOException ioe) {

                    ioe.printStackTrace();
                }

                try (PDPageContentStream cs = new PDPageContentStream(doc, page, true, true)) {


                    int nexty = y - 100;
                    int rowsByDate = 0;

                    for (int c = 0; c < rows; c++) {

                        if (date.equals(model.getValueAt(c, 1))){

                            ++rowsByDate;

                        }

                    }

                    drawTable(x, nexty, rowsByDate, cs, page, doc);

                    font = PDType1Font.HELVETICA_BOLD;
                    fontSize = 12;
                    cs.setFont(font, fontSize);

                    float textx = x;
                    float texty = nexty - rowHeight + (rowHeight - fontSize) / 2;

                    for (int i = 0; i < cols; i++) {

                        if (i == 1)
                            continue;

                        float width = font.getStringWidth(model.getColumnName(i)) / 1000 * fontSize;

                        switch (i) {

                            case 0:
                                textx += (colWidth1 - width) / 2;
                                break;
                            case 2:
                                textx += (colWidth2 - width) / 2;
                                break;
                            case 3:
                                textx += (colWidth3 - width) / 2;
                                break;
                            case 4:
                                textx += (colWidth4 - width) / 2;
                                break;
                            case 5:
                                textx += (colWidth5 - width) / 2;
                                break;
                        }

                        cs.beginText();
                        cs.moveTextPositionByAmount(textx, texty);
                        cs.drawString(model.getColumnName(i));
                        cs.endText();

                        switch (i) {

                            case 0:
                                textx += width + (colWidth1 - width) / 2;
                                break;
                            case 2:
                                textx += width + (colWidth2 - width) / 2;
                                break;
                            case 3:
                                textx += width + (colWidth3 - width) / 2;
                                break;
                            case 4:
                                textx += width + (colWidth4 - width) / 2;
                                break;
                            case 5:
                                textx += width + (colWidth5 - width) / 2;
                                break;
                        }

                    }

                    rowsByDateText += rowsByDate;
                    fillTableLoad(x, nexty, rowsByDateText, cols, model, 0.00, 0, cs, doc);

                } catch (IOException ioe) {

                    ioe.printStackTrace();
                }
            }
        }
    }

    private void drawTable (int x, int nexty, int rows, PDPageContentStream cs, PDPage page, PDDocument doc) throws IOException{

            int nextyStart = nexty;
            int rowCount = rows+1;

            cs.setLineWidth(0.8f);

            //draw the rows
            float tableWidth = page.findMediaBox().getHeight() - x - x;

            for (int i = 0; i <= rows; i++) {

                cs.drawLine(x, nexty, x + tableWidth, nexty);
                nexty -= rowHeight;
                --rowCount;

                if (nexty <= endPage) {

                    int start = 550;

                    PDPage nowa = new PDPage(PDPage.PAGE_SIZE_A4);
                    nowa.setRotation(90);
                    doc.addPage(nowa);

                    PDRectangle pageSize = nowa.findMediaBox();
                    float pageWidth = pageSize.getWidth();

                    try (PDPageContentStream newCS = new PDPageContentStream(doc, nowa, true, true)) {

                        newCS.concatenate2CTM(0, 1, -1, 0, pageWidth, 0);
                        drawTable(x, start, rowCount, newCS, nowa, doc);

                    }

                    nexty += rowHeight;
                    break;

                }
            }

        cs.drawLine(x, nexty, x + tableWidth, nexty);

        //draw the columns
            int nextx = x;

            cs.drawLine(nextx, nextyStart, nextx, nexty);
            nextx += colWidth1;
            cs.drawLine(nextx, nextyStart, nextx, nexty);
            nextx += colWidth2;
            cs.drawLine(nextx, nextyStart, nextx, nexty);
            nextx += colWidth3;
            cs.drawLine(nextx, nextyStart, nextx, nexty);
            nextx += colWidth4;
            cs.drawLine(nextx, nextyStart, nextx, nexty);
            nextx += colWidth5;
            cs.drawLine(nextx, nextyStart, nextx, nexty);

    }

    private void fillTable (int x, int nexty, int rows, int cols, TableModel model, int rowCount, int countPage, PDPageContentStream cs, PDDocument doc) throws IOException{

        PDFont font = PDType1Font.HELVETICA;
        int fontSize = 12;
        cs.setFont(font, fontSize);
        float textx = x;
        float texty = nexty - (rowHeight*2) + (rowHeight-fontSize)/2;
        String text;

        for (int i = rowCount; i < rows; i++) {

            for (int j = 0; j < cols; j++) {

                text = model.getValueAt(i, j).toString();
                float width = font.getStringWidth(text)/1000*fontSize;

                switch (j){

                    case 0:
                        textx += (colWidth1 - width)/2;
                        break;
                    case 1:
                        textx += 2;
                        break;
                    case 2:
                        textx += (colWidth3 - width)/2;
                        break;
                    case 3:
                        textx += 2;
                        break;
                    case 4:

                        if (text.equals("false")){

                            text = resString.getString("statusNotPaid");

                        }else{

                            text = resString.getString("statusPaid");
                        }

                        width = font.getStringWidth(text)/1000*fontSize;
                        textx += (colWidth5 - width)/2;
                        break;
                }

                cs.beginText();
                cs.moveTextPositionByAmount(textx, texty);
                cs.drawString(text);
                cs.endText();

                switch (j){

                    case 0:
                        textx += width + (colWidth1 - width)/2;
                        break;
                    case 1:
                        textx += colWidth2;
                        break;
                    case 2:
                        textx += width + (colWidth3 - width)/2;
                        break;
                    case 3:
                        textx += colWidth4;
                        break;
                    case 4:
                        textx += width + (colWidth5 - width)/2;
                        break;
                }

            }

            textx = x;
            texty -= rowHeight;
            ++rowCount;

            if (texty <= endPage+10){

                int start = 570;

                try(PDPageContentStream newCS = new PDPageContentStream(doc, (PDPage) doc.getDocumentCatalog().getAllPages().get(countPage), true, true)){

                    ++countPage;
                    fillTable(x, start, rows, cols, model, rowCount, countPage, newCS, doc);


                }

                break;

            }

        }

        if (rowCount == rows) {

            font = PDType1Font.HELVETICA_BOLD;
            fontSize = 14;
            cs.setFont(font, fontSize);
            float widthTotal = font.getStringWidth(String.valueOf(total)) / 1000 * fontSize;
            float textxTotal = textx + colWidth1 + colWidth2 + (colWidth3 - widthTotal) / 2;
            cs.beginText();
            cs.moveTextPositionByAmount(textx + colWidth1, texty);
            cs.drawString("Total:");
            cs.endText();
            cs.beginText();
            cs.moveTextPositionByAmount(textxTotal, texty);
            cs.drawString(String.valueOf(total));
            cs.endText();

        }

    }

    private void fillTableLoad (int x, int nexty, int rows, int cols, TableModel model, double totalLoad, int id, PDPageContentStream cs, PDDocument doc) throws IOException{

        PDFont font = PDType1Font.HELVETICA;
        int fontSize = 12;
        cs.setFont(font, fontSize);
        float textx = x;
        float texty = nexty - (rowHeight*2) + (rowHeight-fontSize)/2;
        String text;

        for (int i = rowCountByDate; i < rows; i++, rowCountByDate++) {

            for (int j = 0; j < cols; j++) {

                if (j ==1)
                    continue;

                text = model.getValueAt(i, j).toString();

                float width = font.getStringWidth(text)/1000*fontSize;

                switch (j){

                    case 0:
                        text = String.valueOf(++id);
                        textx += (colWidth1 - width)/2;
                        break;
                    case 2:
                        textx += 2;
                        break;
                    case 3:
                        totalLoad += Double.parseDouble(text);
                        textx += (colWidth3 - width)/2;
                        break;
                    case 4:
                        textx += 2;
                        break;
                    case 5:

                        if (text.equals("false")){

                            text = resString.getString("statusNotPaid");

                        }else{

                            text = resString.getString("statusPaid");
                        }

                        width = font.getStringWidth(text)/1000*fontSize;
                        textx += (colWidth5 - width)/2;
                        break;
                }

                cs.beginText();
                cs.moveTextPositionByAmount(textx, texty);
                cs.drawString(text);
                cs.endText();

                switch (j){

                    case 0:
                        textx += width + (colWidth1 - width)/2;
                        break;
                    case 2:
                        textx += colWidth2;
                        break;
                    case 3:
                        textx += width + (colWidth3 - width)/2;
                        break;
                    case 4:
                        textx += colWidth4;
                        break;
                    case 5:
                        textx += width + (colWidth5 - width)/2;
                        break;
                }

            }

            textx = x;
            texty -= rowHeight;

            if (texty <= endPage+10){

                int start = 570;

                try(PDPageContentStream newCS = new PDPageContentStream(doc, (PDPage) doc.getDocumentCatalog().getAllPages().get(pageCountLoad), true, true)){

                    ++pageCountLoad;
                    ++rowCountByDate;
                    fillTableLoad(x, start, rows, cols, model, totalLoad, id, newCS, doc);


                }

                break;

            }

            if (i == rows-1) {

                font = PDType1Font.HELVETICA_BOLD;
                fontSize = 14;
                cs.setFont(font, fontSize);
                float widthTotal = font.getStringWidth(String.valueOf(total)) / 1000 * fontSize;
                float textxTotal = textx + colWidth1 + colWidth2 + (colWidth3 - widthTotal) / 2;
                cs.beginText();
                cs.moveTextPositionByAmount(textx + colWidth1, texty);
                cs.drawString("Total:");
                cs.endText();
                cs.beginText();
                cs.moveTextPositionByAmount(textxTotal, texty);
                cs.drawString(new BigDecimal(totalLoad).setScale(2, BigDecimal.ROUND_UP).toString());
                cs.endText();

            }

        }

    }
}
